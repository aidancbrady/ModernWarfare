package modernwarfare.client;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntityWarSmokeFX extends EntityFX
{
    float smokeParticleScale;

    public EntityWarSmokeFX(World world, double d, double d1, double d2, float f, float f1, float f2, float f3)
    {
        super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
        motionX *= 0.1D;
        motionY *= 0.1D;
        motionZ *= 0.1D;
        particleRed = f1;
        particleBlue = f2;
        particleGreen = f3;
        particleScale *= 0.75F;
        particleScale *= f;
        smokeParticleScale = particleScale;
        particleMaxAge = (int)(8D / (Math.random() * 0.8D + 0.2D));
        particleMaxAge *= f;
        noClip = false;
    }

    @Override
    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
    {
        float f6 = ((particleAge + f) / particleMaxAge) * 32F;

        if (f6 < 0.0F)
        {
            f6 = 0.0F;
        }

        if (f6 > 1.0F)
        {
            f6 = 1.0F;
        }

        particleScale = smokeParticleScale * f6;
        super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
    }

    @Override
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if(particleAge++ >= particleMaxAge)
        {
            setDead();
        }

        setParticleTextureIndex(7 - (particleAge * 8) / particleMaxAge);
        motionY += 0.004D;
        moveEntity(motionX, motionY, motionZ);

        if(posY == prevPosY)
        {
            motionX *= 1.1D;
            motionZ *= 1.1D;
        }

        motionX *= 0.96D;
        motionY *= 0.96D;
        motionZ *= 0.96D;

        if(onGround)
        {
            motionX *= 0.7D;
            motionZ *= 0.7D;
        }
    }
}
