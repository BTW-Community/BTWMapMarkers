package btw.community.sledge;

import btw.block.tileentity.TileEntityDataPacketHandler;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.TileEntity;
import java.util.ArrayList;

import static btw.community.sledge.MapMarkersAddon.WorldMapMarkers;
import static btw.community.sledge.MapMarkersAddon.mapMarker;

public class MapMarkerTileEntity extends TileEntity implements TileEntityDataPacketHandler {
    private int _iconIndex = 4;

    @Override
    public void writeToNBT(NBTTagCompound nbtTag)
    {
        super.writeToNBT(nbtTag);
        nbtTag.setInteger("icon", this._iconIndex);
        //System.out.println("Tile saved: markerId = " + this.GetMarkerId() + ", iconIndex = " + this._iconIndex);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTag)
    {
        //System.out.println("Tile Loading");
        super.readFromNBT(nbtTag);
        if (nbtTag.hasKey("icon")) {
            //System.out.println("Tile loaded: markerId = " + this.GetMarkerId() + ", iconIndex = " + this._iconIndex);
            this._iconIndex = nbtTag.getInteger("icon");
        }
        //else {
            //System.out.println("Tile loaded: markerId = " + this.GetMarkerId() + ", iconIndex = null");
        //}
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        nbtTag.setInteger("icon", this._iconIndex);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
    }

    @Override
    public void readNBTFromPacket(NBTTagCompound nbtTag) {
        if (nbtTag.hasKey("icon")) {
            //System.out.println("Tile packet loaded: markerId = " + this.GetMarkerId() + ", iconIndex = " + this._iconIndex);
            this._iconIndex = nbtTag.getInteger("icon");
        }
//        else {
//            System.out.println("Tile packet loaded: markerId = " + this.GetMarkerId() + ", iconIndex = null");
//        }
        this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    public void Initialize() {
        removeNearbyBadMarkers();
        updateWorldMapMarkers();
    }

    private void removeNearbyBadMarkers() {
        //noinspection Convert2Diamond
        ArrayList<String> badMarkerIds = new ArrayList<String>();
        for (Object markerObj : WorldMapMarkers.values()) {
            MapMarkerData existingMarker = (MapMarkerData) markerObj;
            if (existingMarker.XPos >= this.xCoord - 64
                    && existingMarker.XPos <= this.xCoord + 64
                    && existingMarker.ZPos >= this.zCoord - 64
                    && existingMarker.ZPos <= this.zCoord + 64
                    && this.worldObj.getBlockId(existingMarker.XPos, existingMarker.YPos, existingMarker.ZPos) != mapMarker.blockID) {
                badMarkerIds.add(existingMarker.MarkerId);
            }
        }
        for (String badMarkerId : badMarkerIds) {
            WorldMapMarkers.remove(badMarkerId);
            //System.out.println("SMMMapMarkers Removed Bad Marker: " + badMarkerId);
        }
    }

    private void updateWorldMapMarkers() {
        MapMarkerData markerData = new MapMarkerData(this.GetMarkerId(), this.xCoord, this.yCoord, this.zCoord, this._iconIndex);
        //noinspection unchecked
        WorldMapMarkers.put(this.GetMarkerId(), markerData);
    }

    public String GetMarkerId() {
        return "SMM-Marker " + this.xCoord + "." + this.yCoord + '.' + this.zCoord;
    }

    public int GetIconIndex() {
        return this._iconIndex;
    }

    public void SetIconIndex(int iconIndex) {
        // skip 6 (default "off map" icon)
        if (iconIndex == 6) iconIndex = 7;
        // skip 9 thru 11 for now
        if (iconIndex == 9) iconIndex = 12;
        // numbering starts back at 4 to skip default player icons
        if (iconIndex > 15) iconIndex = 4;
        this._iconIndex = iconIndex;
        if (this.worldObj.isRemote) {
            updateWorldMapMarkers();
            //System.out.println("TileSetIconIndex remote: " + this.GetMarkerId() + " iconIndex = " + iconIndex + ", iconFileIndex = " + GetIconFileIndex());
            this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
//        else {
//            System.out.println("TileSetIconIndex: " + this.GetMarkerId() + " iconIndex = " + iconIndex + ", iconFileIndex = " + GetIconFileIndex());
//        }
    }

    public int GetIconFileIndex() {
        int[] valueMap = {0, 0, 0, 0, 0, 5, 0, 14, 11, 0, 0, 0, 7, 4, 1, 10};
        return valueMap[_iconIndex];
    }
}