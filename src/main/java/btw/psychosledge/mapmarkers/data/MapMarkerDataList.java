package btw.psychosledge.mapmarkers.data;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import java.util.HashMap;
import java.util.Objects;

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
                MapMarkerData newMarker = new MapMarkerData(tempCompound);
                if (Objects.equals(newMarker.MarkerId, "")) continue;
                mapMarkers.putIfAbsent(newMarker.MarkerId, newMarker);
            } catch (Exception e) {
                System.out.println("bad marker data: " + tempCompound);
            }
        }
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
        mapMarkers.putIfAbsent(markerData.MarkerId, markerData);
    }
}
