package modernwarfare.common;

import net.minecraft.item.ItemStack;

public class BurstShotEntry
{
    public int burstShots;
    public ItemStack itemStack;
    public float xOffset;
    public float yOffset;
    public float zOffset;
    public float rotationYawOffset;
    public float rotationPitchOffset;

    public BurstShotEntry(int i, ItemStack itemstack)
    {
        this(i, itemstack, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    }

    public BurstShotEntry(int i, ItemStack itemstack, float f, float f1, float f2, float f3, float f4)
    {
        burstShots = i;
        itemStack = itemstack;
        xOffset = f;
        yOffset = f1;
        zOffset = f2;
        rotationYawOffset = f3;
        rotationPitchOffset = f4;
    }
}
