package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ItemGunMp5 extends ItemGun
{
    public ItemGunMp5(int i)
    {
        super(i);
        firingSound = "modernwarfare:mp";
        requiredBullet = ModernWarfare.itemBulletLight;
        numBullets = 1;
        damage = 4;
        muzzleVelocity = 3F;
        spread = 1.0F;
        useDelay = 2;
        recoil = 1.0F;
    }

    public EntityBullet getBulletEntity(World world, Entity entity, float f, float f1, float f2, float f3, float f4)
    {
        return new EntityBulletMp5(world, entity, this, f, f1, f2, f3, f4);
    }

    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity, float f)
    {
        return new EntityBulletCasing(world, entity, f);
    }
}
