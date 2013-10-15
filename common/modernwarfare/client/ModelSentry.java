package modernwarfare.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSentry extends ModelBase
{
    ModelRenderer head;
    ModelRenderer fatBarrel;
    ModelRenderer thinBarrel;
    ModelRenderer stand;
    ModelRenderer leg1;
    ModelRenderer leg2;
    ModelRenderer leg3;
    ModelRenderer leg4;
    ModelRenderer foot1;
    ModelRenderer foot2;
    ModelRenderer foot3;
    ModelRenderer foot4;
    ModelRenderer headModels[];
    ModelRenderer bodyModels[];

    public ModelSentry()
    {
        head = new ModelRenderer(this, 0, 0);
        head.addBox(-3F, -3F, -1F, 6, 6, 6);
        fatBarrel = new ModelRenderer(this, 24, 0);
        fatBarrel.addBox(-2F, -2F, -4F, 4, 4, 3);
        thinBarrel = new ModelRenderer(this, 24, 7);
        thinBarrel.addBox(-1F, -1F, -9F, 2, 2, 5);
        stand = new ModelRenderer(this, 46, 0);
        stand.addBox(-1F, 3F, -1F, 2, 10, 2);
        leg1 = new ModelRenderer(this, 38, 0);
        leg1.addBox(-1F, 0.0F, -1F, 2, 14, 2);
        leg1.setRotationPoint(0.0F, 13F, 0.0F);
        leg1.rotateAngleX = 0.6632251F;
        leg1.rotateAngleY = ((float)Math.PI / 4F);
        leg2 = new ModelRenderer(this, 38, 0);
        leg2.addBox(-1F, 0.0F, -1F, 2, 14, 2);
        leg2.setRotationPoint(0.0F, 13F, 0.0F);
        leg2.rotateAngleX = 0.6632251F;
        leg2.rotateAngleY = 2.356194F;
        leg3 = new ModelRenderer(this, 38, 0);
        leg3.addBox(-1F, 0.0F, -1F, 2, 14, 2);
        leg3.setRotationPoint(0.0F, 13F, 0.0F);
        leg3.rotateAngleX = 0.6632251F;
        leg3.rotateAngleY = 3.926991F;
        leg4 = new ModelRenderer(this, 38, 0);
        leg4.addBox(-1F, 0.0F, -1F, 2, 14, 2);
        leg4.setRotationPoint(0.0F, 13F, 0.0F);
        leg4.rotateAngleX = 0.6632251F;
        leg4.rotateAngleY = 5.497787F;
        foot1 = new ModelRenderer(this, 0, 12);
        foot1.addBox(-8F, 23F, -8F, 5, 1, 5);
        foot2 = new ModelRenderer(this, 0, 12);
        foot2.addBox(-8F, 23F, -8F, 5, 1, 5);
        foot2.rotateAngleY = ((float)Math.PI / 2F);
        foot3 = new ModelRenderer(this, 0, 12);
        foot3.addBox(-8F, 23F, -8F, 5, 1, 5);
        foot3.rotateAngleY = (float)Math.PI;
        foot4 = new ModelRenderer(this, 0, 12);
        foot4.addBox(-8F, 23F, -8F, 5, 1, 5);
        foot4.rotateAngleY = ((float)Math.PI * 3F / 2F);
        headModels = (new ModelRenderer[]
                {
                    head, fatBarrel, thinBarrel
                });
        bodyModels = (new ModelRenderer[]
                {
                    stand, leg1, leg2, leg3, leg4, foot1, foot2, foot3, foot4
                });
    }

    /**
     * Sets the models various rotation angles.
     */
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        for (int i = 0; i < headModels.length; i++)
        {
            headModels[i].rotateAngleY = f3 / (180F / (float)Math.PI);
            headModels[i].rotateAngleX = f4 / (180F / (float)Math.PI);
        }
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);

        for (int i = 0; i < headModels.length; i++)
        {
            headModels[i].render(f5);
        }

        for (int j = 0; j < bodyModels.length; j++)
        {
            bodyModels[j].render(f5);
        }
    }
}
