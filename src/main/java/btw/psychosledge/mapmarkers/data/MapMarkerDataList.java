package btw.psychosledge.mapmarkers.data;

import btw.AddonHandler;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

import java.io.*;
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
                AddonHandler.logWarning("bad marker data: " + tempCompound);
            }
        }
        AddonHandler.logger.info("markers loaded: " + mapMarkers.size());
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

    public void ReInit(DataInputStream data) {
        try (ObjectInputStream ois = new ObjectInputStream(data)) {
            mapMarkers = (HashMap)ois.readObject();
        }
        catch (InvalidClassException e){
            AddonHandler.logWarning("InvalidClassException when loading marker data input stream from packet");
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] markersToByteArray() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(mapMarkers);
            return bos.toByteArray();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
