package withoutaname.mods.withoutio;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(WithoutIO.MODID)
@Mod.EventBusSubscriber
public class WithoutIO {
    public static final String MODID = "withoutio";
    public static final Logger LOGGER = LogManager.getLogger();

    public WithoutIO() {}
}
