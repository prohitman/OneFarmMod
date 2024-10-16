package com.prohitman.onefarmmod.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class VertexConsumerWrapper implements VertexConsumer {
    final VertexConsumer real;
    float alphaMultiplier;

    VertexConsumerWrapper(VertexConsumer real, float alphaMultiplier) {
        this.real = real;
        this.alphaMultiplier = alphaMultiplier;
    }

    @Override
    public VertexConsumer vertex(double pX, double pY, double pZ) {
        real.vertex(pX, pY, pZ);
        return this;
    }

    @Override
    public VertexConsumer color(int r, int g, int b, int a) {
        real.color(r, g, b, (int)(a * alphaMultiplier));

        return this;
    }

    @Override
    public VertexConsumer uv(float pU, float pV) {
        real.uv(pU, pV);
        return this;
    }

    @Override
    public VertexConsumer overlayCoords(int pU, int pV) {
        real.overlayCoords(pU, pV);
        return this;
    }

    @Override
    public VertexConsumer uv2(int pU, int pV) {
        real.uv2(pU, pV);
        return this;
    }

    @Override
    public VertexConsumer normal(float pX, float pY, float pZ) {
        real.normal(pX, pY, pZ);
        return this;
    }

    @Override
    public void endVertex() {
        real.endVertex();
    }

    @Override
    public void defaultColor(int pDefaultR, int pDefaultG, int pDefaultB, int pDefaultA) {
        real.defaultColor(pDefaultR, pDefaultG, pDefaultB, pDefaultA);
    }

    @Override
    public void unsetDefaultColor() {
        real.unsetDefaultColor();

    }
    // other methods just call real.whatever and return this

    static class Source implements MultiBufferSource {
        MultiBufferSource real;
        float alphaMultiplier;
        ResourceLocation location;

        Source(MultiBufferSource real, float alphaMultiplier, ResourceLocation location) {
            this.real = real;
            this.alphaMultiplier = alphaMultiplier;
            this.location = location;
        }

        /*@Override
        public VertexConsumer getBuffer(RenderType pRenderType) {
            return new VertexConsumerWrapper(real.getBuffer(RenderType.entityTranslucent(this.location, true)), alphaMultiplier);
        }*/
        @Override
        public VertexConsumer getBuffer(RenderType pRenderType) {
            RenderType wrappedType = new RenderType(pRenderType.toString() + "_translucent", pRenderType.format(), pRenderType.mode(), pRenderType.bufferSize(), pRenderType.affectsCrumbling(), true,
                    () -> {
                        pRenderType.setupRenderState();
                        RenderSystem.setShader(GameRenderer::getRendertypeEntityTranslucentShader);
                    }
                    , () -> pRenderType.clearRenderState()) {

            };
            return new VertexConsumerWrapper(real.getBuffer(wrappedType), alphaMultiplier);
        }
    }
}
