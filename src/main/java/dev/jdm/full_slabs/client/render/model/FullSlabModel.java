package dev.jdm.full_slabs.client.render.model;

import dev.jdm.full_slabs.block.entity.FullSlabBlockEntity;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import net.minecraft.util.math.random.Random;

import java.util.function.Function;
import java.util.function.Supplier;

public class FullSlabModel extends BasicModel {
	
	@Override
	public void emitBlockQuads(BlockRenderView view, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		FullSlabBlockEntity entity = (FullSlabBlockEntity) view.getBlockEntity(pos);
		if(entity == null) return;
		BlockState positiveState = entity.getPositiveSlabState();
		BlockState negativeState = entity.getNegativeSlabState();
		emitModel(view, positiveState, pos, randomSupplier, context);
		emitModel(view, negativeState, pos, randomSupplier, context);
	}

	@Override
	public void setParents(Function<Identifier, UnbakedModel> modelLoader) {
		// TODO Auto-generated method stub
		
	}
}
