package com.iamkaf.hotfireworks;

import net.fabricmc.api.ModInitializer;

/**
 * Fabric entry point.
 */
public class HotFireworksFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        HotFireworksMod.init();
    }
}
