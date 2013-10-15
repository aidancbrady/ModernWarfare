package modernwarfare.common;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

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
		itemIcon = register.registerIcon("modernwarfare:" + getUnlocalizedName().replace("item.", ""));
	}
}
