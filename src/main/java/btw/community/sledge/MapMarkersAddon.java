package btw.community.sledge;

import btw.AddonHandler;
import btw.BTWAddon;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import btw.world.util.BTWWorldData;
import net.minecraft.src.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static btw.crafting.recipe.RecipeManager.addShapelessRecipe;

public class MapMarkersAddon extends BTWAddon {
    private static MapMarkersAddon instance;

    private MapMarkersAddon() {
        super("Map Markers", "2.0.0", "SMM");
    }

    private static final int id_mapMarker = 2900;
    public static Block mapMarker;

    private static final int id_mapMarkerItem = 31900;
    public static Item mapMarkerItem;

    @SuppressWarnings("rawtypes")
    public static Map WorldMapMarkers = new LinkedHashMap<String, MapMarkerData>();

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
        AddDefinitions();
        AddonHandler.logMessage(this.getName() + " Initialized");
    }

    public static MapMarkersAddon getInstance() {
        if (instance == null)
            instance = new MapMarkersAddon();
        return instance;
    }

    @Override
    public BTWWorldData createWorldData() {
        return new WorldDataUtils();
    }

    private static void AddDefinitions()
    {
        mapMarker = new MapMarkerBlock(id_mapMarker);
        Item.itemsList[mapMarker.blockID] = new ItemBlock(mapMarker.blockID - 256);

        mapMarkerItem = (new ItemReed(id_mapMarkerItem - 256, mapMarker)).
                setBuoyant().setFilterableProperties(Item.FILTERABLE_SOLID_BLOCK).
                setUnlocalizedName("smmItemMapMarker").setCreativeTab(CreativeTabs.tabMisc);

        addShapelessRecipe( new ItemStack( mapMarkerItem, 1 ), new Object[] {
                new ItemStack(BTWItems.woodMouldingStubID, 1, InventoryUtils.IGNORE_METADATA),
                new ItemStack( Item.paper )
        } );

        addShapelessRecipe( new ItemStack( mapMarkerItem, 1 ), new Object[] {
                new ItemStack( Item.stick ),
                new ItemStack( Item.paper )
        } );

        TileEntity.addMapping(MapMarkerTileEntity.class, "SMMMapMarker");
    }
}
