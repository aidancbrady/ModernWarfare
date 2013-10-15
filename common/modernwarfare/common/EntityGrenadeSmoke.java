package modernwarfare.common;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityGrenadeSmoke extends EntityGrenade
{
    protected String BOUNCE_SOUND;
    private static final int SMOKE_TIME = 500;
    private static final int MAX_DIAMETER_TIME = 250;
    private static final double MAX_DIAMETER = 8D;

    public EntityGrenadeSmoke(World world)
    {
        super(world);
        BOUNCE_SOUND = "war.smokegrenadebounce";
        setEntityItemStack(new ItemStack(ModernWarfare.itemGrenadeSmoke, 1, 0));
    }

    public EntityGrenadeSmoke(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        BOUNCE_SOUND = "war.smokegrenadebounce";
        setEntityItemStack(new ItemStack(ModernWarfare.itemGrenadeSmoke, 1, 0));
    }

    public EntityGrenadeSmoke(World world, EntityLivingBase entityliving)
    {
        super(world, entityliving);
        BOUNCE_SOUND = "war.smokegrenadebounce";
        setEntityItemStack(new ItemStack(ModernWarfare.itemGrenadeSmoke, 1, 0));
    }

    protected void explode()
    {
        if (!exploded)
        {
            exploded = true;
            worldObj.playSoundAtEntity(this, "war.smokegrenade", 1.0F, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
        }

        if (fuse < -500)
        {
            isDead = true;
        }

        if (exploded)
        {
            double d = Math.min(8D, ((double)(-fuse) * 8D) / 250D);
            int i = Math.min(250, -fuse);

            for (int j = 0; j < i; j++)
            {
                worldObj.spawnParticle("largesmoke", (posX + rand.nextDouble() * d) - 0.5D * d, posY + rand.nextDouble() * d, (posZ + rand.nextDouble() * d) - 0.5D * d, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
