package withoutaname.mods.withoutio.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import withoutaname.mods.withoutio.blocks.ScreenBlock;
import withoutaname.mods.withoutio.blocks.ScreenEntity;
import withoutaname.mods.withoutio.setup.Registration;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ScreenController {
	
	private final Direction facing;
	private final Direction right;
	private final Direction left;
	@Nullable
	private Level level;
	private BlockPos topLeftCorner;
	private int width = 1;
	private int height = 1;
	private boolean removed = false;
	private boolean shouldUpdate = false;
	
	public ScreenController(Direction facing, BlockPos pos) {
		this.facing = facing;
		right = facing.getCounterClockWise();
		left = facing.getClockWise();
		this.topLeftCorner = pos;
	}
	
	public void tryExpand(Direction dir) {
		if (removed) {
			throw new IllegalStateException("Controller is not active any more!");
		}
		BlockPos pos = topLeftCorner;
		BlockPos newTopLeftCorner = topLeftCorner;
		List<BlockPos> newBlocks = new ArrayList<>();
		if (dir == Direction.UP) {
			pos = pos.above();
			newTopLeftCorner = newTopLeftCorner.above();
		} else if (dir == Direction.DOWN) {
			pos = pos.below(height);
		} else if (dir == right) {
			pos = pos.relative(right, width);
		} else if (dir == left) {
			pos = pos.relative(left);
			newTopLeftCorner = newTopLeftCorner.relative(left);
		}
		assert level != null;
		for (int i = 0; i < (dir.getAxis() == Direction.Axis.Y ? width : height); i++) {
			BlockPos newBlockPos = pos.relative(dir.getAxis() == Direction.Axis.Y ? right : Direction.DOWN, i);
			if (!(level.getBlockState(newBlockPos).is(Registration.SCREEN_BLOCK.get())
					&& level.getBlockState(newBlockPos).getValue(ScreenBlock.FACING) == facing)) {
				return;
			}
			if (level.getBlockEntity(newBlockPos) instanceof ScreenEntity entity) {
				if (!entity.getController().removed) {
					ScreenController controller = entity.getController();
					if (dir.getAxis() == Direction.Axis.Y) {
						if (controller.topLeftCorner.get(left.getAxis()) * left.getAxisDirection().getStep() > topLeftCorner.get(left.getAxis()) * left.getAxisDirection().getStep()
								|| controller.topLeftCorner.get(right.getAxis()) * right.getAxisDirection().getStep() - (controller.width - 1) < topLeftCorner.get(right.getAxis()) * right.getAxisDirection().getStep() - (width - 1)) {
							return;
						}
					} else if (controller.topLeftCorner.getY() > topLeftCorner.getY()
							|| controller.topLeftCorner.getY() - (controller.height - 1) < topLeftCorner.getY() - (height - 1)) {
						return;
					}
				}
			} else {
				throw new IllegalStateException("Block must have a ScreenEntity!");
			}
			newBlocks.add(newBlockPos);
		}
		for (BlockPos newBlockPos : newBlocks) {
			if (level.getBlockEntity(newBlockPos) instanceof ScreenEntity entity) {
				entity.getController().removed = true;
				entity.setController(this);
			} else {
				throw new IllegalStateException("Block must have a ScreenEntity!");
			}
		}
		topLeftCorner = newTopLeftCorner;
		if (dir.getAxis() == Direction.Axis.Y) height++;
		else width++;
		tryExpand(dir);
	}
	
	public void setLevel(Level level, @Nullable Runnable scheduleUpdate) {
		this.level = level;
		if (shouldUpdate) {
			shouldUpdate = false;
			if (scheduleUpdate != null) { // should be always true
				scheduleUpdate.run();
			}
		}
	}
	
	public void remove(BlockPos worldPosition) {
		if (removed) return;
		removed = true;
		List<BlockPos> newTopLeftCorners = new ArrayList<>();
		if (topLeftCorner.getY() - (height - 1) < worldPosition.getY()) {
			newTopLeftCorners.add(worldPosition.below());
		}
		if (topLeftCorner.get(left.getAxis()) * left.getAxisDirection().getStep() > worldPosition.get(left.getAxis()) * left.getAxisDirection().getStep()) {
			newTopLeftCorners.add(topLeftCorner.atY(worldPosition.getY()));
		}
		if (topLeftCorner.get(right.getAxis()) * right.getAxisDirection().getStep() + (width - 1) > worldPosition.get(right.getAxis()) * right.getAxisDirection().getStep()) {
			newTopLeftCorners.add(worldPosition.relative(right).atY(topLeftCorner.getY()));
		}
		if (topLeftCorner.getY() > worldPosition.getY()) {
			newTopLeftCorners.add(topLeftCorner);
		}
		assert level != null;
		for (BlockPos pos : newTopLeftCorners) {
			if (level.getBlockEntity(pos) instanceof ScreenEntity entity) {
				ScreenController controller = new ScreenController(facing, pos);
				controller.setLevel(level, null);
				entity.setController(controller);
				controller.tryExpand(right);
				controller.tryExpand(Direction.DOWN);
			}
		}
	}
	
	public BlockPos getTopLeftCorner() {
		return topLeftCorner;
	}
	
	public CompoundTag save() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("width", width);
		tag.putInt("height", height);
		tag.putBoolean("removed", removed);
		return tag;
	}
	
	public void load(CompoundTag tag) {
		width = tag.getInt("width");
		height = tag.getInt("height");
		removed = tag.getBoolean("removed");
		if (level == null) {
			shouldUpdate = true;
		} else {
			throw new IllegalStateException("Level should be set after loading controller!");
		}
	}
	
	public void update() {
		assert level != null;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (level.getBlockEntity(topLeftCorner.relative(right, i).below(j)) instanceof ScreenEntity entity) {
					entity.setController(this);
				} else {
					throw new IllegalStateException("Block must have a ScreenEntity!");
				}
			}
		}
	}
}
