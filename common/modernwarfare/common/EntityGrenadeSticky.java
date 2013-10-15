package modernwarfare.common;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityGrenadeSticky extends EntityGrenade
{
    protected boolean stuckToBlock;
    protected Entity stuckToEntity;
    protected Point3d stuckToEntityOffset;

    public EntityGrenadeSticky(World world)
    {
        super(world);
        stuckToBlock = false;
        stuckToEntity = null;
        stuckToEntityOffset = null;
        setEntityItemStack(new ItemStack(ModernWarfare.itemGrenadeSticky, 1, 0));
    }

    public EntityGrenadeSticky(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        stuckToBlock = false;
        stuckToEntity = null;
        stuckToEntityOffset = null;
        setEntityItemStack(new ItemStack(ModernWarfare.itemGrenadeSticky, 1, 0));
    }

    public EntityGrenadeSticky(World world, EntityLivingBase entityliving)
    {
        super(world, entityliving);
        stuckToBlock = false;
        stuckToEntity = null;
        stuckToEntityOffset = null;
        setEntityItemStack(new ItemStack(ModernWarfare.itemGrenadeSticky, 1, 0));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (stuckToEntity != null && stuckToEntity.isDead)
        {
            stuckToEntity = null;
        }

        if (!stuckToBlock && stuckToEntity == null)
        {
            super.onUpdate();
        }
        else
        {
            handleExplode();
        }

        if (stuckToEntity == null)
        {
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox);

            if (list.size() > 0)
            {
                Entity entity = (Entity)list.get(0);

                if (entity instanceof EntityLiving)
                {
                    stuckToBlock = false;
                    stuckToEntity = entity;
                    stuckToEntityOffset = new Point3d(Double.valueOf(posX - entity.posX), Double.valueOf(posY - entity.posY), Double.valueOf(posZ - entity.posZ));
                }
            }
        }
        else
        {
            prevPosX = posX;
            prevPosY = posY;
            prevPosZ = posZ;
            posX = stuckToEntity.posX + ((Double)stuckToEntityOffset.x).doubleValue();
            posY = stuckToEntity.posY + ((Double)stuckToEntityOffset.y).doubleValue();
            posZ = stuckToEntity.posZ + ((Double)stuckToEntityOffset.z).doubleValue();
        }
    }

    protected void handleBounce()
    {
        if (stuckToEntity == null)
        {
            stuckToBlock = true;
            motionX = motionY = motionZ = 0.0D;
        }
    }
}
