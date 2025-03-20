package btw.psychosledge.mapmarkers.data;

import btw.AddonHandler;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapMarkerDataList {
    public List<MapMarkerData> mapMarkers = new ArrayList<>();

    public MapMarkerDataList() {

    }

    public MapMarkerDataList(NBTTagList tagList) {
        this();
        loadFromNBT(tagList);
    }

    private void loadFromNBT(NBTTagList tagList) {
        mapMarkers.clear();
        AddonHandler.logMessage("Loading marker data from " + tagList.tagCount() + " tags...");
        for (int iTempCount = 0; iTempCount < tagList.tagCount(); ++iTempCount) {
            NBTTagCompound tempCompound = (NBTTagCompound)tagList.tagAt(iTempCount);
            try {
                MapMarkerData newMarker = new MapMarkerData(tempCompound);
                if (Objects.equals(newMarker.MarkerId, "")) continue;
                mapMarkers.add(newMarker);
            } catch (Exception e) {
                AddonHandler.logMessage("bad marker data: " + tempCompound);
            }
        }
        AddonHandler.logMessage("Loaded markers: " + mapMarkers.size());
    }

    public NBTTagList saveToNBT() {
        NBTTagList tagList = new NBTTagList("MapMarkers");
        AddonHandler.logMessage("Saving marker data...");
        for (MapMarkerData mapMarker : mapMarkers) {
            NBTTagCompound tempTagCompound = new NBTTagCompound();
            mapMarker.writeToNBT(tempTagCompound);
            tagList.appendTag(tempTagCompound);
        }AddonHandler.logMessage("Saved markers: " + mapMarkers.size());
        return tagList;
    }

    public void removeMarkerById(String markerId) {
        AddonHandler.logMessage("Removing bad marker: " + markerId);
        mapMarkers.removeIf(mapMarkerData -> Objects.equals(mapMarkerData.MarkerId, markerId));
    }

    public void addMarker(MapMarkerData markerData) {
        mapMarkers.add(markerData);
    }
}
