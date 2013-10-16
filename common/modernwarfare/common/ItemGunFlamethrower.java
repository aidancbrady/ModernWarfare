package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ItemGunFlamethrower extends ItemGun
{
    public ItemGunFlamethrower(int i)
    {
        super(i);
        firingSound = "modernwarfare:flamethrower";
        requiredBullet = ModernWarfare.itemOil;
        numBullets = 1;
        damage = 1;
        muzzleVelocity = 0.75F;
        spread = 0.0F;
        useDelay = 1;
        recoil = 0.0F;
        soundDelay = 12;
        soundRangeFactor = 2.0F;
    }

    public EntityBullet getBulletEntity(World world, Entity entity, float f, float f1, float f2, float f3, float f4)
    {
        return new EntityBulletFlame(world, entity, this, f, f1, f2, f3, f4);
    }

    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity, float f)
    {
        return null;
    }
}
