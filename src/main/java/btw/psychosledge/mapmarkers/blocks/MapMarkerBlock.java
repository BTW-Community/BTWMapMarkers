package btw.psychosledge.mapmarkers.blocks;

import btw.psychosledge.mapmarkers.tileentities.MapMarkerTileEntity;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;

import static btw.psychosledge.mapmarkers.MapMarkersAddon.MAP_MARKER_DATA;
import static btw.psychosledge.mapmarkers.MapMarkersAddon.mapMarkerItem;

public class MapMarkerBlock extends BlockContainer {
    double m_dBlockHeight = 1D;
    double m_dBlockWidth = (2D / 16D);
    double m_dBlockHalfWidth = (m_dBlockWidth / 2D);

    AxisAlignedBB shaftBox = new AxisAlignedBB(
            0.5D - m_dBlockHalfWidth, 0D, 0.5D - m_dBlockHalfWidth,
            0.5D + m_dBlockHalfWidth, m_dBlockHeight, 0.5D + m_dBlockHalfWidth);
    AxisAlignedBB flagBoxActivated = new AxisAlignedBB(
            shaftBox.minX, shaftBox.maxY - 0.25D, shaftBox.minZ - (6D / 16D),
            shaftBox.maxX, shaftBox.maxY, shaftBox.minZ);
    AxisAlignedBB flagBoxDeactivated = new AxisAlignedBB(
            shaftBox.minX, shaftBox.maxY - 0.35D, shaftBox.minZ - (4D / 16D),
            shaftBox.maxX, shaftBox.maxY, shaftBox.minZ);

    public MapMarkerBlock(int blockId) {
        super(blockId, Material.circuits);

        setHardness( 0F );
        setResistance( 0F );
        setBuoyant();
        setStepSound(soundWoodFootstep);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    //side is where player is edge where player is looking
    //blockPos shifts to the block being faced
    //checks if that block has center hard point on the side being faced
    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side) {
        BlockPos targetPos = new BlockPos(x, y, z, Block.getOppositeFacing( side));
        if (WorldUtils.doesBlockHaveCenterHardpointToFacing(world, targetPos.x, targetPos.y, targetPos.z, side)) {
            int iTargetID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
            return CanStickInBlockType(iTargetID);
        }
        return false;
    }

