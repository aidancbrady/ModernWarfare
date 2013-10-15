package modernwarfare.common;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityGrenadeIncendiary extends EntityGrenade
{
    private static final int RADIUS = 2;

    public EntityGrenadeIncendiary(World world)
    {
        super(world);
        setEntityItemStack(new ItemStack(ModernWarfare.itemGrenadeIncendiaryLit, 1, 0));
    }

    public EntityGrenadeIncendiary(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setEntityItemStack(new ItemStack(ModernWarfare.itemGrenadeIncendiaryLit, 1, 0));
    }

    public EntityGrenadeIncendiary(World world, EntityLivingBase entityliving)
    {
        super(world, entityliving);
        setEntityItemStack(new ItemStack(ModernWarfare.itemGrenadeIncendiaryLit, 1, 0));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox);

        if (list.size() > 0)
        {
            Entity entity = (Entity)list.get(0);

            if (entity instanceof EntityLiving)
            {
                entity.setFire(300);
                explode();
            }
        }
    }

    protected void handleBounce()
    {
        explode();
    }

    protected void explode()
    {
        if (!exploded)
        {
            exploded = true;
            worldObj.playSoundAtEntity(this, Block.glass.stepSound.getBreakSound(), (Block.glass.stepSound.getVolume() + 1.0F) / 2.0F, Block.glass.stepSound.getPitch() * 0.8F);
            int i = (int)Math.floor(posX);
            int j = (int)Math.floor(posY);
            int k = (int)Math.floor(posZ);

            for (int l = -2; l <= 2; l++)
            {
                for (int i1 = -2; i1 <= 2; i1++)
                {
                    for (int j1 = -2; j1 <= 2; j1++)
                    {
                        int k1 = Math.abs(l) + Math.abs(i1) + Math.abs(j1);

                        if (k1 <= 2 && worldObj.isAirBlock(i + l, j + i1, k + j1))
                        {
                            worldObj.setBlock(i + l, j + i1, k + j1, Block.fire.blockID);
                        }
                    }
                }
            }

            setDead();
        }
    }
}
