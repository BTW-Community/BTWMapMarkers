package btw.psychosledge.mapmarkers;

import btw.AddonHandler;
import btw.BTWAddon;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import btw.psychosledge.mapmarkers.blocks.MapMarkerBlock;
import btw.psychosledge.mapmarkers.data.MapMarkerDataList;
import btw.psychosledge.mapmarkers.items.MapMarkerItem;
import btw.psychosledge.mapmarkers.tileentities.MapMarkerTileEntity;
import btw.util.color.ColorHelper;
import btw.world.util.data.DataEntry;
import btw.world.util.data.DataProvider;
import net.minecraft.src.*;

import static btw.crafting.recipe.RecipeManager.addCauldronRecipe;
import static btw.crafting.recipe.RecipeManager.addShapelessRecipe;

public class MapMarkersAddon extends BTWAddon {
    public MapMarkersAddon() {
        super();
    }

    //private static MapMarkersAddon instance;

    private static final int id_mapMarker = 2900;
    public static Block mapMarker;

    private static final int id_mapMarkerItem = 31900;
    public static Item mapMarkerItem;

    public static final String SLEDGE_MAP_MARKERS_NAME = "SledgeMapMarkers";
    public static final DataEntry<MapMarkerDataList> MAP_MARKER_DATA = DataProvider.getBuilder(MapMarkerDataList.class)
            .world()
            .filename("SledgeWorldData")
            .name(SLEDGE_MAP_MARKERS_NAME)
            .defaultSupplier(MapMarkerDataList::new)
            .readNBT(tag -> {
                NBTTagList tagList = tag.getTagList(SLEDGE_MAP_MARKERS_NAME);
                return new MapMarkerDataList(tagList);
            })
            .writeNBT((tag, markerList) -> tag.setTag(SLEDGE_MAP_MARKERS_NAME, markerList.saveToNBT()))
            .build();

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
        AddDefinitions();
        AddRecipes();

        AddonHandler.logMessage(this.getName() + " Initialized");
    }

    @Override
    public void preInitialize(){
        MAP_MARKER_DATA.register();
    }

    private void AddDefinitions() {
        mapMarker = new MapMarkerBlock(id_mapMarker);

        mapMarkerItem = (new MapMarkerItem(id_mapMarkerItem - 256, mapMarker, "MapMarkerItem"))
                .setBuoyant()
                .setFilterableProperties(Item.FILTERABLE_SOLID_BLOCK)
                .setCreativeTab(CreativeTabs.tabMisc);

        TileEntity.addMapping(MapMarkerTileEntity.class, "SMMMapMarker");
    }

    private void AddRecipes() {

        //Default, aka white marker
        addShapelessRecipe(new ItemStack(mapMarkerItem, 1, 15), new Object[]{
                new ItemStack(BTWItems.woodMouldingStubID, 1, InventoryUtils.IGNORE_METADATA),
                new ItemStack(Item.paper)
        });

        addShapelessRecipe(new ItemStack(mapMarkerItem, 1, 15), new Object[]{
                new ItemStack(Item.stick),
                new ItemStack(Item.paper)
        });

        //dying any marker with dyes
        for (int i = 0; i < ColorHelper.colorOrder.length; i++) {
            addShapelessRecipe(new ItemStack(mapMarkerItem, 1, i), new Object[]{
                    new ItemStack(mapMarkerItem, 1, InventoryUtils.IGNORE_METADATA),
                    new ItemStack(Item.dyePowder, 1, i)
            });

            addCauldronRecipe(new ItemStack(mapMarkerItem, 16, i), new ItemStack[]{
                    new ItemStack(mapMarkerItem, 16, InventoryUtils.IGNORE_METADATA),
                    new ItemStack(Item.dyePowder, 1, i)
            });
        }
    }
}