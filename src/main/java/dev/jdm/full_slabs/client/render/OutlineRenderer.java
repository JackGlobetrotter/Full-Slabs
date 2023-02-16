package dev.jdm.full_slabs.client.render;

import dev.jdm.full_slabs.FullSlabsMod;
import dev.jdm.full_slabs.block.ExtraSlabBlock;
import dev.jdm.full_slabs.block.FullSlabBlock;
import dev.jdm.full_slabs.block.SlabBlockUtility;
import dev.jdm.full_slabs.block.entity.ExtraSlabBlockEntity;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext.BlockOutlineContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.Objects;

public class OutlineRenderer {

	private static boolean renderSlabOutline(WorldRenderContext renderContext, BlockOutlineContext outlineContext) {
		if (outlineContext.blockState().getBlock() instanceof SlabBlock) {

			renderSlabOutlineShape(renderContext, outlineContext.blockState(), outlineContext.blockPos(),
					renderContext.world(), renderContext.camera().getPos(), FullSlabType.Slab);

			return false;
		}
		return true;
	}

	private static boolean renderFullSlabOutline(WorldRenderContext renderContext, BlockOutlineContext outlineContext) {
		if (outlineContext.blockState().isOf(FullSlabsMod.FULL_SLAB_BLOCK)) {

			renderSlabOutlineShape(renderContext, outlineContext.blockState(), outlineContext.blockPos(),
					renderContext.world(), renderContext.camera().getPos(), FullSlabType.FullSlab);

			return false;
		}
		return true;
	}

	private static boolean renderExtraSlabOutline(WorldRenderContext renderContext,
			BlockOutlineContext outlineContext) {
		if (outlineContext.blockState().isOf(FullSlabsMod.EXTRA_SLAB_BLOCK)) {

			renderSlabOutlineShape(renderContext, outlineContext.blockState(), outlineContext.blockPos(),
					renderContext.world(), renderContext.camera().getPos(), FullSlabType.ExtraSlab);

			return false;
		}
		return true;
	}

	private enum FullSlabType {
		Slab, FullSlab, ExtraSlab
	}

	private static void renderSlabOutlineShape(WorldRenderContext renderContext, BlockState state, BlockPos pos,
			BlockView world, Vec3d cam, FullSlabType type) {

		MatrixStack.Entry entry = renderContext.matrixStack().peek();
		var normalmatrix = entry.getNormalMatrix();
		var shape = Objects.requireNonNull(renderContext.consumers()).getBuffer(RenderLayer.LINES);
		var matrix = entry.getPositionMatrix();

		VoxelShape shapeToRender = type == FullSlabType.Slab ? getRenderedSlabOutlineShape(state, pos)
				: type == FullSlabType.FullSlab ? getRenderedFullSlabOutlineShape(state, pos)
						: getRenderedExtraSlabOutlineShape(state, pos, world);

		shapeToRender
				.forEachEdge((startX, startY, startZ, endX, endY, endZ) -> {
					float nx = (float) (endX - startX);
					float ny = (float) (endY - startY);
					float nz = (float) (endZ - startZ);
					shape
							.vertex(matrix, (float) (startX + pos.getX() - cam.getX()),
									(float) (startY + pos.getY() - cam.getY()),
									(float) (startZ + pos.getZ() - cam.getZ()))
							.color(0f, 0f, 0f, 0.4f)
							.normal(normalmatrix, nx, ny, nz)
							.next();
					shape
							.vertex(matrix, (float) (endX + pos.getX() - cam.getX()),
									(float) (endY + pos.getY() - cam.getY()),
									(float) (endZ + pos.getZ() - cam.getZ()))
							.color(0f, 0f, 0f, 0.4f)
							.normal(normalmatrix, nx, ny, nz)
							.next();
				});
	}

	private static VoxelShape getRenderedFullSlabOutlineShape(BlockState state, BlockPos pos) {
		HitResult hitResult = MinecraftClient.getInstance().crosshairTarget;
		if (hitResult == null)
			return VoxelShapes.fullCube();
		return SlabBlockUtility
				.getShape(SlabBlockUtility.getDirection(state.get(FullSlabBlock.AXIS), hitResult.getPos(), pos));
	}

	private static VoxelShape getRenderedSlabOutlineShape(BlockState state, BlockPos pos) {
		SlabType type = state.get(SlabBlock.TYPE);
		Direction direction;
		if (type == SlabType.DOUBLE) {
			HitResult hitResult = MinecraftClient.getInstance().crosshairTarget;
			if (hitResult == null)
				return VoxelShapes.fullCube();
			direction = SlabBlockUtility.getDirection(state.get(Properties.AXIS), hitResult.getPos(), pos);
		} else
			direction = SlabBlockUtility.getDirection(type, state.get(Properties.AXIS));
		return SlabBlockUtility.getShape(direction);
	}

	private static VoxelShape getRenderedExtraSlabOutlineShape(BlockState state, BlockPos pos, BlockView world) {
		HitResult hitResult = MinecraftClient.getInstance().crosshairTarget;
		if (hitResult == null)
			return VoxelShapes.fullCube();
		SlabType type = state.get(ExtraSlabBlock.TYPE);
		Axis axis = state.get(ExtraSlabBlock.AXIS);
		Direction slabDir = SlabBlockUtility.getDirection(type, axis);
		Direction hitDir = SlabBlockUtility.getDirection(axis, hitResult.getPos(), pos, type);

		if (slabDir == hitDir)
			return SlabBlockUtility.getShape(slabDir);
		ExtraSlabBlockEntity entity = (ExtraSlabBlockEntity) world.getBlockEntity(pos);
		if (entity == null)
			return SlabBlockUtility.getShape(hitDir);
		return entity.getExtraOutlineShape(world, pos, ShapeContext.absent()); // return VoxelShapes.fullCube(); ???? Maybe render dotted outline ? 
	}

	public static void init() {
		WorldRenderEvents.BLOCK_OUTLINE.register(OutlineRenderer::renderSlabOutline);
		WorldRenderEvents.BLOCK_OUTLINE.register(OutlineRenderer::renderFullSlabOutline);
		WorldRenderEvents.BLOCK_OUTLINE.register(OutlineRenderer::renderExtraSlabOutline);
	}
}
