package modernwarfare.common;

import net.minecraft.world.World;

public class EntitySentryMp5 extends EntitySentry
{
    public EntitySentryMp5(World world)
    {
        super(world);
        setParameters();
    }

    public EntitySentryMp5(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setParameters();
    }

    private void setParameters()
    {
        gun = (ItemGun)ModernWarfare.itemGunMp5;
        ATTACK_DELAY = 18;
        range = 32F;
    }
}
