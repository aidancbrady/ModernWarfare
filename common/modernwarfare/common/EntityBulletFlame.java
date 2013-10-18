package modernwarfare.common;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityBulletFlame extends EntityBullet
{
    public EntityBulletFlame(World world)
    {
        super(world);
        setSize(0.5F, 0.5F);
    }

    public EntityBulletFlame(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setSize(0.5F, 0.5F);
    }

    public EntityBulletFlame(World world, Entity entity, ItemGun itemgun)
    {
        super(world, entity, itemgun);
        setSize(0.5F, 0.5F);
    }

    @Override
    public void playServerSound(World world)
    {
        world.playSoundAtEntity(this, ((ItemGun)ModernWarfare.itemGunFlamethrower).firingSound, ((ItemGun)ModernWarfare.itemGunFlamethrower).soundRangeFactor, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
    }

    @Override
    public void onUpdate()
    {
        onEntityUpdate();

        if (timeInAir == 30)
        {
            setEntityDead();
        }

        worldObj.spawnParticle("smoke", posX, posY, posZ, 0.0D, 0.0D, 0.0D);

        if (inGround)
        {
            int i = worldObj.getBlockId(xTile, yTile, zTile);

            if (i != inTile)
            {
                inGround = false;
                motionX *= rand.nextFloat() * 0.2F;
                motionY *= rand.nextFloat() * 0.2F;
                motionZ *= rand.nextFloat() * 0.2F;
                timeInTile = 0;
                timeInAir = 0;
            }
        }
        else {
            timeInAir++;
        }

        Vec3 vec3d = Vec3.createVectorHelper(posX, posY, posZ);
        Vec3 vec3d1 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
        MovingObjectPosition movingobjectposition = worldObj.clip(vec3d, vec3d1);
        vec3d = Vec3.createVectorHelper(posX, posY, posZ);
        vec3d1 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);

        if (movingobjectposition != null)
        {
            vec3d1 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        Entity entity = null;
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
        double d = 0.0D;
        Vec3 vec3d2 = null;

        for (int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);

            if (!entity1.canBeCollidedWith() || (entity1 == owner || owner != null && entity1 == owner.ridingEntity) && timeInAir < 5 || serverSpawned)
            {
                continue;
            }

            float f = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f, f, f);
            MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

            if (movingobjectposition1 == null)
            {
                continue;
            }

            double d1 = vec3d.distanceTo(movingobjectposition1.hitVec);

            if (d1 < d || d == 0.0D)
            {
                vec3d2 = movingobjectposition1.hitVec;
                entity = entity1;
                d = d1;
            }
        }

        if (entity != null)
        {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if (movingobjectposition != null && !worldObj.isRemote)
        {
            if (movingobjectposition.entityHit != null)
            {
                int k = damage;

                if ((owner instanceof IMob) && (movingobjectposition.entityHit instanceof EntityPlayer))
                {
                    if (worldObj.difficultySetting == 0)
                    {
                        k = 0;
                    }

                    if (worldObj.difficultySetting == 1)
                    {
                        k = k / 3 + 1;
                    }

                    if (worldObj.difficultySetting == 3)
                    {
                        k = (k * 3) / 2;
                    }
                }

                k = checkHeadshot(movingobjectposition, vec3d2, k);

                if(movingobjectposition.entityHit instanceof EntityLiving)
                {
                    WarTools.attackEntityIgnoreDelay((EntityLiving)movingobjectposition.entityHit, DamageSource.causeThrownDamage(this, owner), k);
                }
                else {
                    movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, owner), k);
                }

                movingobjectposition.entityHit.setFire(300);
            }
            else
            {
                xTile = movingobjectposition.blockX;
                yTile = movingobjectposition.blockY;
                zTile = movingobjectposition.blockZ;

                if (worldObj.getBlockId(xTile, yTile, zTile) == Block.ice.blockID && Block.ice.getBlockHardness(worldObj, xTile, yTile, zTile) < 1000000F)
                {
                    Block.ice.breakBlock(worldObj, xTile, yTile, zTile, worldObj.getBlockId(xTile, yTile, zTile), worldObj.getBlockMetadata(xTile, yTile, zTile));
                }
                else
                {
                    byte byte0 = (byte)(motionX > 0.0D ? 1 : -1);
                    byte byte1 = (byte)(motionY > 0.0D ? 1 : -1);
                    byte byte2 = (byte)(motionZ > 0.0D ? 1 : -1);
                    boolean flag = worldObj.getBlockId(xTile - byte0, yTile, zTile) == 0 || worldObj.getBlockId(xTile - byte0, yTile, zTile) == Block.snow.blockID;
                    boolean flag1 = worldObj.getBlockId(xTile, yTile - byte1, zTile) == 0 || worldObj.getBlockId(xTile, yTile - byte1, zTile) == Block.snow.blockID;
                    boolean flag2 = worldObj.getBlockId(xTile, yTile, zTile - byte2) == 0 || worldObj.getBlockId(xTile, yTile, zTile - byte2) == Block.snow.blockID;

                    if (flag)
                    {
                        worldObj.setBlock(xTile - byte0, yTile, zTile, Block.fire.blockID);
                    }

                    if (flag1)
                    {
                        worldObj.setBlock(xTile, yTile - byte1, zTile, Block.fire.blockID);
                    }

                    if (flag2)
                    {
                        worldObj.setBlock(xTile, yTile, zTile - byte2, Block.fire.blockID);
                    }
                }
            }

            setEntityDead();
        }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        setRotationToVelocity();

        if (worldObj.handleMaterialAcceleration(boundingBox, Material.water, this))
        {
            setEntityDead();
        }

        setPosition(posX, posY, posZ);
    }

    public void setRotationToVelocity()
    {
        float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / Math.PI);

        for (rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / Math.PI); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }

        for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }

        for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }

        for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }
    }

    @Override
    public void setVelocity(double d, double d1, double d2)
    {
        super.setVelocity(d, d1, d2);
        setRotationToVelocity();
    }

    @Override
    public float getBrightness(float f)
    {
        return 2.0F;
    }
}
