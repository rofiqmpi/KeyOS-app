/**
 * KeyOS - Gboard Enhancement Mod
 *
 * Maintained and customized by KeyOS (@KeyOSdev)
 * Repository: https://github.com/KeyOSdev/KeyOS
 * Licensed under the GNU General Public License v3.0 (GPLv3)
 */
package com.key.board.keyos.extension.keyboard;

import android.content.Context;

import java.util.Arrays;
import java.util.List;

import com.key.board.keyos.extension.R;
import com.key.board.keyos.extension.advancedvoice.GboardAdvancedVoiceSettingsFeature;
import com.key.board.keyos.extension.settings.GboardFeatureGroup;
import com.key.board.keyos.extension.settings.GboardPatchesSettingsContract;
import com.key.board.keyos.extension.settings.GboardSettingsText;
import com.key.board.keyos.extension.writingtools.GboardAiWritingToolsSettingsFeature;

public final class GboardAiVoiceSettingsGroupFeature
        implements GboardPatchesSettingsContract.Feature {
    private final GboardFeatureGroup delegate;

    public GboardAiVoiceSettingsGroupFeature(Context context) {
        delegate = new GboardFeatureGroup(
                GboardSettingsText.get(context, R.string.gboard_patches_group_ai_voice_title),
                GboardSettingsText.get(context, R.string.gboard_patches_group_ai_voice_summary),
                GboardSettingsText.get(context, R.string.gboard_patches_header_badge),
                GboardSettingsText.get(context,
                        R.string.gboard_patches_group_keyboard_header_summary),
                GboardSettingsText.get(context,
                        R.string.gboard_patches_group_keyboard_empty_title),
                GboardSettingsText.get(context,
                        R.string.gboard_patches_group_keyboard_empty_summary),
                Arrays.asList(
                        new GboardAiWritingToolsSettingsFeature(context),
                        new GboardAdvancedVoiceSettingsFeature(context)));
    }

    @Override
    public String getEntryTitle() {
        return delegate.getEntryTitle();
    }

    @Override
    public String getEntrySummary() {
        return delegate.getEntrySummary();
    }

    @Override
    public boolean isAvailable(Context context) {
        return delegate.isAvailable(context);
    }

    @Override
    public List<GboardPatchesSettingsContract.Feature> getNavigationChildren() {
        return delegate.getNavigationChildren();
    }

    @Override
    public GboardPatchesSettingsContract.Screen buildScreen(
            GboardPatchesSettingsContract.FeatureHost host) {
        return delegate.buildScreen(host);
    }
}
