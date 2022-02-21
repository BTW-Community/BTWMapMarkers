package net.minecraft.src;

public class SMMTileEntityMapMarker extends TileEntity {
    private String markerId;

    @Override
    public void writeToNBT(NBTTagCompound nbtTag)
    {
        super.writeToNBT(nbtTag);
        if (markerId != null && markerId.length() > 0)
        {
            nbtTag.setString("id", this.markerId);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTag)
    {
        super.readFromNBT(nbtTag);
        this.markerId = nbtTag.getString("id");
    }

    public String MarkerId() { return this.markerId; }
    public void MarkerId(String id) { this.markerId = id; }
}
