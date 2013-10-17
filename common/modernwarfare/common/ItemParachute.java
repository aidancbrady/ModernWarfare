package modernwarfare.common;

import net.minecraft.item.EnumArmorMaterial;

public class ItemParachute extends ItemArmorWar
{
    public ItemParachute(int i, int j)
    {
        super(i, EnumArmorMaterial.CLOTH, "parachute", j, 1);
        setMaxDamage(7);
    }
}
