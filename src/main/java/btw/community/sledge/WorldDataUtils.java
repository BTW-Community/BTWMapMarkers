package btw.community.sledge;

import btw.world.util.BTWWorldData;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagString;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldServer;

import static btw.community.sledge.MapMarkersAddon.WorldMapMarkers;

public class WorldDataUtils extends BTWWorldData {

    @Override
    public void saveWorldDataToNBT(WorldServer world, NBTTagCompound tag) {
        //System.out.println("SMMMapMarkers Saving...");
        NBTTagCompound markerTags = new NBTTagCompound();
        for (MapMarkerData markerData : WorldMapMarkers.values()) {
            markerTags.setString(markerData.MarkerId, markerData.toString());
            //System.out.println("SMMMapMarkers Saved: " + markerData);
        }
        tag.setCompoundTag("markers", markerTags);
    }

    @Override
    public void loadWorldDataFromNBT(WorldServer world, NBTTagCompound tag) {
        //System.out.println("SMMMapMarkers Loading...");
        WorldMapMarkers.clear();
        NBTTagCompound markerTags = tag.getCompoundTag("markers");
        if (markerTags == null) return;
        for (Object markerObj : markerTags.getTags()) {
            NBTTagString tagString = (NBTTagString) markerObj;
            MapMarkerData markerData = MapMarkerData.Deserialize(tagString.data);
            if (markerData == null) continue;
            WorldMapMarkers.put(tagString.getName(), markerData);
            //System.out.println("SMMMapMarkers Loaded: " + markerData);
        }
    }

    @Override
    public void saveGlobalDataToNBT(WorldInfo info, NBTTagCompound tag) {

    }

    @Override
    public void loadGlobalDataFromNBT(WorldInfo info, NBTTagCompound tag) {

    }

    @Override
    public void copyGlobalData(WorldInfo oldInfo, WorldInfo newInfo) {

    }

    @Override
    public String getFilename() {
        return "MapMarkers";
    }
}
