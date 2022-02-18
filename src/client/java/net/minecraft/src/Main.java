package net.minecraft.src;

public class Main extends FCAddOn {
    public static Main instance = new Main();

    private Main() {
        super("BTW Map Markers", "0.1.0", "BMM");
    }

    @Override
    public void Initialize() {
        System.out.println("Did this do a thing?");
    }
}
