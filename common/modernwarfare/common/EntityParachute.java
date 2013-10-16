package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityParachute extends EntityLiving
{
    Entity entityWearing;
    boolean justServerSpawned;
    private static final int MAX_HEALTH = 4;
    private static final float HEIGHT = 0F;
    private static final float MAX_FALL_SPEED = 0.25F;

    public EntityParachute(World world)
    {
        super(world);
        justServerSpawned = false;
        yOffset = 0.0F;
        setHealth(4);
    }

    public EntityParachute(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1, d2);
        justServerSpawned = true;
    }

    public EntityParachute(World world, Entity entity)
    {
        this(world);
        setAngles(entity.rotationYaw, 0.0F);
        entityWearing = entity;
        yOffset = 0.0F;
        setHealth(4);
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        setMotionAndPosition();
    }

    @Override
    public boolean isInRangeToRenderDist(double d)
    {
        return true;
    }

    @Override
    public void onUpdate()
    {
        if (entityWearing == null)
        {
            if (worldObj.isRemote && !justServerSpawned)
            {
                isDead = true;
                return;
            }

            entityWearing = getNearestPlayer();
            justServerSpawned = false;

            if (entityWearing == null)
            {
                return;
            }
        }

        if (WarTools.onGroundOrInWater(worldObj, entityWearing))
        {
            isDead = true;
            return;
        }

        if (entityWearing.motionY < -0.25D)
        {
            entityWearing.motionY = -0.25D;
        }

        entityWearing.fallDistance = 0.0F;
        setMotionAndPosition();
    }

    public EntityPlayer getNearestPlayer()
    {
        return getNearestPlayer(posX, posY, posZ);
    }

    public EntityPlayer getNearestPlayer(double d, double d1, double d2)
    {
        double d3 = -1D;
        EntityPlayer entityplayer = null;

        for (int i = 0; i < worldObj.loadedEntityList.size(); i++)
        {
            Entity entity = (Entity)worldObj.loadedEntityList.get(i);

            if (!(entity instanceof EntityPlayer) || !entity.isEntityAlive())
            {
                continue;
            }

            double d4 = entity.getDistanceSq(d, d1, d2);

            if (d3 == -1D || d4 < d3)
            {
                d3 = d4;
                entityplayer = (EntityPlayer)entity;
            }
        }

        return entityplayer;
    }

    private void setMotionAndPosition()
    {
        setPosition(entityWearing.posX, entityWearing.posY + (double)(entityWearing.height / 2.0F) + 0.0D, entityWearing.posZ);
        motionX = entityWearing.motionX;
        motionY = entityWearing.motionY;
        motionZ = entityWearing.motionZ;
        rotationYaw = entityWearing.rotationYaw;
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityplayer) {}
    
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(4);
    }
}
