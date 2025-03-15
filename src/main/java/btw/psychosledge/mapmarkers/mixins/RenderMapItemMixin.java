package btw.psychosledge.mapmarkers.mixins;

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

//    @Redirect(method = "renderMap",
//            at = @At(value = "FIELD", target = "Lnet/minecraft/src/MapItemRenderer;field_111277_a:Lnet/minecraft/src/ResourceLocation;"))
//    private ResourceLocation getCustomMapIconPath(){
//        return new ResourceLocation("/misc/mapmarkericons.png");
//    }

    @Group(name = "addVertexes", min = 1, max = 1)
    @Inject(method = "renderMap",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;draw()I", ordinal = 1, shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void addVertexesRelease(EntityPlayer textureManager, TextureManager mapData, MapData par3, CallbackInfo ci, int var4, int var5, Tessellator var6, float var7, int var8, Iterator var9, MapCoord var10, float var11, float var12, float var13, float var14) {
        addVertexesHandler(var6, var8, var10);
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
