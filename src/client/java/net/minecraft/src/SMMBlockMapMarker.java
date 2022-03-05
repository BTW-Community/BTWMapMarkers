package net.minecraft.src;

import java.util.Random;

import static net.minecraft.src.SledgesMapMarkersAddon.WorldMapMarkers;
import static net.minecraft.src.SledgesMapMarkersAddon.mapMarkerItem;

public class SMMBlockMapMarker extends BlockContainer {

    double m_dBlockHeight = 1D;
    double m_dBlockWidth = (1D / 16D);
    double m_dBlockHalfWidth = (m_dBlockWidth / 2D);

    public SMMBlockMapMarker(int blockId) {
        super(blockId, Material.wood);

        setHardness( 0F );
        setResistance( 0F );
        SetBuoyant();
        setStepSound(soundWoodFootstep);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int x, int y, int z )
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
        FCUtilsBlockPos targetPos = new FCUtilsBlockPos( x, y, z, Block.GetOppositeFacing( side ) );
        if ( FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, targetPos.i, targetPos.j, targetPos.k, side ) )
        {
            int iTargetID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
            return CanStickInBlockType(iTargetID);
        }
        return false;
    }

    public boolean CanStickInBlockType( int blockId )
    {
        Block block = Block.blocksList[blockId];
        return block != null;
    }

    @Override
    public boolean canPlaceBlockAt( World world, int x, int y, int z )
    {
        for ( int iFacing = 0; iFacing < 6; iFacing++ )
        {
            FCUtilsBlockPos targetPos = new FCUtilsBlockPos( x, y, z, iFacing );

            if ( FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, targetPos.i, targetPos.j, targetPos.k,
                    Block.GetOppositeFacing( iFacing ) ) )
            {
                return true;
            }
        }

        return canPlaceOn(world, x, y - 1, z);
    }

    protected boolean canPlaceOn( World world, int x, int y, int z)
    {
        return FCUtilsWorld.DoesBlockHaveSmallCenterHardpointToFacing( world, x, y, z, 1, true );
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
    public int idDropped(int metaData, Random random, int fortuneModifier )
    {
        return mapMarkerItem.itemID;
    }

    @Override
    public void onNeighborBlockChange( World world, int x, int y, int z, int neighborBlockId )
    {
        if (!FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing(world, x, y - 1, z, 1))
        {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ, int metaData)
    {
        return metaData;
//        int alignment;
//
//        float xOffsetFromCenter = Math.abs( clickX - 0.5F );
//        float yOffsetFromCenter = Math.abs( clickY - 0.5F );
//        float zOffsetFromCenter = Math.abs( clickZ - 0.5F );
//
//        switch ( side )
//        {
//            case 0:
//                if ( xOffsetFromCenter > zOffsetFromCenter )
//                {
//                    alignment = clickX > 0.5F ? 9 : 11;
//                }
//                else
//                {
//                    alignment = clickZ > 0.5F ? 10 : 8;
//                }
//                break;
//            case 1:
//                if ( xOffsetFromCenter > zOffsetFromCenter )
//                {
//                    alignment = clickX > 0.5F ? 1 : 3;
//                }
//                else
//                {
//                    alignment = clickZ > 0.5F ? 2 : 0;
//                }
//                break;
//            case 2:
//                if ( xOffsetFromCenter > yOffsetFromCenter )
//                {
//                    alignment = clickX > 0.5F ? 6 : 7;
//                }
//                else
//                {
//                    alignment = clickY > 0.5F ? 10 : 2;
//                }
//                break;
//            case 3:
//                if ( xOffsetFromCenter > yOffsetFromCenter )
//                {
//                    alignment = clickX > 0.5F ? 5 : 4;
//                }
//                else
//                {
//                    alignment = clickY > 0.5F ? 8 : 0;
//                }
//                break;
//            case 4:
//                if ( zOffsetFromCenter > yOffsetFromCenter )
//                {
//                    alignment = clickZ > 0.5F ? 6 : 5;
//                }
//                else
//                {
//                    alignment = clickY > 0.5F ? 9 : 1;
//                }
//                break;
//            default: // 5
//                if ( zOffsetFromCenter > yOffsetFromCenter )
//                {
//                    alignment = clickZ > 0.5F ? 7 : 4;
//                }
//                else
//                {
//                    alignment = clickY > 0.5F ? 11 : 3;
//                }
//                break;
//        }
//
//        return alignment;
    }

    @Override
    public boolean onBlockActivated( World world, int x, int y, int z, EntityPlayer player, int facing, float xClick, float yClick, float zClick )
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
    public boolean CanGroundCoverRestOnBlock( World world, int x, int y, int z )
    {
        return world.doesBlockHaveSolidTopSurface( x, y - 1, z );
    }

    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int x, int y, int z )
    {
        return -1F;
    }

    //----------- Client Side Functionality -----------//

    @SuppressWarnings("FieldCanBeLocal")
    private Icon iconStake;
    private Icon[] iconFlags;

    @Override
    public void registerIcons( IconRegister register )
    {
        iconStake = register.registerIcon("fcBlockLogChewedOak_side");
        blockIcon = iconStake;

        iconFlags = new Icon[16];

        for (int i = 0; i < iconFlags.length; ++i)
        {
            iconFlags[i] = register.registerIcon("cloth_" + i);
        }
    }

    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int neighborX, int neighborY, int neighborZ, int side )
    {
        return true;
    }

    @Override
    public int idPicked( World world, int x, int y, int z )
    {
        return idDropped( world.getBlockMetadata( x, y, z ), world.rand, 0 );
    }

    @Override
    protected AxisAlignedBB GetFixedBlockBoundsFromPool() {
        return AxisAlignedBB.getAABBPool().getAABB(
                0.5D - m_dBlockHalfWidth, 0D, 0.5D - m_dBlockHalfWidth,
                0.5D + m_dBlockHalfWidth, m_dBlockHeight, 0.5D + m_dBlockHalfWidth)
                .expand(0D, 0D, (3D / 16D))
                .offset(0D, 0D, -(3D / 16D));
    }

    @Override
    public boolean RenderBlock( RenderBlocks renderer, int x, int y, int z )
    {
        AxisAlignedBB shaft = AxisAlignedBB.getAABBPool().getAABB(
                0.5D - m_dBlockHalfWidth, 0D, 0.5D - m_dBlockHalfWidth,
                0.5D + m_dBlockHalfWidth, m_dBlockHeight, 0.5D + m_dBlockHalfWidth);
        AxisAlignedBB flag = new AxisAlignedBB(shaft.minX, shaft.maxY - 0.25D, shaft.minZ - (6D / 16D), shaft.maxX, shaft.maxY, shaft.minZ);

        //shaft
        renderer.setRenderBounds(shaft);
        renderer.renderStandardBlock( this, x, y, z);

        SMMTileEntityMapMarker tileEntity = (SMMTileEntityMapMarker) renderer.blockAccess.getBlockTileEntity(x, y, z);
        int iconFileIndex;
        if (tileEntity != null) {
            iconFileIndex = tileEntity.GetIconFileIndex();
            //System.out.println("RenderBlock: " + tileEntity.GetMarkerId() + " iconIndex = " + tileEntity.GetIconIndex() + ", IconFileIndex = " + iconFileIndex);
        }
        else {
            iconFileIndex = 0;
            //System.out.println("RenderBlock: missing tile entity");
        }
        //flag
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