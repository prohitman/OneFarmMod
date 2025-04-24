package com.prohitman.onefarmmod;

import com.prohitman.onefarmmod.blocks.BreederBlock;
import com.prohitman.onefarmmod.blocks.OneFarmBlock;
import com.prohitman.onefarmmod.blocks.entities.BreederBlockEntity;
import com.prohitman.onefarmmod.blocks.entities.OneFarmBlockEntity;
import com.prohitman.onefarmmod.blocks.item.ModBlockItem;
import com.prohitman.onefarmmod.client.ModMenuTypes;
import com.prohitman.onefarmmod.client.keybindings.ModKeyBindings;
import com.prohitman.onefarmmod.client.renderer.OneFarmBlockEntityRenderer;
import com.prohitman.onefarmmod.client.screen.BreederScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(OneFarmMod.MODID)
public class OneFarmMod
{
    public static final String MODID = "onefarmmod";
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Block> ONE_FARM_BLOCK = BLOCKS.register("one_farm_block", () -> new OneFarmBlock(BlockBehaviour.Properties.of().strength(8).mapColor(MapColor.COLOR_GRAY)));
    public static final RegistryObject<Block> BREEDER_BLOCK = BLOCKS.register("breeder_block", () -> new BreederBlock(BlockBehaviour.Properties.of().strength(5).mapColor(MapColor.COLOR_RED)));
    public static final RegistryObject<Item> ONE_FARM_BLOCK_ITEM = ITEMS.register("one_farm_block_item", () -> new ModBlockItem(ONE_FARM_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> BREEDER_BLOCK_ITEM = ITEMS.register("breeder_block_item", () -> new ModBlockItem(BREEDER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockEntityType<OneFarmBlockEntity>> ONE_FARM_BLOCK_ENTITY = BLOCK_ENTITIES.register("one_farm_be", () -> BlockEntityType.Builder.of(OneFarmBlockEntity::new, ONE_FARM_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<BreederBlockEntity>> BREEDER_BLOCK_ENTITY = BLOCK_ENTITIES.register("breeder_be", () -> BlockEntityType.Builder.of(BreederBlockEntity::new, BREEDER_BLOCK.get()).build(null));
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OneFarmMod.MODID);

    public static RegistryObject<CreativeModeTab> OB_TAB = CREATIVE_MODE_TABS.register("ob_tab", () ->
            CreativeModeTab.builder().icon(ONE_FARM_BLOCK_ITEM.get()::getDefaultInstance)
                    .title(Component.translatable("itemGroup.onefarmmod"))
                    .displayItems((featureFlags, output) -> {
                        output.accept(ONE_FARM_BLOCK.get());
                        output.accept(BREEDER_BLOCK.get());
                    }).build());
    public OneFarmMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        ModMenuTypes.MENUS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);


        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
/*        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS){
            event.accept(ONE_FARM_BLOCK);
            event.accept(BREEDER_BLOCK);
        }*/
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            event.enqueueWork(() -> {
                BlockEntityRenderers.register(ONE_FARM_BLOCK_ENTITY.get(), OneFarmBlockEntityRenderer::new);
                MenuScreens.register(ModMenuTypes.BREEDER_MENU.get(), BreederScreen::new);
            });
        }

        @SubscribeEvent
        public void registerBindings(RegisterKeyMappingsEvent event){
            event.register(ModKeyBindings.INSTANCE.detailsKey);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientForgeEvents
    {
        @SubscribeEvent
        public static void postRenderStatge(RenderLevelStageEvent event){
            if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES){
                OneFarmBlockEntityRenderer.renderEntireBatch(event.getLevelRenderer(), event.getPoseStack(), event.getRenderTick(), event.getCamera(), event.getPartialTick());
            }
        }
    }


}
