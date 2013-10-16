package modernwarfare.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemGrenadeSmoke extends ItemWar
{
    public ItemGrenadeSmoke(int i)
    {
        super(i);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        itemstack.stackSize--;
        world.playSoundAtEntity(entityplayer, "modernwarfare:grunt", 1.0F, 1.0F / (itemRand.nextFloat() * 0.1F + 0.95F));

        if (!world.isRemote)
        {
            world.spawnEntityInWorld(new EntityGrenadeSmoke(world, entityplayer));
        }

        return itemstack;
    }
}
