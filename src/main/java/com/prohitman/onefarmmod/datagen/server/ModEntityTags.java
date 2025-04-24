package com.prohitman.onefarmmod.datagen.server;

import com.prohitman.onefarmmod.OneFarmMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntityTags extends EntityTypeTagsProvider {
    public static final TagKey<EntityType<?>> FARMABLE_ENTITIES = create("farmable_entities");
    public static final TagKey<EntityType<?>> BLACKLIST_ENTITIES = create("blacklist_entities");
    public static final TagKey<EntityType<?>> MILK_EXTRACTABLE_ENTITIES = create("milk_extractable_entities");
    public static final TagKey<EntityType<?>> WATER_EXTRACTABLE_ENTITIES = create("water_extractable_entities");

    public ModEntityTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> providerCompletableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, providerCompletableFuture, OneFarmMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(MILK_EXTRACTABLE_ENTITIES)
                .add(EntityType.COW)
                .add(EntityType.MOOSHROOM);
        this.tag(WATER_EXTRACTABLE_ENTITIES)
                .add(EntityType.SQUID);
    }

    private static TagKey<EntityType<?>> create(String pName) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(OneFarmMod.MODID, pName));
    }
}
