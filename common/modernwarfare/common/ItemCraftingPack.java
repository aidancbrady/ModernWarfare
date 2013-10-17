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

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
    	if(!world.isRemote)
    	{
    		entityplayer.openGui(ModernWarfare.instance, 0, world, 0, 0, 0);
    	}
    	
        return itemstack;
    }
}
