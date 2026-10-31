/**
 * KeyOS - Gboard Enhancement Mod
 *
 * Maintained and customized by KeyOS (@KeyOS)
 * Repository: https://github.com/KeyOS/KeyOS
 * Licensed under the GNU General Public License v3.0 (GPLv3)
 */
package com.key.board.extension.settings;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.key.board.extension.keyboard.GboardAiVoiceSettingsGroupFeature;
import com.key.board.extension.updater.KeyOSUpdateFeature;

public final class GboardPatchesSettingsFeatureRegistry {
    private static final String TAG = "KeyOS";

    private GboardPatchesSettingsFeatureRegistry() {
    }

    public static List<GboardPatchesSettingsContract.Feature> features(Context context) {
        List<GboardPatchesSettingsContract.Feature> features =
                new ArrayList<GboardPatchesSettingsContract.Feature>();
        addIfAvailable(context, features, new GboardAiVoiceSettingsGroupFeature(context));
        addIfAvailable(context, features, new KeyOSUpdateFeature(context));
        return Collections.unmodifiableList(features);
    }

    private static void addIfAvailable(Context context,
            List<GboardPatchesSettingsContract.Feature> features,
            GboardPatchesSettingsContract.Feature feature) {
        try {
            if (feature.isAvailable(context)) {
                features.add(feature);
            }
        } catch (Throwable throwable) {
            Log.w(TAG, "Failed to evaluate feature availability: " + feature.getClass().getName(),
                    throwable);
        }
    }
}
