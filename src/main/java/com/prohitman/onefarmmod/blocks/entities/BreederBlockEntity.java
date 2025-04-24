package com.prohitman.onefarmmod.blocks.entities;

import com.prohitman.onefarmmod.Config;
import com.prohitman.onefarmmod.OneFarmMod;
import com.prohitman.onefarmmod.blocks.BreederBlock;
import com.prohitman.onefarmmod.client.menu.BreederMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.items.ItemStackHandler;

public class BreederBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private final ItemStackHandler itemHandler = new ItemStackHandler(1);
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        protected void onOpen(Level p_155062_, BlockPos p_155063_, BlockState p_155064_) {
            BreederBlockEntity.this.playSound(p_155064_, SoundEvents.ENDER_CHEST_OPEN);
            BreederBlockEntity.this.updateBlockState(p_155064_, true);
        }

        protected void onClose(Level p_155072_, BlockPos p_155073_, BlockState p_155074_) {
            BreederBlockEntity.this.playSound(p_155074_, SoundEvents.ENDER_CHEST_CLOSE);
            BreederBlockEntity.this.updateBlockState(p_155074_, false);
        }

        protected void openerCountChanged(Level p_155066_, BlockPos p_155067_, BlockState p_155068_, int p_155069_, int p_155070_) {
        }

        protected boolean isOwnContainer(Player p_155060_) {
            if (p_155060_.containerMenu instanceof BreederMenu) {
                return true;
            } else {
                return false;
            }
        }
    };

    public int ticksUntilGen = 0;
    public int maxTicks = Config.breedGenTicks.get();

    public BreederBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(OneFarmMod.BREEDER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BreederBlockEntity entity){
        entity.ticksUntilGen++;
        if(level.getBestNeighborSignal(pos) > 0 && entity.ticksUntilGen > entity.maxTicks && (entity.isEmpty() || entity.getItem(0).getCount() < entity.getItem(0).getItem().getMaxStackSize())){
            for(Direction direction : Direction.values()){
                if(direction != Direction.UP && direction != Direction.DOWN){
                    if(level.getBlockEntity(pos.relative(direction)) instanceof OneFarmBlockEntity oneFarmBlock
                            && level.getBlockEntity(pos.relative(direction.getOpposite())) instanceof OneFarmBlockEntity oneFarmBlock1
                            && oneFarmBlock.getEntityType() != null && oneFarmBlock1.getEntityType() != null
                            && oneFarmBlock.getEntityType() == oneFarmBlock1.getEntityType()){
                        SpawnEggItem spawnEggItem = ForgeSpawnEggItem.fromEntityType(oneFarmBlock.getEntityType());

                        if(spawnEggItem != null && (entity.getItem(0).is(spawnEggItem) || entity.isEmpty())){
                            ItemStack stack = new ItemStack(spawnEggItem, entity.getItem(0).getCount() + 1);
                            entity.setItem(0, stack);
                            entity.markUpdated();
                            entity.ticksUntilGen = 0;
                            break;
                        }
                    }
                }
            }
        }
    }

    public void load(CompoundTag tag) {
        super.load(tag);

        this.ticksUntilGen = tag.getInt("TicksUntilGen");

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("TicksUntilGen", ticksUntilGen);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items);
        }
    }
    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public void setRemoved() {
        super.setRemoved();
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getContainerSize() {
        return 1;
    }

    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    protected void setItems(NonNullList<ItemStack> pItems) {
        this.items = pItems;
    }

    protected Component getDefaultName() {
        return Component.translatable("block.onefarmmod.breeder_block");
    }

    protected AbstractContainerMenu createMenu(int pId, Inventory pPlayer) {
        return new BreederMenu(pId, pPlayer, this);
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
        this.level.setBlock(this.getBlockPos(), pState.setValue(BreederBlock.OPEN, Boolean.valueOf(pOpen)), 3);
    }

    void playSound(BlockState pState, SoundEvent pSound) {
        //Vec3i vec3i = pState.getValue(CacheBlock.FACING).getNormal();
        double d0 = (double)this.worldPosition.getX() + 0.5D  / 2.0D;
        double d1 = (double)this.worldPosition.getY() + 0.5D  / 2.0D;
        double d2 = (double)this.worldPosition.getZ() + 0.5D  / 2.0D;
        this.level.playSound((Player)null, d0, d1, d2, pSound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }
}
