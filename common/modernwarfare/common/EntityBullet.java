package modernwarfare.common;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.ModLoader;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class EntityBullet extends Entity
{
    protected int xTile;
    protected int yTile;
    protected int zTile;
    protected int inTile;
    protected boolean inGround;
    public Entity owner;
    protected int timeInTile;
    protected int timeInAir;
    protected int damage;
    protected float headshotMultiplier;
    protected boolean serverSpawned;
    protected String firingSound;
    protected float soundRangeFactor;
    protected boolean serverSoundPlayed;

    public EntityBullet(World world)
    {
        super(world);
        soundRangeFactor = 8F;
        serverSoundPlayed = false;
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = 0;
        inGround = false;
        timeInAir = 0;
        setSize(0.0625F, 0.03125F);
    }

    public EntityBullet(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1, d2);
        yOffset = 0.0F;
        serverSpawned = true;
    }

    public abstract void playServerSound(World world);

    public EntityBullet(World world, Entity entity, ItemGun itemgun)
    {
        this(world);
        owner = entity;
        damage = itemgun.damage;
        headshotMultiplier = itemgun.headshotMultiplier;
        setLocationAndAngles(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, entity.rotationYaw, entity.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * 0.16F;
        posY -= 0.1D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * 0.16F;
        setPosition(posX, posY, posZ);
        yOffset = 0.0F;
        float f7 = itemgun.spread;

        if (entity instanceof EntityLiving)
        {
            boolean flag = Math.abs(entity.motionX) > 0.1D || Math.abs(entity.motionY) > 0.1D || Math.abs(entity.motionZ) > 0.1D;

            if (flag)
            {
                f7 *= 2.0F;

                if(itemgun instanceof ItemGunMinigun)
                {
                    f7 *= 2.0F;
                }
            }

            if(!entity.onGround)
            {
                f7 *= 2.0F;

                if(itemgun instanceof ItemGunMinigun)
                {
                    f7 *= 2.0F;
                }
            }

            if((entity instanceof EntityPlayer) && (itemgun instanceof ItemGunSniper))
            {
                EntityPlayer entityplayer = (EntityPlayer)entity;

                if(flag)
                {
                    f7 = (float)((double)f7 + 0.25D);
                }

                if(!entity.onGround)
                {
                    f7 = (float)((double)f7 + 0.25D);
                }

                if(!entityplayer.isSneaking())
                {
                    f7 = (float)((double)f7 + 0.25D);
                }

                if(!ModernWarfare.getSniperZoomedIn(entityplayer))
                {
                    f7 = 8F;
                }
            }
        }

        if(entity.riddenByEntity != null && (entity instanceof EntityPlayer))
        {
            owner = entity.riddenByEntity;
        }

        motionX = -MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
        motionZ = MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
        motionY = -MathHelper.sin((rotationPitch / 180F) * (float)Math.PI);
        setBulletHeading(motionX, motionY, motionZ, itemgun.muzzleVelocity, f7 / 2.0F);
        double d2 = 0.0D;
        double d3 = 0.0D;
        double d4 = 0.0D;

        if(entity.ridingEntity != null)
        {
            d2 = entity.ridingEntity.motionX;
            d3 = entity.ridingEntity.onGround ? 0.0D : entity.ridingEntity.motionY;
            d4 = entity.ridingEntity.motionZ;
        }
        else if(entity.riddenByEntity != null)
        {
            d2 = entity.motionX;
            d3 = entity.onGround ? 0.0D : entity.motionY;
            d4 = entity.motionZ;
        }

        motionX += d2;
        motionY += d3;
        motionZ += d4;
    }

    @Override
    protected void entityInit() {}

    public void setBulletHeading(double d, double d1, double d2, float f, float f1)
    {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += rand.nextGaussian() * 0.0075D * (double)f1;
        d1 += rand.nextGaussian() * 0.0075D * (double)f1;
        d2 += rand.nextGaussian() * 0.0075D * (double)f1;
        d *= f;
        d1 *= f;
        d2 *= f;
        motionX = d;
        motionY = d1;
        motionZ = d2;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / Math.PI);
        prevRotationPitch = rotationPitch = (float)((Math.atan2(d1, f3) * 180D) / Math.PI);
        timeInTile = 0;
    }

    @Override
    public boolean isInRangeToRenderDist(double d)
    {
        return true;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if(serverSpawned && !serverSoundPlayed)
        {
        	if(!worldObj.isRemote || owner != ModernWarfare.proxy.getClientPlayer())
        	{
                playServerSound(worldObj);
                serverSoundPlayed = true;
        	}
        }

        if(timeInAir == 200)
        {
            setEntityDead();
        }

        if(prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            prevRotationYaw = rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / Math.PI);
            prevRotationPitch = rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / Math.PI);
        }

        if(inGround)
        {
            int i = worldObj.getBlockId(xTile, yTile, zTile);

            if(i != inTile)
            {
                inGround = false;
                motionX *= rand.nextFloat() * 0.2F;
                motionY *= rand.nextFloat() * 0.2F;
                motionZ *= rand.nextFloat() * 0.2F;
                timeInTile = 0;
                timeInAir = 0;
            }
            else {
                timeInTile++;

                if(timeInTile == 200)
                {
                    setEntityDead();
                }

                return;
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

        if(movingobjectposition != null)
        {
            vec3d1 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        Entity entity = null;
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
        double d = 0.0D;
        Vec3 vec3d2 = null;

        for(int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);

            if(!entity1.canBeCollidedWith() || (entity1 == owner || owner != null && entity1 == owner.ridingEntity || owner != null && entity1 == owner.riddenByEntity) && timeInAir < 5 || serverSpawned)
            {
                continue;
            }

            float f4 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f4, f4, f4);
            MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

            if(movingobjectposition1 == null)
            {
                continue;
            }

            double d1 = vec3d.distanceTo(movingobjectposition1.hitVec);

            if(d1 < d || d == 0.0D)
            {
                vec3d2 = movingobjectposition1.hitVec;
                entity = entity1;
                d = d1;
            }
        }

        if(entity != null)
        {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if(movingobjectposition != null)
        {
            int k = worldObj.getBlockId(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);

            if(movingobjectposition.entityHit != null || k != Block.tallGrass.blockID)
            {
                if(movingobjectposition.entityHit != null)
                {
                    int l = damage;

                    if((owner instanceof IMob) && (movingobjectposition.entityHit instanceof EntityPlayer))
                    {
                        if(worldObj.difficultySetting == 0)
                        {
                            l = 0;
                        }

                        if(worldObj.difficultySetting == 1)
                        {
                            l = l / 3 + 1;
                        }

                        if(worldObj.difficultySetting == 3)
                        {
                            l = (l * 3) / 2;
                        }
                    }

                    l = checkHeadshot(movingobjectposition, vec3d2, l);

                    if(movingobjectposition.entityHit instanceof EntityLiving)
                    {
                        WarTools.attackEntityIgnoreDelay((EntityLiving)movingobjectposition.entityHit, DamageSource.causeThrownDamage(this, owner), l);
                    }
                    else {
                        movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, owner), l);
                    }
                }
                else
                {
                    xTile = movingobjectposition.blockX;
                    yTile = movingobjectposition.blockY;
                    zTile = movingobjectposition.blockZ;
                    inTile = k;
                    motionX = (float)(movingobjectposition.hitVec.xCoord - posX);
                    motionY = (float)(movingobjectposition.hitVec.yCoord - posY);
                    motionZ = (float)(movingobjectposition.hitVec.zCoord - posZ);
                    float f2 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
                    posX -= (motionX / (double)f2) * 0.050000000000000003D;
                    posY -= (motionY / (double)f2) * 0.050000000000000003D;
                    posZ -= (motionZ / (double)f2) * 0.050000000000000003D;
                    inGround = true;

                    if (ModernWarfare.bulletsDestroyGlass && (inTile == Block.glass.blockID || inTile == Block.thinGlass.blockID))
                    {
                        Block block;

                        if (inTile == Block.glass.blockID)
                        {
                            block = Block.glass;
                        }
                        else
                        {
                            block = Block.thinGlass;
                        }

                        WarTools.minecraft.effectRenderer.addBlockDestroyEffects(xTile, yTile, zTile, block.blockID & 0xff, Block.glass.blockID >> 8 & 0xff);
                        WarTools.minecraft.sndManager.playSound(block.stepSound.getBreakSound(), (float)xTile + 0.5F, (float)yTile + 0.5F, (float)zTile + 0.5F, (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                        worldObj.setBlockToAir(xTile, yTile, zTile);
                        block.onBlockDestroyedByPlayer(worldObj, xTile, yTile, zTile, worldObj.getBlockMetadata(xTile, yTile, zTile));
                    }
                }

                worldObj.playSoundAtEntity(this, "modernwarfare:impact", 0.2F, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
                setEntityDead();
            }
        }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        float f1 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / Math.PI);

        for (rotationPitch = (float)((Math.atan2(motionY, f1) * 180D) / Math.PI); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }

        for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }

        for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }

        for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }

        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
        float f3 = 1.0F;
        float f5 = 0.0F;

        if (handleWaterMovement())
        {
            for (int i1 = 0; i1 < 4; i1++)
            {
                float f6 = 0.25F;
                worldObj.spawnParticle("bubble", posX - motionX * (double)f6, posY - motionY * (double)f6, posZ - motionZ * (double)f6, motionX, motionY, motionZ);
            }

            f3 = 0.8F;
            f5 = 0.03F;
        }

        motionX *= f3;
        motionY *= f3;
        motionZ *= f3;
        motionY -= f5;
        setPosition(posX, posY, posZ);
    }

    protected int checkHeadshot(MovingObjectPosition movingobjectposition, Vec3 vec3d, int i)
    {
        float f = 0.0F;

        if ((movingobjectposition.entityHit instanceof EntityPlayer) || (movingobjectposition.entityHit instanceof EntityZombie) || (movingobjectposition.entityHit instanceof EntitySkeleton))
        {
            f = 0.25F;
        }
        else if (movingobjectposition.entityHit instanceof EntityCreeper)
        {
            f = 0.3076923F;
        }
        else if (movingobjectposition.entityHit instanceof EntityEnderman)
        {
            f = 0.173913F;
        }

        if (f > 0.0F)
        {
            double d = movingobjectposition.entityHit.boundingBox.maxY;
            double d1 = movingobjectposition.entityHit.boundingBox.minY;
            double d2 = d - d1;

            if (vec3d.yCoord > d - d2 * (double)f)
            {
                i = Math.round((float)i * headshotMultiplier);
            }
        }

        return i;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("xTile", (short)xTile);
        nbttagcompound.setShort("yTile", (short)yTile);
        nbttagcompound.setShort("zTile", (short)zTile);
        nbttagcompound.setByte("inTile", (byte)inTile);
        nbttagcompound.setByte("inGround", (byte)(inGround ? 1 : 0));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        xTile = nbttagcompound.getShort("xTile");
        yTile = nbttagcompound.getShort("yTile");
        zTile = nbttagcompound.getShort("zTile");
        inTile = nbttagcompound.getByte("inTile") & 0xff;
        inGround = nbttagcompound.getByte("inGround") == 1;
    }

    @Override
    public float getShadowSize()
    {
        return 0.0F;
    }

    public void setEntityDead()
    {
        super.setDead();
        owner = null;
    }
}
