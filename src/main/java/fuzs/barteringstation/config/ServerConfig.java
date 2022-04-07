package fuzs.barteringstation.config;

import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.annotation.Config;

public class ServerConfig extends AbstractConfig {
    @Config(description = "Range on xz plane trading post should search for merchants.")
    @Config.IntRange(min = 1, max = 96)
    public int horizontalRange = 24;
    @Config(description = "Range on y axis trading post should search for merchants.")
    @Config.IntRange(min = 1, max = 96)
    public int verticalRange = 16;

    public ServerConfig() {
        super("");
    }
}
