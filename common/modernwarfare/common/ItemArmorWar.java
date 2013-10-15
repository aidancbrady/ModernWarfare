package modernwarfare.common;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;

public class ItemArmorWar extends ItemArmor
{
    public ItemArmorWar(int i, EnumArmorMaterial enumarmormaterial, int j, int k)
    {
        super(i, enumarmormaterial, j, k);
        setCreativeTab(ModernWarfare.tabModernWarfare);
    }
    
    @Override
	public void registerIcons(IconRegister register)
	{
		itemIcon = register.registerIcon("modernwarfare:" + getUnlocalizedName().replace("item.", ""));
	}
}
