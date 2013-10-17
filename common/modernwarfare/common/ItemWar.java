package modernwarfare.common;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemWar extends Item
{
    public ItemWar(int i)
    {
        super(i);
        setCreativeTab(ModernWarfare.tabModernWarfare);
    }
    
    @Override
	public void registerIcons(IconRegister register)
	{
    	if(itemID == ModernWarfare.itemLightometer.itemID)
    	{
    		itemIcon = (Icon)ModernWarfare.proxy.reloadLightometerIcon();
    		return;
    	}
    	
		itemIcon = register.registerIcon("modernwarfare:" + getUnlocalizedName().replace("item.", ""));
	}
}
