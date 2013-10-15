package modernwarfare.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockWar extends Block
{
    public BlockWar(int i, Material material)
    {
        super(i, material);
        setCreativeTab(ModernWarfare.tabModernWarfare);
    }
}
