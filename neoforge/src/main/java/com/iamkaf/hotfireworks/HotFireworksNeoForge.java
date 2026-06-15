package com.iamkaf.hotfireworks;

import com.iamkaf.hotfireworks.HotFireworksConstants;
import com.iamkaf.hotfireworks.HotFireworksMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(HotFireworksConstants.MOD_ID)
public class HotFireworksNeoForge {
    public HotFireworksNeoForge(IEventBus eventBus) {
        HotFireworksMod.init();
    }
}