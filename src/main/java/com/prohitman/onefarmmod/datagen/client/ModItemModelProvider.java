package com.prohitman.onefarmmod.datagen.client;

import com.prohitman.onefarmmod.OneFarmMod;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, OneFarmMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        //Items

        //Blocks
        createParentBlock(OneFarmMod.ONE_FARM_BLOCK);
        createParentBlock(OneFarmMod.BREEDER_BLOCK);

    }

    private void createSingleTextureCrossBlock(RegistryObject<Block> block){
        singleTexture((block.getId().getPath()),
                mcLoc("item/generated"),
                "layer0", modLoc("block/" + block.getId().getPath()));
    }

    private void createBlockSingle(RegistryObject<Block> block){
        singleTexture((block.getId().getPath()),
                mcLoc("item/generated"),
                "layer0", modLoc("item/" + block.getId().getPath()));
    }

    private void createBlockSingleHandheld(RegistryObject<Block> block){
        singleTexture((block.getId().getPath()),
                mcLoc("item/handheld"),
                "layer0", modLoc("item/" + block.getId().getPath()));
    }

    private void createParent(RegistryObject<Block> handler) {
        withExistingParent(handler.getId().getPath(), modLoc( "block/" + handler.getId().getPath()));
    }

    private void createParentBlock(RegistryObject<Block> handler) {
        withExistingParent(handler.getId().getPath() + "_item", modLoc( "block/" + handler.getId().getPath()));
    }

    private String name(RegistryObject<Block> block) {
        return block.getId().getPath();
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private void createSingle(RegistryObject<Item> item) {
        ModelFile generated = getExistingFile(mcLoc("item/generated"));
        getBuilder(item.getId().getPath()).parent(generated).texture("layer0", modLoc( "item/" + item.getId().getPath()));
    }

    private void createSingleEffectItem(RegistryObject<Item> item) {
        ModelFile generated = getExistingFile(mcLoc("item/generated"));
        getBuilder(item.getId().getPath()).parent(generated).texture("layer0", modLoc( "mob_effect/" + item.getId().getPath()));
    }

    private void createSingleHandHeld(RegistryObject<Item> item) {
        ModelFile generated = getExistingFile(mcLoc("item/handheld"));
        getBuilder(item.getId().getPath()).parent(generated).texture("layer0", modLoc( "item/" + item.getId().getPath()));
    }
}
