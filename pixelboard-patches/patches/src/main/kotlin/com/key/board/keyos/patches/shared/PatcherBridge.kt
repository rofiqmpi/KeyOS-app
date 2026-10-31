package com.key.board.patches.shared

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction as pixelboardAddInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions as pixelboardAddInstructions
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels as pixelboardAddInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction as pixelboardReplaceInstruction
import app.morphe.patcher.patch.AppTarget as KeyOSAppTarget
import app.morphe.patcher.patch.BytecodePatch as KeyOSBytecodePatch
import app.morphe.patcher.patch.BytecodePatchBuilder as KeyOSBytecodePatchBuilder
import app.morphe.patcher.patch.BytecodePatchContext as KeyOSBytecodePatchContext
import app.morphe.patcher.patch.Compatibility as KeyOSCompatibility
import app.morphe.patcher.patch.Option as KeyOSOption
import app.morphe.patcher.patch.Patch as KeyOSPatch
import app.morphe.patcher.patch.PatchBuilder as KeyOSPatchBuilder
import app.morphe.patcher.patch.ResourcePatch as KeyOSResourcePatch
import app.morphe.patcher.patch.ResourcePatchBuilder as KeyOSResourcePatchBuilder
import app.morphe.patcher.patch.ResourcePatchContext as KeyOSResourcePatchContext
import app.morphe.patcher.patch.bytecodePatch as pixelboardBytecodePatch
import app.morphe.patcher.patch.loadPatchesFromJar as pixelboardLoadPatchesFromJar
import app.morphe.patcher.patch.resourcePatch as pixelboardResourcePatch
import app.morphe.patcher.patch.stringOption as pixelboardStringOption
import app.morphe.patcher.util.proxy.mutableTypes.MutableClass as KeyOSMutableClass
import app.morphe.patcher.util.proxy.mutableTypes.MutableField as KeyOSMutableField
import app.morphe.patcher.util.proxy.mutableTypes.MutableField.Companion.toMutable as pixelboardFieldToMutable
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod as KeyOSMutableMethod
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod.Companion.toMutable as pixelboardMethodToMutable
import app.morphe.patcher.util.smali.ExternalLabel as KeyOSExternalLabel
import com.android.tools.smali.dexlib2.iface.Field as DexlibField
import com.android.tools.smali.dexlib2.iface.Method as DexlibMethod
import java.io.File

typealias Patch<T> = KeyOSPatch<T>
typealias BytecodePatch = KeyOSBytecodePatch
typealias BytecodePatchBuilder = KeyOSBytecodePatchBuilder
typealias BytecodePatchContext = KeyOSBytecodePatchContext
typealias ResourcePatch = KeyOSResourcePatch
typealias ResourcePatchBuilder = KeyOSResourcePatchBuilder
typealias ResourcePatchContext = KeyOSResourcePatchContext
typealias Compatibility = KeyOSCompatibility
typealias AppTarget = KeyOSAppTarget
typealias MutableClass = KeyOSMutableClass
typealias MutableField = KeyOSMutableField
typealias MutableMethod = KeyOSMutableMethod
typealias ExternalLabel = KeyOSExternalLabel
typealias Option<T> = KeyOSOption<T>
typealias PatchBuilder<T> = KeyOSPatchBuilder<T>

inline fun resourcePatch(
    name: String? = null,
    description: String? = null,
    default: Boolean = false,
    noinline block: ResourcePatchBuilder.() -> Unit = {},
): ResourcePatch = pixelboardResourcePatch(name, description, default, block)

inline fun bytecodePatch(
    name: String? = null,
    description: String? = null,
    default: Boolean = false,
    noinline block: BytecodePatchBuilder.() -> Unit = {},
): BytecodePatch = pixelboardBytecodePatch(name, description, default, block)

inline fun PatchBuilder<*>.stringOption(
    key: String,
    default: String? = null,
    values: Map<String, String>? = null,
    title: String? = null,
    description: String? = null,
    required: Boolean = false,
    noinline validator: Option<String>.(String?) -> Boolean = { true },
): Option<String> = pixelboardStringOption(key, default, values, title, description, required, validator)

inline fun MutableMethod.addInstruction(index: Int, instruction: String) =
    pixelboardAddInstruction(index, instruction)

inline fun MutableMethod.addInstructions(index: Int, instructions: String) =
    pixelboardAddInstructions(index, instructions)

inline fun MutableMethod.addInstructionsWithLabels(index: Int, instructions: String, vararg labels: ExternalLabel) =
    pixelboardAddInstructionsWithLabels(index, instructions, *labels)

inline fun MutableMethod.replaceInstruction(index: Int, instruction: String) =
    pixelboardReplaceInstruction(index, instruction)

inline fun DexlibField.toMutable(): MutableField =
    pixelboardFieldToMutable()

inline fun DexlibMethod.toMutable(): MutableMethod =
    pixelboardMethodToMutable()

inline fun loadPatchesFromJar(patchFiles: Set<File>) =
    pixelboardLoadPatchesFromJar(patchFiles)
