package btw.psychosledge.mapmarkers;

import btw.AddonHandler;
import btw.BTWAddon;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import btw.psychosledge.mapmarkers.blocks.MapMarkerBlock;
import btw.psychosledge.mapmarkers.data.MapMarkerDataList;
import btw.psychosledge.mapmarkers.data.MarkerCoord;
import btw.psychosledge.mapmarkers.items.MapMarkerItem;
import btw.psychosledge.mapmarkers.tileentities.MapMarkerTileEntity;
import btw.util.color.ColorHelper;
import btw.world.util.data.DataEntry;
import btw.world.util.data.DataProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import static btw.crafting.recipe.RecipeManager.addCauldronRecipe;
import static btw.crafting.recipe.RecipeManager.addShapelessRecipe;

public class MapMarkersAddon extends BTWAddon {
    public MapMarkersAddon() {
        super();
    }

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
    public static final HashMap<String, ArrayList<MarkerCoord>> MAP_SPECIFIC_MARKERS = new HashMap<>();

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
        if (!MinecraftServer.getIsServer()) {
            initClientPacketInfo();
        }
        AddDefinitions();
        AddRecipes();
        AddonHandler.logMessage(this.getName() + " Initialized");
    }

    @Environment(value= EnvType.CLIENT)
    private void initClientPacketInfo() {
        registerPacketHandler(modID + "|mapList", (packet, player) -> {
            if (packet.channel.startsWith(modID + "|mapList")) {
                ByteArrayInputStream stream = new ByteArrayInputStream(packet.data);
                String received = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                String mapName = received.split(":")[0];
                ArrayList<MarkerCoord> markerCoords = new ArrayList<>();
                for (String rawCoord : received.split(":")[1].split("\\|")) {
                    String[] coordParts = rawCoord.split(",");
                    String markerId = coordParts[0];
                    int iconIndex = Integer.parseInt(coordParts[1]);
                    int xPos = Integer.parseInt(coordParts[2]);
                    int zPos = Integer.parseInt(coordParts[3]);
                    int rotation = Integer.parseInt(coordParts[4]);
                    markerCoords.add(new MarkerCoord(markerId, new MapCoord(null, (byte) iconIndex, (byte) xPos, (byte) zPos, (byte) rotation)));
                }
                MAP_SPECIFIC_MARKERS.put(mapName, markerCoords);
            }
        });
    }

    @Override
    public void preInitialize(){
        MAP_MARKER_DATA.register();
    }

    @Override
    public void serverPlayerConnectionInitialized(NetServerHandler serverHandler, EntityPlayerMP playerMP) {
        super.serverPlayerConnectionInitialized(serverHandler, playerMP);
        sendAllWorldMarkersToPlayer(serverHandler);
    }

    private void sendAllWorldMarkersToPlayer(NetServerHandler serverHandler){
        for (String mapName : MAP_SPECIFIC_MARKERS.keySet()) {
            sendMapMarkersToPlayer(mapName, serverHandler);
        }
    }

    public static void sendMapMarkersToPlayer(String mapName, NetServerHandler netServerHandler) {
        ArrayList<MarkerCoord> markers = MAP_SPECIFIC_MARKERS.getOrDefault(mapName, new ArrayList<>());
        if (markers.isEmpty()) return;
        ArrayList<String> markerStrings = new ArrayList<>();
        for (MarkerCoord marker : markers) {
            MapCoord coord = marker.mapCoord();
            markerStrings.add(marker.markerId() + "," + coord.iconSize + "," + coord.centerX + "," + coord.centerZ + "," + coord.iconRotation);
        }
        String markerString = String.join("|", markerStrings);
        markerString = mapName + ":" + markerString;
        Packet250CustomPayload packet = new Packet250CustomPayload("mapmarkers|mapList", markerString.getBytes());
        netServerHandler.sendPacketToPlayer(packet);
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