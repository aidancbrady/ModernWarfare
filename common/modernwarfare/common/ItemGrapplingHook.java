package modernwarfare.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemGrapplingHook extends ItemWar
{
    public ItemGrapplingHook(int i)
    {
        super(i);
        maxStackSize = 1;
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D()
    {
        return true;
    }

    /**
     * Returns true if this item should be rotated by 180 degrees around the Y axis when being held in an entities
     * hands.
     */
    public boolean shouldRotateAroundWhenRendering()
    {
        return true;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (ModernWarfare.grapplingHooks.get(entityplayer) != null)
        {
            int i = ((EntityGrapplingHook)ModernWarfare.grapplingHooks.get(entityplayer)).catchFish();
            entityplayer.swingItem();
        }
        else
        {
            world.playSoundAtEntity(entityplayer, "war.grunt", 1.0F, 1.0F / (itemRand.nextFloat() * 0.1F + 0.95F));

            if (!world.isRemote)
            {
                world.spawnEntityInWorld(new EntityGrapplingHook(world, entityplayer));
            }

            entityplayer.swingItem();
        }

        return itemstack;
    }
}
