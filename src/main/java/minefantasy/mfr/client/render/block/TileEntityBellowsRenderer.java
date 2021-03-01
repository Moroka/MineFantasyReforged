package minefantasy.mfr.client.render.block;

import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.util.TransformUtils;
import minefantasy.mfr.block.BlockBellows;
import minefantasy.mfr.client.model.block.ModelBellows;
import minefantasy.mfr.tile.TileEntityBellows;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.common.model.IModelState;

public class TileEntityBellowsRenderer<T extends TileEntity> extends FastTESR<T> implements IItemRenderer {
	private ModelBellows model;
	private static final ResourceLocation texture = new ResourceLocation("minefantasyreborn:textures/blocks/bellows.png");

	public TileEntityBellowsRenderer() {
		model = new ModelBellows();
	}

	@Override
	public void renderTileEntityFast(T te, double x, double y, double z, float partialTick, int breakStage, float partial, BufferBuilder renderer) {

		renderModelAt((TileEntityBellows) te, x, y, z, partialTick);
	}

	public void renderModelAt(TileEntityBellows tile, double d, double d1, double d2, float f) {
		EnumFacing facing = EnumFacing.NORTH;
		if (tile.hasWorld()) {
			IBlockState state = tile.getWorld().getBlockState(tile.getPos());
			facing = state.getValue(BlockBellows.FACING);
		}
		if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
			facing = facing.rotateYCCW();
		} else {
			facing = facing.rotateY();
		}

		model.rotate(tile.press);
		this.bindTexture(texture); //texture

		GlStateManager.pushMatrix(); // start
		float scale = 1.0F;
		float yOffset = 1.525F;
		GlStateManager.translate((float) d + 0.5F, (float) d1 + yOffset, (float) d2 + 0.5F); // size
		GlStateManager.rotate(facing.getHorizontalAngle(), 0.0F, 1.0F, 0.0F); // rotate based on facing
		GlStateManager.scale(scale, -scale, -scale); // if you read this comment out this line and you can see what happens
		GlStateManager.pushMatrix();

		GlStateManager.disableLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 15 * 16, 15 * 16);
		model.renderModel(0.0625F);
		GlStateManager.enableLighting();

		GlStateManager.popMatrix();
		GlStateManager.color(255, 255, 255);
		GlStateManager.popMatrix(); // end

	}

	public void renderInvModel(double d, double d1, double d2, float f) {
		int j = 90;

		model.rotate(0);

		GlStateManager.pushMatrix(); // start
		float scale = 1.0F;
		float yOffset = 1.525F;
		GlStateManager.translate((float) d + 0.5F, (float) d1 + yOffset, (float) d2 + 0.5F); // size
		GlStateManager.rotate(j, 0.0F, 1.0F, 0.0F); // rotate based on metadata
		GlStateManager.scale(scale, -scale, -scale); // if you read this comment out this line and you can see what happens
		GlStateManager.pushMatrix();
		float level = 0F;
		model.renderModel(0.0625F);

		GlStateManager.popMatrix();
		GlStateManager.color(255, 255, 255);
		GlStateManager.popMatrix(); // end

	}

	@Override
	public void renderItem(ItemStack stack, ItemCameraTransforms.TransformType transformType) {

		GlStateManager.pushMatrix();

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		renderInvModel(0F, 0F, 0F, 0F);

		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.popMatrix();
	}

	@Override
	public IModelState getTransforms() {
		return TransformUtils.DEFAULT_BLOCK;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}
}
