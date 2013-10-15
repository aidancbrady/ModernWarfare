package modernwarfare.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderParachute extends RenderLiving
{
    public RenderParachute()
    {
        super(new ModelParachute(), 0.0F);
    }
    
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return new ResourceLocation("render/Parachute.png");
    }
}
