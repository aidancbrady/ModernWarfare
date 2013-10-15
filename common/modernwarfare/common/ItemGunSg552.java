package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ItemGunSg552 extends ItemGun
{
    public ItemGunSg552(int i)
    {
        super(i);
        firingSound = "war.sg";
        requiredBullet = ModernWarfare.itemBulletMedium;
        numBullets = 1;
        damage = 8;
        muzzleVelocity = 6F;
        spread = 0.25F;
        useDelay = 5;
        recoil = 3F;
    }

    public EntityBullet getBulletEntity(World world, Entity entity, float f, float f1, float f2, float f3, float f4)
    {
        return new EntityBulletSg552(world, entity, this, f, f1, f2, f3, f4);
    }

    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity, float f)
    {
        return new EntityBulletCasing(world, entity, f);
    }
}
