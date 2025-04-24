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

    private void createParentBlock(RegistryObject<Block> handler) {
        withExistingParent(handler.getId().getPath() + "_item", modLoc( "block/" + handler.getId().getPath()));
    }
}
