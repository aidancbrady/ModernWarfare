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

    @Override
    public EntityBullet getBulletEntity(World world, Entity entity)
    {
        return new EntityBulletRocket(world, entity, this);
    }

    @Override
    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity)
    {
        return null;
    }
}
