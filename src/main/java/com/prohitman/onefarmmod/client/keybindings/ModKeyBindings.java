package com.prohitman.onefarmmod.client.keybindings;

import com.mojang.blaze3d.platform.InputConstants;
import com.prohitman.onefarmmod.OneFarmMod;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static final ModKeyBindings INSTANCE = new ModKeyBindings();

    private ModKeyBindings() {}

    private static final String CATEGORY = "key.categories." + OneFarmMod.MODID;

    public final KeyMapping detailsKey =
            new KeyMapping(
                    "key.onefarmmod.detailskey",
                    KeyConflictContext.GUI,
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_LEFT_SHIFT,
                    "key.categories.onefarmmod");
}
