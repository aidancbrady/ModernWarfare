package modernwarfare.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityAtv extends EntityLandVehicle implements IInventory
{
    public ItemStack gunA;
    public ItemStack gunB;
    public int deathTime;
    public int DEATH_TIME_MAX;
    public int soundLoopTime;
    public String SOUND_RIDING;
    public int SOUND_LOOP_TIME_MAX;

    public EntityAtv(World world)
    {
        super(world);
        gunA = null;
        gunB = null;
        deathTime = -13;
        DEATH_TIME_MAX = 100;
        soundLoopTime = 0;
        SOUND_RIDING = "war.atv";
        SOUND_LOOP_TIME_MAX = 3;
        setSize(1.0F, 1.0F);
        yOffset = 0.3F;
        stepHeight = 1.0F;
        ignoreFrustumCheck = true;
    }

    public EntityAtv(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1 + (double)yOffset, d2);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        prevPosX = d;
        prevPosY = d1;
        prevPosZ = d2;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return true;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return 0.29999999999999999D;
    }

    public float getEyeHeight()
    {
        return 0.7F;
    }

    public void onHurt()
    {
        worldObj.playSoundAtEntity(this, "war.mechhurt", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
    }

    public void onDeath()
    {
        if (deathTime == -13)
        {
            deathTime = DEATH_TIME_MAX;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (rand.nextInt(MAX_HEALTH) > health * 2)
        {
            if (Math.random() < 0.75D)
            {
                spawnParticles("smoke", 4, false);
            }
            else
            {
                spawnParticles("largesmoke", 1, false);
            }
        }

        if (health > 0 && deathTime != -13)
        {
            deathTime = -13;
        }

        if (deathTime >= 0)
        {
            if (deathTime == 0)
            {
                Explosion explosion = new Explosion(worldObj, null, posX, (float)posY, (float)posZ, 3F);
                explosion.doExplosionA();
                worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 4F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
                spawnParticles("explode", 64, true);
                spawnParticles("smoke", 64, true);
                setDead();
            }
            else if (rand.nextInt(DEATH_TIME_MAX) > deathTime)
            {
                spawnParticles("flame", 8, false);
            }

            deathTime--;
        }

        if (riddenByEntity != null)
        {
            if (soundLoopTime <= 0)
            {
                worldObj.playSoundEffect(posX + motionX * 1.5D, posY + (onGround ? 0.0D : motionY) * 1.5D, posZ + motionZ * 1.5D, SOUND_RIDING, 1.0F, 1.0F + (float)(getSpeed() / MAX_SPEED / 4D));
                soundLoopTime = SOUND_LOOP_TIME_MAX;
            }

            soundLoopTime--;
        }
        else
        {
            soundLoopTime = 0;
        }
    }

    public void spawnParticles(String s, int i, boolean flag)
    {
        for (int j = 0; j < i; j++)
        {
            double d = (posX + rand.nextDouble() * (double)width * 1.5D) - (double)width * 0.75D;
            double d1 = ((posY + rand.nextDouble() * (double)height) - (double)height * 0.5D) + 0.25D;
            double d2 = (posZ + rand.nextDouble() * (double)width) - (double)width * 0.5D;
            double d3 = flag ? rand.nextDouble() - 0.5D : 0.0D;
            double d4 = flag ? rand.nextDouble() - 0.5D : 0.0D;
            double d5 = flag ? rand.nextDouble() - 0.5D : 0.0D;

            if (Math.random() < 0.75D)
            {
                worldObj.spawnParticle(s, d, d1, d2, d3, d4, d5);
            }
            else
            {
                worldObj.spawnParticle(s, d, d1, d2, d3, d4, d5);
            }
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer entityplayer)
    {
        if (entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == ModernWarfare.itemWrench.itemID)
        {
            if (health > 0 && health < MAX_HEALTH)
            {
                worldObj.playSoundAtEntity(this, "war.wrench", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                health = Math.min(health + 4, MAX_HEALTH);
                entityplayer.swingItem();
                entityplayer.getCurrentEquippedItem().damageItem(1, entityplayer);

                if (entityplayer.getCurrentEquippedItem().getItemDamage() <= 0)
                {
                    entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = null;
                }
            }

            return true;
        }

        if (riddenByEntity != null && (riddenByEntity instanceof EntityPlayer) && riddenByEntity != entityplayer)
        {
            return true;
        }

        if (!worldObj.isRemote)
        {
            entityplayer.mountEntity(this);
            ItemCustomUseDelay.doNotUseThisTick = worldObj.getWorldTime();
        }

        return true;
    }

    /**
     * Reads the entity from NBT (calls an abstract helper method to read specialized data)
     */
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        NBTTagList nbttaglist = nbttagcompound.getTagList("Pos");
        NBTTagList nbttaglist1 = nbttagcompound.getTagList("Motion");
        NBTTagList nbttaglist2 = nbttagcompound.getTagList("Rotation");
        setPosition(0.0D, 0.0D, 0.0D);
        motionX = ((NBTTagDouble)nbttaglist1.tagAt(0)).data;
        motionY = ((NBTTagDouble)nbttaglist1.tagAt(1)).data;
        motionZ = ((NBTTagDouble)nbttaglist1.tagAt(2)).data;

        if (Math.abs(motionX) > 10D)
        {
            motionX = 0.0D;
        }

        if (Math.abs(motionY) > 10D)
        {
            motionY = 0.0D;
        }

        if (Math.abs(motionZ) > 10D)
        {
            motionZ = 0.0D;
        }

        prevPosX = lastTickPosX = posX = ((NBTTagDouble)nbttaglist.tagAt(0)).data;
        prevPosY = lastTickPosY = posY = ((NBTTagDouble)nbttaglist.tagAt(1)).data;
        prevPosZ = lastTickPosZ = posZ = ((NBTTagDouble)nbttaglist.tagAt(2)).data;
        prevRotationYaw = rotationYaw = ((NBTTagFloat)nbttaglist2.tagAt(0)).data;
        prevRotationPitch = rotationPitch = ((NBTTagFloat)nbttaglist2.tagAt(1)).data;
        fallDistance = nbttagcompound.getFloat("FallDistance");
        setFire(nbttagcompound.getShort("Fire"));
        setAir(nbttagcompound.getShort("Air"));
        onGround = nbttagcompound.getBoolean("OnGround");
        setPosition(posX, posY, posZ);
        readEntityFromNBT(nbttagcompound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        NBTTagList nbttaglist = nbttagcompound.getTagList("GunA");

        if (nbttaglist.tagCount() > 0)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(0);
            byte byte0 = nbttagcompound1.getByte("Slot");

            if (byte0 == 0)
            {
                gunA = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        NBTTagList nbttaglist1 = nbttagcompound.getTagList("GunB");

        if (nbttaglist1.tagCount() > 0)
        {
            NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbttaglist1.tagAt(0);
            byte byte1 = nbttagcompound2.getByte("Slot");

            if (byte1 == 0)
            {
                gunB = ItemStack.loadItemStackFromNBT(nbttagcompound2);
            }
        }

        health = nbttagcompound.getInteger("Health");
        deathTime = nbttagcompound.getInteger("DeathTime");
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        NBTTagList nbttaglist = new NBTTagList();

        if (gunA != null)
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte)0);
            gunA.writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
        }

        nbttagcompound.setTag("GunA", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        if (gunB != null)
        {
            NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            nbttagcompound2.setByte("Slot", (byte)0);
            gunB.writeToNBT(nbttagcompound2);
            nbttaglist1.appendTag(nbttagcompound2);
        }

        nbttagcompound.setTag("GunB", nbttaglist1);
        nbttagcompound.setInteger("Health", health);
        nbttagcompound.setInteger("DeathTime", deathTime);
    }

    public void fireGuns()
    {
        if (gunA != null)
        {
            ((ItemGun)gunA.getItem()).onItemRightClickEntity(gunA, worldObj, this, -1.8F, 0.0F, 0.5625F, 90F, 0.0F);
        }

        if (gunB != null)
        {
            ((ItemGun)gunB.getItem()).onItemRightClickEntity(gunB, worldObj, this, -1.8F, 0.0F, -0.3125F, 90F, 0.0F);
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 2;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int i)
    {
        if (i == 0)
        {
            return gunA;
        }

        if (i == 1)
        {
            return gunB;
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (i == 0)
        {
            ItemStack itemstack = gunA;
            gunA = null;
            return itemstack;
        }

        if (i == 1)
        {
            ItemStack itemstack1 = gunB;
            gunB = null;
            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack decrStackSize(int i, int j)
    {
        ItemStack itemstack = null;

        if (i == 0 && gunA != null)
        {
            itemstack = gunA;
            gunA = null;
        }
        else if (i == 1 && gunB != null)
        {
            itemstack = gunB;
            gunB = null;
        }

        return itemstack;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        if (itemstack == null || (itemstack.getItem() instanceof ItemGun))
        {
            if (i == 0)
            {
                gunA = itemstack;
            }
            else if (i == 1)
            {
                gunB = itemstack;
            }
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "ATV";
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 1;
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged()
    {
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return entityplayer.getDistanceSq(posX, posY, posZ) <= 64D;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}

	@Override
	public boolean isInvNameLocalized() 
	{
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}
}
