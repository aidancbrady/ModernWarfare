package modernwarfare.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemTelescope extends ItemWar
{
    public ItemTelescope(int i)
    {
        super(i);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        ModernWarfare.proxy.useZoom();
        return itemstack;
    }
}
