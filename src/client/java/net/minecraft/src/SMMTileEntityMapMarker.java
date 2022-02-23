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
        SMMDefinitions.WorldMapMarkers.put(this._markerId, new SMMMapMarkerData(this._markerId, this.xCoord, this.yCoord, this.zCoord, this._iconIndex));
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
        if (iconIndex == 6) iconIndex = 7;
        if (iconIndex == 13) iconIndex = 4;
        this._iconIndex = iconIndex;
        updateWorldMapMarkers();
    }
}
