package dev.jdm.full_slabs.mixin.block;

import dev.jdm.full_slabs.compatibility.MoGlass;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import net.wurstclient.glass.GlassSlabBlock;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GlassSlabBlock.class)
public class GlassSlabBlockMixin {

    @Inject(method = "isInvisibleToGlassSlab", at=@At("HEAD"), cancellable = true)
    private void isInvisibleToGlassSlab(BlockState blockState_1, BlockState blockState_2, Direction direction_1, CallbackInfoReturnable<Boolean> cir) {
        boolean res = MoGlass.isVisibleToGlassSlabCompat(blockState_1, blockState_2);
        if(!res)
            cir.setReturnValue(false);
    }
}

