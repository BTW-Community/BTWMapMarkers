package btw.community.sledge;

public class MapMarkerData {
    public final String MarkerId;
    public final int XPos;
    public final int YPos;
    public final int ZPos;
    public final int IconIndex;

    public MapMarkerData(String markerId, int xPos, int yPos, int zPos, int iconIndex) {
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

    public static MapMarkerData Deserialize(String serialized) {
        try {
            String[] splits = serialized.split("[,]");
            String markerId = splits[0].replace("MarkerId: ", "").trim();
            int xPos = Integer.parseInt(splits[1].replace("XPos: ", "").trim());
            int yPos = Integer.parseInt(splits[2].replace("YPos: ", "").trim());
            int zPos = Integer.parseInt(splits[3].replace("ZPos: ", "").trim());
            int iconIndex = Integer.parseInt(splits[4].replace("IconIndex: ", "").trim());

            return new MapMarkerData(markerId, xPos, yPos, zPos, iconIndex);
        } catch (NumberFormatException e) {
            //System.out.println("MapMarker Data was not able to be deserialized for '" + serialized + "' with error " + e);
            return null;
        }
    }
}
