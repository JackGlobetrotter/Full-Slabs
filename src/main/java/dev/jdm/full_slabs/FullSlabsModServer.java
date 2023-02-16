package dev.jdm.full_slabs;

import dev.jdm.full_slabs.config.CustomControls;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;


@Environment(EnvType.SERVER)
public class FullSlabsModServer implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		CustomControls.serverInit();
	}
}
