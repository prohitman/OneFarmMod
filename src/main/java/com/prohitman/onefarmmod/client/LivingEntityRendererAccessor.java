package com.prohitman.onefarmmod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;

public interface LivingEntityRendererAccessor {
    void scaleForHologram(LivingEntity entity, PoseStack poseStack, float partialTicks);
}
