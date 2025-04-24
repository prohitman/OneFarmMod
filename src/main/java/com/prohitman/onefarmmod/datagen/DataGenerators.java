package com.prohitman.onefarmmod.datagen;

import com.prohitman.onefarmmod.OneFarmMod;
import com.prohitman.onefarmmod.datagen.client.ModBlockStateProvider;
import com.prohitman.onefarmmod.datagen.client.ModItemModelProvider;
import com.prohitman.onefarmmod.datagen.client.ModLanguageProvider;
import com.prohitman.onefarmmod.datagen.server.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = OneFarmMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ModBlockTags blockTags = new ModBlockTags(dataGenerator.getPackOutput(), lookupProvider, event.getExistingFileHelper());
        ModEntityTags entityTags = new ModEntityTags(dataGenerator.getPackOutput(), lookupProvider, event.getExistingFileHelper());

        dataGenerator.addProvider(event.includeClient(), (DataProvider.Factory<ModBlockStateProvider>)
                output -> new ModBlockStateProvider(output, event.getExistingFileHelper()));

        dataGenerator.addProvider(event.includeClient(), (DataProvider.Factory<ModItemModelProvider>)
                output -> new ModItemModelProvider(output, event.getExistingFileHelper()));

        dataGenerator.addProvider(event.includeClient(), (DataProvider.Factory<ModLanguageProvider>)
                output -> new ModLanguageProvider(dataGenerator.getPackOutput(), "en_us"));

        dataGenerator.addProvider(event.includeServer(), (DataProvider.Factory<ModBlockTags>)
                output -> blockTags);

        dataGenerator.addProvider(event.includeServer(), (DataProvider.Factory<ModItemTags>)
                output -> new ModItemTags(output, lookupProvider, blockTags.contentsGetter(), event.getExistingFileHelper()));

        dataGenerator.addProvider(event.includeServer(), new ModRecipes(dataGenerator.getPackOutput()));

        dataGenerator.addProvider(event.includeServer(), new LootTableProvider(dataGenerator.getPackOutput(), Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(ModLootTables::new, LootContextParamSets.BLOCK))));


        dataGenerator.addProvider(event.includeServer(), (DataProvider.Factory<ModEntityTags>)
                output -> entityTags);
        try {
            dataGenerator.run();
        } catch (IOException ignored) {
            throw new RuntimeException(ignored);
        }
    }
}
