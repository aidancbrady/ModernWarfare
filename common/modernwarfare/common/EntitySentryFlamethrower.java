package modernwarfare.common;

import net.minecraft.world.World;

public class EntitySentryFlamethrower extends EntitySentry
{
    public EntitySentryFlamethrower(World world)
    {
        super(world);
        setParameters();
    }

    public EntitySentryFlamethrower(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setParameters();
    }

    private void setParameters()
    {
        gun = (ItemGun)ModernWarfare.itemGunFlamethrower;
        ATTACK_DELAY = 1;
        range = 16F;
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return 0;
    }
}
