package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ItemGun extends ItemCustomUseDelay
{
    public String firingSound;
    public Item requiredBullet;
    public int numBullets;
    public int burstShots;
    public int damage;
    public float headshotMultiplier;
    public float muzzleVelocity;
    public float spread;
    public float recoil;
    public int soundDelay;
    public float soundRangeFactor;
    protected long lastSound;
    protected long lastEmptySound;

    public ItemGun(int i)
    {
        super(i);
        numBullets = 1;
        burstShots = 0;
        damage = 0;
        headshotMultiplier = 2.0F;
        muzzleVelocity = 1.5F;
        spread = 1.0F;
        recoil = 1.0F;
        soundDelay = -1;
        soundRangeFactor = 4F;
        lastSound = 0L;
        lastEmptySound = 0L;
        maxStackSize = 1;
    }

    @Override
    public float getDamageVsEntity(Entity entity, ItemStack itemstack)
    {
        return 4;
    }

    @Override
    public boolean use(ItemStack itemstack, World world, Entity entity, float f, float f1, float f2, float f3, float f4)
    {
        return fireBullet(world, entity, itemstack, false, f, f1, f2, f3, f4);
    }

    public boolean fireBullet(World world, Entity entity, ItemStack itemstack, boolean flag, float f, float f1, float f2, float f3, float f4)
    {
        if (!ModernWarfare.reloadTimes.containsKey(entity))
        {
            int i;

            if (entity instanceof EntityPlayer)
            {
                i = WarTools.useItemInInventory((EntityPlayer)entity, requiredBullet.itemID);
            }
            else if (entity.riddenByEntity != null && (entity.riddenByEntity instanceof EntityPlayer))
            {
                i = WarTools.useItemInInventory((EntityPlayer)entity.riddenByEntity, requiredBullet.itemID);
            }
            else
            {
                i = 1;
            }

            if (i > 0)
            {
                if (world.getWorldTime() - lastSound < 0L)
                {
                    lastSound = world.getWorldTime() - (long)soundDelay;
                }

                if (soundDelay == 0 || lastSound == 0L || world.getWorldTime() - lastSound > (long)soundDelay)
                {
                    world.playSoundAtEntity(entity, firingSound, 1.0F, 1.0F / (itemRand.nextFloat() * 0.1F + 0.95F));
                    lastSound = world.getWorldTime();
                }

                if (!world.isRemote)
                {
                    for (int j = 0; j < numBullets; j++)
                    {
                        EntityBullet entitybullet = getBulletEntity(world, entity, f, f1, f2, f3, f4);

                        if (entitybullet != null)
                        {
                            world.spawnEntityInWorld(entitybullet);
                        }
                    }

                    EntityBulletCasing entitybulletcasing = getBulletCasingEntity(world, entity, f1);

                    if ((entity instanceof EntityPlayer) && ModernWarfare.ammoCasings && entitybulletcasing != null)
                    {
                        world.spawnEntityInWorld(entitybulletcasing);
                    }

                    if (!flag && burstShots > 0)
                    {
                        ModernWarfare.burstShots.put(entity, new BurstShotEntry(burstShots, itemstack));
                    }
                }

                if (entity instanceof EntityPlayer)
                {
                    double d = Math.min(recoil, entity.rotationPitch + 90F);
                    double d1 = world.rand.nextFloat() * recoil * 0.5F - recoil * 0.25F;

                    if (!entity.isSneaking())
                    {
                        d *= 2D;
                        d1 *= 2D;

                        if (this instanceof ItemGunMinigun)
                        {
                            d *= 2D;
                            d1 *= 2D;
                        }
                    }

                    if (!entity.onGround)
                    {
                        d *= 2D;
                        d1 *= 2D;
                    }

                    ModernWarfare.currentRecoilV += d;
                    ModernWarfare.currentRecoilH += d1;
                    entity.rotationPitch -= d;
                    entity.rotationYaw += d1;

                    if (i == 2 && !(this instanceof ItemGunMinigun) && !(this instanceof ItemGunLaser))
                    {
                        ModernWarfare.handleReload(world, (EntityPlayer)entity, true);
                    }
                }

                return true;
            }

            if (lastEmptySound == 0L || world.getWorldTime() - lastEmptySound > 20L)
            {
                world.playSoundAtEntity(entity, "modernwarfare:gunempty", 1.0F, 1.0F / (itemRand.nextFloat() * 0.1F + 0.95F));
                lastEmptySound = world.getWorldTime();
            }
        }

        return false;
    }

    @Override
    public boolean isFull3D()
    {
        return true;
    }

    public abstract EntityBullet getBulletEntity(World world, Entity entity, float f, float f1, float f2, float f3, float f4);

    public abstract EntityBulletCasing getBulletCasingEntity(World world, Entity entity, float f);
}
