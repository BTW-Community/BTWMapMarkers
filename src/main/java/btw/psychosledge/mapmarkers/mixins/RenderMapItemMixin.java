package btw.psychosledge.mapmarkers.mixins;

import btw.psychosledge.mapmarkers.MapMarkersAddon;
import btw.psychosledge.mapmarkers.data.MarkerCoord;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(MapItemRenderer.class)
public abstract class RenderMapItemMixin {
    @Unique
    private static final ResourceLocation customMapIcons = new ResourceLocation("mapmarkers", "misc/mapmarkericons.png");

    @Inject(method = "renderMap",
    at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", ordinal = 1))
    private void drawMarkers(EntityPlayer player, TextureManager textureManager, MapData mapData, CallbackInfo ci){
        Tessellator tessellator = Tessellator.instance;
        textureManager.bindTexture(customMapIcons);
        int n = 0;
        int n2 = 0;
        byte by = 0;
        ArrayList<MarkerCoord> markerCoords = new ArrayList<>(MapMarkersAddon.MAP_SPECIFIC_MARKERS.getOrDefault(mapData.mapName, new ArrayList<>()));
        for (MarkerCoord markerCoord : markerCoords) {
            if (markerCoord == null) continue;
            MapCoord mapCoord = markerCoord.mapCoord();
            GL11.glPushMatrix();
            GL11.glTranslatef((float)n2 + (float)mapCoord.centerX / 2.0f + 64.0f, (float)by + (float)mapCoord.centerZ / 2.0f + 64.0f, -0.02f);
            GL11.glRotatef((float)(mapCoord.iconRotation * 360) / 16.0f, 0.0f, 0.0f, 1.0f);
            GL11.glScalef(4.0f, 4.0f, 3.0f);
            GL11.glTranslatef(-0.125f, 0.125f, 0.0f);
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
            tessellator.draw();
            GL11.glPopMatrix();
            ++n;
        }
    }
}
