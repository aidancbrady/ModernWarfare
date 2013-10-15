package modernwarfare.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderSentry extends RenderLiving
{
    public RenderSentry()
    {
        super(new ModelSentry(), 0.5F);
    }
    
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return new ResourceLocation("render/Sentry.png");
    }
}
