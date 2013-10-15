package modernwarfare.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityBulletShot extends EntityBullet
{
    public EntityBulletShot(World world)
    {
        super(world);
        setSize(0.03125F, 0.03125F);
    }

    public EntityBulletShot(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setSize(0.03125F, 0.03125F);
    }

    public EntityBulletShot(World world, Entity entity, ItemGun itemgun, float f, float f1, float f2, float f3, float f4)
    {
        super(world, entity, itemgun, f, f1, f2, f3, f4);
        setSize(0.03125F, 0.03125F);
    }

    public void playServerSound(World world)
    {
        world.playSoundAtEntity(this, ((ItemGun)ModernWarfare.itemGunShotgun).firingSound, ((ItemGun)ModernWarfare.itemGunShotgun).soundRangeFactor, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
    }
}
