package net.minecraft.src;

import java.util.Locale;

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

    @Override
    public String toString() {
        return "MarkerId: " + MarkerId + ", XPos: " + XPos + ", YPos: " + YPos + ", ZPos: " + ZPos + ", IconIndex: " + IconIndex;
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static SMMMapMarkerData Deserialize(String serialized) {
        try {
            serialized = serialized.toLowerCase(Locale.ROOT);
            String[] splits = serialized.split("[,]");
            String markerId = splits[0].replace("markerid: ", "").trim();
            int xPos = Integer.parseInt(splits[1].replace("xpos: ", "").trim());
            int yPos = Integer.parseInt(splits[2].replace("ypos: ", "").trim());
            int zPos = Integer.parseInt(splits[3].replace("zpos: ", "").trim());
            int iconIndex = Integer.parseInt(splits[4].replace("iconindex: ", "").trim());

            return new SMMMapMarkerData(markerId, xPos, yPos, zPos, iconIndex);
        } catch (NumberFormatException e) {
            System.out.println("MapMarker Data was not able to be deserialized for '" + serialized + "' with error " + e);
            return null;
        }
    }
}
