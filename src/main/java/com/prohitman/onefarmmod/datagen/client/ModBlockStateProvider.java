package com.prohitman.onefarmmod.datagen.client;

import com.prohitman.onefarmmod.OneFarmMod;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, OneFarmMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(OneFarmMod.ONE_FARM_BLOCK.get());
        //simpleBlock(OneFarmMod.BREEDER_BLOCK.get());

    }

    private void createCrossBlock(RegistryObject<Block> block) {
        simpleBlock(block.get(), models().cross(block.getId().getPath(),
                modLoc("block/" + block.getId().getPath())).renderType("cutout_mipped"));
    }

    private void createCubeSideTop(RegistryObject<Block> block){
        simpleBlock(block.get(),
                new ConfiguredModel(models()
                        .cubeColumn(block.getId().getPath(),
                                modLoc("block/" + block.getId().getPath() + "_side"),
                                modLoc("block/" + block.getId().getPath() + "_top"))));
    }

    private void createCubeButtomTop(RegistryObject<Block> block){
        horizontalBlock(block.get(), models()
                .cubeBottomTop(block.getId().getPath(),
                                modLoc("block/" + block.getId().getPath() + "_side"),
                                modLoc("block/" + block.getId().getPath() + "_bottom"),
                                modLoc("block/" + block.getId().getPath() + "_top")));
    }

    public void simpleSideTopBottomBlock(RegistryObject<Block> block){
        simpleBlock(block.get(),
                new ConfiguredModel(models()
                        .cubeBottomTop(block.getId().getPath(),
                                modLoc("block/" + block.getId().getPath() + "_side"),
                                modLoc("block/" + block.getId().getPath() + "_bottom"),
                                modLoc("block/" + block.getId().getPath() + "_top"))));
    }
}
