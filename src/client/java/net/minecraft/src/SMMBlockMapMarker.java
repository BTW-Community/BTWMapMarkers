package net.minecraft.src;

import java.util.Random;

import static net.minecraft.src.SledgesMapMarkersAddon.WorldMapMarkers;
import static net.minecraft.src.SledgesMapMarkersAddon.mapMarkerItem;

public class SMMBlockMapMarker extends BlockContainer {

    double m_dBlockHeight = 1D;
    double m_dBlockWidth = (2D / 16D);
    double m_dBlockHalfWidth = (m_dBlockWidth / 2D);

    //AxisAlignedBB shaftBox = AxisAlignedBB.getAABBPool().getAABB(
    AxisAlignedBB shaftBox = new AxisAlignedBB(
            0.5D - m_dBlockHalfWidth, 0D, 0.5D - m_dBlockHalfWidth,
            0.5D + m_dBlockHalfWidth, m_dBlockHeight, 0.5D + m_dBlockHalfWidth);
    AxisAlignedBB flagBox = new AxisAlignedBB(
            shaftBox.minX, shaftBox.maxY - 0.25D, shaftBox.minZ - (6D / 16D),
            shaftBox.maxX, shaftBox.maxY, shaftBox.minZ);

    public SMMBlockMapMarker(int blockId) {
        super(blockId, Material.wood);

        setHardness( 0F );
        setResistance( 0F );
        SetBuoyant();
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

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side) {
        FCUtilsBlockPos targetPos = new FCUtilsBlockPos(x, y, z, Block.GetOppositeFacing( side));
        if (FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing(world, targetPos.i, targetPos.j, targetPos.k, side)) {
            int iTargetID = world.getBlockId(targetPos.i, targetPos.j, targetPos.k);
            return CanStickInBlockType(iTargetID);
        }
        return false;
    }

    public boolean CanStickInBlockType(int blockId) {
        Block block = Block.blocksList[blockId];
        return block != null;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        for (int facing = 0; facing < 6; facing++) {
            FCUtilsBlockPos targetPos = new FCUtilsBlockPos(x, y, z, facing);

            if (FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing(world, targetPos.i, targetPos.j, targetPos.k,
                    Block.GetOppositeFacing(facing)))
            {
                return true;
            }
        }

        return canPlaceOn(world, x, y - 1, z);
    }

    protected boolean canPlaceOn( World world, int x, int y, int z) {
        int facing = world.getBlockMetadata(x, y, z);
        return FCUtilsWorld.DoesBlockHaveSmallCenterHardpointToFacing(world, x, y, z,
                Block.GetOppositeFacing(facing), true);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        SMMTileEntityMapMarker tileEntity = (SMMTileEntityMapMarker) createNewTileEntity(world);
        world.setBlockTileEntity(x, y, z, tileEntity);
        tileEntity.Initialize();
        //System.out.println("onBlockAdded: " + tileEntity.GetMarkerId());
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        SMMTileEntityMapMarker tile = (SMMTileEntityMapMarker) world.getBlockTileEntity(x, y, z);
        if (tile != null) {
            WorldMapMarkers.remove(tile.GetMarkerId());
        }
        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public int idDropped(int metaData, Random random, int fortuneModifier)
    {
        return mapMarkerItem.itemID;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockId) {
        int facing = GetFacing(world, x, y, z);

        if (!canPlaceBlockOnSide(world, x, y, z, Block.GetOppositeFacing(facing))) {
            // pop the block off if it no longer has a valid anchor point
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockWithNotify(x, y, z, 0);
        }
    }

    @Override
    public boolean HasSmallCenterHardPointToFacing(IBlockAccess blockAccess, int x, int y, int z, int facing,
                                                   boolean ignoreTransparency)
    {
        // only has upwards facing hard point for torches
        return facing == 1 && GetFacing( blockAccess, x, y, z ) == 0;
    }

    public boolean CanBeCrushedByFallingEntity(World world, int x, int y, int z, EntityFallingSand entity) {
        return true;
    }

    public void OnCrushedByFallingEntity(World world, int x, int y, int z, EntityFallingSand entity) {
        if ( !world.isRemote ) {
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        }
    }

    @Override
    public void OnNeighborDisrupted(World world, int x, int y, int z, int toFacing) {
        if (toFacing == GetFacing(world, x, y, z)) {
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockWithNotify(x, y, z, 0);
        }
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ,
                             int metaData)
    {
        return side;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int facing, float xClick,
                                    float yClick, float zClick)
    {
        SMMTileEntityMapMarker tile = (SMMTileEntityMapMarker) world.getBlockTileEntity(x, y, z);
        if (tile == null) {
            return false;
        }
        int iconIndex = tile.GetIconIndex();
        //System.out.println("OnBlockActivated: " + tile.GetMarkerId() + " iconIndexBefore = " + iconIndex);
        tile.SetIconIndex(iconIndex + 1);
        return true;
    }

    @Override
    public boolean CanGroundCoverRestOnBlock(World world, int x, int y, int z) {
        return world.doesBlockHaveSolidTopSurface(x, y - 1, z);
    }

    @Override
    public float GroundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int x, int y, int z)
    {
        return -1F;
    }

    //----------- Client Side Functionality -----------//

    @SuppressWarnings("FieldCanBeLocal")
    private Icon iconStake;
    private Icon[] iconFlags;

    @Override
    public void registerIcons(IconRegister register) {
        iconStake = register.registerIcon("fcBlockLogChewedOak_side");
        blockIcon = iconStake;

        iconFlags = new Icon[16];

        for (int i = 0; i < iconFlags.length; ++i) {
            iconFlags[i] = register.registerIcon("cloth_" + i);
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int neighborX, int neighborY, int neighborZ,
                                        int side)
    {
        return true;
    }

    @Override
    public int idPicked(World world, int x, int y, int z) {
        return idDropped( world.getBlockMetadata(x, y, z ), world.rand, 0);
    }

    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        int metaData = blockAccess.getBlockMetadata(x, y, z);

        AxisAlignedBB box = shaftBox.MakeTemporaryCopy();
        box.ExpandToInclude(flagBox);
        box = getBoxPosition(box, metaData);

        return box;
    }

    private AxisAlignedBB getBoxPosition(AxisAlignedBB box, int position) {
        box = box.MakeTemporaryCopy();
        box.RotateAroundJToFacing(position);
        box.TiltToFacingAlongJ(position);
        return box;
    }

    @Override
    public boolean RenderBlock(RenderBlocks renderer, int x, int y, int z) {
        int metaData = renderer.blockAccess.getBlockMetadata(x, y, z);

        AxisAlignedBB shaft = getBoxPosition(shaftBox, metaData);
        AxisAlignedBB flag = getBoxPosition(flagBox, metaData);

        SMMTileEntityMapMarker tileEntity = (SMMTileEntityMapMarker) renderer.blockAccess.getBlockTileEntity(x, y, z);
        int iconFileIndex = 0;
        if (tileEntity != null) {
            iconFileIndex = tileEntity.GetIconFileIndex();
            //System.out.println("RenderBlock: " + tileEntity.GetMarkerId() + " iconIndex = " + tileEntity.GetIconIndex() + ", IconFileIndex = " + iconFileIndex);
        }

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
    public TileEntity createNewTileEntity(World world) { return new SMMTileEntityMapMarker(); }
}