package net.minecraft.src;

public class SMMBlockMapMarker extends Block {
    public SMMBlockMapMarker(int blockId) {
        super(blockId, Material.wood);
        setCreativeTab(CreativeTabs.tabDecorations);
        setHardness(0.1F);
        setUnlocalizedName("smmBlockMapMarker");
    }
}
