package btw.psychosledge.mapmarkers.mixins;

import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(MapItemRenderer.class)
public abstract class RenderMapItemMixin {

    private static final ResourceLocation originalMapIcons = new ResourceLocation("textures/map/map_icons.png");
    private static final ResourceLocation customMapIcons = new ResourceLocation("mapmarkers:/misc/mapmarkericons.png");

    @Redirect(method = "renderMap",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;startDrawingQuads()V"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;startDrawingQuads()V", ordinal = 1, shift = At.Shift.BEFORE),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;draw()I", ordinal = 1, shift = At.Shift.BEFORE)))
    private void disableStandardDrawing(Tessellator instance){

    }

    @Group(name = "addVertexes", min = 1, max = 1)
    @Inject(method = "renderMap",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;draw()I", ordinal = 1, shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void addVertexesRelease(EntityPlayer player, TextureManager textureManager, MapData mapData, CallbackInfo ci, int var4, int var5, Tessellator tessellator, float var7, int n, Iterator var9, MapCoord mapCoord, float var11, float var12, float var13, float var14) {
        if(mapCoord.iconRotation == 0) {
            textureManager.bindTexture(customMapIcons);
        }
        else {
            textureManager.bindTexture(originalMapIcons);
        }
        addVertexesHandler(tessellator, n, mapCoord);
    }

    private void addVertexesHandler(Tessellator tessellator, int n, MapCoord mapCoord) {
        int modified = (mapCoord.iconSize & 255);

        float modifiedVar22 = (float)(modified % 4) / 4.0F;
        float modifiedVar23 = (float)(modified / 4) / 4.0F;
        float modifiedVar24 = (float)(modified % 4 + 1) / 4.0F;
        float modifiedVar25 = (float)(modified / 4 + 1) / 4.0F;

        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-1.0D, 1.0D, (float)-n * 0.001F, modifiedVar22, modifiedVar23);
        tessellator.addVertexWithUV(1.0D, 1.0D, (float)-n * 0.001F, modifiedVar24, modifiedVar23);
        tessellator.addVertexWithUV(1.0D, -1.0D, (float)-n * 0.001F, modifiedVar24, modifiedVar25);
        tessellator.addVertexWithUV(-1.0D, -1.0D, (float)-n * 0.001F, modifiedVar22, modifiedVar25);
    }
}
