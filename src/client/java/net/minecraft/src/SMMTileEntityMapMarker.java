package net.minecraft.src;

public class SMMTileEntityMapMarker extends TileEntity {
    private String _markerId;

    @Override
    public void writeToNBT(NBTTagCompound nbtTag)
    {
        super.writeToNBT(nbtTag);
        if (_markerId != null && _markerId.length() > 0)
        {
            nbtTag.setString("id", this._markerId);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTag)
    {
        super.readFromNBT(nbtTag);
        SetMarkerId(nbtTag.getString("id"));
    }

    public String GetMarkerId() {
        return this._markerId;
    }

    public void SetMarkerId(String markerId) {
        this._markerId = markerId;
        validate();
    }
}
