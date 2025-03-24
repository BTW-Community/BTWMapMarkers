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

    public MapMarkerData(String string) {
        loadFromString(string);
    }

    @Override
    public String toString() {
        return XPos + "," + YPos + "," + ZPos + "," + IconIndex;
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

    private void loadFromString(String string){
        String[] parts = string.split(",");
        XPos = Integer.parseInt(parts[0]);
        YPos = Integer.parseInt(parts[1]);
        ZPos = Integer.parseInt(parts[2]);
        IconIndex = Integer.parseInt(parts[3]);
    }
}
