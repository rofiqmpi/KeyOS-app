package com.key.board.patches.shared

import com.key.board.patches.gboard.shared.generated.GboardTargetAdmission

internal object Constants {
    const val GBOARD_PACKAGE_NAME = "com.google.android.inputmethod.latin"
    const val GBOARD_PATCHED_PACKAGE_NAME = "com.key.board"
    const val GBOARD_PATCH_AUTHOR = "KeyOS"
    const val GBOARD_PATCH_AUTHOR_URL = "https://github.com/KeyOSdev"
    const val GBOARD_PATCH_REPOSITORY_URL = "https://github.com/KeyOSdev/KeyOS"
    val GBOARD_PATCH_VERSION = PatchBuildInfo.VERSION

    val COMPATIBILITY_GBOARD = Compatibility(
        name = "Gboard",
        packageName = GboardTargetAdmission.packageName,
        apkFileType = GboardTargetAdmission.apkFileType,
        appIconColor = 0x1A73E8,
        signatures = GboardTargetAdmission.signatures,
        targets = GboardTargetAdmission.versionNames.map { versionName ->
            AppTarget(
                version = versionName,
                isExperimental = false,
            )
        },
    )
}
