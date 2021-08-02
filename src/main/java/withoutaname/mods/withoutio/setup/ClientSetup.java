package withoutaname.mods.withoutio.setup;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
	
	@SubscribeEvent
	public static void onTextureStitch(@Nonnull TextureStitchEvent.Pre event) {
		if (event.getMap().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
			event.addSprite(ScreenRenderer.FONT);
		}
		
	}
}
