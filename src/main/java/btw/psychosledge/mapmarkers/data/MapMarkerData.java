package btw.psychosledge.mapmarkers.data;

import net.minecraft.src.NBTTagCompound;

import java.io.Serializable;

public class MapMarkerData implements Serializable {
    public String MarkerId;
    public int XPos;
    public int YPos;
    public int ZPos;
    public int IconIndex;

    public MapMarkerData(String markerId, int xPos, int yPos, int zPos, int iconIndex) {
        MarkerId = markerId;
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
        return "MarkerId: " + MarkerId + ", XPos: " + XPos + ", YPos: " + YPos + ", ZPos: " + ZPos + ", IconIndex: " + IconIndex;
    }

    public void writeToNBT(NBTTagCompound tagCompound){
        tagCompound.setString("MarkerId", MarkerId);
        tagCompound.setInteger("XPos", XPos);
        tagCompound.setInteger("YPos", YPos);
        tagCompound.setInteger("ZPos", ZPos);
        tagCompound.setInteger("IconIndex", IconIndex);
    }

    public void loadFromNBT(NBTTagCompound tagCompound){
        MarkerId = tagCompound.getString("MarkerId");
        XPos = tagCompound.getInteger("XPos");
        YPos = tagCompound.getInteger("YPos");
        ZPos = tagCompound.getInteger("ZPos");
        IconIndex = tagCompound.getInteger("IconIndex");
    }
}
