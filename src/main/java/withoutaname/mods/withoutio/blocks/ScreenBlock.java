package withoutaname.mods.withoutio.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import withoutaname.mods.withoutio.setup.Registration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

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
		BlockEntity blockEntity = pContext.getLevel().getBlockEntity(pContext.getClickedPos());
		return this.defaultBlockState().setValue(FACING,
						pContext.getHorizontalDirection().getOpposite());
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean p_60570_) {
		if (!(oldState.is(Registration.SCREEN_BLOCK.get()) && oldState.getValue(FACING) == state.getValue(FACING))) {
			BlockEntity entity = level.getBlockEntity(pos);
			if (entity instanceof ScreenEntity screenEntity) {
				screenEntity.updateController();
			} else {
				throw new IllegalStateException("Couldn't update Controller!");
			}
		}
		super.onPlace(state, level, pos, oldState, p_60570_);
	}
	
	@Override
	public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
		super.tick(pState, pLevel, pPos, pRandom);
		if (pLevel.getBlockEntity(pPos) instanceof ScreenEntity entity) {
			entity.getController().update();
		}
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new ScreenEntity(pPos, pState);
	}
	
	@Override
	public RenderShape getRenderShape(@Nonnull BlockState pState) {
		return RenderShape.MODEL;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING, FRAME_TOP, FRAME_BOTTOM, FRAME_RIGHT, FRAME_LEFT);
	}
	
	public static Property<Boolean> getFrameProperty(Direction facing, Direction dir) {
		if (dir == Direction.UP) return FRAME_TOP;
		else if (dir == Direction.DOWN) return FRAME_BOTTOM;
		else if (dir == facing.getCounterClockWise()) return FRAME_RIGHT;
		else if (dir == facing.getClockWise()) return FRAME_LEFT;
		else throw new IllegalArgumentException("No frame in this direction!");
	}
}
