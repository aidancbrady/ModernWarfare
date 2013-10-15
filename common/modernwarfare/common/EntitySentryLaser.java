package modernwarfare.common;

import net.minecraft.world.World;

public class EntitySentryLaser extends EntitySentry
{
    public EntitySentryLaser(World world)
    {
        super(world);
        setParameters();
    }

    public EntitySentryLaser(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setParameters();
    }

    private void setParameters()
    {
        gun = (ItemGun)ModernWarfare.itemGunLaser;
        ATTACK_DELAY = 50;
        range = 32F;
    }
}
