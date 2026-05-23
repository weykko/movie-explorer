package ru.urfu.movie_explorer.ui.screens.profile.edit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.PhotoLibrary

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import ru.urfu.movie_explorer.R
import ru.urfu.movie_explorer.ui.screens.profile.util.AvatarFiles

/**
 * Экран редактирования профиля.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onFinished: () -> Unit,
    viewModel: EditProfileViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val finished by viewModel.finishEvent.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) {
            viewModel.finishConsumed()
            onFinished()
        }
    }

    val storagePermission = remember { storagePermissionForRuntime() }
    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (!granted) onBackClick()
    }

    LaunchedEffect(Unit) {
        val perm = storagePermission ?: return@LaunchedEffect
        if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
            storagePermissionLauncher.launch(perm)
        }
    }

    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION,
                )
            }
            viewModel.onAvatarChanged(uri.toString())
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture(),
    ) { saved ->
        val uri = pendingCameraUri
        if (saved && uri != null) {
            viewModel.onAvatarChanged(uri.toString())
        }
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            val uri = AvatarFiles.createTempAvatarUri(context)
            pendingCameraUri = uri
            cameraLauncher.launch(uri)
        }
    }

    val notificationsPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { _ ->
        viewModel.save()
    }

    var showSourceDialog by remember { mutableStateOf(false) }


    if (showSourceDialog) {
        AvatarSourceDialog(
            canRemove = !uiState.avatarUri.isNullOrBlank(),
            onDismiss = { showSourceDialog = false },
            onGallery = {
                showSourceDialog = false
                galleryLauncher.launch(
                    androidx.activity.result.PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly,
                    ),
                )
            },
            onCamera = {
                showSourceDialog = false
                val cameraGranted = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.CAMERA,
                ) == PackageManager.PERMISSION_GRANTED
                if (cameraGranted) {
                    val uri = AvatarFiles.createTempAvatarUri(context)
                    pendingCameraUri = uri
                    cameraLauncher.launch(uri)
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            onRemove = {
                showSourceDialog = false
                viewModel.onAvatarChanged(null)
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.profile_edit_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.details_back),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            AvatarPicker(
                avatarUri = uiState.avatarUri,
                onClick = { showSourceDialog = true },
            )

            OutlinedTextField(
                value = uiState.nickname,
                onValueChange = viewModel::onNicknameChanged,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.profile_nickname)) },
                placeholder = { Text(text = stringResource(R.string.profile_nickname_hint)) },
                shape = RoundedCornerShape(16.dp),
            )

            OutlinedTextField(
                value = uiState.socialUrl,
                onValueChange = viewModel::onSocialUrlChanged,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.profile_social)) },
                placeholder = { Text(text = stringResource(R.string.profile_social_hint)) },
                shape = RoundedCornerShape(16.dp),
            )

            var showTimePicker by remember { mutableStateOf(false) }
            val movieTimeField = remember(uiState.movieTimeText) {
                TextFieldValue(
                    text = uiState.movieTimeText,
                    selection = TextRange(uiState.movieTimeText.length),
                )
            }
            OutlinedTextField(
                value = movieTimeField,
                onValueChange = { tfv -> viewModel.onMovieTimeTextChanged(tfv.text) },
                singleLine = true,
                isError = uiState.movieTimeError,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.profile_movie_time)) },
                placeholder = { Text(text = stringResource(R.string.profile_movie_time_hint)) },
                supportingText = {
                    Text(
                        text = if (uiState.movieTimeError) {
                            stringResource(R.string.profile_movie_time_invalid)
                        } else {
                            stringResource(R.string.profile_movie_time_subtitle)
                        },
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { showTimePicker = true }) {
                        Icon(
                            imageVector = Icons.Rounded.AccessTime,
                            contentDescription = stringResource(R.string.profile_movie_time_pick),
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(16.dp),
            )

            if (showTimePicker) {
                MovieTimePickerDialog(
                    initialHour = uiState.movieTimeHour ?: DEFAULT_HOUR,
                    initialMinute = uiState.movieTimeMinute ?: 0,
                    onDismiss = { showTimePicker = false },
                    onConfirm = { hour, minute ->
                        showTimePicker = false
                        viewModel.onMovieTimePicked(hour, minute)
                    },
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val needsPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        uiState.movieTimeHour != null &&
                        ContextCompat.checkSelfPermission(
                            context, Manifest.permission.POST_NOTIFICATIONS,
                        ) != PackageManager.PERMISSION_GRANTED
                    if (needsPermission) {
                        notificationsPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        viewModel.save()
                    }
                },
                enabled = uiState.canSave,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(text = stringResource(R.string.profile_done))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieTimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit,
) {
    val state = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true,
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.profile_movie_time_pick)) },
        text = { TimePicker(state = state) },
        confirmButton = {
            TextButton(onClick = { onConfirm(state.hour, state.minute) }) {
                Text(text = stringResource(R.string.profile_done))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.profile_cancel))
            }
        },
    )
}

private const val DEFAULT_HOUR = 19

@Composable
private fun AvatarPicker(
    avatarUri: String?,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(140.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (!avatarUri.isNullOrBlank()) {
            AsyncImage(
                model = avatarUri,
                contentDescription = stringResource(R.string.profile_avatar),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.AddAPhoto,
                contentDescription = stringResource(R.string.profile_pick_avatar),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp),
            )
        }
    }
}

@Composable
private fun AvatarSourceDialog(
    canRemove: Boolean,
    onDismiss: () -> Unit,
    onGallery: () -> Unit,
    onCamera: () -> Unit,
    onRemove: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.profile_pick_avatar)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AvatarSourceRow(
                    icon = Icons.Rounded.PhotoLibrary,
                    label = stringResource(R.string.profile_pick_gallery),
                    onClick = onGallery,
                )
                AvatarSourceRow(
                    icon = Icons.Rounded.PhotoCamera,
                    label = stringResource(R.string.profile_pick_camera),
                    onClick = onCamera,
                )
                if (canRemove) {
                    AvatarSourceRow(
                        icon = Icons.Rounded.DeleteOutline,
                        label = stringResource(R.string.profile_remove_avatar),
                        onClick = onRemove,
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.profile_cancel))
            }
        },
    )
}

@Composable
private fun AvatarSourceRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    tint: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint)
        Spacer(modifier = Modifier.padding(end = 12.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = tint)
    }
}

/**
 * Возвращает имя runtime-permission на чтение пользовательских изображений или `null`,
 * если на текущей версии Android разрешение не требуется (Android 13+ использует Photo Picker).
 */
private fun storagePermissionForRuntime(): String? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> null
    else -> Manifest.permission.READ_EXTERNAL_STORAGE
}
