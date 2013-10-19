package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityBulletShotgun extends EntityBullet
{
    public EntityBulletShotgun(World world)
    {
        super(world);
        setSize(0.03125F, 0.03125F);
    }

    public EntityBulletShotgun(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setSize(0.03125F, 0.03125F);
    }

    public EntityBulletShotgun(World world, Entity entity, ItemGun itemgun)
    {
        super(world, entity, itemgun);
        setSize(0.03125F, 0.03125F);
    }

    @Override
    public void playServerSound(World world)
    {
        world.playSoundAtEntity(this, ((ItemGun)ModernWarfare.itemGunShotgun).firingSound, ((ItemGun)ModernWarfare.itemGunShotgun).soundRangeFactor, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
    }
}
