package withoutaname.mods.withoutio.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import withoutaname.mods.withoutio.setup.Registration;
import withoutaname.mods.withoutio.tools.ScreenController;

import javax.annotation.Nonnull;

public class ScreenEntity extends BlockEntity {
	
	private ScreenController controller;
	
	public ScreenEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		super(Registration.SCREEN_ENTITY.get(), pWorldPosition, pBlockState);
		controller = new ScreenController(pBlockState.getValue(ScreenBlock.FACING), pWorldPosition);
	}
	
	public void updateController() {
		for (Direction dir : getDirs()) {
			assert level != null;
			BlockEntity entity = level.getBlockEntity(getBlockPos().relative(dir));
			if (entity instanceof ScreenEntity screenEntity)
				screenEntity.controller.tryExpand(dir.getOpposite());
		}
	}
	
	@Override
	public void setLevel(Level level) {
		super.setLevel(level);
		if (!level.isClientSide) {
			controller.setLevel(level, () -> level.getBlockTicks().scheduleTick(worldPosition, getBlockState().getBlock(), 0));
		}
	}
	
	@Override
	public void setRemoved() {
		if (level == null || !level.isClientSide) {
			controller.remove(worldPosition);
		}
		super.setRemoved();
	}
	
	public void setController(ScreenController controller) {
		if ((level == null || !level.isClientSide) && this.controller != controller) {
			this.controller = controller;
			updateFrame();
		}
	}
	
	public ScreenController getController() {
		return controller;
	}
	
	private void updateFrame() {
		assert level != null;
		BlockState newState = getBlockState();
		for (Direction dir : getDirs()) {
			BlockState blockState = level.getBlockState(worldPosition.relative(dir));
			boolean isScreen = blockState.is(Registration.SCREEN_BLOCK.get());
			boolean shouldBeFrame = !(isScreen
					&& level.getBlockEntity(worldPosition.relative(dir)) instanceof ScreenEntity screenEntity
					&& screenEntity.controller == controller);
			Direction facing = getBlockState().getValue(ScreenBlock.FACING);
			if (isScreen) {
				level.setBlockAndUpdate(
						worldPosition.relative(dir),
						blockState.setValue(ScreenBlock.getFrameProperty(facing, dir.getOpposite()), shouldBeFrame)
				);
			}
			newState = newState.setValue(ScreenBlock.getFrameProperty(facing, dir), shouldBeFrame);
		}
		level.setBlockAndUpdate(worldPosition, newState);
	}
	
	@Nonnull
	private Direction[] getDirs() {
		return new Direction[]{
				Direction.UP,
				getBlockState().getValue(ScreenBlock.FACING).getCounterClockWise(),
				Direction.DOWN,
				getBlockState().getValue(ScreenBlock.FACING).getClockWise()
		};
	}
	
	@Override
	public CompoundTag save(CompoundTag tag) {
		if (controller.getTopLeftCorner().equals(worldPosition)) {
			tag.put("controller", controller.save());
		}
		return super.save(tag);
	}
	
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("controller")) {
			controller.load(tag.getCompound("controller"));
		}
	}
}
