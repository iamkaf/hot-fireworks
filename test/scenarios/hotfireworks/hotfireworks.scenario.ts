import { Capability, Readiness, describe, expect, test } from "@teakit/test";

describe.configure({
  timeout: "6m",
  readiness: [Readiness.ClientReady, Readiness.IntegratedServerReady, Readiness.PlayerSpawned],
  capabilities: [
    Capability.RuntimeTiming,
    Capability.PlayerInventory,
    Capability.ServerCommands,
    Capability.WorldBlock,
    Capability.WorldFill,
  ],
});

describe("Hot Fireworks", () => {
  test("reacts to fire damage without destabilizing the run", async ({ commands, player, runtime, world }) => {
    await commands.run("/gamemode survival");
    await commands.run("/difficulty peaceful");
    await commands.run("/effect clear @s");
    await commands.run("/clear @s");
    await commands.run("/tp @s 0.5 80 0.5 0 0");
    await world.clear({ x: -4, y: 80, z: -4 }, { x: 4, y: 84, z: 4 });
    await world.fill({ x: -4, y: 79, z: -4 }, { x: 4, y: 79, z: 4 }, "minecraft:glass");

    await commands.assert("/give @s minecraft:firework_rocket 3");
    await expect(player.inventory()).toContainItem("minecraft:firework_rocket");

    await commands.run("/damage @s 1 minecraft:on_fire");
    await runtime.wait(2_000, { timeoutMs: 4_000 });

    await commands.assert("/execute if entity @s");
    await expect(() => world.block({ x: 0, y: 79, z: 0 })).toEventuallyEqual(
      expect.objectContaining({ id: "minecraft:glass" }),
      { timeout: "3s" },
    );
  });
});
