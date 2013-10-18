package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ItemGunRocketLauncherLaser extends ItemGun
{
    public ItemGunRocketLauncherLaser(int i)
    {
        super(i);
        firingSound = "modernwarfare:rocket";
        requiredBullet = ModernWarfare.itemBulletRocketLaser;
        numBullets = 1;
        damage = 10;
        muzzleVelocity = 1.0F;
        spread = 0.0F;
        useDelay = 40;
        recoil = 0.0F;
    }

    @Override
    public EntityBullet getBulletEntity(World world, Entity entity)
    {
        return new EntityBulletRocketLaser(world, entity, this);
    }

    @Override
    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity)
    {
        return null;
    }
}
