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

    public EntityBullet getBulletEntity(World world, Entity entity, float f, float f1, float f2, float f3, float f4)
    {
        return new EntityBulletDeagle(world, entity, this, f, f1, f2, f3, f4);
    }

    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity, float f)
    {
        return new EntityBulletCasing(world, entity, f);
    }
}
