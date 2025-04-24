package com.prohitman.onefarmmod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OneFarmMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static ForgeConfigSpec COMMON_CONFIG;

    public static final ForgeConfigSpec.IntValue farmGenTicks;
    public static final ForgeConfigSpec.IntValue breedGenTicks;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.push("Configs");
        farmGenTicks = COMMON_BUILDER.comment("The amount of ticks it takes for the farm block to generate loot (each second is 20 ticks).")
                .worldRestart()
                .defineInRange("farmGenTicks", 200, 0, 10000);
        breedGenTicks = COMMON_BUILDER.comment("The amount of ticks it takes for the breeder block to generate the spawn eggs (each second is 20 ticks).")
                .defineInRange("breedGenTicks", 200, 0, 10000);

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
