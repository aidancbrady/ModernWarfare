package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ItemGunRocketLauncherLaser extends ItemGun
{
    public ItemGunRocketLauncherLaser(int i)
    {
        super(i);
        firingSound = "war.rocket";
        requiredBullet = ModernWarfare.itemBulletRocketLaser;
        numBullets = 1;
        damage = 10;
        muzzleVelocity = 1.0F;
        spread = 0.0F;
        useDelay = 40;
        recoil = 0.0F;
    }

    public EntityBullet getBulletEntity(World world, Entity entity, float f, float f1, float f2, float f3, float f4)
    {
        return new EntityBulletRocketLaser(world, entity, this, f, f1, f2, f3, f4);
    }

    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity, float f)
    {
        return null;
    }
}
