package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityBulletMp5 extends EntityBullet
{
    public EntityBulletMp5(World world)
    {
        super(world);
    }

    public EntityBulletMp5(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
    }

    public EntityBulletMp5(World world, Entity entity, ItemGun itemgun)
    {
        super(world, entity, itemgun);
    }

    @Override
    public void playServerSound(World world)
    {
        world.playSoundAtEntity(this, ((ItemGun)ModernWarfare.itemGunMp5).firingSound, ((ItemGun)ModernWarfare.itemGunMp5).soundRangeFactor, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
    }
}
