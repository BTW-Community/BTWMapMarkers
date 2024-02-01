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
    AxisAlignedBB flagBoxActivated = new AxisAlignedBB(
            shaftBox.minX, shaftBox.maxY - 0.25D, shaftBox.minZ - (6D / 16D),
            shaftBox.maxX, shaftBox.maxY, shaftBox.minZ);
    AxisAlignedBB flagBoxDeactivated = new AxisAlignedBB(
            shaftBox.minX, shaftBox.maxY - 0.35D, shaftBox.minZ - (4D / 16D),
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
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        MapMarkerTileEntity tile = (MapMarkerTileEntity) world.getBlockTileEntity(x, y, z);
        if (tile != null) {
            WorldMapMarkers.remove(tile.GetMarkerId());
        }
        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
        int itemID = mapMarkerItem.itemID;
        MapMarkerTileEntity tile = (MapMarkerTileEntity) world.getBlockTileEntity(x, y, z);

        if (tile != null && !player.capabilities.isCreativeMode)
        {
            ItemStack stackDropped = new ItemStack(itemID, 1, tile.GetIconIndex());
            dropBlockAsItem_do(world, x, y, z, stackDropped);
        }
    }

    @Override
    public int idDropped(int metaData, Random random, int fortuneModifier)
    {
        return 0;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    public int getDamageValue(World world, int x, int y, int z)
    {
        MapMarkerTileEntity tile = (MapMarkerTileEntity) world.getBlockTileEntity(x, y, z);
        return this.damageDropped(tile.GetIconIndex());
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockId) {
        if (!canBlockStay(world, x, y, z)) {
            MapMarkerTileEntity tile = (MapMarkerTileEntity) world.getBlockTileEntity(x, y, z);
            if (tile != null) {

                // pop the block off if it no longer has a valid anchor point
                dropBlockAsItem(world, x, y, z, tile.GetIconIndex(), 0);
                WorldMapMarkers.remove(tile.GetMarkerId());
            }
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving player, ItemStack stack)
    {
        int meta = world.getBlockMetadata(x, y, z);
        int playerRotation = ((MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) + 2) % 4; //turns playerRotation into 0 - 4
        MapMarkerTileEntity tile = (MapMarkerTileEntity) world.getBlockTileEntity(x, y, z);

        if (tile != null) {
            tile.Initialize();
            tile.setIconIndex(stack.getItemDamage());
            if (meta <= 1)
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
        if (heldItem == null)
        {
//            int iconIndex = tile.GetIconIndex();
//            tile.setIconIndex(iconIndex + 1);
            return false;
        }
        else if (tile.isHidden() && heldItem.getItem() instanceof ItemMap){
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
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int neighborX, int neighborY, int neighborZ, int side)
    {
        return true;
    }

    @Override
    public int idPicked(World world, int x, int y, int z) {
        return idDropped( world.getBlockMetadata(x, y, z ), world.rand, 0);
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
        if (tileEntity != null) {
            iconFileIndex = tileEntity.GetIconFileIndex();
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