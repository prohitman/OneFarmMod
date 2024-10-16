package com.prohitman.onefarmmod.datagen.client;

import com.prohitman.onefarmmod.OneFarmMod;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import org.codehaus.plexus.util.StringUtils;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, OneFarmMod.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        addBlock(OneFarmMod.ONE_FARM_BLOCK);
        addBlock(OneFarmMod.BREEDER_BLOCK);

        //add("itemGroup.onefarmblock", "Unsorted Starfall");
    }

    public void addEffect(RegistryObject<MobEffect> key){
        add(key.get().getDescriptionId(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")));
    }

    public void addBlock(RegistryObject<Block> key) {
        add(key.get().getDescriptionId(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")));
    }

    public void addItem(RegistryObject<Item> key){
        add(key.get().getDescriptionId(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")));
    }

    public void addItem(RegistryObject<Item> key, String name){
        add(key.get().getDescriptionId(), name);
    }

    public void addSound(RegistryObject<SoundEvent> key){
        add("sounds." + key.getId().toLanguageKey(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")));
    }
}
