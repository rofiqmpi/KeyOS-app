package com.key.board.patches.gboard.features.advancedvoice

import com.key.board.patches.gboard.shared.GboardFlagFamilyFeature
import com.key.board.patches.gboard.shared.gboardFlagFamilyFeaturePatch

internal val gboardAdvancedVoiceFlagValuePatch = gboardFlagFamilyFeaturePatch(
    description = "Apply Advanced Voice and Rambler typed policy after 18.0.3 stock flag getter.",
    feature = GboardFlagFamilyFeature.ADVANCED_VOICE_TYPING,
)
