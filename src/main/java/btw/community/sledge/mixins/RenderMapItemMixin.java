package btw.community.sledge.mixins;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;

@Mixin(MapItemRenderer.class)
public abstract class RenderMapItemMixin {
    //    @Redirect(method = "renderMap", at = @At(value = "FIELD", target = "Lnet/minecraft/src/MapCoord;iconSize:B"))
//    private byte expandByte(MapCoord mapCoord){
//        //int value = (mapCoord.iconSize & 255);
//        int value = (mapCoord.iconSize);
//        int shifted = value < 0 ? value + 256 : value;
//        byte modified = (byte)(shifted);
//        float point1 = (float)(modified % 4 + 0) / 4.0F;
//        float point2 = (float)(modified / 4 + 0) / 4.0F;
//        float point3 = (float)(modified % 4 + 1) / 4.0F;
//        float point4 = (float)(modified / 4 + 1) / 4.0F;
//        System.out.println(value + ":" + modified + ":" + (mapCoord.iconSize & 255) + " = " + point1 + " " + point2 + " " + point3 + " " + point4);
//        return mapCoord.iconSize;
//    }
    //

//    @ModifyVariable(method = "renderMap", at = @At(value = "STORE", target = "Lnet/minecraft/src/MapItemRenderer;renderMap(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/RenderEngine;Lnet/minecraft/src/MapData;)V"))
//    private float modifyIconPosition1(float value)
//    {
//        System.out.println("resultFloat1: " + value);
//        return value;
//    }

//    @ModifyVariable(method = "renderMap", at = @At(value = "STORE", target = "Lnet/minecraft/src/MapItemRenderer;renderMap(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/RenderEngine;Lnet/minecraft/src/MapData;)V"))
//    private int flipZIndex(int value) {
//        //System.out.println("flippingZ: " + value);
//        return -value;
//    }

    @Shadow()
    private GameSettings gameSettings;
    @Shadow()
    private int[] intArray;
    @Shadow()
    private int bufferedImage;

    /**
     * @author psychosledge
     * @reason To allow expansion of the byte value used to choose the correct icon to display given the visible player. The additional players in this case are from the custom map markers
     */
    @Overwrite
    public void renderMap(EntityPlayer par1EntityPlayer, RenderEngine par2RenderEngine, MapData par3MapData)
    {
        for (int var4 = 0; var4 < 16384; ++var4)
        {
            byte var5 = par3MapData.colors[var4];

            if (var5 / 4 == 0)
            {
                this.intArray[var4] = (var4 + var4 / 128 & 1) * 8 + 16 << 24;
            }
            else
            {
                int var6 = MapColor.mapColorArray[var5 / 4].colorValue;
                int var7 = var5 & 3;
                short var8 = 220;

                if (var7 == 2)
                {
                    var8 = 255;
                }

                if (var7 == 0)
                {
                    var8 = 180;
                }

                int var9 = (var6 >> 16 & 255) * var8 / 255;
                int var10 = (var6 >> 8 & 255) * var8 / 255;
                int var11 = (var6 & 255) * var8 / 255;

                if (this.gameSettings.anaglyph)
                {
                    int var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
                    int var13 = (var9 * 30 + var10 * 70) / 100;
                    int var14 = (var9 * 30 + var11 * 70) / 100;
                    var9 = var12;
                    var10 = var13;
                    var11 = var14;
                }

                this.intArray[var4] = -16777216 | var9 << 16 | var10 << 8 | var11;
            }
        }

        par2RenderEngine.createTextureFromBytes(this.intArray, 128, 128, this.bufferedImage);
        byte var15 = 0;
        byte var16 = 0;
        Tessellator var17 = Tessellator.instance;
        float var18 = 0.0F;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.bufferedImage);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        var17.startDrawingQuads();
        var17.addVertexWithUV((double)((float)(var15 + 0) + var18), (double)((float)(var16 + 128) - var18), -0.009999999776482582D, 0.0D, 1.0D);
        var17.addVertexWithUV((double)((float)(var15 + 128) - var18), (double)((float)(var16 + 128) - var18), -0.009999999776482582D, 1.0D, 1.0D);
        var17.addVertexWithUV((double)((float)(var15 + 128) - var18), (double)((float)(var16 + 0) + var18), -0.009999999776482582D, 1.0D, 0.0D);
        var17.addVertexWithUV((double)((float)(var15 + 0) + var18), (double)((float)(var16 + 0) + var18), -0.009999999776482582D, 0.0D, 0.0D);
        var17.draw();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        par2RenderEngine.resetBoundTexture();
        par2RenderEngine.bindTexture("/misc/mapicons.png");
        int var19 = 0;

        for (Iterator var20 = par3MapData.playersVisibleOnMap.values().iterator(); var20.hasNext(); ++var19)
        {
            MapCoord var21 = (MapCoord)var20.next();
            GL11.glPushMatrix();
            GL11.glTranslatef((float)var15 + (float)var21.centerX / 2.0F + 64.0F, (float)var16 + (float)var21.centerZ / 2.0F + 64.0F, -0.02F);
            GL11.glRotatef((float)(var21.iconRotation * 360) / 16.0F, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(4.0F, 4.0F, 3.0F);
            GL11.glTranslatef(-0.125F, 0.125F, 0.0F);
            int iconSlot = (var21.iconSize & 255);
            float var22 = (float)(iconSlot % 4 + 0) / 4.0F;
            float var23 = (float)(iconSlot / 4 + 0) / 4.0F;
            float var24 = (float)(iconSlot % 4 + 1) / 4.0F;
            float var25 = (float)(iconSlot / 4 + 1) / 4.0F;
            var17.startDrawingQuads();
            var17.addVertexWithUV(-1.0D, 1.0D, (double)((float)-var19 * 0.001F), (double)var22, (double)var23);
            var17.addVertexWithUV(1.0D, 1.0D, (double)((float)-var19 * 0.001F), (double)var24, (double)var23);
            var17.addVertexWithUV(1.0D, -1.0D, (double)((float)-var19 * 0.001F), (double)var24, (double)var25);
            var17.addVertexWithUV(-1.0D, -1.0D, (double)((float)-var19 * 0.001F), (double)var22, (double)var25);
            var17.draw();
            GL11.glPopMatrix();
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, -0.04F);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
}
