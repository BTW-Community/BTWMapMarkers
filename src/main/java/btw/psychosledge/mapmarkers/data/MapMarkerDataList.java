package btw.psychosledge.mapmarkers.data;

import btw.AddonHandler;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

import java.util.HashMap;

public class MapMarkerDataList {
    public HashMap<String, MapMarkerData> mapMarkers = new HashMap<>();

    public MapMarkerDataList() {

    }

    public MapMarkerDataList(NBTTagList tagList) {
        this();
        loadFromNBT(tagList);
    }

    private void loadFromNBT(NBTTagList tagList) {
        mapMarkers.clear();
        for (int iTempCount = 0; iTempCount < tagList.tagCount(); ++iTempCount) {
            NBTTagCompound tempCompound = (NBTTagCompound)tagList.tagAt(iTempCount);
            try {
                if (tempCompound.hasNoTags()) continue;
                MapMarkerData newMarker = new MapMarkerData(tempCompound);
                mapMarkers.putIfAbsent(newMarker.toString(), newMarker);
            } catch (Exception e) {
                AddonHandler.logWarning("bad marker data: " + tempCompound);
            }
        }
        AddonHandler.logMessage("markers loaded: " + mapMarkers.size());
    }

    public NBTTagList saveToNBT() {
        NBTTagList tagList = new NBTTagList("MapMarkers");
        for (MapMarkerData mapMarker : mapMarkers.values()) {
            NBTTagCompound tempTagCompound = new NBTTagCompound();
            mapMarker.writeToNBT(tempTagCompound);
            tagList.appendTag(tempTagCompound);
        }
        return tagList;
    }

    public void removeMarkerById(String markerId) {
        mapMarkers.remove(markerId);
    }

    public void addMarker(MapMarkerData markerData) {
        mapMarkers.putIfAbsent(markerData.toString(), markerData);
    }

}
