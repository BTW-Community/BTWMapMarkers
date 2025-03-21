package btw.psychosledge.mapmarkers.interfaces;

import net.minecraft.src.MapCoord;

import java.util.Collection;

public interface IMarkerCacheAccessible {
    Collection<MapCoord> sledgeMapMarkersAddon$getMarkerCache(String mapName);
}
