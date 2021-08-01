package withoutaname.mods.withoutio.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
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
import withoutaname.mods.withoutio.setup.Registration;

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
		final Direction dir = pContext.getHorizontalDirection().getOpposite();
		final Level level = pContext.getLevel();
		final BlockPos pos = pContext.getClickedPos();
		final ScreenBlock block = Registration.SCREEN_BLOCK.get();
		return this.defaultBlockState()
				.setValue(FACING, dir)
				.setValue(FRAME_TOP, !level
						.getBlockState(pos.above())
						.is(block))
				.setValue(FRAME_BOTTOM, !level
						.getBlockState(pos.below())
						.is(block))
				.setValue(FRAME_RIGHT, !level
						.getBlockState(pos.relative(dir.getCounterClockWise()))
						.is(block))
				.setValue(FRAME_LEFT, !level
						.getBlockState(pos.relative(dir.getClockWise()))
						.is(block));
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pPos, @Nonnull BlockState pState) {
		return new ScreenEntity(pPos, pState);
	}
	
	@Nonnull
	@Override
	public RenderShape getRenderShape(@Nonnull BlockState pState) {
		return RenderShape.MODEL;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING, FRAME_TOP, FRAME_BOTTOM, FRAME_RIGHT, FRAME_LEFT);
	}
}
