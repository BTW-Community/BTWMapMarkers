package net.minecraft.src;

public class SMMTileEntityMapMarker extends TileEntity {
    private String _markerId;
    private int _iconIndex = 4;

    @Override
    public void writeToNBT(NBTTagCompound nbtTag)
    {
        super.writeToNBT(nbtTag);
        if (_markerId != null && _markerId.length() > 0)
        {
            nbtTag.setString("markerId", this._markerId);
            nbtTag.setInteger("icon", this._iconIndex);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTag)
    {
        super.readFromNBT(nbtTag);
        SetMarkerId(nbtTag.getString("markerId"));
        if (nbtTag.hasKey("icon")) {
            SetIconIndex(nbtTag.getInteger("icon"));
        }
    }

    private void updateWorldMapMarkers() {
        if (this._markerId == null) return;
        SMMMapMarkerData markerData = new SMMMapMarkerData(this._markerId, this.xCoord, this.yCoord, this.zCoord, this._iconIndex);
        SMMDefinitions.WorldMapMarkers.put(this._markerId, markerData);
        System.out.println("SMMMapMarker Set: " + markerData);
    }

    public String GetMarkerId() {
        return this._markerId;
    }

    public void SetMarkerId(String markerId) {
        this._markerId = markerId;
        updateWorldMapMarkers();
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
        updateWorldMapMarkers();
    }

    public int GetIconFileIndex() {
        int[] valueMap = {0, 0, 0, 0, 0, 5, 0, 14, 11, 0, 0, 0, 7, 4, 1, 10};
        return valueMap[_iconIndex];
    }
}
