package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ItemGunM4 extends ItemGun
{
    public ItemGunM4(int i)
    {
        super(i);
        firingSound = "war.m";
        requiredBullet = ModernWarfare.itemBulletLight;
        numBullets = 1;
        burstShots = 2;
        damage = 5;
        muzzleVelocity = 4F;
        spread = 0.5F;
        useDelay = 10;
        recoil = 1.0F;
    }

    public EntityBullet getBulletEntity(World world, Entity entity, float f, float f1, float f2, float f3, float f4)
    {
        return new EntityBulletM4(world, entity, this, f, f1, f2, f3, f4);
    }

    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity, float f)
    {
        return new EntityBulletCasing(world, entity, f);
    }
}
