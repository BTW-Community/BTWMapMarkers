package btw.psychosledge.mapmarkers.mixins;

import btw.psychosledge.mapmarkers.data.MapMarkerData;
import btw.psychosledge.mapmarkers.data.MarkerCoord;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;

import static btw.psychosledge.mapmarkers.MapMarkersAddon.MAP_MARKER_DATA;
import static btw.psychosledge.mapmarkers.MapMarkersAddon.MAP_SPECIFIC_MARKERS;

@Mixin(MapData.class)
public abstract class MapDataMixin extends WorldSavedData {

    @Shadow public int xCenter;
    @Shadow public int zCenter;
    @Shadow public byte scale;
    @Shadow public byte dimension;

    public MapDataMixin(String string) {
        super(string);
    }

    @Inject(method = "updateVisiblePlayers", at = @At("TAIL"))
    private void addMapMarkersSP(EntityPlayer player, ItemStack itemStack, CallbackInfo ci) {
        addMarkersToMapData(player.worldObj);
    }

    @Unique
    private void addMarkersToMapData(World world) {
        ArrayList<MarkerCoord> thisMapMarkers = MAP_SPECIFIC_MARKERS.getOrDefault(mapName, new ArrayList<>());
        MAP_SPECIFIC_MARKERS.putIfAbsent(mapName, thisMapMarkers);
        thisMapMarkers.clear();
        Collection<MapMarkerData> worldMarkers = world.getData(MAP_MARKER_DATA).mapMarkers.values();
        for (MapMarkerData markerData : worldMarkers) {
            if (IsLocationInMap(markerData.XPos, markerData.ZPos)) {
                func_82567_a(markerData.IconIndex, world, markerData.toString(), markerData.XPos, markerData.ZPos, 1);
            }
        }
    }

    @Unique
    private boolean IsLocationInMap(int x, int z )
    {
        // check if location is within map bounds
        int iMapScale = 1 << scale;
        float fRelativeI = (float)((double)x - (double)xCenter) / (float)iMapScale;
        float fRelativeK = (float)((double)z - (double)zCenter) / (float)iMapScale;

        return !(Math.abs(fRelativeI) > 64F) && !(Math.abs(fRelativeK) > 64F);
    }

    @Unique
    private void func_82567_a(int iconIndex, World world, String markerId, double xPos, double zPos, double rotation) {
        byte var15;
        int var10 = 1 << this.scale;
        float var11 = (float)(xPos - (double)this.xCenter) / (float)var10;
        float var12 = (float)(zPos - (double)this.zCenter) / (float)var10;
        byte var13 = (byte)((double)(var11 * 2.0f) + 0.5);
        byte var14 = (byte)((double)(var12 * 2.0f) + 0.5);
        int var16 = 63;
        if (var11 >= (float)(-var16) && var12 >= (float)(-var16) && var11 <= (float)var16 && var12 <= (float)var16) {
            var15 = (byte)((rotation += rotation < 0.0 ? -8.0 : 8.0) * 16.0 / 360.0);
            if (this.dimension < 0) {
                int var17 = (int)(world.getWorldInfo().getWorldTime() / 10L);
                var15 = (byte)(var17 * var17 * 34187121 + var17 * 121 >> 15 & 0xF);
            }
        } else {
            iconIndex = 6;
            var15 = 0;
            if (var11 <= (float)(-var16)) {
                var13 = (byte)((double)(var16 * 2) + 2.5);
            }
            if (var12 <= (float)(-var16)) {
                var14 = (byte)((double)(var16 * 2) + 2.5);
            }
            if (var11 >= (float)var16) {
                var13 = (byte)(var16 * 2 + 1);
            }
            if (var12 >= (float)var16) {
                var14 = (byte)(var16 * 2 + 1);
            }
        }

        MAP_SPECIFIC_MARKERS.get(mapName).add(new MarkerCoord(markerId, new MapCoord(null, (byte)iconIndex, var13, var14, var15)));
    }
}