    public boolean CanStickInBlockType(int blockId) {
        Block block = Block.blocksList[blockId];
        return block != null;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        MapMarkerTileEntity tile = (MapMarkerTileEntity) world.getBlockTileEntity(x, y, z);
        if (tile != null) {
            world.getData(MAP_MARKER_DATA).removeMarkerById(tile.GetMarkerId());
        }
        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public void onFluidFlowIntoBlock(World world, int x, int y, int z, BlockFluid fluidBlock) {
        dropAsItem(world, x, y, z);
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
        if (!player.capabilities.isCreativeMode) {
            dropAsItem(world, x, y, z);
        }
    }

    private void dropAsItem(World world, int x, int y, int z) {
        int itemID = mapMarkerItem.itemID;
        MapMarkerTileEntity tile = (MapMarkerTileEntity) world.getBlockTileEntity(x, y, z);
        if (tile != null) {
            world.getData(MAP_MARKER_DATA).removeMarkerById(tile.GetMarkerId());
            int iconIndex = tile.getIconIndex();
            ItemStack stackDropped = new ItemStack(itemID, 1, iconIndex);
            dropBlockAsItem_do(world, x, y, z, stackDropped);
        }
        world.removeBlockTileEntity(x, y, z);
        world.setBlockToAir(x, y, z);
    }

    @Override
    public int getDamageValue(World world, int x, int y, int z)
    {
        return ((MapMarkerTileEntity) world.getBlockTileEntity(x, y, z)).getIconIndex();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockId) {
        //facing should be the part that sticks out, the method works out the anchor
        int iFacing = getFacing(world, x, y, z);
        if ( !canPlaceBlockOnSide( world, x, y, z, iFacing) ) {
            dropAsItem(world, x, y, z);
        }
    }

    @Override
    public boolean hasSmallCenterHardPointToFacing(IBlockAccess blockAccess, int x, int y, int z, int facing, boolean ignoreTransparency)
    {
        // only has upwards facing hard point for torches
        int blockFacing = getFacing(blockAccess, x, y, z);
        return facing == 1 && blockFacing == 1;
    }

    public boolean canBeCrushedByFallingEntity(World world, int x, int y, int z, EntityFallingSand entity) {
        return true;
    }

    public void onCrushedByFallingEntity(World world, int x, int y, int z, EntityFallingSand entity) {
        if ( !world.isRemote ) {
            dropAsItem(world, x, y, z);
        }
    }

    @Override
    public int getFacing(int iMetadata)
    {
        // shaft facing is the tip that sticks out
        return iMetadata;
    }

    @Override
    public int setFacing(int iMetadata, int iFacing)
    {
        //return iMetadata | iFacing;
        return iFacing;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ,
                             int metaData)
    {
        metaData = setFacing(metaData, side);
        return metaData;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack)
    {
        int facing = world.getBlockMetadata(x, y, z);
        int playerRotation = ((MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) + 2) % 4; //turns playerRotation into 0 - 4
        MapMarkerTileEntity tile = (MapMarkerTileEntity) world.getBlockTileEntity(x, y, z);

        if (tile != null) {
            tile.Initialize();
            tile.setIconIndex(stack.getItemDamage());
            if (facing <= 1)
            {
                tile.setFlagRotation(Direction.directionToFacing[playerRotation]);
            }
            else tile.setFlagRotation(0);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int facing, float xClick,
                                    float yClick, float zClick)
    {
        if (world.isRemote) return false;
        MapMarkerTileEntity tile = (MapMarkerTileEntity) world.getBlockTileEntity(x, y, z);
        if (tile == null) {
            return false;
        }
        ItemStack heldItem = player.getCurrentEquippedItem();
        if (heldItem == null) return false;
        if (tile.isHidden() && heldItem.getItem() instanceof ItemMap){
            tile.attemptActivate(heldItem);
            return true;
        }

        return false;
    }

    @Override
    public boolean canGroundCoverRestOnBlock(World world, int x, int y, int z) {
        return world.doesBlockHaveSolidTopSurface(x, y - 1, z);
    }

    @Override
    public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int x, int y, int z)
    {
        return -1F;
    }

    //----------- Client Side Functionality -----------//

    private Icon[] iconFlags;

    @Override
    public void registerIcons(IconRegister register) {
        blockIcon = register.registerIcon("btw:stripped_oak_log");

        iconFlags = new Icon[16];

        for (int i = 0; i < iconFlags.length; ++i) {
            iconFlags[i] = register.registerIcon("wool_colored_" + ItemDye.dyeItemNames[i]);
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int neighborX, int neighborY, int neighborZ, int side)
    {
        return true;
    }

    @Override
    @Environment(value= EnvType.CLIENT)
    public int idPicked(World world, int x, int y, int z) {
        return mapMarkerItem.itemID;
    }

    @Override
    public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        MapMarkerTileEntity tile = (MapMarkerTileEntity) blockAccess.getBlockTileEntity(x, y, z);
        int metaData = blockAccess.getBlockMetadata(x, y, z);

        AxisAlignedBB box = shaftBox.makeTemporaryCopy();
        AxisAlignedBB flagBox = tile.isHidden() ? flagBoxDeactivated : flagBoxActivated;
        box.expandToInclude(flagBox);
        box = getBoxPosition(box, metaData, tile);

        return box;
    }

    private AxisAlignedBB getBoxPosition(AxisAlignedBB box, int position, MapMarkerTileEntity tile) {

        box = box.makeTemporaryCopy();

        if (tile != null) {

            if (position == 0)
            {
                int flagRotation = tile.getFlagRotation();
                int tempRot = flagRotation;

                if (flagRotation == 4) tempRot = 5;
                if (flagRotation == 5) tempRot = 4;

                box.rotateAroundYToFacing(tempRot);
            }
            else if (position == 1)
            {
                box.rotateAroundYToFacing(tile.getFlagRotation());
            }
            else box.rotateAroundYToFacing(position);
        }

        box.tiltToFacingAlongY(position);

        return box;
    }

    @Override
    public boolean renderBlock(RenderBlocks renderer, int x, int y, int z) {
        MapMarkerTileEntity tileEntity = (MapMarkerTileEntity) renderer.blockAccess.getBlockTileEntity(x, y, z);
        int metaData = renderer.blockAccess.getBlockMetadata(x, y, z);

        AxisAlignedBB shaft = getBoxPosition(shaftBox, metaData, tileEntity);
        AxisAlignedBB flagBox = tileEntity.isHidden() ? flagBoxDeactivated : flagBoxActivated;
        AxisAlignedBB flag = getBoxPosition(flagBox, metaData, tileEntity);

        int iconFileIndex = tileEntity.GetIconFileIndex();

        //shaftBox
        renderer.setRenderBounds(shaft);
        renderer.renderStandardBlock( this, x, y, z);

        //flagBox
        renderer.unlockBlockBounds();
        renderer.setRenderBounds(flag);
        renderer.setOverrideBlockTexture(iconFlags[iconFileIndex]);
        renderer.renderStandardBlock(this, x, y, z);
        renderer.clearOverrideBlockTexture();

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world) { return new MapMarkerTileEntity(); }
}