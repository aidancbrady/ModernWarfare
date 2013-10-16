package modernwarfare.common;

import java.util.Random;

import modernwarfare.client.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        int l = world.getBlockId(i, j - 1, k);

        if (l == 0 || !Block.blocksList[l].isOpaqueCube())
        {
            return false;
        }
        else
        {
            return world.getBlockMaterial(i, j - 1, k).isSolid();
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        canSnowStay(world, i, j, k);
    }

    private boolean canSnowStay(World world, int i, int j, int k)
    {
        if (!canPlaceBlockAt(world, i, j, k))
        {
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockToAir(i, j, k);
            onBlockDestroyed(world, i, j, k);
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int i, Random random, int j)
    {
        return ModernWarfare.itemGrapplingHook.itemID;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 1;
    }

    /**
     * The type of render function that is called for this block
     */
    @SideOnly(Side.CLIENT)
    public int getRenderType()
    {
        return ClientProxy.RENDER_ID;
    }

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: blockAccess, x, y, z, side
     */
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        Material material = iblockaccess.getBlockMaterial(i, j, k);

        if (l == 1)
        {
            return true;
        }

        if (material == blockMaterial)
        {
            return false;
        }
        else
        {
            return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
        }
    }

    /**
     * Called right before the block is destroyed by a player.  Args: world, x, y, z, metaData
     */
    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
    {
        onBlockDestroyed(world, i, j, k);
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    public void onBlockDestroyedByExplosion(World world, int i, int j, int k, Explosion explosion)
    {
        onBlockDestroyed(world, i, j, k);
    }

    private void onBlockDestroyed(World world, int i, int j, int k)
    {
        int ai[][] =
        {
            {
                i - 1, j - 1, k
            }, {
                i + 1, j - 1, k
            }, {
                i, j - 1, k - 1
            }, {
                i, j - 1, k + 1
            }
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
