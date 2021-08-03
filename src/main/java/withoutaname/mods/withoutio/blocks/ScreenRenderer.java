package withoutaname.mods.withoutio.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import withoutaname.mods.withoutalib.blocks.BaseRenderer;
import withoutaname.mods.withoutio.WithoutIO;
import withoutaname.mods.withoutio.setup.Registration;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ScreenRenderer extends BaseRenderer<ScreenEntity> {
	
	public static final int CHAR_WIDTH = 6;
	public static final int CHAR_HEIGHT = 10;
	public static final ResourceLocation FONT = new ResourceLocation(WithoutIO.MODID, "font/screen");
	
	public ScreenRenderer(BlockEntityRendererProvider.Context context) {}
	
	public static void register() {
		BlockEntityRenderers.register(Registration.SCREEN_ENTITY.get(), ScreenRenderer::new);
	}
	
	@Override
	public void render(ScreenEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
		if (entity.getBlockState().getValue(ScreenBlock.FRAME_LEFT) && entity.getBlockState().getValue(ScreenBlock.FRAME_TOP)) {
			poseStack.pushPose();
			BlockState state = entity.getBlockState();
			poseStack.translate(.5, .5, .5);
			poseStack.mulPose(Quaternion.fromXYZDegrees(new Vector3f(180, state.getValue(ScreenBlock.FACING).get2DDataValue() * 90, 0)));
			poseStack.translate(-.5, -.5, -.5);
			poseStack.scale(1 / 128F, 1 / 128F, 1 / 128F);
			TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(FONT);
			VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.translucent());
			String text = "Here's a display which should be controllable by redstone.\nCurrently there is only a model and the text rendering for the screen.\n\tTabs and\n\t\tline breaks work as well.";
			List<Byte> input = new ArrayList<>();
			for (Byte b : text.getBytes(StandardCharsets.US_ASCII)) {
				input.add(b);
			}
			int x = 0;
			int y = 0;
			for (byte b : input) {
				if (b == 9) {
					x = x / 4 * 4 + 4;
				} else if (b == 10 || b == 11 || b == 12) {
					x = 0;
					y++;
				} else if (b >= 32 && b <= 126) {
					if (x >= 40) {
						x = 0;
						y++;
						if (b == 32) continue;
					}
					if (y >= 40) {
						break;
					}
					addChar(vertexConsumer, poseStack, 8 + x * CHAR_WIDTH, 8 + y * CHAR_HEIGHT, b, sprite);
					x++;
				}
			}
			poseStack.popPose();
		}
	}
	
	private void addChar(VertexConsumer vertexConsumer, PoseStack poseStack, int x, int y, byte b, TextureAtlasSprite sprite) {
		int u = b % 16 * CHAR_WIDTH;
		int v = (b / 16) * CHAR_HEIGHT;
		addFace(vertexConsumer, poseStack, v(x, y, 0), v(x, y + CHAR_HEIGHT, 0), v(x + CHAR_WIDTH, y + CHAR_HEIGHT, 0), v(x + CHAR_WIDTH, y, 0), sprite.getU(u / 8D), sprite.getU((u + CHAR_WIDTH) / 8D), sprite.getV(v / 8D), sprite.getV((v + CHAR_HEIGHT) / 8D));
	}
}
