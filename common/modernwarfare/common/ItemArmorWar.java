package modernwarfare.common;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemArmorWar extends ItemArmor
{
	private String materialName;
	
    public ItemArmorWar(int i, EnumArmorMaterial enumarmormaterial, String material, int j, int k)
    {
        super(i, enumarmormaterial, j, k);
        materialName = material;
        setCreativeTab(ModernWarfare.tabModernWarfare);
    }
    
    @Override
	public void registerIcons(IconRegister register)
	{
		itemIcon = register.registerIcon("modernwarfare:" + getUnlocalizedName().replace("item.", ""));
	}
    
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer)
    {
		return "modernwarfare:armor/" + materialName + "_" + layer + ".png";
    }
}
