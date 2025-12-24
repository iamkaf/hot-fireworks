package com.iamkaf.hotfireworks;

import com.iamkaf.amber.api.core.v2.AmberInitializer;
import com.iamkaf.hotfireworks.platform.Services;

/**
 * Common entry point for the mod.
 * Replace the contents with your own implementation.
 */
public class HotFireworksMod {

    /**
     * Called during mod initialization for all loaders.
     */
    public static void init() {
        // Initialize Amber
        AmberInitializer.initialize(HotFireworksConstants.MOD_ID);

        HotFireworksConstants.LOG.info("Initializing {} on {}...", HotFireworksConstants.MOD_NAME, Services.PLATFORM.getPlatformName());

        // Register the firework handler
        FireworksHandler.register();
    }
}
