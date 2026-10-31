/**
 * KeyOS - Gboard Enhancement Mod
 *
 * Maintained and customized by KeyOS (@KeyOS)
 * Repository: https://github.com/KeyOS/KeyOS
 * Licensed under the GNU General Public License v3.0 (GPLv3)
 */
package com.key.board.keyos.extension.updater;

import org.junit.Assert;
import org.junit.Test;

import com.key.board.keyos.extension.settings.GboardPatchesSettingsContract;

public class KeyOSUpdateFeatureTest {

    @Test
    public void featureMetadataMatchesExpectedTitles() {
        KeyOSUpdateFeature feature = new KeyOSUpdateFeature(null);
        Assert.assertEquals("KeyOS updates", feature.getEntryTitle());
        Assert.assertTrue(feature.isAvailable(null));
        Assert.assertNotNull(feature.getEntrySummary());
    }

    @Test
    public void buildScreenCreatesSectionsAndStatusBlock() {
        KeyOSUpdateFeature feature = new KeyOSUpdateFeature(null);
        GboardPatchesSettingsContract.FeatureHost host = new GboardPatchesSettingsContract.FeatureHost() {
            @Override
            public android.content.Context getContext() {
                return null;
            }

            @Override
            public void submit(GboardPatchesSettingsContract.Intent intent) {
            }
        };

        GboardPatchesSettingsContract.Screen screen = feature.buildScreen(host);
        Assert.assertNotNull(screen);
        Assert.assertEquals("KeyOS updates", screen.getToolbarTitle());
        Assert.assertEquals("Updates", screen.getHeaderTitle());
        Assert.assertFalse(screen.isRestartActionVisible());
        Assert.assertTrue(screen.getStatusBlocks().isEmpty());
        Assert.assertFalse(screen.getSections().isEmpty());
    }

    @Test
    public void contractRowsSupportAnimatingFlag() {
        GboardPatchesSettingsContract.CommandRow defaultRow =
                new GboardPatchesSettingsContract.CommandRow("Title", "Summary", true, () -> {});
        Assert.assertFalse(defaultRow.isAnimating());

        GboardPatchesSettingsContract.CommandRow animatingRow =
                new GboardPatchesSettingsContract.CommandRow("Title", "Summary", false, () -> {}, null, null, true);
        Assert.assertTrue(animatingRow.isAnimating());

        GboardPatchesSettingsContract.PrimaryCommandRow defaultPrimary =
                new GboardPatchesSettingsContract.PrimaryCommandRow("Title", "Summary", true, () -> {});
        Assert.assertFalse(defaultPrimary.isAnimating());

        GboardPatchesSettingsContract.PrimaryCommandRow animatingPrimary =
                new GboardPatchesSettingsContract.PrimaryCommandRow("Title", "Summary", false, () -> {}, true);
        Assert.assertTrue(animatingPrimary.isAnimating());
    }

    @Test
    public void checkingAndDownloadingStatusesProduceAnimatingRow() {
        KeyOSUpdateManager updateManager = KeyOSUpdateManager.getInstance();
        KeyOSUpdateFeature feature = new KeyOSUpdateFeature(null);
        GboardPatchesSettingsContract.FeatureHost host = new GboardPatchesSettingsContract.FeatureHost() {
            @Override
            public android.content.Context getContext() {
                return null;
            }

            @Override
            public void submit(GboardPatchesSettingsContract.Intent intent) {
            }
        };

        try {
            updateManager.setCurrentStatusForTest(KeyOSUpdateManager.Status.CHECKING);
            GboardPatchesSettingsContract.Screen checkingScreen = feature.buildScreen(host);
            GboardPatchesSettingsContract.Section actionSection = checkingScreen.getSections().get(0);
            GboardPatchesSettingsContract.PrimaryCommandRow checkingRow =
                    (GboardPatchesSettingsContract.PrimaryCommandRow) actionSection.getItems().get(0);
            Assert.assertTrue("Row must animate when checking", checkingRow.isAnimating());
            Assert.assertFalse("Row must be disabled when checking", checkingRow.isEnabled());

            updateManager.setCurrentStatusForTest(KeyOSUpdateManager.Status.DOWNLOADING);
            GboardPatchesSettingsContract.Screen downloadingScreen = feature.buildScreen(host);
            GboardPatchesSettingsContract.Section downloadingSection = downloadingScreen.getSections().get(0);
            GboardPatchesSettingsContract.PrimaryCommandRow downloadingRow =
                    (GboardPatchesSettingsContract.PrimaryCommandRow) downloadingSection.getItems().get(0);
            Assert.assertTrue("Row must animate when downloading", downloadingRow.isAnimating());
            Assert.assertFalse("Row must be disabled when downloading", downloadingRow.isEnabled());

            updateManager.setCurrentStatusForTest(KeyOSUpdateManager.Status.IDLE);
            GboardPatchesSettingsContract.Screen idleScreen = feature.buildScreen(host);
            GboardPatchesSettingsContract.Section idleSection = idleScreen.getSections().get(0);
            GboardPatchesSettingsContract.PrimaryCommandRow idleRow =
                    (GboardPatchesSettingsContract.PrimaryCommandRow) idleSection.getItems().get(0);
            Assert.assertFalse("Row must not animate when idle", idleRow.isAnimating());
            Assert.assertTrue("Row must be enabled when idle", idleRow.isEnabled());
        } finally {
            updateManager.setCurrentStatusForTest(KeyOSUpdateManager.Status.IDLE);
        }
    }
}
