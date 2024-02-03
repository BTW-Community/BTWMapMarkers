package btw.community.sledge.mixins;

import btw.community.sledge.MapMarkerData;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MapData;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static btw.community.sledge.MapMarkersAddon.WorldMapMarkers;

@Mixin(MapData.class)
public abstract class MapDataMixin {

    @Shadow public int xCenter;
    @Shadow public int zCenter;
    @Shadow public byte scale;
    @Shadow protected abstract void func_82567_a(int par1, World par2World, String par3Str, double par4, double par6, double par8);

    @Inject(method = "updateVisiblePlayers", at = @At(value = "INVOKE", target = "Ljava/util/Map;clear()V", shift = At.Shift.AFTER, ordinal = 0))
    public void addMapMarkers(EntityPlayer player, ItemStack par2ItemStack, CallbackInfo ci) {
        for (Object markerObj : WorldMapMarkers.values()) {
            MapMarkerData marker = (MapMarkerData) markerObj;
            if (IsLocationInMap(player.worldObj, marker.XPos, marker.YPos, marker.ZPos)) {
                func_82567_a(marker.IconIndex, player.worldObj, marker.MarkerId, marker.XPos, marker.ZPos, 1);
            }
        }
    }

    private boolean IsLocationInMap(World world, int x, int y, int z )
    {
        // check if location is within map bounds
        int iMapScale = 1 << scale;
        float fRelativeI = (float)((double)x - (double)xCenter) / (float)iMapScale;
        float fRelativeK = (float)((double)z - (double)zCenter) / (float)iMapScale;

        return !(Math.abs(fRelativeI) > 64F) && !(Math.abs(fRelativeK) > 64F);
    }
}
