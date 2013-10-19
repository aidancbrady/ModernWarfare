package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ItemGunShotgun extends ItemGun
{
    public ItemGunShotgun(int i)
    {
        super(i);
        firingSound = "modernwarfare:shotgun";
        requiredBullet = ModernWarfare.itemBulletShell;
        numBullets = 12;
        damage = 2;
        muzzleVelocity = 3F;
        spread = 8F;
        useDelay = 16;
        recoil = 8F;
    }

    @Override
    public EntityBullet getBulletEntity(World world, Entity entity)
    {
        return new EntityBulletShotgun(world, entity, this);
    }

    @Override
    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity)
    {
        return new EntityBulletCasingShell(world, entity);
    }
}
