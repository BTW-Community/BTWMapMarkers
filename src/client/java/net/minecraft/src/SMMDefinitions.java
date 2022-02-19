package net.minecraft.src;

public class SMMDefinitions {
    private static final int id_mapMarker = 2900;
    public static Block mapMarker;

    public static void AddDefinitions()
    {
        mapMarker = new SMMBlockMapMarker(id_mapMarker);
        Item.itemsList[mapMarker.blockID] = new ItemBlock(mapMarker.blockID - 256);

    }
}
