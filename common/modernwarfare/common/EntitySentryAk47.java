package modernwarfare.common;

import net.minecraft.world.World;

public class EntitySentryAk47 extends EntitySentry
{
    public EntitySentryAk47(World world)
    {
        super(world);
        setParameters();
    }

    public EntitySentryAk47(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setParameters();
    }

    private void setParameters()
    {
        gun = (ItemGun)ModernWarfare.itemGunAk47;
        ATTACK_DELAY = 25;
        range = 32F;
    }
}
