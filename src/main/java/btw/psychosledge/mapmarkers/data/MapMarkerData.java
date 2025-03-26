package btw.psychosledge.mapmarkers.data;

import net.minecraft.src.NBTTagCompound;

public class MapMarkerData {
    public int XPos;
    public int YPos;
    public int ZPos;
    public int IconIndex;

    public MapMarkerData(int xPos, int yPos, int zPos, int iconIndex) {
        XPos = xPos;
        YPos = yPos;
        ZPos = zPos;
        IconIndex = iconIndex;
    }

    public MapMarkerData(NBTTagCompound tempCompound) {
        loadFromNBT(tempCompound);
    }

    @Override
    public String toString() {
        return XPos + "." + YPos + "." + ZPos + "." + IconIndex;
    }

    public void writeToNBT(NBTTagCompound tagCompound){
        tagCompound.setInteger("XPos", XPos);
        tagCompound.setInteger("YPos", YPos);
        tagCompound.setInteger("ZPos", ZPos);
        tagCompound.setInteger("IconIndex", IconIndex);
    }

    private void loadFromNBT(NBTTagCompound tagCompound){
        XPos = tagCompound.getInteger("XPos");
        YPos = tagCompound.getInteger("YPos");
        ZPos = tagCompound.getInteger("ZPos");
        IconIndex = tagCompound.getInteger("IconIndex");
    }
}