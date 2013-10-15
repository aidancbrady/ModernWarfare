package modernwarfare.common;

import net.minecraft.world.World;

public class EntitySentryRocketLauncherLaser extends EntitySentry
{
    public EntitySentryRocketLauncherLaser(World world)
    {
        super(world);
        setParameters();
    }

    public EntitySentryRocketLauncherLaser(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setParameters();
    }

    private void setParameters()
    {
        gun = (ItemGun)ModernWarfare.itemGunRocketLauncherLaser;
        ATTACK_DELAY = 200;
        range = 32F;
    }
}
