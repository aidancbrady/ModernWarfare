package modernwarfare.common;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

public abstract class EntityLandVehicle extends Entity
{
    private double lastTurnSpeed;
    public boolean lastOnGround;
    public int health;
    public double prevMotionX;
    public double prevMotionY;
    public double prevMotionZ;
    public Entity lastCollidedEntity;
    public double ACCEL_FORWARD_STOPPED;
    public double ACCEL_FORWARD_FULL;
    public double ACCEL_BACKWARD_STOPPED;
    public double ACCEL_BACKWARD_FULL;
    public double ACCEL_BRAKE;
    public double TURN_SPEED_STOPPED;
    public double TURN_SPEED_FULL;
    public double MAX_SPEED;
    public double FALL_SPEED;
    public double ROTATION_PITCH_DELTA_MAX;
    public double SPEED_MULT_WATER;
    public double SPEED_MULT_UNMOUNTED;
    public double SPEED_MULT_DECEL;
    public double STOP_SPEED;
    public double TURN_SPEED_RENDER_MULT;
    public double COLLISION_SPEED_MIN;
    public int COLLISION_DAMAGE_ENTITY;
    public int COLLISION_DAMAGE_SELF;
    public int MAX_HEALTH;
    public boolean COLLISION_DAMAGE;
    public boolean COLLISION_FLIGHT_PLAYER;
    public boolean COLLISION_FLIGHT_ENTITY;

    public EntityLandVehicle(World world)
    {
        super(world);
        lastTurnSpeed = 0.0D;
        lastOnGround = true;
        prevMotionX = 0.0D;
        prevMotionY = 0.0D;
        prevMotionZ = 0.0D;
        lastCollidedEntity = null;
        ACCEL_FORWARD_STOPPED = 0.02D;
        ACCEL_FORWARD_FULL = 0.005D;
        ACCEL_BACKWARD_STOPPED = 0.01D;
        ACCEL_BACKWARD_FULL = 0.0025D;
        ACCEL_BRAKE = 0.04D;
        TURN_SPEED_STOPPED = 10D;
        TURN_SPEED_FULL = 2D;
        MAX_SPEED = 0.75D;
        FALL_SPEED = 0.06D;
        ROTATION_PITCH_DELTA_MAX = 10D;
        SPEED_MULT_WATER = 0.9D;
        SPEED_MULT_UNMOUNTED = 0.95D;
        SPEED_MULT_DECEL = 0.95D;
        STOP_SPEED = 0.01D;
        TURN_SPEED_RENDER_MULT = 2D;
        COLLISION_SPEED_MIN = 0.5D;
        COLLISION_DAMAGE_ENTITY = 10;
        COLLISION_DAMAGE_SELF = 10;
        MAX_HEALTH = 100;
        COLLISION_DAMAGE = true;
        COLLISION_FLIGHT_PLAYER = true;
        COLLISION_FLIGHT_ENTITY = true;
        preventEntitySpawning = true;
        yOffset = height / 2.0F;
        health = MAX_HEALTH;
    }

    public EntityLandVehicle(World world, double x, double y, double z)
    {
        this(world);
        
        setPosition(x, y + yOffset, z);
        
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        
        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;
    }

