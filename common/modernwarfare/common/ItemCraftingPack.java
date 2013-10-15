package modernwarfare.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCraftingPack extends ItemWar
{
    public ItemCraftingPack(int i)
    {
        super(i);
        setMaxStackSize(1);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        entityplayer.openGui(ModernWarfare.instance, 14, world, (int)entityplayer.posX, (int)entityplayer.posY, (int)entityplayer.posZ);
        return itemstack;
    }
}
