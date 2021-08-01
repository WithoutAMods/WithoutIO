package withoutaname.mods.withoutio.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import withoutaname.mods.withoutalib.datagen.BaseBlockStateProvider;
import withoutaname.mods.withoutio.WithoutIO;
import withoutaname.mods.withoutio.blocks.ScreenBlock;
import withoutaname.mods.withoutio.setup.Registration;

public class BlockStates extends BaseBlockStateProvider {
	
	public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, WithoutIO.MODID, exFileHelper);
	}
	
	@Override
	protected void registerStatesAndModels() {
		BlockModelBuilder screenMain = models()
				.withExistingParent("screen_main", mcLoc("block/block"))
				.texture("front", "withoutio:block/screen_front")
				.texture("side", "withoutio:block/screen_side")
				.texture("particle", "#side");
		addCube(screenMain, v(0, 0, .5F), v(16, 16, 16), dir -> dir == Direction.NORTH ? "#front" : "#side");
		
		BlockModelBuilder screenFrameBottom = models()
				.withExistingParent("screen_frame_bottom", mcLoc("block/block"))
				.texture("side", "withoutio:block/screen_side");
		addCube(screenFrameBottom, v(0, 0, 0), v(16, .5F, .5F), dir -> dir == Direction.SOUTH ? null : "#side");
		BlockModelBuilder screenFrameTop = models()
				.withExistingParent("screen_frame_top", mcLoc("block/block"))
				.texture("side", "withoutio:block/screen_side");
		addCube(screenFrameTop, v(0, 15.5F, 0), v(16, 16, .5F), dir -> dir == Direction.SOUTH ? null : "#side");
		BlockModelBuilder screenFrameLeft = models()
				.withExistingParent("screen_frame_right", mcLoc("block/block"))
				.texture("side", "withoutio:block/screen_side");
		addCube(screenFrameLeft, v(15.5F, 0, 0), v(16, 16, .5F), dir -> dir == Direction.SOUTH ? null : "#side");
		BlockModelBuilder screenFrameRight = models()
				.withExistingParent("screen_frame_left", mcLoc("block/block"))
				.texture("side", "withoutio:block/screen_side");
		addCube(screenFrameRight, v(0, 0, 0), v(.5F, 16, .5F), dir -> dir == Direction.SOUTH ? null : "#side");
		
		MultiPartBlockStateBuilder screenStateBuilder = getMultipartBuilder(Registration.SCREEN_BLOCK.get());
		for (Direction dir : ScreenBlock.FACING.getPossibleValues()) {
			screenStateBuilder
					.part()
					.modelFile(screenMain)
					.rotationY((dir.get2DDataValue() + 2) * 90 % 360)
					.addModel()
					.condition(ScreenBlock.FACING, dir);
			for (int i = 0; i < 4; i++) {
				screenStateBuilder
						.part()
						.modelFile(switch (i) {
							case 0 -> screenFrameBottom;
							case 1 -> screenFrameTop;
							case 2 -> screenFrameLeft;
							case 3 -> screenFrameRight;
							default -> throw new IllegalStateException("Unexpected value: " + i);
						})
						.rotationY((dir.get2DDataValue() + 2) * 90 % 360)
						.addModel()
						.condition(ScreenBlock.FACING, dir)
						.condition(switch (i) {
							case 0 -> ScreenBlock.FRAME_BOTTOM;
							case 1 -> ScreenBlock.FRAME_TOP;
							case 2 -> ScreenBlock.FRAME_LEFT;
							case 3 -> ScreenBlock.FRAME_RIGHT;
							default -> throw new IllegalStateException("Unexpected value: " + i);
						}, true);
			}
		}
		
		ItemModelBuilder screenItem = itemModels()
				.withExistingParent("screen", mcLoc("block/block"))
				.texture("front", "withoutio:block/screen_front")
				.texture("side", "withoutio:block/screen_side")
				.transforms()
				.transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT)
				.rotation(0, 135, 0)
				.scale(0.4F)
				.end()
				.end();
		addCube(screenItem, v(0, 0, .5F), v(16, 16, 16), dir -> dir == Direction.NORTH ? "#front" : "#side"); // main
		addCube(screenItem, v(0, 0, 0), v(16, .5F, .5F), dir -> dir == Direction.SOUTH ? null : "#side"); // frame bottom
		addCube(screenItem, v(0, 15.5F, 0), v(16, 16, .5F), dir -> dir == Direction.SOUTH ? null : "#side"); // frame top
		addCube(screenItem, v(15.5F, 0, 0), v(16, 16, .5F), dir -> dir == Direction.SOUTH ? null : "#side"); // frame left
		addCube(screenItem, v(0, 0, 0), v(.5F, 16, .5F), dir -> dir == Direction.SOUTH ? null : "#side"); // frame right
	}
	
}
