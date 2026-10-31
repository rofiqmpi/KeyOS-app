package com.key.board.patches.gboard.features.accessibility

import com.key.board.patches.gboard.shared.GboardMethodTarget
import com.key.board.patches.gboard.shared.findMutableMethodOrNull
import com.key.board.patches.gboard.shared.gboardPatchesExtensionCarrierPatch
import com.key.board.patches.gboard.shared.isMethodReference
import com.key.board.patches.gboard.shared.runtimeabi.RuntimeAbiCatalog
import com.key.board.patches.gboard.shared.runtimeabi.RuntimeCallEmitter
import com.key.board.patches.gboard.shared.runtimeabi.RuntimeCallId
import com.key.board.patches.shared.Constants.COMPATIBILITY_GBOARD
import com.key.board.patches.shared.addInstructions
import com.key.board.patches.shared.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode

private val accessPointsPanelInflateTargets = listOf(
    GboardMethodTarget(
        classType = "Lcom/google/android/libraries/inputmethod/accesspoint/widget/AccessPointsPanelV2;",
        name = "onFinishInflate",
        parameterTypes = emptyList(),
        returnType = "V",
    ),
    GboardMethodTarget(
        classType = "Lcom/google/android/libraries/inputmethod/accesspoint/widget/AccessPointsPanel;",
        name = "onFinishInflate",
        parameterTypes = emptyList(),
        returnType = "V",
    ),
)

private val accessPointsPanelVisibilityTargets = listOf(
    GboardMethodTarget(
        classType = "Lcom/google/android/libraries/inputmethod/accesspoint/widget/AccessPointsPanelV2;",
        name = "onVisibilityChanged",
        parameterTypes = listOf("Landroid/view/View;", "I"),
        returnType = "V",
    ),
    GboardMethodTarget(
        classType = "Lcom/google/android/libraries/inputmethod/accesspoint/widget/AccessPointsPanel;",
        name = "onVisibilityChanged",
        parameterTypes = listOf("Landroid/view/View;", "I"),
        returnType = "V",
    ),
    GboardMethodTarget(
        classType = "Lcom/google/android/libraries/inputmethod/accesspoint/widget/AccessPointsPanelViewPage;",
        name = "onVisibilityChanged",
        parameterTypes = listOf("Landroid/view/View;", "I"),
        returnType = "V",
    ),
)

internal val gboardFeaturesMenuAccessibilityPatch = bytecodePatch(
    description = "Fix accessibility focus and window state events when opening features menu.",
) {
    compatibleWith(COMPATIBILITY_GBOARD)
    dependsOn(gboardPatchesExtensionCarrierPatch)

    execute {
        val inflateCall = RuntimeCallId.FEATURES_MENU_ACCESSIBILITY_ON_INFLATE
        val inflateRef = RuntimeAbiCatalog.abi(inflateCall).reference
        accessPointsPanelInflateTargets.forEach { target ->
            val method = findMutableMethodOrNull(target) ?: return@forEach
            val implementation = method.implementation ?: return@forEach
            val instructions = implementation.instructions
            if (instructions.any { it.isMethodReference(inflateRef) }) {
                return@forEach
            }
            val returnIndex = instructions.indexOfLast { it.opcode == Opcode.RETURN_VOID }
            if (returnIndex == -1) return@forEach
            method.addInstructions(
                returnIndex,
                RuntimeCallEmitter.invoke(inflateCall, "p0"),
            )
        }

        val visibilityCall = RuntimeCallId.FEATURES_MENU_ACCESSIBILITY_ON_VISIBILITY_CHANGED
        val visibilityRef = RuntimeAbiCatalog.abi(visibilityCall).reference
        accessPointsPanelVisibilityTargets.forEach { target ->
            val method = findMutableMethodOrNull(target) ?: return@forEach
            val implementation = method.implementation ?: return@forEach
            val instructions = implementation.instructions
            if (instructions.any { it.isMethodReference(visibilityRef) }) {
                return@forEach
            }
            val returnIndex = instructions.indexOfLast { it.opcode == Opcode.RETURN_VOID }
            if (returnIndex == -1) return@forEach
            method.addInstructions(
                returnIndex,
                RuntimeCallEmitter.invoke(visibilityCall, "p0"),
            )
        }
    }
}
