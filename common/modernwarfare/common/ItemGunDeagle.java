package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ItemGunDeagle extends ItemGun
{
    public ItemGunDeagle(int i)
    {
        super(i);
        firingSound = "modernwarfare:deagle";
        requiredBullet = ModernWarfare.itemBulletMedium;
        numBullets = 1;
        damage = 10;
        muzzleVelocity = 4F;
        spread = 2.0F;
        useDelay = 8;
        recoil = 4F;
    }

    @Override
    public EntityBullet getBulletEntity(World world, Entity entity)
    {
        return new EntityBulletDeagle(world, entity, this);
    }

    @Override
    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity)
    {
        return new EntityBulletCasing(world, entity);
    }
}
