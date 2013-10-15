package modernwarfare.common;

import net.minecraft.world.World;

public class EntitySentryShotgun extends EntitySentry
{
    public EntitySentryShotgun(World world)
    {
        super(world);
        setParameters();
    }

    public EntitySentryShotgun(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setParameters();
    }

    private void setParameters()
    {
        gun = (ItemGun)ModernWarfare.itemGunShotgun;
        ATTACK_DELAY = 100;
        range = 16F;
    }
}
