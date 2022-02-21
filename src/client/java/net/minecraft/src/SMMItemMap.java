package net.minecraft.src;

public class SMMItemMap extends FCItemMap{

    protected SMMItemMap(int itemId) {
        super(itemId);
    }

    public void AddMarker(SMMBlockMapMarker marker, World world, int x, int z, double rotation) {
        String markerId = getMarkerId(x, z);
        ItemStack itemStack = new ItemStack(marker);
        MapData mapData = this.getMapData(itemStack, world);
        FCAddOnHandler.LogMessage("adding " + markerId + " to " + mapData.mapName);
        addToMap(world, mapData, markerId, x, z, rotation);
    }

    public void RemoveMarker(SMMBlockMapMarker marker, World world, int x, int z) {
        String markerId = getMarkerId(x, z);
        ItemStack itemStack = new ItemStack(marker);
        MapData mapData = this.getMapData(itemStack, world);
        FCAddOnHandler.LogMessage("removing " + markerId + " from " + mapData.mapName);
        mapData.markersOnMap.remove(markerId);
    }

    private String getMarkerId(int x, int z) {
        return "SMM-Marker-" + x + '.' + z;
    }

    private void addToMap(World world, MapData mapData, String markerId, double xPosition, double zPosition, double rotation)
    {
        int icon = 6;

        int mapScale = 1 << mapData.scale;
        float xRelative = (float)(xPosition - (double)mapData.xCenter) / (float)mapScale;
        float zRelative = (float)(zPosition - (double)mapData.zCenter) / (float)mapScale;
        byte xLocal = (byte)((int)((double)(xRelative * 2.0F) + 0.5D));
        byte zLocal = (byte)((int)((double)(zRelative * 2.0F) + 0.5D));
        byte maxValue = 63;
        byte rotationValue;

        if (xRelative >= (float)(-maxValue) && zRelative >= (float)(-maxValue) && xRelative <= (float)maxValue && zRelative <= (float)maxValue)
        {
            rotation += rotation < 0.0D ? -8.0D : 8.0D;
            rotationValue = (byte)((int)(rotation * 16.0D / 360.0D));

            if (mapData.dimension < 0)
            {
                int var17 = (int)(world.getWorldInfo().getWorldTime() / 10L);
                rotationValue = (byte)(var17 * var17 * 34187121 + var17 * 121 >> 15 & 15);
            }
        }
        else
        {
            if (Math.abs(xRelative) >= 320.0F || Math.abs(zRelative) >= 320.0F)
            {
                FCAddOnHandler.LogMessage("removing " + markerId + " from " + mapData.mapName);
                mapData.markersOnMap.remove(markerId);
                return;
            }

            rotationValue = 0;

            if (xRelative <= (float)(-maxValue))
            {
                xLocal = (byte)((int)((double)(maxValue * 2) + 2.5D));
            }

            if (zRelative <= (float)(-maxValue))
            {
                zLocal = (byte)((int)((double)(maxValue * 2) + 2.5D));
            }

            if (xRelative >= (float)maxValue)
            {
                xLocal = (byte)(maxValue * 2 + 1);
            }

            if (zRelative >= (float)maxValue)
            {
                zLocal = (byte)(maxValue * 2 + 1);
            }
        }

        mapData.markersOnMap.put(markerId, new MapCoord(mapData, (byte)icon, xLocal, zLocal, rotationValue));
    }
}
