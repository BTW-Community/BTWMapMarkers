package btw.community.sledge;

import btw.AddonHandler;
import btw.BTWAddon;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import btw.world.util.BTWWorldData;
import net.minecraft.src.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static btw.crafting.recipe.RecipeManager.addCauldronRecipe;
import static btw.crafting.recipe.RecipeManager.addShapelessRecipe;

public class MapMarkersAddon extends BTWAddon {

    private MapMarkersAddon() {
        super("Map Markers", "2.2.0", "SMM");
    }

    private static final int id_mapMarker = 2900;
    public static Block mapMarker;

    private static final int id_mapMarkerItem = 31900;
    public static Item mapMarkerItem;

    public static Map<String, MapMarkerData> WorldMapMarkers = new LinkedHashMap<>();

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
        AddDefinitions();
        AddRecipes();
        AddonHandler.logMessage(this.getName() + " Initialized");
    }

    @Override
    public BTWWorldData createWorldData() {
        return new WorldDataUtils();
    }

    private static void AddDefinitions()
    {
        mapMarker = new MapMarkerBlock(id_mapMarker);
        Item.itemsList[mapMarker.blockID] = new ItemBlock(mapMarker.blockID - 256);

        mapMarkerItem = (new MapMarkerItem(id_mapMarkerItem - 256, mapMarker,
                "smmItemMapMarker")).
                setBuoyant().setFilterableProperties(Item.FILTERABLE_SOLID_BLOCK).
                setCreativeTab(CreativeTabs.tabMisc);

        TileEntity.addMapping(MapMarkerTileEntity.class, "SMMMapMarker");
    }

    private void AddRecipes() {

        int[] dyes = {
                15, //white
                10, //lime green
                1, //red
                4, //blue
                8, //gray
                11, //yellow
                14, //orange
                5, //purple
        };

        int[] markers = {
                0,
                5,
                7,
                8,
                12,
                13,
                14,
                15
        };

        //Default, aka white marker
        addShapelessRecipe( new ItemStack( mapMarkerItem, 1 ), new Object[] {
                new ItemStack( BTWItems.woodMouldingStubID, 1, InventoryUtils.IGNORE_METADATA),
                new ItemStack( Item.paper )
        } );

        addShapelessRecipe( new ItemStack( mapMarkerItem, 1 ), new Object[] {
                new ItemStack( Item.stick ),
                new ItemStack( Item.paper )
        } );

        //dying any marker with dyes
        for (int i = 0; i < markers.length; i++) {
            addShapelessRecipe( new ItemStack( mapMarkerItem, 1, markers[i] ), new Object[] {
                    new ItemStack( mapMarkerItem, 1, InventoryUtils.IGNORE_METADATA ),
                    new ItemStack( Item.dyePowder, 1, dyes[i] )
            } );
        }

        //cauldron dying any marker with dyes
        for (int i = 0; i < markers.length; i++) {
            addCauldronRecipe( new ItemStack( mapMarkerItem, 16, markers[i] ), new ItemStack[] {
                    new ItemStack( mapMarkerItem, 16, InventoryUtils.IGNORE_METADATA ),
                    new ItemStack( Item.dyePowder, 1, dyes[i] )
            } );
        }
    }
}
