package net.minecraft.src;

public class SledgesMapMarkersAddon extends FCAddOn {
    public static SledgesMapMarkersAddon instance = new SledgesMapMarkersAddon();

    private SledgesMapMarkersAddon() {
        super("Sledge Map Markers", "0.1.0", "SMM");
    }

    @Override
    public void Initialize() {
        System.out.println("Where does this show up?");
    }
}
