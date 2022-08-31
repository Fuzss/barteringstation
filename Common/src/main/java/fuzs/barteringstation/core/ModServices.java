package fuzs.barteringstation.core;

import fuzs.puzzleslib.core.CoreServices;
import fuzs.puzzleslib.util.PuzzlesUtil;

public class ModServices extends CoreServices {
    public static final CommonAbstractions ABSTRACTIONS = PuzzlesUtil.loadServiceProvider(CommonAbstractions.class);
}
