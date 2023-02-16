package dev.micalobia.full_slabs.client.render.model;

import dev.micalobia.full_slabs.block.ExtraSlabBlock;
import dev.micalobia.full_slabs.block.SlabBlockUtility;
import dev.micalobia.full_slabs.block.entity.ExtraSlabBlockEntity;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.joml.Vector3f;
import net.minecraft.world.BlockRenderView;

import net.minecraft.util.math.random.Random;

import java.util.function.Function;
import java.util.function.Supplier;

public class ExtraSlabModel extends BasicModel {
	@SuppressWarnings("unchecked")
	@Override
	public void emitBlockQuads(BlockRenderView view, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		ExtraSlabBlockEntity entity = (ExtraSlabBlockEntity) view.getBlockEntity(pos);
		if(entity == null) return;
		Direction direction = SlabBlockUtility.getDirection(state.get(ExtraSlabBlock.TYPE), state.get(ExtraSlabBlock.AXIS));
		BlockState baseState = entity.getBaseState();
		BlockState extraState = entity.getExtraState();
		emitModel(view, baseState, pos, randomSupplier, context);
		if(extraState != null) {
			// This section just translates the extra model half a block
			Vector3f unit = direction.getOpposite().getUnitVector();
			unit.normalize(0.5f); // Half normal of inner slab face
			context.pushTransform((quad) -> {
				for(int i = 0; i < 4; ++i) {
					Vector3f vec = quad.copyPos(i, null);
					vec.add(unit);
					quad.pos(i, vec);
				}
				return true;
			});
			emitModel(view, extraState, pos, randomSupplier, context);
			context.popTransform();
		}
	}

	@Override
	public void setParents(Function<Identifier, UnbakedModel> modelLoader) {
		// TODO Auto-generated method stub
		
	}
}

