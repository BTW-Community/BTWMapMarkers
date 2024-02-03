package btw.community.sledge;

import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import net.minecraft.src.*;

import java.util.Random;

import static btw.community.sledge.MapMarkersAddon.WorldMapMarkers;
import static btw.community.sledge.MapMarkersAddon.mapMarkerItem;

public class MapMarkerBlock extends BlockContainer {

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

    public MapMarkerBlock(int blockId) {
        super(blockId, Material.wood);

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
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        for (int facing = 0; facing < 6; facing++) {
            BlockPos targetPos = new BlockPos(x, y, z, facing);

            if (WorldUtils.doesBlockHaveCenterHardpointToFacing(world, targetPos.x, targetPos.y, targetPos.z,
                    Block.getOppositeFacing(facing)))
            {
                return true;
            }
        }

        return canPlaceOn(world, x, y - 1, z);
    }

    protected boolean canPlaceOn( World world, int x, int y, int z) {
        int facing = world.getBlockMetadata(x, y, z);
        return WorldUtils.doesBlockHaveSmallCenterHardpointToFacing(world, x, y, z,
                Block.getOppositeFacing(facing), true);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        MapMarkerTileEntity tileEntity = (MapMarkerTileEntity) createNewTileEntity(world);
        world.setBlockTileEntity(x, y, z, tileEntity);
        tileEntity.Initialize();
        //System.out.println("onBlockAdded: " + tileEntity.GetMarkerId());
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        MapMarkerTileEntity tile = (MapMarkerTileEntity) world.getBlockTileEntity(x, y, z);
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
        int facing = getFacing(world, x, y, z);

        if (!canPlaceBlockOnSide(world, x, y, z, Block.getOppositeFacing(facing))) {
            // pop the block off if it no longer has a valid anchor point
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockWithNotify(x, y, z, 0);
        }
    }

    @Override
    public boolean hasSmallCenterHardPointToFacing(IBlockAccess blockAccess, int x, int y, int z, int facing,
                                                   boolean ignoreTransparency)
    {
        // only has upwards facing hard point for torches
        return facing == 1 && getFacing( blockAccess, x, y, z ) == 0;
    }

    public boolean canBeCrushedByFallingEntity(World world, int x, int y, int z, EntityFallingSand entity) {
        return true;
    }

    public void onCrushedByFallingEntity(World world, int x, int y, int z, EntityFallingSand entity) {
        if ( !world.isRemote ) {
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        }
    }

    @Override
    public void onNeighborDisrupted(World world, int x, int y, int z, int toFacing) {
        if (toFacing == getFacing(world, x, y, z)) {
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
        MapMarkerTileEntity tile = (MapMarkerTileEntity) world.getBlockTileEntity(x, y, z);
        if (tile == null) {
            return false;
        }
        int iconIndex = tile.GetIconIndex();
        //System.out.println("OnBlockActivated: " + tile.GetMarkerId() + " iconIndexBefore = " + iconIndex);
        tile.SetIconIndex(iconIndex + 1);
        return true;
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

    @SuppressWarnings("FieldCanBeLocal")
    private Icon iconStake;
    private Icon[] iconFlags;

    @Override
    public void registerIcons(IconRegister register) {
        iconStake = register.registerIcon("fcBlockLogStrippedOak_side");
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
    public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        int metaData = blockAccess.getBlockMetadata(x, y, z);

        AxisAlignedBB box = shaftBox.makeTemporaryCopy();
        box.expandToInclude(flagBox);
        box = getBoxPosition(box, metaData);

        return box;
    }

    private AxisAlignedBB getBoxPosition(AxisAlignedBB box, int position) {
        box = box.makeTemporaryCopy();
        box.rotateAroundYToFacing(position);
        box.tiltToFacingAlongY(position);
        return box;
    }

    @Override
    public boolean renderBlock(RenderBlocks renderer, int x, int y, int z) {
        int metaData = renderer.blockAccess.getBlockMetadata(x, y, z);

        AxisAlignedBB shaft = getBoxPosition(shaftBox, metaData);
        AxisAlignedBB flag = getBoxPosition(flagBox, metaData);

        MapMarkerTileEntity tileEntity = (MapMarkerTileEntity) renderer.blockAccess.getBlockTileEntity(x, y, z);
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
    public TileEntity createNewTileEntity(World world) { return new MapMarkerTileEntity(); }
}