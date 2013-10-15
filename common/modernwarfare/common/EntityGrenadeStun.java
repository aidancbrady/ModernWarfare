package modernwarfare.common;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityGrenadeStun extends EntityGrenade
{
    protected String BOUNCE_SOUND;
    static final double MAX_DISTANCE = 32D;
    static final double MIN_DISTANCE = 8D;
    static final float MAX_ANGLE = 180F;
    static final float MIN_PITCH_ANGLE = 15F;
    static final float MIN_YAW_ANGLE = 15F;
    public static final int MAX_FLASH_TIME_PLAYER = 500;
    public static final int MAX_FLASH_TIME_ENTITY = 200;

    public EntityGrenadeStun(World world)
    {
        super(world);
        BOUNCE_SOUND = "war.stungrenadebounce";
        setEntityItemStack(new ItemStack(ModernWarfare.itemGrenadeStun, 1, 0));
    }

    public EntityGrenadeStun(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        BOUNCE_SOUND = "war.stungrenadebounce";
        setEntityItemStack(new ItemStack(ModernWarfare.itemGrenadeStun, 1, 0));
    }

    public EntityGrenadeStun(World world, EntityLivingBase entityliving)
    {
        super(world, entityliving);
        BOUNCE_SOUND = "war.stungrenadebounce";
        setEntityItemStack(new ItemStack(ModernWarfare.itemGrenadeStun, 1, 0));
    }

    protected void explode()
    {
        if (!exploded)
        {
            exploded = true;
            worldObj.playSoundAtEntity(this, "war.stungrenade", 4F, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
            ArrayList arraylist = getEntityLivingsInRange(32D);

            for (int i = 0; i < arraylist.size(); i++)
            {
                EntityLivingBase entityliving = (EntityLivingBase)arraylist.get(i);

                if (!entityliving.canEntityBeSeen(this))
                {
                    continue;
                }

                double d = posX - entityliving.posX;
                double d1 = posY - entityliving.posY;
                double d2 = posZ - entityliving.posZ;
                float f = entityliving.rotationPitch;
                float f1 = (float)(Math.atan(Math.sqrt(d * d + d2 * d2) / d1) * (180D / Math.PI));

                if (d1 >= 0.0D)
                {
                    f1 -= 90F;
                }
                else
                {
                    f1 += 90F;
                }

                float f2 = f - f1;
                float f3 = entityliving.rotationYaw % 360F;

                if (f3 < -180F)
                {
                    f3 += 360F;
                }

                if (f3 < 0.0F)
                {
                    f3 *= -1F;
                }
                else if (f3 < 180F)
                {
                    f3 *= -1F;
                }
                else
                {
                    f3 = 360F - f3;
                }

                float f4;

                if (d >= 0.0D && d2 >= 0.0D)
                {
                    f4 = (float)(Math.atan(Math.abs(d / d2)) * (180D / Math.PI));
                }
                else if (d >= 0.0D && d2 <= 0.0D)
                {
                    f4 = 90F + (float)(Math.atan(Math.abs(d2 / d)) * (180D / Math.PI));
                }
                else if (d <= 0.0D && d2 >= 0.0D)
                {
                    f4 = -(90F - (float)(Math.atan(Math.abs(d2 / d)) * (180D / Math.PI)));
                }
                else
                {
                    f4 = -(180F - (float)(Math.atan(Math.abs(d / d2)) * (180D / Math.PI)));
                }

                float f5 = f4 - f3;

                if (f5 > 180F)
                {
                    f5 -= 360F;
                }
                else if (f5 < -180F)
                {
                    f5 += 360F;
                }

                f2 = Math.abs(f2);
                float f6;

                if (f2 < 15F)
                {
                    f6 = 1.0F;
                }
                else if (f2 > 180F)
                {
                    f6 = 0.0F;
                }
                else
                {
                    f6 = 1.0F - (f2 - 15F) / 165F;
                }

                f5 = Math.abs(f5);
                float f7;

                if (f5 < 15F)
                {
                    f7 = 1.0F;
                }
                else if (f5 > 180F)
                {
                    f7 = 0.0F;
                }
                else
                {
                    f7 = 1.0F - (f5 - 15F) / 165F;
                }

                float f8 = Math.min(f6, f7);
                float f9 = getDistanceToEntity(entityliving);
                float f10;

                if ((double)f9 < 8D)
                {
                    f10 = 1.0F;
                }
                else
                {
                    f10 = 1.0F - (float)(((double)f9 - 8D) / 24D);
                }

                int j;

                if (entityliving instanceof EntityPlayer)
                {
                    j = Math.round(500F * f10 * f8);
                }
                else
                {
                    j = Math.round(200F * f10);
                }
            }

            isDead = true;
        }
    }

    public ArrayList getEntityLivingsInRange(double d)
    {
        ArrayList arraylist = new ArrayList();

        for (int i = 0; i < worldObj.loadedEntityList.size(); i++)
        {
            Entity entity = (Entity)worldObj.loadedEntityList.get(i);

            if ((entity instanceof EntityLivingBase) && entity.isEntityAlive() && getDistanceSqToEntity(entity) < d * d)
            {
                arraylist.add((EntityLivingBase)entity);
            }
        }

        return arraylist;
    }

    public ArrayList getPlayersInRange(double d)
    {
        ArrayList arraylist = new ArrayList();

        for (int i = 0; i < worldObj.loadedEntityList.size(); i++)
        {
            Entity entity = (Entity)worldObj.loadedEntityList.get(i);

            if ((entity instanceof EntityPlayer) && entity.isEntityAlive() && getDistanceSqToEntity(entity) < d * d)
            {
                arraylist.add((EntityPlayer)entity);
            }
        }

        return arraylist;
    }
}
