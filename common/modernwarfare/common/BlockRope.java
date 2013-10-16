package modernwarfare.common;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import modernwarfare.client.ClientProxy;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockRope extends BlockContainerWar
{
    public float ascensionSpeed;
    public float descensionSpeed;

    public BlockRope(int i, int k)
    {
        super(i, Material.cloth);
        ascensionSpeed = 0.2F;
        descensionSpeed = -0.15F;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        if (entity instanceof EntityLiving)
        {
            entity.fallDistance = 0.0F;

            if (entity.motionY < (double)descensionSpeed)
            {
                entity.motionY = descensionSpeed;
            }

            if (entity.isCollidedHorizontally)
            {
                entity.motionY = ascensionSpeed;
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityRope();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
        float f = 0.125F;

        if (l == 2)
        {
            setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        }

        if (l == 3)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        }

        if (l == 4)
        {
            setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if (l == 5)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        }

        return super.getCollisionBoundingBoxFromPool(world, i, j, k);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
        float f = 0.125F;

        if (l == 2)
        {
            setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        }

        if (l == 3)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        }

        if (l == 4)
        {
            setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if (l == 5)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        }

        return super.getSelectedBoundingBoxFromPool(world, i, j, k);
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
    @SideOnly(Side.CLIENT)
    public int getRenderType()
    {
        return ClientProxy.RENDER_ID;
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }
}
