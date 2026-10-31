/**
 * KeyOS - Gboard Enhancement Mod
 *
 * Maintained and customized by KeyOS (@KeyOSdev)
 * Repository: https://github.com/KeyOSdev/KeyOS
 * Licensed under the GNU General Public License v3.0 (GPLv3)
 */
package com.key.board.patches.gboard.features.rambler

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

private val ramblerOnVoiceTextTargets = listOf(
    GboardMethodTarget(
        classType = "Lfhv;",
        name = "j",
        parameterTypes = listOf(
            "Laaru;",
            "Lsgm;",
            "Ljava/lang/String;",
        ),
        returnType = "V",
    ),
    GboardMethodTarget(
        classType = "Lfde;",
        name = "j",
        parameterTypes = listOf(
            "Laajc;",
            "Lrzc;",
            "Ljava/lang/String;",
        ),
        returnType = "V",
    ),
)

internal val gboardRamblerDictationPromptFixPatch = bytecodePatch(
    description = "Ensure Rambler dictation commits verbatim spoken transcript instead of AI prompt responses.",
) {
    compatibleWith(COMPATIBILITY_GBOARD)
    dependsOn(gboardPatchesExtensionCarrierPatch)

    execute {
        val method = ramblerOnVoiceTextTargets.firstNotNullOfOrNull { findMutableMethodOrNull(it) } ?: return@execute
        val implementation = method.implementation ?: return@execute
        val instructions = implementation.instructions
        val call = RuntimeCallId.RAMBLER_DICTATION_RUNTIME_FILTER_VOICE_TEXT
        if (instructions.any { it.isMethodReference(RuntimeAbiCatalog.abi(call).reference) }) {
            return@execute
        }

        // p1 is the Laajc voice text proto parameter.
        // Method j has 30 locals (registers 30-33), so range invocation is required for p1.
        method.addInstructions(
            0,
            RuntimeCallEmitter.invoke(call, "p1 .. p1")
        )
    }
}
