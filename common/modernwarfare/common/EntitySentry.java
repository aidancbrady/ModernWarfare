package modernwarfare.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntitySentry extends EntityGuardians implements IMob
{
    boolean restricted;

    /** How high this entity is considered to be */
    float height;
    private static final float MAX_TURN_SPEED = 10F;
    private static final int MAX_HEALTH = 20;
    protected ItemGun gun;
    protected ItemStack itemStack;
    protected int ATTACK_DELAY;
    private Map angerMap;

    public EntitySentry(World world)
    {
        super(world);
        height = 1.5F;
        angerMap = new HashMap();
    }

    public EntitySentry(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1, d2);
    }

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        if ((damagesource.getEntity() instanceof EntityLiving) && okToAttack(damagesource.getEntity()))
        {
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(32D, 32D, 32D));

            for (int j = 0; j < list.size(); j++)
            {
                Entity entity = (Entity)list.get(j);

                if (!(entity instanceof EntitySentry))
                {
                    continue;
                }

                EntitySentry entitysentry = (EntitySentry)entity;

                if (entitysentry.getOwner() != null && !entitysentry.getOwner().equals("") && entitysentry.getOwner().equals(getOwner()))
                {
                    entitysentry.becomeAngryAt(damagesource.getEntity());
                }
            }

            becomeAngryAt(damagesource.getEntity());
        }

        return super.attackEntityFrom(damagesource, i);
    }

    private void becomeAngryAt(Entity entity)
    {
        if(angerMap.containsKey(entity))
        {
            angerMap.remove(entity);
        }

        angerMap.put(entity, Integer.valueOf(400 + rand.nextInt(400)));
    }

    @Override
    public void onUpdate()
    {
        onEntityUpdate();
        onLivingUpdate();
    }

    @Override
    protected void attackEntity(Entity entity, float f)
    {
        if(okToAttack(entity))
        {
            if(attackTime == 0 && !worldObj.isRemote && worldObj != null && gun != null && worldObj.rand != null)
            {
                if(itemStack == null)
                {
                    itemStack = new ItemStack(gun);
                }

                if(ItemGun.canFire(itemStack))
                {
                	ItemGun.addDelay(itemStack);
                	gun.fireBullet(worldObj, this, itemStack, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
                	attackTime = ATTACK_DELAY;
                }
            }

            hasAttacked = true;
        }
    }

    @Override
    protected void updateEntityActionState()
    {
        if(!isDead)
        {
            if(worldObj.getWorldTime() % 20L == 0L)
            {
                entityToAttack = findPlayerToAttack();
            }

            if(entityToAttack != null && canEntityBeSeen(entityToAttack))
            {
                restricted = false;
                faceEntity(entityToAttack, 10F, 10F);

                if(!restricted)
                {
                    attackEntity(entityToAttack, range);
                }
            }
            else {
                rotationYaw++;
                rotationPitch = 0.0F;
            }
        }

        for(Iterator iterator = angerMap.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            int i = ((Integer)entry.getValue()).intValue() - 1;

            if(i <= 0)
            {
                iterator.remove();
            }
            else {
                entry.setValue(Integer.valueOf(i));
            }
        }
    }

    @Override
    protected Entity findPlayerToAttack()
    {
        EntityLiving entityliving = getNearestAnger(this);

        if(entityliving != null)
        {
            return entityliving;
        }
        else {
            return super.findPlayerToAttack();
        }
    }

    public EntityLiving getNearestAnger(Entity entity)
    {
        return getNearestAnger(entity.posX, entity.posY, entity.posZ);
    }

    public EntityLiving getNearestAnger(double d, double d1, double d2)
    {
        double d3 = -1D;
        EntityLiving entityliving = null;
        Iterator iterator = angerMap.entrySet().iterator();

        do {
            if(!iterator.hasNext())
            {
                break;
            }

            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            Entity entity = (Entity)entry.getKey();

            if((entity instanceof EntityLiving) && entity.isEntityAlive())
            {
                double d4 = entity.getDistanceSq(d, d1, d2);

                if(d3 == -1D || d4 < d3 && canEntityBeSeen(entity) && okToAttack(entity))
                {
                    d3 = d4;
                    entityliving = (EntityLiving)entity;
                }
            }
        }
        while(true);

        return entityliving;
    }

    @Override
    public void faceEntity(Entity entity, float f, float f1)
    {
        if(!okToAttack(entity))
        {
            return;
        }
        else {
            double xDiff = entity.posX - posX;
            double zDiff = entity.posZ - posZ;
            double d2 = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2D - (posY + getEyeHeight());
            double d3 = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
            float f2 = (float)((Math.atan2(zDiff, xDiff) * 180D) / Math.PI) - 90F;
            float f3 = (float)((Math.atan2(d2, d3) * 180D) / Math.PI);
            rotationPitch = -updateRotation(-rotationPitch, f3, f1);
            rotationYaw = updateRotation(rotationYaw, f2, f);
            return;
        }
    }

    private float updateRotation(float f, float f1, float f2)
    {
        float f3;

        for (f3 = f1 - f; f3 < -180F; f3 += 360F) { }

        for (; f3 >= 180F; f3 -= 360F) { }

        if (f3 > f2)
        {
            restricted = true;
            f3 = f2;
        }

        if (f3 < -f2)
        {
            restricted = true;
            f3 = -f2;
        }

        return f + f3;
    }

    @Override
    protected int getDropItemId()
    {
        return gun.requiredBullet.itemID;
    }

    @Override
    protected String getHurtSound()
    {
        return "modernwarfare:mechhurt";
    }

    @Override
    protected String getDeathSound()
    {
        return null;
    }

    @Override
    public void knockBack(Entity entity, float i, double d, double d1) {}

    public void setEntityDead()
    {
        super.setDead();
        entityToAttack = null;
        gun = null;
    }

    @Override
    public boolean interact(EntityPlayer entityplayer)
    {
        if(entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == ModernWarfare.itemWrench.itemID)
        {
            if(getHealth() > 0 && getHealth() < 20)
            {
                worldObj.playSoundAtEntity(this, "modernwarfare:wrench", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                setHealth(Math.min(getHealth() + 2, 20));
                entityplayer.swingItem();
                entityplayer.getCurrentEquippedItem().damageItem(1, entityplayer);

                if(entityplayer.getCurrentEquippedItem().getItemDamage() <= 0)
                {
                    entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = null;
                }
            }

            return true;
        }
        else {
            return false;
        }
    }
    
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(20);
    }
}
