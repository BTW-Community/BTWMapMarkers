package btw.psychosledge.mapmarkers.data;

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
        for (int iTempCount = 0; iTempCount < tagList.tagCount(); ++iTempCount) {
            NBTTagCompound tempCompound = (NBTTagCompound)tagList.tagAt(iTempCount);
            MapMarkerData newMarker = new MapMarkerData(tempCompound);
            mapMarkers.add(newMarker);
        }
    }

    public NBTTagList saveToNBT() {
        NBTTagList tagList = new NBTTagList("MapMarkers");
        for (MapMarkerData mapMarker : mapMarkers) {
            NBTTagCompound tempTagCompound = new NBTTagCompound();
            mapMarker.writeToNBT(tempTagCompound);
            tagList.appendTag(tempTagCompound);
        }
        return tagList;
    }

    public void removeMarkerById(String markerId) {
        mapMarkers.removeIf(mapMarkerData -> Objects.equals(mapMarkerData.MarkerId, markerId));
    }

    public void addMarker(MapMarkerData markerData) {
        mapMarkers.add(markerData);
    }
}
