package com.prohitman.onefarmmod.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.prohitman.onefarmmod.OneFarmMod;
import com.prohitman.onefarmmod.client.menu.BreederMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BreederScreen extends AbstractContainerScreen<BreederMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(OneFarmMod.MODID,
            "textures/gui/breeder_gui.png");
    public BreederScreen(BreederMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;

        this.imageHeight = 154;
        this.imageWidth = 175;
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, 175, 154);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
