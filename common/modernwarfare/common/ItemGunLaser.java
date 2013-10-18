package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class ItemGunLaser extends ItemGun
{
    public ItemGunLaser(int i)
    {
        super(i);
        firingSound = "modernwarfare:laser";
        requiredBullet = Item.redstone;
        numBullets = 1;
        damage = 10;
        muzzleVelocity = 1.5F;
        spread = 0.0F;
        useDelay = 10;
        recoil = 0.0F;
        soundRangeFactor = 2.0F;
    }

    @Override
    public EntityBullet getBulletEntity(World world, Entity entity)
    {
        return new EntityBulletLaser(world, entity, this);
    }

    @Override
    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity)
    {
        return null;
    }
}
