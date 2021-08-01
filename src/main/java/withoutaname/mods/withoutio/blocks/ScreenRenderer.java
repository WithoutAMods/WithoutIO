package withoutaname.mods.withoutio.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import withoutaname.mods.withoutalib.blocks.BaseRenderer;
import withoutaname.mods.withoutio.setup.Registration;

import javax.annotation.Nonnull;

public class ScreenRenderer extends BaseRenderer<ScreenEntity> {
	
	public ScreenRenderer(BlockEntityRendererProvider.Context context) {}
	
	public static void register() {
		BlockEntityRenderers.register(Registration.SCREEN_ENTITY.get(), ScreenRenderer::new);
	}
	
	@Override
	public void render(@Nonnull ScreenEntity entity, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
	
	}
}
