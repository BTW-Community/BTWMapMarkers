package net.minecraft.src;

import java.util.Random;

import static net.minecraft.src.SMMDefinitions.mapMarkerItem;

public class SMMBlockMapMarker extends Block {

    public SMMBlockMapMarker(int blockId) {
        super(blockId, Material.wood);
        setHardness( 2F );
        setResistance( 5F );
        SetAxesEffectiveOn( true );

        SetFireProperties(FCEnumFlammability.PLANKS);
        setStepSound(soundWoodFootstep);
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
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
        for ( int iFacing = 0; iFacing < 6; iFacing++ )
        {
            FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );

            if ( FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, targetPos.i, targetPos.j, targetPos.k,
                    Block.GetOppositeFacing( iFacing ) ) )
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
        FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, Block.GetOppositeFacing( iFacing ) );

        if ( !FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, targetPos.i, targetPos.j, targetPos.k, iFacing ) )
        {
            iFacing = FindValidFacing( world, i, j, k );
        }

        return SetFacing( iMetadata, iFacing );
    }

    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState(
            IBlockAccess blockAccess, int i, int j, int k )
    {
        double m_dBlockHeight = (24D / 16D);
        double m_dBlockWidth = (2D / 16D);
        double m_dBlockHalfWidth = (m_dBlockWidth / 2D);
        AxisAlignedBB box = AxisAlignedBB.getAABBPool().getAABB(
                0.5D - m_dBlockHalfWidth, 0D, 0.5D - m_dBlockHalfWidth,
                0.5D + m_dBlockHalfWidth, m_dBlockHeight, 0.5D + m_dBlockHalfWidth);

        box.TiltToFacingAlongJ( GetFacing( blockAccess, i, j, k ) );

        return box;
    }

    @Override
    public int idDropped(int iMetaData, Random random, int iFortuneModifier )
    {
        return mapMarkerItem.itemID;
    }

    @Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
        // pop the block off if it no longer has a valid anchor point

        int iFacing = GetFacing( world, i, j, k );

        FCUtilsBlockPos anchorPos = new FCUtilsBlockPos( i, j, k, Block.GetOppositeFacing( iFacing ) );

        if ( !FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, anchorPos.i, anchorPos.j, anchorPos.k, iFacing ) )
        {
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            world.setBlockWithNotify( i, j, k, 0 );
        }
    }

//    @Override
//    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
//    {
//        ItemStack equippedItem = player.getCurrentEquippedItem();
//
//        //Do some color application thingy
//
//        return false;
//    }

    @Override
    public int GetFacing( int iMetadata )
    {
        return ( iMetadata & 7 );
    }

    @Override
    public int SetFacing( int iMetadata, int iFacing )
    {
        iMetadata &= ~7; // filter out old facing

        iMetadata |= iFacing;

        return iMetadata;
    }

    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
        return world.doesBlockHaveSolidTopSurface( i, j - 1, k );
    }

    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
        return -1F;
    }

    private int FindValidFacing( World world, int i, int j, int k )
    {
        for ( int iFacing = 0; iFacing < 6; iFacing++ )
        {
            FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );

            if ( FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, targetPos.i, targetPos.j, targetPos.k,
                    Block.GetOppositeFacing( iFacing ) ) )
            {
                return Block.GetOppositeFacing( iFacing );
            }
        }

        return 0;
    }

    //----------- Client Side Functionality -----------//

    private Icon m_IconTop;
    private Icon m_IconSide;

    @Override
    public void registerIcons( IconRegister register )
    {
        Icon sideIcon = register.registerIcon( "smmBlockMapMarker_side" );

        blockIcon = sideIcon;

        m_IconTop = register.registerIcon( "smmBlockMapMarker_top" );
        m_IconSide = sideIcon;
    }

    @Override
    public Icon getBlockTexture( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
        int iFacing = GetFacing( blockAccess, i, j, k);

        if ( iSide == iFacing || iSide == Block.GetOppositeFacing( iFacing ) )
        {
           return m_IconTop;
        }
        else
        {
           return m_IconSide;
        }
    }

    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
        return true;
    }

    @Override
    public int idPicked( World world, int i, int j, int k )
    {
        return idDropped( world.getBlockMetadata( i, j, k ), world.rand, 0 );
    }

    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
        IBlockAccess blockAccess = renderer.blockAccess;

        int iFacing = GetFacing( blockAccess, i, j, k );

        if ( iFacing == 0 )
        {
            renderer.SetUvRotateSouth( 3 );
            renderer.SetUvRotateNorth( 3 );
            renderer.SetUvRotateEast( 3 );
            renderer.SetUvRotateWest( 3 );
        }
        else if ( iFacing == 2 )
        {
            renderer.SetUvRotateSouth( 1 );
            renderer.SetUvRotateNorth( 2 );
        }
        else if ( iFacing == 3 )
        {
            renderer.SetUvRotateSouth( 2 );
            renderer.SetUvRotateNorth( 1 );
            renderer.SetUvRotateTop( 3 );
            renderer.SetUvRotateBottom( 3 );
        }
        else if ( iFacing == 4 )
        {
            renderer.SetUvRotateEast( 1 );
            renderer.SetUvRotateWest( 2 );
            renderer.SetUvRotateTop( 2 );
            renderer.SetUvRotateBottom( 1 );
        }
        else if (  iFacing == 5 )
        {
            renderer.SetUvRotateEast( 2 );
            renderer.SetUvRotateWest( 1 );
            renderer.SetUvRotateTop( 1 );
            renderer.SetUvRotateBottom( 2 );
        }

        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState(
                renderer.blockAccess, i, j, k ) );

        renderer.renderStandardBlock( this, i, j, k );

        renderer.ClearUvRotation();

        return true;
    }
}