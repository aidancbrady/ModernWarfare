package modernwarfare.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemSentry extends ItemWar
{
    public ItemSentry(int i)
    {
        super(i);
        setHasSubtypes(true);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int i)
    {
        return i;
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        return (new StringBuilder()).append(ModernWarfare.sentryNames[itemstack.getItemDamage()]).append(" Sentry").toString();
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        float f = 1.0F;
        float f1 = entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * f;
        float f2 = entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * f;
        double d = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double)f;
        double d1 = (entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * (double)f + 1.6200000000000001D) - (double)entityplayer.yOffset;
        double d2 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double)f;
        Vec3 vec3d = Vec3.createVectorHelper(d, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.01745329F - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.01745329F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.01745329F);
        float f6 = MathHelper.sin(-f1 * 0.01745329F);
        float f7 = f4 * f5;
        float f8 = f6;
        float f9 = f3 * f5;
        double d3 = 5D;
        Vec3 vec3d1 = vec3d.addVector((double)f7 * d3, (double)f8 * d3, (double)f9 * d3);
        MovingObjectPosition movingobjectposition = world.clip(vec3d, vec3d1, true);

        if (movingobjectposition == null)
        {
            return itemstack;
        }

        if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE && movingobjectposition.sideHit == 1)
        {
            int i = movingobjectposition.blockX;
            int j = movingobjectposition.blockY;
            int k = movingobjectposition.blockZ;
            int l = world.getBlockId(i, j + 1, k);
            int i1 = world.getBlockId(i, j + 2, k);

            if (l == 0 && i1 == 0)
            {
                try
                {
                    if (!world.isRemote)
                    {
                        Constructor constructor = ModernWarfare.sentryEntityClasses[itemstack.getItemDamage()].getConstructor(new Class[] {World.class});
                        EntitySentry entitysentry = (EntitySentry)constructor.newInstance(new Object[] {world});
                        entitysentry.setOwner(entityplayer.username);
                        entitysentry.setLocationAndAngles((double)i + 0.5D, (double)j + 1.0D, (double)k + 0.5D, 0.0F, 0.0F);
                        world.spawnEntityInWorld(entitysentry);
                    }
                } catch (Exception e) {
                    return itemstack;
                }

                itemstack.stackSize--;
            }
        }

        return itemstack;
    }
}
