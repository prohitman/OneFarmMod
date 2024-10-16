package com.prohitman.onefarmmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.prohitman.onefarmmod.OneFarmMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class HologramOverlayLayer extends RenderLayer {
    public static final ResourceLocation OVERLAY = new ResourceLocation(OneFarmMod.MODID, "textures/entity/overlay_light_blue.png");

    public HologramOverlayLayer(RenderLayerParent pRenderer) {
        super(pRenderer);

    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, Entity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.eyes(OVERLAY));
        this.getParentModel().renderToBuffer(pPoseStack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.5F);

    }
}
