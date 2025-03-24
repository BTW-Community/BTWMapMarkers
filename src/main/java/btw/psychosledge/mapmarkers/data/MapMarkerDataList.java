package btw.psychosledge.mapmarkers.data;

import btw.AddonHandler;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    public byte[] markersToByteArray() {
        ArrayList<String> markerStrings = new ArrayList<>();
        for(MapMarkerData marker : mapMarkers.values()){
            markerStrings.add(marker.toString());
        }
        String markers = String.join("|", markerStrings);
        return markers.getBytes();
    }

    public void loadFromBytes(byte[] bytes){
        String string = new String(bytes, StandardCharsets.UTF_8);
        String[] parts = string.split("\\|");
        for (String part : parts){
            if (!part.contains(",")) continue;
            MapMarkerData marker = new MapMarkerData(part);
            mapMarkers.putIfAbsent(marker.toString(), marker);
        }
    }
}
