package dev.micalobia.full_slabs.mixin.item;

import net.minecraft.block.Block;
import net.minecraft.item.VerticallyAttachableBlockItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VerticallyAttachableBlockItem.class)
public interface VerticallyAttachableBlockItemAccessor {
	@Accessor("wallBlock")
	Block wallBlock();
}
