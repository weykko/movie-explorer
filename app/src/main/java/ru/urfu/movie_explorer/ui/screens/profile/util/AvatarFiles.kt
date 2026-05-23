package ru.urfu.movie_explorer.ui.screens.profile.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

/**
 * Утилиты для работы с файлами аватарок профиля.
 *
 * Камера пишет фото в приватный кэш приложения; FileProvider превращает локальный
 * файл в content-Uri, который безопасно показать стандартному системному приложению камеры.
 */
internal object AvatarFiles {

    private const val AVATARS_DIR = "avatars"
    private const val AUTHORITY_SUFFIX = ".fileprovider"

    /** Создаёт пустой файл `avatar_<timestamp>.jpg` в приватном кэше и возвращает content-Uri. */
    fun createTempAvatarUri(context: Context): Uri {
        val dir = File(context.cacheDir, AVATARS_DIR).apply { mkdirs() }
        val file = File(dir, "avatar_${System.currentTimeMillis()}.jpg")
        if (!file.exists()) file.createNewFile()
        val authority = context.packageName + AUTHORITY_SUFFIX
        return FileProvider.getUriForFile(context, authority, file)
    }
}
