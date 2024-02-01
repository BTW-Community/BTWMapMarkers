package btw.community.sledge;

import btw.block.tileentity.TileEntityDataPacketHandler;
import btw.item.items.MapItem;
import net.minecraft.src.*;

import java.util.ArrayList;

import static btw.community.sledge.MapMarkersAddon.WorldMapMarkers;
import static btw.community.sledge.MapMarkersAddon.mapMarker;

public class MapMarkerTileEntity extends TileEntity implements TileEntityDataPacketHandler {
    private int _iconIndex = 4;
    private int rotation = 0;
    private boolean hidden = false;

    @Override
    public void writeToNBT(NBTTagCompound nbtTag)
    {
        super.writeToNBT(nbtTag);
        nbtTag.setInteger("icon", this._iconIndex);
        nbtTag.setInteger("rotation", this.rotation);
        nbtTag.setBoolean("hidden", this.hidden);
        //worldObj.markBlockRangeForRenderUpdate( xCoord, yCoord, zCoord, xCoord, yCoord, zCoord );
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
        //worldObj.markBlockRangeForRenderUpdate( xCoord, yCoord, zCoord, xCoord, yCoord, zCoord );
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    public void Initialize() {
        hidden = true;
        removeNearbyBadMarkers();
        updateWorldMapMarkers();
    }

    private void removeNearbyBadMarkers() {
        ArrayList<String> badMarkerIds = new ArrayList<>();
        for (MapMarkerData existingMarker : WorldMapMarkers.values()) {
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
        }
    }

    private void updateWorldMapMarkers() {
        if (!this.worldObj.isRemote) {

            MapMarkerData markerData = new MapMarkerData(this.GetMarkerId(), this.xCoord, this.yCoord, this.zCoord, this._iconIndex);
            if (hidden) {
                WorldMapMarkers.remove(this.GetMarkerId());
            } else {
                WorldMapMarkers.put(this.GetMarkerId(), markerData);
            }
       }
    }

    public String GetMarkerId() {
        return "SMM-Marker " + this.xCoord + "." + this.yCoord + '.' + this.zCoord;
    }

    public int GetIconIndex() {
        return this._iconIndex;
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
        if (isLocationOnMap(stack)) {
            this.hidden = false;
        }
        else {
            this.hidden = true;
        }
        updateWorldMapMarkers();
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    private boolean isLocationOnMap(ItemStack stack) {
        MapItem mapItem = (MapItem) stack.getItem();
        MapData mapData = mapItem.getMapData(stack, worldObj);
        int mapScale = 1 << mapData.scale;
        float relativeX = (float) ((double) xCoord - (double) mapData.xCenter) / (float) mapScale;
        float relativeZ = (float) ((double) zCoord - (double) mapData.zCenter) / (float) mapScale;
        //System.out.println("Remote " + worldObj.isRemote + " Scale " + mapScale + " x " + xCoord + " centerX " + mapData.xCenter + " relativeX " + relativeX + " z " + zCoord + " centerZ " + mapData.zCenter + " relativeZ " + relativeZ);
        return !(Math.abs(relativeX) > 64F) && !(Math.abs(relativeZ) > 64F);
    }

    public boolean isHidden(){
        return this.hidden;
    }

    public void setIconIndex(int iconIndex) {
        // start at 4 to skip default player icons
        if (iconIndex < 4) iconIndex = 4;
        // skip 6 (default "off map" icon)
        if (iconIndex == 6) iconIndex = 7;
        // skip 9 thru 11 for now
        if (iconIndex == 9) iconIndex = 12;
        // numbering starts back at 4 to skip default player icons
        if (iconIndex > 15) iconIndex = 4;
        this._iconIndex = iconIndex;
        updateWorldMapMarkers();
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    public int GetIconFileIndex() {
        int[] valueMap = {0, 0, 0, 0, 0, 5, 0, 14, 11, 0, 0, 0, 7, 4, 1, 10};
        return valueMap[_iconIndex];
    }
}