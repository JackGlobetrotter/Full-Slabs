package dev.jdm.full_slabs;

import com.google.gson.GsonBuilder;
import dev.jdm.full_slabs.block.ExtraSlabBlock;
import dev.jdm.full_slabs.block.FullSlabBlock;
import dev.jdm.full_slabs.block.entity.ExtraSlabBlockEntity;
import dev.jdm.full_slabs.block.entity.FullSlabBlockEntity;
import dev.jdm.full_slabs.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction.Axis;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import virtuoel.statement.api.StateRefresher;

import java.util.Set;


public class FullSlabsMod implements ModInitializer {
	public static final String MOD_ID = "full_slabs";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static BlockEntityType<FullSlabBlockEntity> FULL_SLAB_BLOCK_ENTITY;
	public static BlockEntityType<ExtraSlabBlockEntity> EXTRA_SLAB_BLOCK_ENTITY;
	public static Block FULL_SLAB_BLOCK;
	public static Block EXTRA_SLAB_BLOCK;
	public static Set<Identifier> TILTED_SLABS;

	public static boolean SODIUM_COMPATIBILITY;

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}

	private static <T extends ConfigData> GsonConfigSerializer<T> createConfigSerializer(Config definition, Class<T> configClass) {
		return new GsonConfigSerializer<>(definition, configClass, new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create());
	}

	@Override
	public void onInitialize() {
		FULL_SLAB_BLOCK = Registry.register(Registries.BLOCK, id("full_slab_block"), new FullSlabBlock(Settings.copy(Blocks.BEDROCK).nonOpaque().luminance(FullSlabBlock::stateToLuminance)));
		FULL_SLAB_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("full_slab"), FabricBlockEntityTypeBuilder.create(FullSlabBlockEntity::new, FULL_SLAB_BLOCK).build());

		EXTRA_SLAB_BLOCK = Registry.register(Registries.BLOCK, id("extra_slab_block"), new ExtraSlabBlock(Settings.copy(Blocks.BEDROCK).luminance(ExtraSlabBlock::stateToLuminance)));
		EXTRA_SLAB_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("extra_slab"), FabricBlockEntityTypeBuilder.create(ExtraSlabBlockEntity::new, EXTRA_SLAB_BLOCK).build());
		AutoConfig.register(ModConfig.class, FullSlabsMod::createConfigSerializer);
		TILTED_SLABS = AutoConfig.getConfigHolder(ModConfig.class).getConfig().getTiltableSlabs();

		injectBlockstateProperty(SlabBlock.class, Properties.AXIS, Axis.Y);
	}

	private static <B extends Block, P extends Comparable<P>> void injectBlockstateProperty(Class<B> class1, Property<P> property, P defaultValue) {
		Registries.BLOCK.forEach(block -> {
			if(class1.isAssignableFrom(block.getClass()))
				StateRefresher.INSTANCE.addBlockProperty(block, property, defaultValue);
		});
		RegistryEntryAddedCallback.event(Registries.BLOCK).register((rawId, id, block) -> {
			if(class1.isAssignableFrom(block.getClass()))
				StateRefresher.INSTANCE.addBlockProperty(block, property, defaultValue);
		});
		StateRefresher.INSTANCE.reorderBlockStates();
	}
	
}