    @Override
    protected void entityInit() {}

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity)
    {
        return entity.boundingBox;
    }

    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float f)
    {
        if(MAX_HEALTH != -1)
        {
            onHurt();
            health -= f;

            if(health <= 0)
            {
                onDeath();
            }
        }

        return true;
    }

    public void onHurt() {}

    public void onDeath()
    {
        setDead();
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

    @Override
    public float getShadowSize()
    {
        return 0.0F;
    }

    @Override
    public boolean interactFirst(EntityPlayer entityplayer)
    {
        if(riddenByEntity != null && (riddenByEntity instanceof EntityPlayer) && riddenByEntity != entityplayer)
        {
            return true;
        }

        if(!worldObj.isRemote)
        {
            entityplayer.mountEntity(this);
        }

        return true;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if(getSpeed() > 0.0D)
        {
            double motionYaw = getMotionYaw();
            double yawDiff = rotationYaw - motionYaw;
            projectMotion(yawDiff);
        }

        boolean flag = false;
        boolean flag1 = true;

        if(getSpeed() != 0.0D)
        {
            double d2 = (rotationYaw * Math.PI) / 180D;
            double d6 = Math.cos(d2);
            flag1 = -d6 > 0.0D && motionX > 0.0D || -d6 < 0.0D && motionX < 0.0D;
        }

        if(onGround)
        {
            if(riddenByEntity != null)
            {
                Minecraft minecraft = Minecraft.getMinecraft();

                if(getSpeed() != 0.0D)
                {
                    double newMotion = 0.0D;

                    if(minecraft.currentScreen == null && Keyboard.isKeyDown(minecraft.gameSettings.keyBindLeft.keyCode))
                    {
                        newMotion = -getTurnSpeed() * (flag1 ? 1 : -1);
                    }
                    else if(minecraft.currentScreen == null && Keyboard.isKeyDown(minecraft.gameSettings.keyBindRight.keyCode))
                    {
                        newMotion = getTurnSpeed() * (flag1 ? 1 : -1);
                    }

                    if(newMotion != 0.0D)
                    {
                        rotationYaw += newMotion;
                        projectMotion(newMotion);
                    }

                    lastTurnSpeed = newMotion * (flag1 ? 1 : -1);
                }

                double d5 = 0.0D;

                if(riddenByEntity != null)
                {
                    if(minecraft.currentScreen == null && Keyboard.isKeyDown(minecraft.gameSettings.keyBindForward.keyCode))
                    {
                        d5 = -(flag1 ? getAccelForward() : ACCEL_BRAKE);
                        flag = true;
                    }
                    else if(minecraft.currentScreen == null && Keyboard.isKeyDown(minecraft.gameSettings.keyBindBack.keyCode))
                    {
                        d5 = flag1 ? ACCEL_BRAKE : getAccelBackward();
                        flag = true;
                    }
                }

                if(d5 != 0.0D)
                {
                    double d7 = (rotationYaw * Math.PI) / 180D;
                    double d8 = Math.cos(d7);
                    double d9 = Math.sin(d7);
                    
                    motionX += d5 * d8;
                    motionZ += d5 * d9;
                }
            }

            if(!flag)
            {
                multiplySpeed(SPEED_MULT_DECEL);
            }

            if(riddenByEntity == null)
            {
                multiplySpeed(SPEED_MULT_UNMOUNTED);
            }

            double d3 = getSpeed();

            if(d3 > MAX_SPEED)
            {
                multiplySpeed(MAX_SPEED / d3);
            }
        }

        if(handleWaterMovement())
        {
            multiplySpeed(SPEED_MULT_WATER);
        }

        if(!flag && getSpeed() < STOP_SPEED)
        {
            multiplySpeed(0.0D);
        }

        moveEntity(motionX, motionY, motionZ);
        int i = flag1 ? 1 : -1;

        if(onGround && lastOnGround)
        {
            if(prevPosY - posY > 0.01D)
            {
                rotationPitch = 45 * i;
            }
            else if(prevPosY - posY < -0.01D)
            {
                rotationPitch = -45 * i;
            }
            else {
                rotationPitch = 0.0F;
            }

            motionY -= 0.001D;
        }
        else {
            setRotationPitch(Math.max(Math.min((float)((-90D*motionY)/getSpeed())*(float)i, 90F), -90F) / 2.0F);
            motionY = posY - prevPosY - FALL_SPEED;
        }

        lastOnGround = onGround;
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.2D, 0.0D, 0.2D));

        if(list != null && list.size() > 0)
        {
            for(int j = 0; j < list.size(); j++)
            {
                Entity entity = (Entity)list.get(j);

                if(entity != riddenByEntity && entity.canBePushed())
                {
                    handleCollision(entity);
                }
            }
        }

        if(riddenByEntity != null && getPrevSpeed() - getSpeed() > COLLISION_SPEED_MIN)
        {
            if(lastCollidedEntity != null)
            {
                if(COLLISION_FLIGHT_ENTITY)
                {
                    lastCollidedEntity.addVelocity(prevMotionX, prevMotionY + 1.0D, prevMotionZ);
                }

                if(COLLISION_DAMAGE)
                {
                    lastCollidedEntity.attackEntityFrom(DamageSource.causeThrownDamage(this, riddenByEntity), COLLISION_DAMAGE_ENTITY);
                }
            }

            if(COLLISION_DAMAGE)
            {
                attackEntityFrom(DamageSource.causeThrownDamage(this, lastCollidedEntity), COLLISION_DAMAGE_SELF);
            }

            if(COLLISION_FLIGHT_PLAYER)
            {
                riddenByEntity.addVelocity(prevMotionX, prevMotionY + 1.0D, prevMotionZ);
                riddenByEntity.mountEntity(null);
            }
        }

        lastCollidedEntity = null;
        
        prevMotionX = motionX;
        prevMotionY = motionY;
        prevMotionZ = motionZ;

        if(riddenByEntity != null && riddenByEntity.isDead)
        {
            riddenByEntity = null;
        }
    }

    public double getMotionYaw()
    {
        double d;

        if(motionX >= 0.0D && motionZ >= 0.0D)
        {
            d = Math.atan(Math.abs(motionZ / motionX)) * (180D / Math.PI) + 180D;
        }
        else if(motionX >= 0.0D && motionZ <= 0.0D)
        {
            d = Math.atan(Math.abs(motionX / motionZ)) * (180D / Math.PI) + 90D;
        }
        else if(motionX <= 0.0D && motionZ >= 0.0D)
        {
            d = Math.atan(Math.abs(motionX / motionZ)) * (180D / Math.PI) + 270D;
        }
        else {
            d = Math.atan(Math.abs(motionZ / motionX)) * (180D / Math.PI);
        }

        return d;
    }

    public void projectMotion(double d)
    {
        double d1 = (d * Math.PI) / 180D;
        double d2 = Math.cos(d1) * motionX - Math.sin(d1) * motionZ;
        double d3 = Math.sin(d1) * motionX + Math.cos(d1) * motionZ;
        double d4 = getSpeed();
        double d5 = d4 * Math.cos(d1);
        d2 *= d5 / d4;
        d3 *= d5 / d4;
        motionX = d2;
        motionZ = d3;
    }

    public double getSpeed()
    {
        return Math.sqrt(motionX * motionX + motionZ * motionZ);
    }

    public void multiplySpeed(double d)
    {
        motionX *= d;
        motionZ *= d;
    }

    public double getTurnSpeed()
    {
        return scaleOnSpeed(TURN_SPEED_STOPPED, TURN_SPEED_FULL);
    }

    public double getAccelForward()
    {
        return scaleOnSpeed(ACCEL_FORWARD_STOPPED, ACCEL_FORWARD_FULL);
    }

    public double getAccelBackward()
    {
        return scaleOnSpeed(ACCEL_BACKWARD_STOPPED, ACCEL_BACKWARD_FULL);
    }

    public double scaleOnSpeed(double d, double d1)
    {
        return d - (d - d1)*(getSpeed()/MAX_SPEED);
    }

    public void handleCollision(Entity entity)
    {
        entity.applyEntityCollision(this);

        if(entity.riddenByEntity != this && entity.ridingEntity != this)
        {
            lastCollidedEntity = entity;
        }
    }

    public void setRotationPitch(float f)
    {
        if((f - rotationPitch) > ROTATION_PITCH_DELTA_MAX)
        {
            rotationPitch += ROTATION_PITCH_DELTA_MAX;
        }
        else if((rotationPitch - f) > ROTATION_PITCH_DELTA_MAX)
        {
            rotationPitch -= ROTATION_PITCH_DELTA_MAX;
        }
        else {
            rotationPitch = f;
        }
    }

    public double getPrevSpeed()
    {
        return Math.sqrt((prevMotionX*prevMotionX) + (prevMotionZ*prevMotionZ));
    }

    public float getTurnSpeedForRender()
    {
        return (float)(lastTurnSpeed*TURN_SPEED_RENDER_MULT);
    }
}
