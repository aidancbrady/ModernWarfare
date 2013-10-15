package modernwarfare.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelAtv extends ModelBase
{
    public ModelRenderer body;
    public ModelRenderer front;
    public ModelRenderer wheels[];

    public ModelAtv()
    {
        body = new ModelRenderer(this, 0, 0);
        body.addBox(0.0F, 0.0F, 0.0F, 16, 6, 10);
        body.setRotationPoint(-8F, -5F, -5F);
        front = new ModelRenderer(this, 0, 16);
        front.addBox(0.0F, 0.0F, 0.0F, 1, 1, 10);
        front.setRotationPoint(7F, -6F, -5F);
        wheels = new ModelRenderer[4];
        wheels[0] = new ModelRenderer(this, 22, 16);
        wheels[0].addBox(-2F, -2F, -1F, 4, 4, 2);
        wheels[0].setRotationPoint(5F, 1.0F, 5F);
        wheels[1] = new ModelRenderer(this, 22, 16);
        wheels[1].addBox(-2F, -2F, -1F, 4, 4, 2);
        wheels[1].setRotationPoint(5F, 1.0F, -5F);
        wheels[2] = new ModelRenderer(this, 22, 16);
        wheels[2].addBox(-2F, -2F, -1F, 4, 4, 2);
        wheels[2].setRotationPoint(-5F, 1.0F, 5F);
        wheels[3] = new ModelRenderer(this, 22, 16);
        wheels[3].addBox(-2F, -2F, -1F, 4, 4, 2);
        wheels[3].setRotationPoint(-5F, 1.0F, -5F);
    }

    public void render(float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);
        body.render(f5);
        front.render(f5);

        for (int i = 0; i < wheels.length; i++)
        {
            wheels[i].render(f5);
        }
    }

    /**
     * Sets the models various rotation angles.
     */
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        wheels[0].rotateAngleY = f4 / (180F / (float)Math.PI);
        wheels[1].rotateAngleY = f4 / (180F / (float)Math.PI);
        wheels[2].rotateAngleY = -f4 / (180F / (float)Math.PI);
        wheels[3].rotateAngleY = -f4 / (180F / (float)Math.PI);
    }
}
