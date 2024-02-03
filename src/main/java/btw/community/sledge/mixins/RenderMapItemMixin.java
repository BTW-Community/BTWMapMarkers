package btw.community.sledge.mixins;

import net.minecraft.src.MapCoord;
import net.minecraft.src.MapItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MapItemRenderer.class)
public abstract class RenderMapItemMixin {
    @Redirect(method = "renderMap", at = @At(value = "FIELD", target = "Lnet/minecraft/src/MapCoord;iconSize:B"))
    private byte injected(MapCoord mapCoord){
        int modified = (mapCoord.iconSize & 255);
        return (byte)modified;
    }

    @ModifyVariable(method = "renderMap", at = @At(value = "STORE", target = "Lnet/minecraft/src/MapItemRenderer;renderMap(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/RenderEngine;Lnet/minecraft/src/MapData;)V"))
    private int injected(int value) { return -value; }
}
