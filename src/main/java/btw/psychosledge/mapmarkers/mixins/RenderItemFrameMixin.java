package btw.psychosledge.mapmarkers.mixins;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderItemFrame.class)
public abstract class RenderItemFrameMixin extends Render {
    @Redirect(method = "func_82402_b",
    at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V", ordinal = 0))
    private void removeYOffsetWhenDrawingMap(float x, float y, float z, EntityItemFrame entityItemFrame){
        float yOverride = -0.18f;
        if(isMap(entityItemFrame)){
            yOverride = 0;
        }
        GL11.glTranslatef(x, yOverride, z);
    }

    @Redirect(method = "func_82402_b",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityItemFrame;getRotation()I"))
    private int ignoreRotationWhenDrawingMap(EntityItemFrame entityItemFrame){
        if(isMap(entityItemFrame)) return 0;
        return entityItemFrame.getRotation();
    }

    @Redirect(method = "func_82402_b",
            at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glScalef(FFF)V", ordinal = 0))
    private void increaseScaleOfMap(float x, float y, float z, EntityItemFrame entityItemFrame){
        if (entityItemFrame.getRotation() % 2 != 0) {
            GL11.glScalef(0.0078125F, 0.0078125F, 0.0078125F);
        }
        else {
            GL11.glScalef(x, y, z);
        }
    }

    @Redirect(method = "func_82402_b",
            at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V", ordinal = 4))
    private void removeZOffsetWhenDrawingMap(float x, float y, float z, EntityItemFrame entityItemFrame){
        if (entityItemFrame.getRotation() % 2 != 0) {
            GL11.glTranslatef(-64F, -64F, -4F);
        }
        else {
            GL11.glTranslatef(x, y + 42F, z);
        }
    }

    @Redirect(method = "func_82402_b",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;addVertexWithUV(DDDDD)V", ordinal = 0))
    private void removeEdge1(Tessellator instance, double d, double e, double f, double g, double h, EntityItemFrame entityItemFrame){
        if (entityItemFrame.getRotation() % 2 != 0) {
            instance.addVertexWithUV(0, 128, 0.0, 0.0, 1.0);
        }
        else {
            instance.addVertexWithUV(d, e, f, g, h);
        }
    }

    @Redirect(method = "func_82402_b",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;addVertexWithUV(DDDDD)V", ordinal = 1))
    private void removeEdge2(Tessellator instance, double d, double e, double f, double g, double h, EntityItemFrame entityItemFrame){
        if (entityItemFrame.getRotation() % 2 != 0) {
            instance.addVertexWithUV(128, 128, 0.0, 1.0, 1.0);
        }
        else {
            instance.addVertexWithUV(d, e, f, g, h);
        }
    }

    @Redirect(method = "func_82402_b",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;addVertexWithUV(DDDDD)V", ordinal = 2))
    private void removeEdge3(Tessellator instance, double d, double e, double f, double g, double h, EntityItemFrame entityItemFrame){
        if (entityItemFrame.getRotation() % 2 != 0) {
            instance.addVertexWithUV(128, 0, 0.0, 1.0, 0.0);
        }
        else {
            instance.addVertexWithUV(d, e, f, g, h);
        }
    }

    @Redirect(method = "func_82402_b",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;addVertexWithUV(DDDDD)V", ordinal = 3))
    private void removeEdge4(Tessellator instance, double d, double e, double f, double g, double h, EntityItemFrame entityItemFrame){
        if (entityItemFrame.getRotation() % 2 != 0) {
            instance.addVertexWithUV(0, 0, 0.0, 0.0, 0.0);
        }
        else {
            instance.addVertexWithUV(d, e, f, g, h);
        }
    }

    @Unique
    private boolean isMap(EntityItemFrame entityItemFrame){
        ItemStack itemStack = entityItemFrame.getDisplayedItem();
        if (itemStack != null) {
            EntityItem entityItem = new EntityItem(entityItemFrame.worldObj, 0.0, 0.0, 0.0, itemStack);
            return entityItem.getEntityItem().getItem() == Item.map;
        }
        return false;
    }
}