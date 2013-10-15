package modernwarfare.common;

import net.minecraft.world.World;

public class EntitySentrySg552 extends EntitySentry
{
    public EntitySentrySg552(World world)
    {
        super(world);
        setParameters();
    }

    public EntitySentrySg552(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setParameters();
    }

    private void setParameters()
    {
        gun = (ItemGun)ModernWarfare.itemGunSg552;
        ATTACK_DELAY = 50;
        range = 48F;
    }
}
