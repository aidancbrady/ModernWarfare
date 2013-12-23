package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class ItemGun extends ItemWar
{
    public String firingSound;
    public Item requiredBullet;
    public int useDelay;
    public int numBullets;
    public int damage;
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
        damage = 0;
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
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
    {
    	if(!world.isRemote)
    	{
    		if(stack.stackTagCompound == null) 
    		{ 
    			return;
    		}
    		
    		if(stack.stackTagCompound.getInteger("delay") > 0)
    		{
    			stack.stackTagCompound.setInteger("delay", stack.stackTagCompound.getInteger("delay")-1);
    		}
    	}
    }

    public boolean fireBullet(World world, Entity entity, ItemStack itemstack)
    {
        if(!ModernWarfare.reloadTimes.containsKey(entity))
        {
            int ammoUsed;

            if(entity instanceof EntityPlayer)
            {
            	boolean creative = ((EntityPlayer)entity).capabilities.isCreativeMode;
                ammoUsed = WarTools.useItemInInventory((EntityPlayer)entity, requiredBullet.itemID, !creative);
            }
            else if(entity.riddenByEntity != null && (entity.riddenByEntity instanceof EntityPlayer))
            {
            	boolean creative = ((EntityPlayer)entity.riddenByEntity).capabilities.isCreativeMode;
                ammoUsed = WarTools.useItemInInventory((EntityPlayer)entity.riddenByEntity, requiredBullet.itemID, !creative);
            }
            else {
                ammoUsed = 1;
            }

            if(ammoUsed > 0)
            {
                if(world.getWorldTime() - lastSound < 0L)
                {
                    lastSound = world.getWorldTime() - (long)soundDelay;
                }

                if(soundDelay == 0 || lastSound == 0L || world.getWorldTime() - lastSound > (long)soundDelay)
                {
                    world.playSoundAtEntity(entity, firingSound, 1.0F, 1.0F / (itemRand.nextFloat() * 0.1F + 0.95F));
                    lastSound = world.getWorldTime();
                }

                if(!world.isRemote)
                {
                    for(int j = 0; j < numBullets; j++)
                    {
                        EntityBullet bullet = getBulletEntity(world, entity);

                        if(bullet != null)
                        {
                            world.spawnEntityInWorld(bullet);
                        }
                    }

                    EntityBulletCasing bulletCasing = getBulletCasingEntity(world, entity);

                    if((entity instanceof EntityPlayer) && ModernWarfare.ammoCasings && bulletCasing != null)
                    {
                        world.spawnEntityInWorld(bulletCasing);
                    }
                }

                if(entity instanceof EntityPlayer)
                {
                    if(ammoUsed == 2 && !(this instanceof ItemGunMinigun) && !(this instanceof ItemGunLaser))
                    {
                        ModernWarfare.handleReload(world, (EntityPlayer)entity, true);
                    }
                }

                return true;
            }

            if(lastEmptySound == 0L || world.getWorldTime() - lastEmptySound > 20L)
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

    public abstract EntityBullet getBulletEntity(World world, Entity entity);

    public abstract EntityBulletCasing getBulletCasingEntity(World world, Entity entity);
    
    public static boolean canFire(ItemStack itemStack)
    {
		if(itemStack.stackTagCompound == null) 
		{ 
			return true;
		}
		
		return itemStack.stackTagCompound.getInteger("delay") == 0;
    }
    
    public static void addDelay(ItemStack itemStack)
    {
		if(itemStack.stackTagCompound == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		itemStack.stackTagCompound.setInteger("delay", ((ItemGun)itemStack.getItem()).useDelay);
    }
}
