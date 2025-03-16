package btw.psychosledge.mapmarkers.mixins;

import btw.psychosledge.mapmarkers.MapMarkersAddon;
import btw.psychosledge.mapmarkers.data.MapMarkerData;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static btw.psychosledge.mapmarkers.MapMarkersAddon.MAP_MARKER_DATA;

@Mixin(MapData.class)
public abstract class MapDataMixin {

    @Shadow public int xCenter;
    @Shadow public int zCenter;
    @Shadow public byte scale;
    @Shadow public byte dimension;

    @Inject(method = "updateVisiblePlayers", at = @At(value = "INVOKE", target = "Ljava/util/Map;clear()V", shift = At.Shift.AFTER, ordinal = 0))
    public void addMapMarkers(EntityPlayer player, ItemStack par2ItemStack, CallbackInfo ci) {
        for (MapMarkerData markerData : player.worldObj.getData(MAP_MARKER_DATA).mapMarkers) {
            String locationKey = markerData.MarkerId + "-" + dimension + "@" + xCenter + "." + zCenter + ":" + scale;
            if (IsLocationInMap(markerData.XPos, markerData.ZPos)) {
                copy_func_82567_a(markerData.IconIndex, player.worldObj, markerData.XPos, markerData.ZPos, 1, locationKey);
            }
            else {
                MapMarkersAddon.MAPDATA_MARKER_CACHE.remove(locationKey);
            }
        }
    }

    private boolean IsLocationInMap(int x, int z )
    {
        // check if location is within map bounds
        int iMapScale = 1 << scale;
        float fRelativeI = (float)((double)x - (double)xCenter) / (float)iMapScale;
        float fRelativeK = (float)((double)z - (double)zCenter) / (float)iMapScale;

        return !(Math.abs(fRelativeI) > 64F) && !(Math.abs(fRelativeK) > 64F);
    }

    private void copy_func_82567_a(int par1, World par2World, double par4, double par6, double par8, String locationKey) {
        byte var15;
        int var10 = 1 << this.scale;
        float var11 = (float)(par4 - (double)this.xCenter) / (float)var10;
        float var12 = (float)(par6 - (double)this.zCenter) / (float)var10;
        byte var13 = (byte)((double)(var11 * 2.0f) + 0.5);
        byte var14 = (byte)((double)(var12 * 2.0f) + 0.5);
        int var16 = 63;
        if (var11 >= (float)(-var16) && var12 >= (float)(-var16) && var11 <= (float)var16 && var12 <= (float)var16) {
            var15 = (byte)((par8 += par8 < 0.0 ? -8.0 : 8.0) * 16.0 / 360.0);
            if (this.dimension < 0) {
                int var17 = (int)(par2World.getWorldInfo().getWorldTime() / 10L);
                var15 = (byte)(var17 * var17 * 34187121 + var17 * 121 >> 15 & 0xF);
            }
        } else {
            if (Math.abs(var11) >= 320.0f || Math.abs(var12) >= 320.0f) {
                MapMarkersAddon.MAPDATA_MARKER_CACHE.remove(locationKey);
                return;
            }
            par1 = 6;
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

        MapMarkersAddon.MAPDATA_MARKER_CACHE.putIfAbsent(locationKey, new MapCoord((MapData)(Object)this, (byte)par1, var13, var14, var15));
    }
}
