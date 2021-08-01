package withoutaname.mods.withoutio.setup;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import withoutaname.mods.withoutio.WithoutIO;

import javax.annotation.Nonnull;

public class ModSetup {
	
	public static final CreativeModeTab DEFAULT_CREATIVE_TAB = new CreativeModeTab(WithoutIO.MODID) {
		
		@Nonnull
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Registration.SCREEN_ITEM.get());
		}
		
	};
	public static final Item.Properties DEFAULT_ITEM_PROPERTIES = new Item.Properties().tab(DEFAULT_CREATIVE_TAB);
	
}
