package modernwarfare.common;

import net.minecraft.world.World;

public class EntitySentryMinigun extends EntitySentry
{
    public EntitySentryMinigun(World world)
    {
        super(world);
        setParameters();
    }

    public EntitySentryMinigun(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setParameters();
    }

    private void setParameters()
    {
        gun = (ItemGun)ModernWarfare.itemGunMinigun;
        ATTACK_DELAY = 5;
        range = 32F;
    }
}
