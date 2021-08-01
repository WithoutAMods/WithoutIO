package withoutaname.mods.withoutio.setup;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import withoutaname.mods.withoutio.blocks.ScreenBlock;
import withoutaname.mods.withoutio.blocks.ScreenEntity;

import static withoutaname.mods.withoutio.WithoutIO.MODID;

public class Registration {
	
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);
	private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);
	
	public static void init() {
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static final RegistryObject<ScreenBlock> SCREEN_BLOCK = BLOCKS.register("screen", ScreenBlock::new);
	public static final RegistryObject<BlockItem> SCREEN_ITEM = ITEMS.register("screen", () -> new BlockItem(SCREEN_BLOCK.get(), ModSetup.DEFAULT_ITEM_PROPERTIES));
	public static final RegistryObject<BlockEntityType<ScreenEntity>> SCREEN_ENTITY = BLOCK_ENTITIES.register("screen", () -> BlockEntityType.Builder.of(ScreenEntity::new, SCREEN_BLOCK.get()).build(null));
	
}
