package withoutaname.mods.withoutio.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ScreenBlock extends BaseEntityBlock {
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty FRAME_TOP = BooleanProperty.create("frame_up");
	public static final BooleanProperty FRAME_BOTTOM = BooleanProperty.create("frame_down");
	public static final BooleanProperty FRAME_RIGHT = BooleanProperty.create("frame_right");
	public static final BooleanProperty FRAME_LEFT = BooleanProperty.create("frame_left");
	
	public ScreenBlock() {
		super(Properties.of(Material.METAL)
				.sound(SoundType.METAL)
				.strength(1.5F, 6.0F));
	}
	
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return updateFrame(pContext.getLevel(),
				pContext.getClickedPos(),
				this.defaultBlockState().setValue(FACING,
						pContext.getHorizontalDirection().getOpposite()));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
		pState = updateFrame(pLevel, pCurrentPos, pState);
		return super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
	}
	
	private BlockState updateFrame(LevelAccessor level, BlockPos pos, BlockState state) {
		Direction dir = state.getValue(FACING);
		return state
				.setValue(FRAME_TOP, shouldBeFrame(level, pos, dir, Direction.UP))
				.setValue(FRAME_BOTTOM, shouldBeFrame(level, pos, dir, Direction.DOWN))
				.setValue(FRAME_RIGHT, shouldBeFrame(level, pos, dir, dir.getCounterClockWise()))
				.setValue(FRAME_LEFT, shouldBeFrame(level, pos, dir, dir.getClockWise()));
	}
	
	private boolean shouldBeFrame(LevelAccessor level, BlockPos pos, Direction facing, Direction dir) {
		BlockState state = level.getBlockState(pos.relative(dir));
		return !(state.is(this) && state.getValue(FACING) == facing);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new ScreenEntity(pPos, pState);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public RenderShape getRenderShape(@Nonnull BlockState pState) {
		return RenderShape.MODEL;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING, FRAME_TOP, FRAME_BOTTOM, FRAME_RIGHT, FRAME_LEFT);
	}
}
