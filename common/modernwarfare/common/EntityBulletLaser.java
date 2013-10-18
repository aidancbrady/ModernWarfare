package modernwarfare.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;

public class EntityBulletLaser extends EntityBullet
{
    public EntityBulletLaser(World world)
    {
        super(world);
        setSize(0.5F, 0.5F);
    }

    public EntityBulletLaser(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setSize(0.5F, 0.5F);
    }

    public EntityBulletLaser(World world, Entity entity, ItemGun itemgun)
    {
        super(world, entity, itemgun);
        setSize(0.5F, 0.5F);
    }

    @Override
    public void playServerSound(World world)
    {
        world.playSoundAtEntity(this, ((ItemGun)ModernWarfare.itemGunLaser).firingSound, ((ItemGun)ModernWarfare.itemGunLaser).soundRangeFactor, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
    }

    @Override
    public void onUpdate()
    {
        onEntityUpdate();

        if (timeInAir == 200)
        {
            setEntityDead();
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
        MovingObjectPosition movingobjectposition = rayTraceBlocks(vec3d, vec3d1);
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

            float f1 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f1, f1, f1);
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
                if (movingobjectposition.entityHit instanceof EntityCreature)
                {
                    if (entity instanceof EntityPig)
                    {
                        int l = rand.nextInt(3);

                        for (int i2 = 0; i2 < l; i2++)
                        {
                            entity.dropItem(Item.porkCooked.itemID, 1);
                        }
                    }
                    else if (entity instanceof EntityCow)
                    {
                        int i1 = rand.nextInt(3) + 1;

                        for (int j2 = 0; j2 < i1; j2++)
                        {
                            dropItem(Item.beefCooked.itemID, 1);
                        }
                    }
                    else if (entity instanceof EntityChicken)
                    {
                        dropItem(Item.chickenCooked.itemID, 1);
                    }

                    movingobjectposition.entityHit.setDead();

                    if (movingobjectposition.entityHit.isDead)
                    {
                        for (int j1 = 0; j1 < 16; j1++)
                        {
                            doSmoke(movingobjectposition.entityHit.posX, movingobjectposition.entityHit.posY + (movingobjectposition.entityHit.height / 2.0F), movingobjectposition.entityHit.posZ, movingobjectposition.entityHit.width / 2.0F, movingobjectposition.entityHit.height / 2.0F);
                        }
                    }
                }

                if (!movingobjectposition.entityHit.isDead)
                {
                    damageEntity(movingobjectposition, vec3d2);
                }

                setEntityDead();
            }
            else {
                int k = worldObj.getBlockId(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);

                if (k != Block.glass.blockID && k != Block.thinGlass.blockID)
                {
                    if (k == Block.blockDiamond.blockID || k == Block.blockGold.blockID || k == Block.blockIron.blockID)
                    {
                        int k1 = movingobjectposition.sideHit;

                        if (k1 == 0 || k1 == 1)
                        {
                            motionY *= -1D;
                        }
                        else if (k1 == 2 || k1 == 3)
                        {
                            motionZ *= -1D;
                        }
                        else if (k1 == 4 || k1 == 5)
                        {
                            motionX *= -1D;
                        }
                    }
                    else {
                        if (k != Block.bedrock.blockID && k != Block.obsidian.blockID && Block.blocksList[k].getBlockHardness(worldObj, movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) < 1000000F)
                        {
                            if (k == Block.sand.blockID)
                            {
                                worldObj.setBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ, Block.glass.blockID);
                            }
                            else if (k == Block.ice.blockID)
                            {
                                worldObj.setBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ, Block.waterStill.blockID);
                            }
                            else if (ModernWarfare.laserSetsFireToBlocks && WarTools.isFlammable(k))
                            {
                                worldObj.setBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ, Block.fire.blockID);
                            }
                            else {
                                worldObj.setBlockToAir(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
                            }

                            for (int l1 = 0; l1 < 16; l1++)
                            {
                                doSmoke(movingobjectposition.blockX + 0.5D, movingobjectposition.blockY + 0.5D, movingobjectposition.blockZ + 0.5D, 0.5D, 0.5D);
                            }
                        }

                        setEntityDead();
                    }
                }
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

        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 1.0F;
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 1.0F;
        setPosition(posX, posY, posZ);
    }

    public void damageEntity(MovingObjectPosition movingobjectposition, Vec3 vec3d)
    {
        int i = damage;

        if ((owner instanceof IMob) && (movingobjectposition.entityHit instanceof EntityPlayer))
        {
            if (worldObj.difficultySetting == 0)
            {
                i = 0;
            }

            if (worldObj.difficultySetting == 1)
            {
                i = i / 3 + 1;
            }

            if (worldObj.difficultySetting == 3)
            {
                i = (i * 3) / 2;
            }
        }

        i = checkHeadshot(movingobjectposition, vec3d, i);

        if (movingobjectposition.entityHit instanceof EntityLiving)
        {
            WarTools.attackEntityIgnoreDelay((EntityLiving)movingobjectposition.entityHit, DamageSource.causeThrownDamage(this, owner), i);
        }
        else {
            movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, owner), i);
        }
    }

    public void doSmoke(double x, double y, double z, double width, double height)
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

        try {
            dataoutputstream.writeInt(10);
            dataoutputstream.writeDouble(x);
            dataoutputstream.writeDouble(y);
            dataoutputstream.writeDouble(z);
            dataoutputstream.writeDouble(width);
            dataoutputstream.writeDouble(height);
        } catch (IOException ioexception) {
            System.out.println("[ModernWarfare] An error occured while writing packet data.");
            ioexception.printStackTrace();
        }

        Packet250CustomPayload packet250custompayload = new Packet250CustomPayload();
        packet250custompayload.channel = "MDWF";
        packet250custompayload.data = bytearrayoutputstream.toByteArray();
        packet250custompayload.length = packet250custompayload.data.length;
        PacketDispatcher.sendPacketToAllAround(x, y, z, 40, worldObj.provider.dimensionId, packet250custompayload);
        System.out.println("[ModernWarfare] Sent '10' packet to server");
    }

    public float getEntityBrightness(float f)
    {
        return 2.0F;
    }

    public MovingObjectPosition rayTraceBlocks(Vec3 vec3d, Vec3 vec3d1)
    {
        return rayTraceBlocks_do(vec3d, vec3d1, false);
    }

    public MovingObjectPosition rayTraceBlocks_do(Vec3 vec3d, Vec3 vec3d1, boolean flag)
    {
        if (Double.isNaN(vec3d.xCoord) || Double.isNaN(vec3d.yCoord) || Double.isNaN(vec3d.zCoord))
        {
            return null;
        }

        if (Double.isNaN(vec3d1.xCoord) || Double.isNaN(vec3d1.yCoord) || Double.isNaN(vec3d1.zCoord))
        {
            return null;
        }

        int i = MathHelper.floor_double(vec3d1.xCoord);
        int j = MathHelper.floor_double(vec3d1.yCoord);
        int k = MathHelper.floor_double(vec3d1.zCoord);
        int l = MathHelper.floor_double(vec3d.xCoord);
        int i1 = MathHelper.floor_double(vec3d.yCoord);
        int j1 = MathHelper.floor_double(vec3d.zCoord);

        for (int k1 = 200; k1-- >= 0;)
        {
            if (Double.isNaN(vec3d.xCoord) || Double.isNaN(vec3d.yCoord) || Double.isNaN(vec3d.zCoord))
            {
                return null;
            }

            if (l == i && i1 == j && j1 == k)
            {
                return null;
            }

            double d = 999D;
            double d1 = 999D;
            double d2 = 999D;

            if (i > l)
            {
                d = (double)l + 1.0D;
            }

            if (i < l)
            {
                d = (double)l + 0.0D;
            }

            if (j > i1)
            {
                d1 = (double)i1 + 1.0D;
            }

            if (j < i1)
            {
                d1 = (double)i1 + 0.0D;
            }

            if (k > j1)
            {
                d2 = (double)j1 + 1.0D;
            }

            if (k < j1)
            {
                d2 = (double)j1 + 0.0D;
            }

            double d3 = 999D;
            double d4 = 999D;
            double d5 = 999D;
            double d6 = vec3d1.xCoord - vec3d.xCoord;
            double d7 = vec3d1.yCoord - vec3d.yCoord;
            double d8 = vec3d1.zCoord - vec3d.zCoord;

            if (d != 999D)
            {
                d3 = (d - vec3d.xCoord) / d6;
            }

            if (d1 != 999D)
            {
                d4 = (d1 - vec3d.yCoord) / d7;
            }

            if (d2 != 999D)
            {
                d5 = (d2 - vec3d.zCoord) / d8;
            }

            byte byte0 = 0;

            if (d3 < d4 && d3 < d5)
            {
                if (i > l)
                {
                    byte0 = 4;
                }
                else
                {
                    byte0 = 5;
                }

                vec3d.xCoord = d;
                vec3d.yCoord += d7 * d3;
                vec3d.zCoord += d8 * d3;
            }
            else if (d4 < d5)
            {
                if (j > i1)
                {
                    byte0 = 0;
                }
                else
                {
                    byte0 = 1;
                }

                vec3d.xCoord += d6 * d4;
                vec3d.yCoord = d1;
                vec3d.zCoord += d8 * d4;
            }
            else
            {
                if (k > j1)
                {
                    byte0 = 2;
                }
                else
                {
                    byte0 = 3;
                }

                vec3d.xCoord += d6 * d5;
                vec3d.yCoord += d7 * d5;
                vec3d.zCoord = d2;
            }

            Vec3 vec3d2 = Vec3.createVectorHelper(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
            l = (int)(vec3d2.xCoord = MathHelper.floor_double(vec3d.xCoord));

            if (byte0 == 5)
            {
                l--;
                vec3d2.xCoord++;
            }

            i1 = (int)(vec3d2.yCoord = MathHelper.floor_double(vec3d.yCoord));

            if (byte0 == 1)
            {
                i1--;
                vec3d2.yCoord++;
            }

            j1 = (int)(vec3d2.zCoord = MathHelper.floor_double(vec3d.zCoord));

            if (byte0 == 3)
            {
                j1--;
                vec3d2.zCoord++;
            }

            int l1 = worldObj.getBlockId(l, i1, j1);
            int i2 = worldObj.getBlockMetadata(l, i1, j1);
            Block block = Block.blocksList[l1];

            if (l1 > 0 && block.canCollideCheck(i2, flag) && l1 != Block.glass.blockID && l1 != Block.thinGlass.blockID)
            {
                MovingObjectPosition movingobjectposition = block.collisionRayTrace(worldObj, l, i1, j1, vec3d, vec3d1);

                if (movingobjectposition != null)
                {
                    return movingobjectposition;
                }
            }
        }

        return null;
    }
}
