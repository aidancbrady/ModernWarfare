package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityBulletCasingShell extends EntityBulletCasing
{
    public EntityBulletCasingShell(World world)
    {
        super(world);
        droppedItem = null;
    }

    public EntityBulletCasingShell(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
    }

    public EntityBulletCasingShell(World world, Entity entity, float f)
    {
        super(world, entity, f);
        droppedItem = ModernWarfare.itemBulletCasingShell;
    }
}
