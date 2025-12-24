package com.iamkaf.hotfireworks;

import com.iamkaf.hotfireworks.platform.Services;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Common entry point for the mod.
 * Replace the contents with your own implementation.
 */
public class HotFireworksMod {

    /**
     * Called during mod initialization for all loaders.
     */
    public static void init() {
        HotFireworksConstants.LOG.info("Initializing {} on {}...", HotFireworksConstants.MOD_NAME, Services.PLATFORM.getPlatformName());
    }
}
