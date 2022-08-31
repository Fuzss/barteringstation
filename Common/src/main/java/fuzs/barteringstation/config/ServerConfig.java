package fuzs.barteringstation.config;

import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

public class ServerConfig implements ConfigCore {
    @Config(description = "Range on xz plane bartering station should search for available piglins.")
    @Config.IntRange(min = 1, max = 96)
    public int horizontalRange = 12;
    @Config(description = "Range on y axis bartering station should search for available piglins.")
    @Config.IntRange(min = 1, max = 96)
    public int verticalRange = 4;
    @Config(description = {"Delay in ticks after a bartering station tries to give gold ingots to piglins again. The piglin inspecting the gold ingot always takes half that time, the other half is a cool-down.", "Bartering manually in vanilla always takes 120 ticks, this option doesn't change that."})
    @Config.IntRange(min = 20)
    public int barterDelay = 300;
}
