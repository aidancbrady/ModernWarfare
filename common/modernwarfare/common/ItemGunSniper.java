package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ItemGunSniper extends ItemGun
{
    public ItemGunSniper(int i)
    {
        super(i);
        firingSound = "modernwarfare:sniper";
        requiredBullet = ModernWarfare.itemBulletHeavy;
        numBullets = 1;
        damage = 12;
        muzzleVelocity = 8F;
        spread = 0.0F;
        useDelay = 20;
        recoil = 8F;
        soundRangeFactor = 8F;
    }

    @Override
    public EntityBullet getBulletEntity(World world, Entity entity)
    {
        return new EntityBulletSniper(world, entity, this);
    }

    @Override
    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity)
    {
        return new EntityBulletCasing(world, entity);
    }
}
