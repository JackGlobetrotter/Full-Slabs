package dev.micalobia.full_slabs.util;

import com.mojang.serialization.Lifecycle;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.HashSet;
import java.util.Set;

public class TiltedSlabs {
	private static final Set<Identifier> tiltedSlabs = new HashSet<>();

	public static void register(Block tiltedSlab) {
		tiltedSlabs.add(Helper.fetchId(tiltedSlab));
	}

	public static void register(Identifier tiltedSlab) {
		tiltedSlabs.add(tiltedSlab);
	}

	public static boolean contains(Block slab) {
		return tiltedSlabs.contains(Helper.fetchId(slab));
	}

	public static boolean contains(Identifier slab) {
		return tiltedSlabs.contains(slab);
	}

	static {
		register(Blocks.SMOOTH_STONE_SLAB);
	}
}