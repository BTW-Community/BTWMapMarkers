package btw.psychosledge.mapmarkers.mixins;

import btw.psychosledge.mapmarkers.MapMarkersAddon;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapItemRenderer.class)
public abstract class RenderMapItemMixin {

    private static final ResourceLocation customMapIcons = new ResourceLocation("mapmarkers:/misc/mapmarkericons.png");

    @Inject(method = "renderMap",
            at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", ordinal = 1))
    private void drawMarkers(EntityPlayer player, TextureManager textureManager, MapData mapData, CallbackInfo ci){
        textureManager.bindTexture(customMapIcons);
        int n = 0;
        int n2 = 0;
        byte by = 0;
        Tessellator tessellator = Tessellator.instance;
        for (MapCoord mapCoord : MapMarkersAddon.MAPDATA_MARKER_CACHE.values()) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)n2 + (float)mapCoord.centerX / 2.0f + 64.0f, (float)by + (float)mapCoord.centerZ / 2.0f + 64.0f, -0.02f);
            GL11.glRotatef((float)(mapCoord.iconRotation * 360) / 16.0f, 0.0f, 0.0f, 1.0f);
            GL11.glScalef(4.0f, 4.0f, 3.0f);
            GL11.glTranslatef(-0.125f, 0.125f, 0.0f);
            addVertexesHandler(tessellator, n, mapCoord);
            tessellator.draw();
            GL11.glPopMatrix();
            ++n;
        }
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
