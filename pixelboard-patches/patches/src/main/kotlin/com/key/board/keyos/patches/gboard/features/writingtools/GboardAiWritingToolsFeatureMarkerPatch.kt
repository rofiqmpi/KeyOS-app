/**
 * KeyOS - Gboard Enhancement Mod
 *
 * Maintained and customized by KeyOS (@KeyOSdev)
 * Repository: https://github.com/KeyOSdev/KeyOS
 * Licensed under the GNU General Public License v3.0 (GPLv3)
 */
package com.key.board.patches.gboard.features.writingtools

import com.key.board.patches.shared.resourcePatch
import com.key.board.patches.gboard.shared.applyFeatureMarker
import com.key.board.patches.shared.Constants.COMPATIBILITY_GBOARD

internal val gboardAiWritingToolsFeatureMarkerPatch = resourcePatch(
    description = "Mark AI Writing Tools feature injected into target APK for settings UI filtering."
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    finalize {
        applyFeatureMarker(AI_WRITING_TOOLS_FEATURE_MARKER_NAME)
    }
}
