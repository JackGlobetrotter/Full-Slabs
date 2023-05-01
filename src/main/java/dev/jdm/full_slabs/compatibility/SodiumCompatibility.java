package dev.jdm.full_slabs.compatibility;

import dev.jdm.full_slabs.FullSlabsMod;
import net.fabricmc.loader.api.FabricLoader;

public class SodiumCompatibility {

    public static boolean load(){
        FabricLoader loader = FabricLoader.getInstance();
        if (loader.isModLoaded("sodium") && !loader.isModLoaded("indium")) {
            String issue = "Fullslabs mod requires Indium mod to be loaded if Sodium mod is used";
            String action = "Include Indium mod to make Fullslabs mod work with Sodium";

            FullSlabsMod.LOGGER.error(issue);
            FullSlabsMod.LOGGER.error(action);
            throw new RuntimeException(issue + ". " + action);
        }
        return true;
    }
}
