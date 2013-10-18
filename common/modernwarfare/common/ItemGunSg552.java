package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ItemGunSg552 extends ItemGun
{
    public ItemGunSg552(int i)
    {
        super(i);
        firingSound = "modernwarfare:sg";
        requiredBullet = ModernWarfare.itemBulletMedium;
        numBullets = 1;
        damage = 8;
        muzzleVelocity = 6F;
        spread = 0.25F;
        useDelay = 5;
        recoil = 3F;
    }

    @Override
    public EntityBullet getBulletEntity(World world, Entity entity)
    {
        return new EntityBulletSg552(world, entity, this);
    }

    @Override
    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity)
    {
        return new EntityBulletCasing(world, entity);
    }
}
