package minefantasy.mfr.client.render.item;

import codechicken.lib.model.bakedmodels.WrappedItemModel;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.util.TransformUtils;
import minefantasy.mfr.api.archery.AmmoMechanicsMFR;
import minefantasy.mfr.init.ComponentListMFR;
import minefantasy.mfr.item.gadget.ItemCrossbow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.model.IModelState;

import java.util.function.Supplier;

/**
 * @author Anonymous Productions
 * <p>
 * Sources are provided for educational reasons.
 * though small bits of code, or methods can be used in your own creations.
 */

public class RenderCrossbow extends WrappedItemModel implements IItemRenderer {
    public RenderCrossbow(Supplier<ModelResourceLocation> wrappedModel) {
        super(wrappedModel);
    }

    @Override
    public void renderItem(ItemStack stack, TransformType type) {
        GlStateManager.pushMatrix();

        if (type != TransformType.GUI) {
            GlStateManager.scale(2F, 2F, 2F);
            GlStateManager.translate(0.2F, 0.5F, 0.35F);
            renderPart(stack, "stock", type);
            renderPart(stack, "mechanism", type);
            renderPart(stack, "muzzle", type);
            renderPart(stack, "mod", type);
            if (AmmoMechanicsMFR.isFirearmLoaded(stack)){
                renderString("string_loaded");
            }
            else{
                renderString( "string_unloaded");
            }
            renderArrow(stack);
        }
        else{
            GlStateManager.scale(1.5F, 1.5F, 1.5F);
            GlStateManager.translate(0.2F, 0.5F, 0.35F);
            renderPart(stack, "stock", type);
            renderPart(stack, "mechanism", type);
            renderPart(stack, "muzzle", type);
            renderPart(stack, "mod", type);
            if (AmmoMechanicsMFR.isFirearmLoaded(stack)){
                renderString("string_loaded");
            }
            else{
                renderString( "string_unloaded");
            }
        }

        GlStateManager.popMatrix();
    }

    private void renderPart(ItemStack stack, String part_name, TransformType type) {
        boolean isArms = part_name.equalsIgnoreCase("mechanism");
        boolean isStock = part_name.equalsIgnoreCase("stock");

        ItemCrossbow crossbow = (ItemCrossbow) stack.getItem();
        ItemStack part = new ItemStack((Item) crossbow.getItem(stack, part_name));

        GlStateManager.pushMatrix();

        if (type != TransformType.GUI){
            if (isArms){
                GlStateManager.rotate(90, -1, 1, 0);
            }
        }

        Minecraft.getMinecraft().getRenderItem().renderItem(part, TransformType.NONE);

        GlStateManager.popMatrix();

    }

    private void renderString(String part_name){
        GlStateManager.pushMatrix();

        ItemStack string_stack = part_name.equalsIgnoreCase("string_unloaded") ? new ItemStack(ComponentListMFR.CROSSBOW_STRING_UNLOADED) : new ItemStack(ComponentListMFR.CROSSBOW_STRING_LOADED);
        GlStateManager.rotate(90, -1, 1, 0);

        Minecraft.getMinecraft().getRenderItem().renderItem(string_stack, TransformType.NONE);

        GlStateManager.popMatrix();
    }

    private void renderArrow(ItemStack stack){
        ItemStack arrowStack = AmmoMechanicsMFR.getArrowOnBow(stack);

        if (!arrowStack.isEmpty() && AmmoMechanicsMFR.isFirearmLoaded(stack)) {

            GlStateManager.pushMatrix(); //arrow start

            GlStateManager.scale(0.375F, 0.375F, 0.375F);
            GlStateManager.translate(0.2F,-0.05F,0F);
            GlStateManager.rotate(90, 0, 0, 1);
            GlStateManager.rotate(90, 1, 1, 0);

            Minecraft.getMinecraft().getRenderItem().renderItem(arrowStack, TransformType.NONE);

            GlStateManager.popMatrix(); //arrow end
        }
    }

    @Override
    public IModelState getTransforms() {
        return TransformUtils.DEFAULT_BOW;
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