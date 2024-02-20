package btw.community.sledge.mixins;

import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(MapItemRenderer.class)
public abstract class RenderMapItemMixin {
    @Redirect(method = "renderMap",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;addVertexWithUV(DDDDD)V"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;startDrawingQuads()V", ordinal = 1),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;draw()I", ordinal = 1, shift = At.Shift.BEFORE)))
    private void disableAddVertex(Tessellator tessellator, double par1, double par3, double par5, double par7, double par9){

    }

    @Redirect(method = "renderMap",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;bindTexture(Ljava/lang/String;)V"))
    private void getCustomMapIconPath(RenderEngine renderEngine, String par1Str){
        renderEngine.bindTexture("/misc/mapmarkericons.png");
    }

    @Group(name = "addVertexes", min = 1, max = 1)
    @Inject(method = "renderMap",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;draw()I", ordinal = 1, shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void addVertexesRelease(EntityPlayer par1EntityPlayer, RenderEngine par2RenderEngine, MapData par3MapData, CallbackInfo ci, int var15, int var16, Tessellator var17, float var18, int var19, Iterator var20, MapCoord var21) {
        addVertexesHandler(var17, var19, var21);
    }

    @Group(name = "addVertexes", min = 1, max = 1)
    @Inject(method = "renderMap",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;draw()I", ordinal = 1, shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void addVertexesDev(EntityPlayer par1EntityPlayer, RenderEngine par2RenderEngine, MapData par3MapData, CallbackInfo ci, byte var15, byte var16, Tessellator var17, float var18, int var19, Iterator var20, MapCoord var21) {
        addVertexesHandler(var17, var19, var21);
    }

    private void addVertexesHandler(Tessellator var17, int var19, MapCoord var21) {
        int modified = (var21.iconSize & 255);
        float modifiedVar22 = (float)(modified % 4 + 0) / 4.0F;
        float modifiedVar23 = (float)(modified / 4 + 0) / 4.0F;
        float modifiedVar24 = (float)(modified % 4 + 1) / 4.0F;
        float modifiedVar25 = (float)(modified / 4 + 1) / 4.0F;

        var17.addVertexWithUV(-1.0D, 1.0D, (double)((float)-var19 * 0.001F), (double)modifiedVar22, (double)modifiedVar23);
        var17.addVertexWithUV(1.0D, 1.0D, (double)((float)-var19 * 0.001F), (double)modifiedVar24, (double)modifiedVar23);
        var17.addVertexWithUV(1.0D, -1.0D, (double)((float)-var19 * 0.001F), (double)modifiedVar24, (double)modifiedVar25);
        var17.addVertexWithUV(-1.0D, -1.0D, (double)((float)-var19 * 0.001F), (double)modifiedVar22, (double)modifiedVar25);
    }
}
