package modernwarfare.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabModernWarfare extends CreativeTabs
{
	public CreativeTabModernWarfare()
	{
		super("tabModernWarfare");
	}
	
	@Override
	public ItemStack getIconItemStack()
	{
		return new ItemStack(ModernWarfare.itemGrenade);
	}
}