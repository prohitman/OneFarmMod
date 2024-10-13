package com.prohitman.onefarmmod.client.renderer;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class LightBlueOverlayTexture implements AutoCloseable{
    private final DynamicTexture texture = new DynamicTexture(16, 16, false);
    public LightBlueOverlayTexture() {
        NativeImage nativeimage = this.texture.getPixels();
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                if (i < 8) {
                    // Light blue color (0xADD8E6) with some transparency
                    nativeimage.setPixelRGBA(j, i, 0x80ADD8E6);
                } else {
                    int k = (int) ((1.0F - (float) j / 15.0F * 0.75F) * 255.0F);
                    nativeimage.setPixelRGBA(j, i, k << 24 | 0xADD8E6);
                }
            }
        }
        RenderSystem.activeTexture(33985);
        this.texture.bind();
        nativeimage.upload(0, 0, 0, 0, 0, nativeimage.getWidth(), nativeimage.getHeight(), false, true, false, false);
        RenderSystem.activeTexture(33984);
    }

    public void setupOverlayColor() {
        RenderSystem.setupOverlayColor(this.texture::getId, 16);
    }

    public void teardownOverlayColor() {
        RenderSystem.teardownOverlayColor();
    }

    @Override
    public void close() {
        this.texture.close();
    }
}
