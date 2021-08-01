package withoutaname.mods.withoutio.setup;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import withoutaname.mods.withoutio.WithoutIO;
import withoutaname.mods.withoutio.blocks.ScreenRenderer;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = WithoutIO.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
	
	public static void init(@Nonnull final FMLClientSetupEvent event) {
		ScreenRenderer.register();
	}
}
