package net.minecraft.src;

public class SMMMapMarkerData {
    public final String MarkerId;
    public final int XPos;
    public final int YPos;
    public final int ZPos;
    public final int IconIndex;

    public SMMMapMarkerData(String markerId, int xPos, int yPos, int zPos, int iconIndex) {
        MarkerId = markerId;
        XPos = xPos;
        YPos = yPos;
        ZPos = zPos;
        IconIndex = iconIndex;
    }
}
