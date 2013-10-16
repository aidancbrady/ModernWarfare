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

    public EntityBullet getBulletEntity(World world, Entity entity, float f, float f1, float f2, float f3, float f4)
    {
        return new EntityBulletSniper(world, entity, this, f, f1, f2, f3, f4);
    }

    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity, float f)
    {
        return new EntityBulletCasing(world, entity, f);
    }
}
