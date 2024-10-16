package com.prohitman.onefarmmod.datagen.server;

import com.prohitman.onefarmmod.OneFarmMod;
import com.prohitman.onefarmmod.blocks.OneFarmBlock;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.stream.Collectors;

public class ModLootTables extends VanillaBlockLoot {

    @Override
    protected void generate(){
        this.dropSelf(OneFarmMod.ONE_FARM_BLOCK.get());
        this.dropSelf(OneFarmMod.BREEDER_BLOCK.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getEntries().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(OneFarmMod.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
