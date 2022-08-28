package fuzs.barteringstation.config;

import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.annotation.Config;

public class ServerConfig extends AbstractConfig {
    @Config(description = "Range on xz plane trading post should search for merchants.")
    @Config.IntRange(min = 1, max = 96)
    public int horizontalRange = 8;
    @Config(description = "Range on y axis trading post should search for merchants.")
    @Config.IntRange(min = 1, max = 96)
    public int verticalRange = 3;
    @Config(description = "Min delay in ticks after a bartering station tries to give gold ingots to piglins again. Bartering itself (the piglin inspecting the gold ingot) takes 120 ticks.")
    public int barterDelay = 300;

    public ServerConfig() {
        super("");
    }
}
