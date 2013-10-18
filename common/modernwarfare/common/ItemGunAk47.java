package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ItemGunAk47 extends ItemGun
{
    public ItemGunAk47(int i)
    {
        super(i);
        firingSound = "modernwarfare:ak";
        requiredBullet = ModernWarfare.itemBulletLight;
        numBullets = 1;
        damage = 5;
        muzzleVelocity = 4F;
        spread = 0.5F;
        useDelay = 2;
        recoil = 2.0F;
    }

    @Override
    public EntityBullet getBulletEntity(World world, Entity entity)
    {
        return new EntityBulletAk47(world, entity, this);
    }

    @Override
    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity)
    {
        return new EntityBulletCasing(world, entity);
    }
}
