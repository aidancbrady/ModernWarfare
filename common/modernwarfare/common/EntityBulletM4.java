package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityBulletM4 extends EntityBullet
{
    public EntityBulletM4(World world)
    {
        super(world);
    }

    public EntityBulletM4(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
    }

    public EntityBulletM4(World world, Entity entity, ItemGun itemgun)
    {
        super(world, entity, itemgun);
    }

    @Override
    public void playServerSound(World world)
    {
        world.playSoundAtEntity(this, ((ItemGun)ModernWarfare.itemGunM4).firingSound, ((ItemGun)ModernWarfare.itemGunM4).soundRangeFactor, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
    }
}
