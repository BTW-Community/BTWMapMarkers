package net.minecraft.src;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.minecraft.src.FCRecipes.AddShapelessRecipe;

public class SMMDefinitions {
    private static final int id_mapMarker = 2900;
    public static Block mapMarker;

    private static final int id_mapMarkerItem = 31900;
    public static Item mapMarkerItem;

    @SuppressWarnings("rawtypes")
    public static Map WorldMapMarkers = new LinkedHashMap<String, SMMMapMarkerData>();

    public static void AddDefinitions()
    {
        mapMarker = new SMMBlockMapMarker(id_mapMarker);
        Item.itemsList[mapMarker.blockID] = new ItemBlock(mapMarker.blockID - 256);

        mapMarkerItem = (new ItemReed(id_mapMarkerItem - 256, mapMarker)).
                SetBuoyant().SetFilterableProperties(Item.m_iFilterable_SolidBlock).
                setUnlocalizedName("smmItemMapMarker").setCreativeTab(CreativeTabs.tabTools);

        AddShapelessRecipe( new ItemStack( mapMarkerItem, 1 ), new Object[] {
                new ItemStack( FCBetterThanWolves.fcBlockWoodMouldingItemStubID, 1, FCUtilsInventory.m_iIgnoreMetadata ),
                new ItemStack( Item.paper )
        } );

        AddShapelessRecipe( new ItemStack( mapMarkerItem, 1 ), new Object[] {
                new ItemStack( Item.stick ),
                new ItemStack( Item.paper )
        } );

        TileEntity.addMapping(SMMTileEntityMapMarker.class, "SMMMapMarker");
    }
}