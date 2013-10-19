package modernwarfare.common;

import java.util.Random;

import modernwarfare.client.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGrapplingHook extends BlockWar
{
    public BlockGrapplingHook(int i, int k)
    {
        super(i, Material.wood);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		blockIcon = register.registerIcon("modernwarfare:blockGrapplingHook");
	}

	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
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
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        int l = world.getBlockId(i, j - 1, k);

        if(l == 0 || !Block.blocksList[l].isOpaqueCube())
        {
            return false;
        }
        else {
            return world.getBlockMaterial(i, j - 1, k).isSolid();
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        canSnowStay(world, i, j, k);
    }

    private boolean canSnowStay(World world, int i, int j, int k)
    {
        if(!canPlaceBlockAt(world, i, j, k))
        {
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockToAir(i, j, k);
            onBlockDestroyed(world, i, j, k);
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public int idDropped(int i, Random random, int j)
    {
        return ModernWarfare.itemGrapplingHook.itemID;
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType()
    {
        return ClientProxy.RENDER_ID;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        Material material = iblockaccess.getBlockMaterial(i, j, k);

        if (l == 1)
        {
            return true;
        }

        if(material == blockMaterial)
        {
            return false;
        }
        else {
            return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
    {
        onBlockDestroyed(world, i, j, k);
    }

    @Override
    public void onBlockDestroyedByExplosion(World world, int i, int j, int k, Explosion explosion)
    {
        onBlockDestroyed(world, i, j, k);
    }

    private void onBlockDestroyed(World world, int i, int j, int k)
    {
        int ai[][] =
        {
            {i - 1, j - 1, k}, 
            {i + 1, j - 1, k}, 
            {i, j - 1, k - 1}, 
            {i, j - 1, k + 1}
        };

        for (int l = 0; l < ai.length; l++)
        {
            if (world.getBlockId(ai[l][0], ai[l][1], ai[l][2]) != ModernWarfare.blockRope.blockID)
            {
                continue;
            }

            for (int i1 = ai[l][1]; world.getBlockId(ai[l][0], i1, ai[l][2]) == ModernWarfare.blockRope.blockID; i1--)
            {
                world.setBlockToAir(ai[l][0], i1, ai[l][2]);
            }
        }
    }
}
