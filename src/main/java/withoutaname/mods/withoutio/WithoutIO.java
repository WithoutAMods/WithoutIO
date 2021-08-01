package withoutaname.mods.withoutio;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import withoutaname.mods.withoutio.setup.ClientSetup;
import withoutaname.mods.withoutio.setup.Registration;

@Mod(WithoutIO.MODID)
@Mod.EventBusSubscriber
public class WithoutIO {
	public static final String MODID = "withoutio";
	public static final Logger LOGGER = LogManager.getLogger();
	
	public WithoutIO() {
		Registration.init();
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
	}
}
