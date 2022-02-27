package net.minecraft.src;

import java.util.Random;

import static net.minecraft.src.SMMDefinitions.mapMarkerItem;

public class SMMBlockMapMarker extends Block implements ITileEntityProvider {

    public SMMBlockMapMarker(int blockId) {
        super(blockId, Material.wood);

        setHardness( 0F );
        setResistance( 0F );
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
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);
        SMMTileEntityMapMarker tileEntity = (SMMTileEntityMapMarker) createNewTileEntity(par1World);
        par1World.setBlockTileEntity(par2,par3,par4,tileEntity);
        tileEntity.SetMarkerId("SMM-Marker-" + par2 + '.' + par4);
        par1World.setBlockMetadataWithNotify(par2, par3, par4, tileEntity.GetIconFileIndex());
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        super.breakBlock(world, x, y, z, par5, par6);
        SMMTileEntityMapMarker tile = getBlockTileEntity(world, x, y, z);
        if (tile == null) return;
        SMMDefinitions.WorldMapMarkers.remove(tile.GetMarkerId());
        world.removeBlockTileEntity(x, y, z);
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

        FCUtilsBlockPos anchorPos = new FCUtilsBlockPos( i, j, k, iFacing );

        if ( !FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, anchorPos.i, anchorPos.j, anchorPos.k, iFacing ) )
        {
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            world.setBlockWithNotify( i, j, k, 0 );
        }
    }

    @Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
        SMMTileEntityMapMarker tile = getBlockTileEntity(world, i, j, k);
        if (tile != null) {
            tile.SetIconIndex(tile.GetIconIndex() + 1);
            world.setBlockMetadataWithNotify(i, j, k, tile.GetIconFileIndex());
            return true;
        }
        return false;
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

    //----------- Client Side Functionality -----------//

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
    public Icon getBlockTexture( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
        int iFacing = GetFacing( blockAccess, i, j, k);

        if ( iSide == iFacing || iSide == Block.GetOppositeFacing( iFacing ) )
        {
            return iconFlags[blockAccess.getBlockMetadata(i, j, k)];
        }
        else
        {
           return iconStake;
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
    protected AxisAlignedBB GetFixedBlockBoundsFromPool() {
        double m_dBlockHeight = 1D;
        double m_dBlockWidth = (1D / 16D);
        double m_dBlockHalfWidth = (m_dBlockWidth / 2D);

        return AxisAlignedBB.getAABBPool().getAABB(
                0.5D - m_dBlockHalfWidth, 0D, 0.5D - m_dBlockHalfWidth,
                0.5D + m_dBlockHalfWidth, m_dBlockHeight, 0.5D + m_dBlockHalfWidth);
    }

    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
        AxisAlignedBB shaft = GetFixedBlockBoundsFromPool();
        AxisAlignedBB flag = new AxisAlignedBB(shaft.minX, shaft.maxY - 0.25D, shaft.minZ - (6D / 16D), shaft.maxX, shaft.maxY, shaft.minZ);

        //shaft
        renderer.setRenderBounds(shaft);
        renderer.renderStandardBlock( this, i, j, k);

        //flag
        renderer.unlockBlockBounds();
        renderer.setRenderBounds(flag);
        renderer.setOverrideBlockTexture(iconFlags[renderer.blockAccess.getBlockMetadata(i, j, k)]);
        renderer.renderStandardBlock(this, i, j, k);
        renderer.clearOverrideBlockTexture();

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world) { return new SMMTileEntityMapMarker(); }

    private SMMTileEntityMapMarker getBlockTileEntity(IBlockAccess blockAccess, int i, int j, int k) {
        return (SMMTileEntityMapMarker) blockAccess.getBlockTileEntity(i, j, k);
    }
}