package btw.psychosledge.mapmarkers.interfaces;

import net.minecraft.src.MapCoord;

import java.util.ArrayList;

public interface IMarkerCacheAccessible {
    ArrayList<MapCoord> sledgeMapMarkersAddon$getMarkerCache(String mapName);
}
