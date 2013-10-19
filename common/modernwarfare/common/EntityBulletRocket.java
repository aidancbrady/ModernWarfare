package modernwarfare.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityBulletRocket extends EntityBullet
{
    public EntityBulletRocket(World world)
    {
        super(world);
        setSize(0.25F, 0.25F);
    }

    public EntityBulletRocket(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setSize(0.25F, 0.25F);
    }

    public EntityBulletRocket(World world, Entity entity, ItemGun itemgun)
    {
        super(world, entity, itemgun);
        setSize(0.25F, 0.25F);
    }

    @Override
    public void playServerSound(World world)
    {
        world.playSoundAtEntity(this, ((ItemGun)ModernWarfare.itemGunRocketLauncher).firingSound, ((ItemGun)ModernWarfare.itemGunRocketLauncher).soundRangeFactor, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
    }

    @Override
    public void onUpdate()
    {
        onEntityUpdate();
        
        canRender = true;

        if (timeInAir == 200)
        {
            explode();
            return;
        }

        if (timeInAir % 2 == 0)
        {
            double d = 0.625D;
            double d1 = Math.sqrt(motionX * motionX + motionZ * motionZ + motionY * motionY);
            worldObj.spawnParticle("smoke", posX - (motionX / d1) * d, posY - (motionY / d1) * d, posZ - (motionZ / d1) * d, 0.0D, 0.0D, 0.0D);
        }

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
        else
        {
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
        double d2 = 0.0D;

        for (int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);

            if (!entity1.canBeCollidedWith() || (entity1 == owner || owner != null && entity1 == owner.ridingEntity) && timeInAir < 5 || serverSpawned)
            {
                continue;
            }

            float f2 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f2, f2, f2);
            MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

            if (movingobjectposition1 == null)
            {
                continue;
            }

            double d3 = vec3d.distanceTo(movingobjectposition1.hitVec);

            if (d3 < d2 || d2 == 0.0D)
            {
                entity = entity1;
                d2 = d3;
            }
        }

        if (entity != null)
        {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if (movingobjectposition != null && !worldObj.isRemote)
        {
            int k = worldObj.getBlockId(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);

            if (movingobjectposition.entityHit != null || k != Block.tallGrass.blockID && k != Block.vine.blockID)
            {
                if (movingobjectposition.entityHit != null)
                {
                    int l = damage;

                    if ((owner instanceof IMob) && (movingobjectposition.entityHit instanceof EntityPlayer))
                    {
                        if (worldObj.difficultySetting == 0)
                        {
                            l = 0;
                        }

                        if (worldObj.difficultySetting == 1)
                        {
                            l = l / 3 + 1;
                        }

                        if (worldObj.difficultySetting == 3)
                        {
                            l = (l * 3) / 2;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof EntityLiving)
                    {
                        WarTools.attackEntityIgnoreDelay((EntityLiving)movingobjectposition.entityHit, DamageSource.causeThrownDamage(this, owner), l);
                    }
                    else {
                        movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, owner), l);
                    }
                }

                explode();
            }
        }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / Math.PI);

        for (rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / Math.PI); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }

        for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }

        for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }

        for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }

        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
        float f1 = 1.002557F;
        float f3 = 0.0F;

        if (handleWaterMovement())
        {
            for (int i1 = 0; i1 < 4; i1++)
            {
                float f4 = 0.25F;
                worldObj.spawnParticle("bubble", posX - motionX * (double)f4, posY - motionY * (double)f4, posZ - motionZ * (double)f4, motionX, motionY, motionZ);
            }

            f1 = 0.95F;
            f3 = 0.03F;
        }

        motionX *= f1;
        motionY *= f1;
        motionZ *= f1;
        motionY -= f3;
        setPosition(posX, posY, posZ);
    }
    
    private void doFX()
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

        try {
            dataoutputstream.writeInt(11);
            dataoutputstream.writeDouble(posX);
            dataoutputstream.writeDouble(posY);
            dataoutputstream.writeDouble(posZ);
        } catch (IOException ioexception) {
            System.out.println("[ModernWarfare] An error occured while writing packet data.");
            ioexception.printStackTrace();
        }

        Packet250CustomPayload packet250custompayload = new Packet250CustomPayload();
        packet250custompayload.channel = "MDWF";
        packet250custompayload.data = bytearrayoutputstream.toByteArray();
        packet250custompayload.length = packet250custompayload.data.length;
        PacketDispatcher.sendPacketToAllAround(posX, posY, posZ, 40, worldObj.provider.dimensionId, packet250custompayload);
        System.out.println("[ModernWarfare] Sent '11' packet to server");
    }

    private void explode()
    {
        Explosion explosion = new Explosion(worldObj, null, posX, (float)posY, (float)posZ, 3F);
        explosion.doExplosionA();

        if (ModernWarfare.explosionsDestroyBlocks)
        {
            explosion.doExplosionB(true);
        }
        else {
            worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 4F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
        }
        
        doFX();

        setEntityDead();
    }
}
