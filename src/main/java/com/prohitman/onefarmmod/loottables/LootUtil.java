package com.prohitman.onefarmmod.loottables;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.registries.ForgeRegistries;

public class LootUtil {
    public static LootTable getEntityLootTable(Entity entity) {
        ResourceLocation entityLootTablePath = getEntityLootTablePath(entity);

        MinecraftServer server = entity.getServer();
        if (server == null) return null;

        return server.getLootData().getLootTable(entityLootTablePath);
    }

    private static ResourceLocation getEntityLootTablePath(Entity entity) {
        ResourceLocation entityType = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());

        String modId = entityType.getNamespace();

        String lootTablePath = "entities/" + entityType.getPath();

        return new ResourceLocation(modId, lootTablePath);
    }
}
