package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ItemGunM4 extends ItemGun
{
    public ItemGunM4(int i)
    {
        super(i);
        firingSound = "modernwarfare:m";
        requiredBullet = ModernWarfare.itemBulletLight;
        numBullets = 1;
        damage = 5;
        muzzleVelocity = 4F;
        spread = 0.5F;
        useDelay = 10;
        recoil = 1.0F;
    }

    @Override
    public EntityBullet getBulletEntity(World world, Entity entity)
    {
        return new EntityBulletM4(world, entity, this);
    }

    @Override
    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity)
    {
        return new EntityBulletCasing(world, entity);
    }
}
