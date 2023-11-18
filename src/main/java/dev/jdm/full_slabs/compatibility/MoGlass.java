package dev.jdm.full_slabs.compatibility;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Collections;

public class MoGlass {
    public static boolean isVisibleToGlassSlabCompat(BlockState blockState_1, BlockState blockState_2) { //TODO: only a temporary fix to disable inter-slab culling, should get the block position to determine weather walls should be rendered
        Direction.Axis axis1 = blockState_1.get(Properties.AXIS);
        Direction.Axis axis2 = blockState_2.get(Properties.AXIS);

        Collection<Property<?>> t =  blockState_1.getProperties();

        SlabType type1 = blockState_1.get(SlabBlock.TYPE);
        SlabType type2 = blockState_1.get(SlabBlock.TYPE);

        if(type1 == SlabType.DOUBLE || type2 == SlabType.DOUBLE ) return false; //TODO: Work on more specific conditions
        if(type1 == type2  && axis1 == axis2 && axis1 != Direction.Axis.Y)
            return true;

        return false;
    }
}
