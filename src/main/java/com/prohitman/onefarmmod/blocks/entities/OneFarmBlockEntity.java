package com.prohitman.onefarmmod.blocks.entities;

import com.prohitman.onefarmmod.OneFarmMod;
import com.prohitman.onefarmmod.blocks.OneFarmBlock;
import com.prohitman.onefarmmod.loottables.LootUtil;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class OneFarmBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
    private final ItemStackHandler itemHandler = new ItemStackHandler(27);
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        protected void onOpen(Level p_155062_, BlockPos p_155063_, BlockState p_155064_) {
            OneFarmBlockEntity.this.playSound(p_155064_, SoundEvents.FROG_HURT);
            OneFarmBlockEntity.this.updateBlockState(p_155064_, true);
        }

        protected void onClose(Level p_155072_, BlockPos p_155073_, BlockState p_155074_) {
            OneFarmBlockEntity.this.playSound(p_155074_, SoundEvents.ALLAY_AMBIENT_WITH_ITEM);
            OneFarmBlockEntity.this.updateBlockState(p_155074_, false);
        }

        protected void openerCountChanged(Level p_155066_, BlockPos p_155067_, BlockState p_155068_, int p_155069_, int p_155070_) {
        }

        protected boolean isOwnContainer(Player p_155060_) {
            if (p_155060_.containerMenu instanceof ChestMenu) {
                Container container = ((ChestMenu)p_155060_.containerMenu).getContainer();
                return container == OneFarmBlockEntity.this;
            } else {
                return false;
            }
        }
    };

    public int tickCount;
    private EntityType<?> entityType;
    private CompoundTag entityTag;
    private Entity displayEntity;
    private Entity prevDisplayEntity;
    private float prevSwitchProgress;
    private float switchProgress;
    public float previousRotation;
    public float rotation;
    private UUID displayUUID;
    private UUID prevDisplayUUID;
    public int ticksUntilNextGen;

    public OneFarmBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(OneFarmMod.ONE_FARM_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, OneFarmBlockEntity entity){
        //System.out.println("Previous Rotation: " + entity.previousRotation);
        //System.out.println("Current Rotation: " + entity.rotation);
        entity.tickCount++;
        //entity.prevSwitchProgress = entity.switchProgress;
        entity.previousRotation = entity.rotation;
        if(entity.displayEntity == null){
            entity.displayEntity = entity.getDisplayEntity(level);
        }

        if (entity.prevDisplayUUID != entity.displayUUID) {
            /*if (entity.switchProgress < 10.0F) {
                if (entity.switchProgress == 0) {
                    level.playSound((Player) null, pos, SoundEvents.GOAT_AMBIENT, SoundSource.BLOCKS);
                }
                entity.switchProgress++;
            } else {*/
                entity.prevDisplayEntity = entity.displayEntity;
                entity.prevDisplayUUID = entity.displayUUID;
                entity.markUpdated();
            //}
            if (!entity.isRemoved() && level.isClientSide) {
                //AlexsCaves.PROXY.playWorldSound(entity, (byte) 3);
            }
        }
        //System.out.println("Display??" + entity.displayEntity);

        if (entity.displayEntity != null) {
            //System.out.println("ticks??" + (entity.ticksUntilNextGen >= 100) + !level.isClientSide);

            if(entity.ticksUntilNextGen >= 100 && !level.isClientSide){
                //entity.markUpdated();
                //System.out.println("Generating loot...");

                LootTable entityLoot = LootUtil.getEntityLootTable(entity.displayEntity);
                //entityLoot.getParamSet();
                LootParams params = new LootParams.Builder((ServerLevel) level)
                        .withParameter(LootContextParams.ORIGIN, entity.getBlockPos().getCenter())
                        .withParameter(LootContextParams.THIS_ENTITY, entity.displayEntity)
                        .withParameter(LootContextParams.BLOCK_STATE, entity.getBlockState())
                        .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                        .withParameter(LootContextParams.BLOCK_ENTITY, entity)
                        .withParameter(LootContextParams.EXPLOSION_RADIUS, 0f)
                        .create(LootContextParamSets.BLOCK);
                List<ItemStack> items = entityLoot.getRandomItems(params);
                //System.out.println("Item generated: " + items);
                //entity.itemHandler.setStackInSlot(0, items.get(0));
                entity.fillItemsInSlots(items);
                entity.ticksUntilNextGen = 0;
            }
            if(entity.displayUUID != entity.displayEntity.getUUID()){
                entity.displayUUID = entity.displayEntity.getUUID();
                //entity.switchProgress = 0.0F;
            }
        }

        float redstoneSignal = level.getBestNeighborSignal(pos) * 1F;
        if (redstoneSignal > 0.0F) {
            //System.out.println("received signal!!");
            //entity.rotation = (entity.rotation + redstoneSignal)*0.3f;
            entity.rotation += 0.5f;
            entity.ticksUntilNextGen++;
        }
        //entity.rotation += 0.1f;
        entity.markUpdated();
        //System.out.println("Rotation: " + entity.rotation + "Prev Rotation: " + entity.previousRotation);
    }

    public void fillItemsInSlots(List<ItemStack> items){
        for(ItemStack stack : items){
            if(!stack.isEmpty()){
                for(int i=0; i<this.getContainerSize(); i++){
                    if(this.getItem(i).is(stack.getItem()) && (this.getItem(i).getCount() + stack.getCount()) <= stack.getItem().getMaxStackSize()){
                        stack.setCount(stack.getCount() + this.getItem(i).getCount());
                        this.setItem(i, stack);
                        break;
                    } else if(this.getItem(i).isEmpty()){
                        this.setItem(i, stack);
                        break;
                    }
                }
            }
        }

        this.markUpdated();
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("EntityType")) {
            String str = tag.getString("EntityType");
            this.entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(str));
        }
        if (tag.contains("EntityTag")) {
            this.entityTag = tag.getCompound("EntityTag");
        }
        this.rotation = tag.getFloat("Rotation");
        this.ticksUntilNextGen = tag.getInt("NextGen");

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.entityType != null) {
            tag.putString("EntityType", ForgeRegistries.ENTITY_TYPES.getKey(this.entityType).toString());
        }
        if (this.entityTag != null) {
            tag.put("EntityTag", this.entityTag);
        }
        tag.putFloat("Rotation", this.rotation);
        tag.putFloat("PrevRotation", this.previousRotation);
        tag.putInt("NextGen", this.ticksUntilNextGen);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        BlockPos pos = this.getBlockPos();
        float f = displayEntity == null ? 1.0F : Math.max(displayEntity.getBbWidth(), displayEntity.getBbHeight());
        return new AABB(pos.offset(-1, -1, -1), pos.offset(2, 2, 2)).inflate(Math.max(f - 0.5F, 1F));
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        if (packet != null && packet.getTag() != null) {
            if (packet.getTag().contains("EntityType")) {
                String str = packet.getTag().getString("EntityType");
                this.entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(str));
            }
            this.entityTag = packet.getTag().getCompound("EntityTag");
            this.rotation = packet.getTag().getFloat("Rotation");
            this.previousRotation = packet.getTag().getFloat("PrevRotation");
            this.ticksUntilNextGen = packet.getTag().getInt("NextGen");
            this.tickCount = packet.getTag().getInt("TickCount");
        }
    }

    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag compoundtag = new CompoundTag();
        if (this.entityType != null) {
            compoundtag.putString("EntityType", ForgeRegistries.ENTITY_TYPES.getKey(this.entityType).toString());
        }
        if (this.entityTag != null) {
            compoundtag.put("EntityTag", this.entityTag);
        }
        compoundtag.putFloat("Rotation", this.rotation);
        compoundtag.putFloat("PrevRotation", this.previousRotation);
        compoundtag.putInt("NextGen", this.ticksUntilNextGen);
        compoundtag.putInt("TickCount", this.tickCount);
        return compoundtag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
    }

    public void setEntity(EntityType<?> entityType, CompoundTag entityTag, float playerRot) {
        System.out.println("Entity Set..." + entityType.getDescriptionId());
        this.entityType = entityType;
        this.entityTag = entityTag;
        this.rotation = playerRot;
        displayEntity = null;
        this.markUpdated();
    }

    public Entity getDisplayEntity(Level level) {
        if (displayEntity == null && entityType != null) {
            displayEntity = EntityType.loadEntityRecursive(entityTag, level, Function.identity());
        }
        if (displayEntity == null && prevDisplayEntity != null) {
            return prevDisplayEntity;
        }
        return displayEntity;
    }

    public float getSwitchAmount(float partialTicks) {
        return (prevSwitchProgress + (switchProgress - prevSwitchProgress) * partialTicks) * 0.1F;
    }

    public float getRotation(float partialTicks) {
        return (previousRotation + (rotation - previousRotation) * partialTicks);
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public void setRemoved() {
        //AlexsCaves.PROXY.clearSoundCacheFor(this);
        //level.playSound((Player) null, this.getBlockPos(), ACSoundRegistry.HOLOGRAM_STOP.get(), SoundSource.BLOCKS);
        super.setRemoved();
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getContainerSize() {
        return 27;
    }

    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    protected void setItems(NonNullList<ItemStack> pItems) {
        this.items = pItems;
    }

    protected Component getDefaultName() {
        return Component.translatable("block.onefarmmod.one_farm_block");
    }

    protected AbstractContainerMenu createMenu(int pId, Inventory pPlayer) {
        return ChestMenu.threeRows(pId, pPlayer, this);
    }

    public void startOpen(Player pPlayer) {
        if (!this.remove && !pPlayer.isSpectator()) {
            this.openersCounter.incrementOpeners(pPlayer, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    public void stopOpen(Player pPlayer) {
        if (!this.remove && !pPlayer.isSpectator()) {
            this.openersCounter.decrementOpeners(pPlayer, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    void updateBlockState(BlockState pState, boolean pOpen) {
        this.level.setBlock(this.getBlockPos(), pState.setValue(OneFarmBlock.OPEN, Boolean.valueOf(pOpen)), 3);
    }

    void playSound(BlockState pState, SoundEvent pSound) {
        //Vec3i vec3i = pState.getValue(CacheBlock.FACING).getNormal();
        double d0 = (double)this.worldPosition.getX() + 0.5D  / 2.0D;
        double d1 = (double)this.worldPosition.getY() + 0.5D  / 2.0D;
        double d2 = (double)this.worldPosition.getZ() + 0.5D  / 2.0D;
        this.level.playSound((Player)null, d0, d1, d2, pSound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }
}
