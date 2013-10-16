package modernwarfare.common;

import java.lang.reflect.Method;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityGuardians extends EntityCreature
{
    protected int attackStrength;
    protected float range;

    public EntityGuardians(World world)
    {
        super(world);
        range = 32F;
        attackStrength = 2;
        setHealth(20);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(17, "");
        dataWatcher.addObject(18, new Float(getHealth()));
    }

    public String getOwner()
    {
        return dataWatcher.getWatchableObjectString(17);
    }

    public void setOwner(String s)
    {
        dataWatcher.updateObject(17, s);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        if (super.attackEntityFrom(damagesource, i))
        {
            if (riddenByEntity == damagesource.getEntity() || ridingEntity == damagesource.getEntity())
            {
                return true;
            }

            if (damagesource.getEntity() != this)
            {
                entityToAttack = damagesource.getEntity();
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        return getNearestEntityLivingInRange(this, range);
    }

    public EntityLiving getNearestEntityLivingInRange(Entity entity, double d)
    {
        return getNearestEntityLivingInRange(entity.posX, entity.posY, entity.posZ, d);
    }

    public EntityLiving getNearestEntityLivingInRange(double d, double d1, double d2, double d3)
    {
        double d4 = -1D;
        EntityLiving entityliving = null;

        for (int i = 0; i < worldObj.loadedEntityList.size(); i++)
        {
            Entity entity = (Entity)worldObj.loadedEntityList.get(i);

            if (!(entity instanceof EntityLiving) || !ModernWarfare.sentriesKillAnimals && (!(entity instanceof IMob) || (entity instanceof EntityPigZombie)) || (entity instanceof EntityPlayer) || (entity instanceof EntityWolf) && ((EntityWolf)entity).isTamed() || !entity.isEntityAlive())
            {
                continue;
            }

            double d5 = entity.getDistanceSq(d, d1, d2);

            if ((d3 < 0.0D || d5 < d3 * d3) && (d4 == -1D || d5 < d4) && canEntityBeSeen(entity) && okToAttack(entity))
            {
                d4 = d5;
                entityliving = (EntityLiving)entity;
            }
        }

        return entityliving;
    }

    protected boolean okToAttack(Entity entity)
    {
        return !(entity instanceof EntityGuardians) && (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).username.equals(getOwner())) && (!(entity instanceof EntityWolf) || !((EntityWolf)entity).isTamed() || !((EntityWolf)entity).getOwner().equals(getOwner()));
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    public float getBlockPathWeight(int i, int j, int k)
    {
        return 1.0F;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setString("Owner", getOwner() == null ? "" : getOwner());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        setOwner(nbttagcompound.getString("Owner"));
    }

    protected void updateEntityActionState()
    {
        super.updateEntityActionState();

        if (!worldObj.isRemote)
        {
            dataWatcher.updateObject(18, Float.valueOf(getHealth()));
        }
    }
}
