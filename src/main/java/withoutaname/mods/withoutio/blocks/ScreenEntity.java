package withoutaname.mods.withoutio.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import withoutaname.mods.withoutio.setup.Registration;

public class ScreenEntity extends BlockEntity {
	
	public ScreenEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		super(Registration.SCREEN_ENTITY.get(), pWorldPosition, pBlockState);
	}
}
