package com.prohitman.onefarmmod.blocks.item;

import com.prohitman.onefarmmod.blocks.BreederBlock;
import com.prohitman.onefarmmod.blocks.OneFarmBlock;
import com.prohitman.onefarmmod.client.ModKeyHandler;
import com.prohitman.onefarmmod.client.keybindings.ModKeyBindings;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModBlockItem extends BlockItem {
    public ModBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        if(ModKeyHandler.isKeyPressed(ModKeyBindings.INSTANCE.detailsKey)){
            if(this.getBlock() instanceof OneFarmBlock){
                pTooltip.add(Component.translatable("item.tooltip.onefarmblock"));
            } else if(this.getBlock() instanceof BreederBlock){
                pTooltip.add(Component.translatable("item.tooltip.breederblock"));
            }
        } else {
            pTooltip.add(Component.translatable("item.tooltip.press_shift"));
        }
    }
}
