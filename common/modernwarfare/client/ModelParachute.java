package modernwarfare.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelParachute extends ModelBase
{
    ModelRenderer top;
    ModelRenderer side1;
    ModelRenderer side2;
    ModelRenderer side3;
    ModelRenderer side4;
    ModelRenderer models[];

    public ModelParachute()
    {
        top = new ModelRenderer(this, 0, 0);
        top.addBox(-8F, 8F, -8F, 16, 1, 16);
        side1 = new ModelRenderer(this, 0, 0);
        side1.addBox(-8F, 8F, -8F, 16, 16, 1);
        side2 = new ModelRenderer(this, 0, 0);
        side2.addBox(-8F, 8F, 7F, 16, 16, 1);
        side3 = new ModelRenderer(this, 0, 0);
        side3.addBox(-8F, 8F, -8F, 1, 16, 16);
        side4 = new ModelRenderer(this, 0, 0);
        side4.addBox(7F, 8F, -8F, 1, 16, 16);
        models = (new ModelRenderer[]
                {
                    top, side1, side2, side3, side4
                });
    }

    /**
     * Sets the models various rotation angles.
     */
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        for (int i = 0; i < models.length; i++)
        {
            models[i].rotateAngleY = f3 / (180F / (float)Math.PI);
            models[i].rotateAngleX = f4 / (180F / (float)Math.PI);
        }
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);

        for (int i = 0; i < models.length; i++)
        {
            models[i].render(f5);
        }
    }
}
