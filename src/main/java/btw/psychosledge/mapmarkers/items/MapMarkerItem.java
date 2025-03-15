package btw.psychosledge.mapmarkers.items;

import btw.item.items.PlaceAsBlockItem;
import btw.util.color.ColorHelper;
import net.minecraft.src.*;

import java.util.List;

public class MapMarkerItem extends PlaceAsBlockItem {
    private final String name;

    public MapMarkerItem(int iItemID, Block block, String name) {
        super(iItemID, block.blockID);
        this.name = name;

        setHasSubtypes(true);
        setUnlocalizedName(name);
    }

    @Override
    public void getSubItems(int id, CreativeTabs tab, List list)
    {
        for (int iColor = 0; iColor < 16; ++iColor) {
            list.add(new ItemStack(id, 1, iColor));
        }
    }

    private final Icon[] flagIcons = new Icon[16];

    @Override
    public void registerIcons(IconRegister register) {
        for (int i = 0; i < flagIcons.length; i++) {
            flagIcons[i] = register.registerIcon("mapmarkers:" + name + "_" + ColorHelper.colorOrderCapital[i]);
        }
    }

    @Override
    public Icon getIconFromDamage(int damage) {
        if (damage == Short.MAX_VALUE) return flagIcons[15];
        return flagIcons[damage];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String baseName = "item." + name;
        if (stack.getItemDamage() == Short.MAX_VALUE) return baseName;
        return baseName + "_" + ColorHelper.colorOrder[stack.getItemDamage()];
    }
}
