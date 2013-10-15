package modernwarfare.common;

import net.minecraft.world.World;

public class EntitySentryM4 extends EntitySentry
{
    public EntitySentryM4(World world)
    {
        super(world);
        setParameters();
    }

    public EntitySentryM4(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setParameters();
    }

    private void setParameters()
    {
        gun = (ItemGun)ModernWarfare.itemGunM4;
        ATTACK_DELAY = 125;
        range = 32F;
    }
}
