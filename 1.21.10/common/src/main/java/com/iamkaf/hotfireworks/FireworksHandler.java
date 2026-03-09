package com.iamkaf.hotfireworks;

import com.iamkaf.amber.api.event.v1.events.common.EntityEvent;
import com.iamkaf.amber.api.functions.v1.ItemFunctions;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles the automatic firework launching when player takes fire/heat damage.
 */
public class FireworksHandler {
    private static final int COOLDOWN_TICKS = 19;
    private static final Map<UUID, Long> lastFireworkTick = new ConcurrentHashMap<>();

    /**
     * Register the event handler.
     */
    public static void register() {
        EntityEvent.ENTITY_DAMAGE.register((entity, source, amount) -> {
            // Only handle players
            if (!(entity instanceof Player player)) {
                return InteractionResult.PASS;
            }

            // Only on server side
            if (player.level().isClientSide()) {
                return InteractionResult.PASS;
            }

            // Check cooldown
            long currentTick = player.level().getGameTime();
            UUID playerUuid = player.getUUID();
            Long lastTick = lastFireworkTick.get(playerUuid);
            if (lastTick != null && currentTick - lastTick < COOLDOWN_TICKS) {
                return InteractionResult.PASS;
            }

            // Check if damage is from a fire/heat source
            if (!isFireDamage(source)) {
                return InteractionResult.PASS;
            }

            // Try to launch a firework
            if (launchRandomFirework(player)) {
                lastFireworkTick.put(playerUuid, currentTick);
            }

            return InteractionResult.PASS;
        });
    }

    /**
     * Check if the damage source is one of the fire/heat related types.
     */
    private static boolean isFireDamage(net.minecraft.world.damagesource.DamageSource source) {
        return source.is(DamageTypes.CAMPFIRE) ||
                source.is(DamageTypes.LIGHTNING_BOLT) ||
                source.is(DamageTypes.ON_FIRE) ||
                source.is(DamageTypes.LAVA) ||
                source.is(DamageTypes.HOT_FLOOR) ||
                source.is(DamageTypes.UNATTRIBUTED_FIREBALL) ||
                source.is(DamageTypes.FIREBALL) ||
                source.is(DamageTypes.EXPLOSION) ||
                source.is(DamageTypes.PLAYER_EXPLOSION);
    }

    /**
     * Search player's inventory for fireworks and launch a random one.
     *
     * @return true if a firework was launched, false otherwise
     */
    private static boolean launchRandomFirework(Player player) {
        List<ItemStack> fireworks = new ArrayList<>();

        // Collect all fireworks in the inventory
        ItemFunctions.forEach(player.getInventory(), stack -> {
            if (stack.is(Items.FIREWORK_ROCKET)) {
                fireworks.add(stack);
            }
        });

        // No fireworks found
        if (fireworks.isEmpty()) {
            return false;
        }

        // Pick a random firework
        ItemStack chosen = fireworks.get(player.getRandom().nextInt(fireworks.size()));

        // Launch the firework
        launchFirework(player, chosen);

        // Consume one rocket from the stack
        chosen.shrink(1);

        return true;
    }

    /**
     * Launch a single firework rocket from the player's inventory.
     */
    private static void launchFirework(Player player, ItemStack stack) {
        // For simplicity, we'll launch from the player's position
        FireworkRocketEntity firework = new FireworkRocketEntity(
                player.level(),
                player.getX(),
                player.getY() + 1.5,  // Slightly above player
                player.getZ(),
                stack
        );

        player.level().addFreshEntity(firework);

        HotFireworksConstants.LOG.debug("Launched firework for {} due to fire damage", player.getName().getString());
    }
}
