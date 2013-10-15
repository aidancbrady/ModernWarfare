package modernwarfare.common;

import net.minecraft.world.World;

public class EntitySentrySniper extends EntitySentry
{
    public EntitySentrySniper(World world)
    {
        super(world);
        setParameters();
    }

    public EntitySentrySniper(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setParameters();
    }

    private void setParameters()
    {
        gun = (ItemGun)ModernWarfare.itemGunSniper;
        ATTACK_DELAY = 100;
        range = 64F;
    }
}
