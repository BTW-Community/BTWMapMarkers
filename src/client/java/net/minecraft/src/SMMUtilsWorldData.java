package net.minecraft.src;

import static net.minecraft.src.SledgesMapMarkersAddon.WorldMapMarkers;

public class SMMUtilsWorldData implements FCAddOnUtilsWorldData{

    @Override
    public void saveWorldDataToNBT(WorldServer world, NBTTagCompound tag) {
        System.out.println("SMMMapMarkers Saving...");
        NBTTagCompound markerTags = new NBTTagCompound();
        for (Object markerObj : WorldMapMarkers.values()) {
            SMMMapMarkerData markerData = (SMMMapMarkerData) markerObj;
            markerTags.setString(markerData.MarkerId, markerData.toString());
            System.out.println("SMMMapMarkers Saved: " + markerData);
        }
        tag.setCompoundTag("markers", markerTags);
    }

    @Override
    public void loadWorldDataFromNBT(WorldServer world, NBTTagCompound tag) {
        System.out.println("SMMMapMarkers Loading...");
        NBTTagCompound markerTags = tag.getCompoundTag("markers");
        if (markerTags == null) return;
        for (Object markerObj : markerTags.getTags()) {
            NBTTagString tagString = (NBTTagString) markerObj;
            SMMMapMarkerData markerData = SMMMapMarkerData.Deserialize(tagString.data);
            if (markerData == null) continue;
            //noinspection unchecked
            WorldMapMarkers.put(tagString.getName(), markerData);
            System.out.println("SMMMapMarkers Loaded: " + markerData);
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
        return null;
    }
}
