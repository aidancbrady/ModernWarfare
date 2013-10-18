package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ItemGunMinigun extends ItemGun
{
    public ItemGunMinigun(int i)
    {
        super(i);
        firingSound = "modernwarfare:minigun";
        requiredBullet = ModernWarfare.itemBulletLight;
        numBullets = 1;
        damage = 4;
        muzzleVelocity = 4F;
        spread = 2.0F;
        useDelay = 1;
        recoil = 1.0F;
    }

    @Override
    public EntityBullet getBulletEntity(World world, Entity entity)
    {
        return new EntityBulletMinigun(world, entity, this);
    }

    @Override
    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity)
    {
        return new EntityBulletCasing(world, entity);
    }
}
