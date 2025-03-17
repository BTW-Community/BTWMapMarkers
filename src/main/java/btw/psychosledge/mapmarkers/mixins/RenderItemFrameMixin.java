package btw.psychosledge.mapmarkers.mixins;

import btw.client.texture.CustomUpdatingTexture;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderItemFrame.class)
public abstract class RenderItemFrameMixin extends Render {
    @Final
    @Shadow private RenderBlocks renderBlocksInstance;
    @Shadow private Icon field_94147_f;
    @Final
    @Shadow private static ResourceLocation mapBackgroundTextures;



    private void renderFrameItemAsBlock(EntityItemFrame par1EntityItemFrame) {
        //substitute bail if map
        ItemStack itemStack = par1EntityItemFrame.getDisplayedItem();
        if (itemStack != null) {
            EntityItem entityItem = new EntityItem(par1EntityItemFrame.worldObj, 0.0, 0.0, 0.0, itemStack);
            if (entityItem.getEntityItem().getItem() == Item.map){
                return;
            }
        }
        GL11.glPushMatrix();
        GL11.glRotatef(par1EntityItemFrame.rotationYaw, 0.0f, 1.0f, 0.0f);
        this.renderManager.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        Block var2 = Block.planks;
        float var3 = 0.0625f;
        float var4 = 0.75f;
        float var5 = var4 / 2.0f;
        GL11.glPushMatrix();
        this.renderBlocksInstance.overrideBlockBounds(0.0, 0.5f - var5 + 0.0625f, 0.5f - var5 + 0.0625f, var3 * 0.5f, 0.5f + var5 - 0.0625f, 0.5f + var5 - 0.0625f);
        this.renderBlocksInstance.setOverrideBlockTexture(this.field_94147_f);
        this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0f);
        this.renderBlocksInstance.clearOverrideBlockTexture();
        this.renderBlocksInstance.unlockBlockBounds();
        GL11.glPopMatrix();
        this.renderBlocksInstance.setOverrideBlockTexture(Block.planks.getIcon(1, 2));
        GL11.glPushMatrix();
        this.renderBlocksInstance.overrideBlockBounds(0.0, 0.5f - var5, 0.5f - var5, var3 + 1.0E-4f, var3 + 0.5f - var5, 0.5f + var5);
        this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.renderBlocksInstance.overrideBlockBounds(0.0, 0.5f + var5 - var3, 0.5f - var5, var3 + 1.0E-4f, 0.5f + var5, 0.5f + var5);
        this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.renderBlocksInstance.overrideBlockBounds(0.0, 0.5f - var5, 0.5f - var5, var3, 0.5f + var5, var3 + 0.5f - var5);
        this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.renderBlocksInstance.overrideBlockBounds(0.0, 0.5f - var5, 0.5f + var5 - var3, var3, 0.5f + var5, 0.5f + var5);
        this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0f);
        GL11.glPopMatrix();
        this.renderBlocksInstance.unlockBlockBounds();
        this.renderBlocksInstance.clearOverrideBlockTexture();
        GL11.glPopMatrix();
    }

    private void func_82402_b(EntityItemFrame par1EntityItemFrame) {
        ItemStack var2 = par1EntityItemFrame.getDisplayedItem();
        if (var2 != null) {
            EntityItem var3 = new EntityItem(par1EntityItemFrame.worldObj, 0.0, 0.0, 0.0, var2);
            var3.getEntityItem().stackSize = 1;
            var3.hoverStart = 0.0f;
            GL11.glPushMatrix();
            //substitute y of 0f
            GL11.glTranslatef(-0.453125f * (float) Direction.offsetX[par1EntityItemFrame.hangingDirection], 0f, -0.453125f * (float)Direction.offsetZ[par1EntityItemFrame.hangingDirection]);
            GL11.glRotatef(180.0f + par1EntityItemFrame.rotationYaw, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(-90 * par1EntityItemFrame.getRotation(), 0.0f, 0.0f, 1.0f);
            switch (par1EntityItemFrame.getRotation()) {
                case 1: {
                    GL11.glTranslatef(-0.16f, -0.16f, 0.0f);
                    break;
                }
                case 2: {
                    GL11.glTranslatef(0.0f, -0.32f, 0.0f);
                    break;
                }
                case 3: {
                    GL11.glTranslatef(0.16f, -0.16f, 0.0f);
                }
            }
            if (var3.getEntityItem().getItem() == Item.map) {
                this.renderManager.renderEngine.bindTexture(mapBackgroundTextures);
                Tessellator var4 = Tessellator.instance;
                //substitute GLL calls
                float var10 = -0.015625F + (0.0625F / 2.0F);
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(-0.5F, -0.5F, var10 - 2.0E-4F);
                GL11.glScalef(0.0078125F, 0.0078125F, 0.0078125F);
                GL11.glNormal3f(0.0F, 0.0F, -1.0F);
                var4.startDrawingQuads();
                //substitute value for vertices
                int var5 = 0;
                var4.addVertexWithUV(0 - var5, 128 + var5, 0.0, 0.0, 1.0);
                var4.addVertexWithUV(128 + var5, 128 + var5, 0.0, 1.0, 1.0);
                var4.addVertexWithUV(128 + var5, 0 - var5, 0.0, 1.0, 0.0);
                var4.addVertexWithUV(0 - var5, 0 - var5, 0.0, 0.0, 0.0);
                var4.draw();
                MapData var6 = Item.map.getMapData(var3.getEntityItem(), par1EntityItemFrame.worldObj);
                GL11.glTranslatef(0.0f, 0.0f, -1.0f);
                if (var6 != null) {
                    this.renderManager.itemRenderer.mapItemRenderer.renderMap(null, this.renderManager.renderEngine, var6);
                }
            } else {
                CustomUpdatingTexture customUpdateTexture;
                RenderItem.renderInFrame = true;
                Icon icon = var3.getEntityItem().getItem().getIconFromDamage(0);
                if (icon instanceof CustomUpdatingTexture) {
                    customUpdateTexture = (CustomUpdatingTexture)((Object)icon);
                    customUpdateTexture.updateActive(CustomUpdatingTexture.ITEM_FRAME);
                }
                RenderManager.instance.renderEntityWithPosYaw(var3, 0.0, 0.0, 0.0, 0.0f, 0.0f);
                icon = var3.getEntityItem().getItem().getIconFromDamage(0);
                if (icon instanceof CustomUpdatingTexture) {
                    customUpdateTexture = (CustomUpdatingTexture)((Object)icon);
                    customUpdateTexture.updateInert(CustomUpdatingTexture.ITEM_FRAME);
                }
                RenderItem.renderInFrame = false;
            }
            GL11.glPopMatrix();
        }
    }
}
