package net.minecraft.src;

import java.awt.*;

public class SMMTileEntityMapMarker extends TileEntity{
    public Color markerColor = Color.WHITE;

    public void writeToNBT(NBTTagCompound nbtTag)
    {
        super.writeToNBT(nbtTag);
        nbtTag.setInteger("color", markerColor.getRGB());
    }

    public void readFromNBT(NBTTagCompound nbtTag)
    {
        super.readFromNBT(nbtTag);

        this.markerColor = new Color(nbtTag.getInteger("color"));
    }
}
