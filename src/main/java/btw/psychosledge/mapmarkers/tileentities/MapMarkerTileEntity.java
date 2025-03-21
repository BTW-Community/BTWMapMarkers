package btw.psychosledge.mapmarkers.tileentities;

import btw.block.tileentity.TileEntityDataPacketHandler;
import btw.psychosledge.mapmarkers.data.MapMarkerData;
import net.minecraft.src.*;

import java.util.ArrayList;

import static btw.psychosledge.mapmarkers.MapMarkersAddon.MAP_MARKER_DATA;
import static btw.psychosledge.mapmarkers.MapMarkersAddon.mapMarker;

public class MapMarkerTileEntity extends TileEntity implements TileEntityDataPacketHandler {
    private int _iconIndex = 0;
    private int rotation = 0;
    private boolean hidden = false;

    @Override
    public void writeToNBT(NBTTagCompound nbtTag)
    {
        super.writeToNBT(nbtTag);
        nbtTag.setInteger("icon", this._iconIndex);
        nbtTag.setInteger("rotation", this.rotation);
        nbtTag.setBoolean("hidden", this.hidden);
        
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTag)
    {
        super.readFromNBT(nbtTag);
        if (nbtTag.hasKey("icon")) {
            this._iconIndex = nbtTag.getInteger("icon");
        }
        if (nbtTag.hasKey("rotation"))
        {
            this.rotation = nbtTag.getInteger("rotation");
        }
        if (nbtTag.hasKey("hidden"))
        {
            this.hidden = nbtTag.getBoolean("hidden");
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        nbtTag.setInteger("icon", this._iconIndex);
        nbtTag.setInteger("rotation", this.rotation);
        nbtTag.setBoolean("hidden", this.hidden);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
    }

    @Override
    public void readNBTFromPacket(NBTTagCompound nbtTag) {
        if (nbtTag.hasKey("icon")) {
            this._iconIndex = nbtTag.getInteger("icon");
        }
        if (nbtTag.hasKey("rotation"))
        {
            this.rotation = nbtTag.getInteger("rotation");
        }
        if (nbtTag.hasKey("hidden"))
        {
            this.hidden = nbtTag.getBoolean("hidden");
        }

        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    public void Initialize() {
        hidden = true;
        removeNearbyBadMarkers();
    }

    @Override
    public void updateEntity(){
        super.updateEntity();
    }

    private void removeNearbyBadMarkers() {
        ArrayList<String> badMarkerIds = new ArrayList<>();
        var markers = worldObj.getData(MAP_MARKER_DATA);
        for (MapMarkerData existingMarker : markers.mapMarkers.values()) {
            if (existingMarker.XPos >= this.xCoord - 64
                    && existingMarker.XPos <= this.xCoord + 64
                    && existingMarker.ZPos >= this.zCoord - 64
                    && existingMarker.ZPos <= this.zCoord + 64
                    && this.worldObj.getBlockId(existingMarker.XPos, existingMarker.YPos, existingMarker.ZPos) != mapMarker.blockID) {
                badMarkerIds.add(existingMarker.MarkerId);
            }
        }
        for (String badMarkerId : badMarkerIds) {
            markers.removeMarkerById(badMarkerId);
        }
    }

    private void updateWorldMapMarkers() {
        if (this.worldObj != null && !this.worldObj.isRemote) {

            if (hidden) {
                worldObj.getData(MAP_MARKER_DATA).removeMarkerById(this.GetMarkerId());
            } else {
                MapMarkerData markerData = new MapMarkerData(this.GetMarkerId(), this.xCoord, this.yCoord, this.zCoord, this._iconIndex);
                worldObj.getData(MAP_MARKER_DATA).addMarker(markerData);
            }
       }
    }

    public String GetMarkerId() {
        return "SMM-Marker " + this.xCoord + "." + this.yCoord + '.' + this.zCoord;
    }

    public void setFlagRotation (int rotation)
    {
        this.rotation = rotation;
    }

    public int getFlagRotation() {
        return this.rotation;
    }

    public void attemptActivate(ItemStack stack){
        if (worldObj.isRemote) return;
        this.hidden = !isLocationOnMap(stack);
        updateWorldMapMarkers();
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    private boolean isLocationOnMap(ItemStack stack) {
        ItemMap mapItem = (ItemMap) stack.getItem();
        MapData mapData = mapItem.getMapData(stack, worldObj);
        int mapScale = 1 << mapData.scale;
        float relativeX = (float) ((double) xCoord - (double) mapData.xCenter) / (float) mapScale;
        float relativeZ = (float) ((double) zCoord - (double) mapData.zCenter) / (float) mapScale;

        return !(Math.abs(relativeX) > 64F) && !(Math.abs(relativeZ) > 64F);
    }

    public boolean isHidden(){
        return this.hidden;
    }

    public int getIconIndex() { return _iconIndex; }
    public void setIconIndex(int iconIndex) {
        this._iconIndex = iconIndex;
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    public int GetIconFileIndex() {
        return _iconIndex;
    }
}