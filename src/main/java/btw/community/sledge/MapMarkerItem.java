package btw.community.sledge;

import net.minecraft.src.*;

import java.util.List;

public class MapMarkerItem extends ItemReed {
    private String name;

    public MapMarkerItem(int iItemID, Block block, String name) {
        super(iItemID, block.blockID);
        this.name = name;

        setHasSubtypes(true);
        setUnlocalizedName(name);
    }

    @Override
    public void getSubItems(int id, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(id, 1, 4));
        list.add(new ItemStack(id, 1, 5));
        list.add(new ItemStack(id, 1, 7));
        list.add(new ItemStack(id, 1, 8));
        list.add(new ItemStack(id, 1, 12));
        list.add(new ItemStack(id, 1, 13));
        list.add(new ItemStack(id, 1, 14));
        list.add(new ItemStack(id, 1, 15));
    }

    private Icon[] flagIcons = new Icon[16];

    @Override
    public void registerIcons(IconRegister register) {
        flagIcons[0] = register.registerIcon(name + "_0");
        flagIcons[4] = register.registerIcon(name + "_4");
        flagIcons[5] = register.registerIcon(name + "_5");
        flagIcons[7] = register.registerIcon(name + "_7");
        flagIcons[8] = register.registerIcon(name + "_8");
        flagIcons[12] = register.registerIcon(name + "_12");
        flagIcons[13] = register.registerIcon(name + "_13");
        flagIcons[14] = register.registerIcon(name + "_14");
        flagIcons[15] = register.registerIcon(name + "_15");
    }

    @Override
    public Icon getIconFromDamage(int damage) {
        return flagIcons[damage];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return  "item." + name + "_" + stack.getItemDamage();
    }
}
