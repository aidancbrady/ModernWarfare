package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ItemGunRocketLauncher extends ItemGun
{
    public ItemGunRocketLauncher(int i)
    {
        super(i);
        firingSound = "modernwarfare:rocket";
        requiredBullet = ModernWarfare.itemBulletRocket;
        numBullets = 1;
        damage = 10;
        muzzleVelocity = 1.5F;
        spread = 0.0F;
        useDelay = 20;
        recoil = 0.0F;
    }

    public EntityBullet getBulletEntity(World world, Entity entity, float f, float f1, float f2, float f3, float f4)
    {
        return new EntityBulletRocket(world, entity, this, f, f1, f2, f3, f4);
    }

    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity, float f)
    {
        return null;
    }
}
