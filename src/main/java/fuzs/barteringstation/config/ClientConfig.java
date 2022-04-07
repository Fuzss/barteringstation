package fuzs.barteringstation.config;

import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.annotation.Config;

public class ClientConfig extends AbstractConfig {
    @Config(description = "Way of showing current cooldown of a bartering station.")
    public CooldownRenderType cooldownRenderType = CooldownRenderType.NONE;

    public ClientConfig() {
        super("");
    }

    public enum CooldownRenderType {
        NONE, OVERLAY, ARROWS, BOTH;

        public boolean arrows() {
            return this == ARROWS || this == BOTH;
        }

        public boolean overlay() {
            return this == OVERLAY || this == BOTH;
        }
    }
}
