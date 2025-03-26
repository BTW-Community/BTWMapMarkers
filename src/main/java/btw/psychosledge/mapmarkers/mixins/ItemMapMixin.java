package btw.psychosledge.mapmarkers.mixins;

import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static btw.psychosledge.mapmarkers.MapMarkersAddon.sendMapMarkersToPlayer;

@Mixin(ItemMap.class)
public class ItemMapMixin extends ItemMapBase {
    protected ItemMapMixin(int par1) {
        super(par1);
    }

    @Inject(method = "getUpdatePacket", at = @At("HEAD"))
    private void sendMapSpecificPacket(ItemStack itemStack, World world, EntityPlayer player, CallbackInfoReturnable<Packet> cir){
        EntityPlayerMP playerMP = (EntityPlayerMP) player;
        NetServerHandler netServerHandler = playerMP.playerNetServerHandler;
        String mapName = "map_" + itemStack.getItemDamage();
        sendMapMarkersToPlayer(mapName, netServerHandler);
    }
}
