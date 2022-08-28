package fuzs.barteringstation.config;

import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

public class ClientConfig implements ConfigCore {
    @Config(description = "Way of showing current cooldown of a bartering station.")
    public CooldownRenderType cooldownRenderType = CooldownRenderType.ARROWS;

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
