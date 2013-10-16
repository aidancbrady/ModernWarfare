package modernwarfare.client;

import modernwarfare.common.EntityAtv;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderAtv extends Render
{
    protected ModelAtv model;

    public RenderAtv()
    {
        shadowSize = 0.5F;
        model = new ModelAtv();
    }

    public void render(EntityAtv entityatv, double d, double d1, double d2, float f, float f1)
    {
        GL11.glPushMatrix();
        float f2 = entityatv.prevRotationPitch + (entityatv.rotationPitch - entityatv.prevRotationPitch) * f1;
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glRotatef(180F - f, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-f2, 0.0F, 0.0F, 1.0F);
        GL11.glScalef(-1F, -1F, 1.0F);
        GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(1.5F, 1.5F, 1.5F);
        model.render(0.0F, 0.0F, 0.0F, 0.0F, entityatv.getTurnSpeedForRender(), 0.0625F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glPushMatrix();
        GL11.glScalef(0.6666667F, 0.6666667F, 0.6666667F);
        GL11.glScalef(0.7F, 0.7F, 0.7F);
        GL11.glRotatef(225F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-0.125F, 1.125F, 0.625F);
        Tessellator tessellator = Tessellator.instance;

        for(int i = -1; i <= 1; i += 2)
        {
            ItemStack itemstack = null;

            if(i == -1)
            {
                itemstack = entityatv.gunA;
            }
            else {
                itemstack = entityatv.gunB;
                GL11.glTranslatef(0.0F, 0.0F, -1.25F);
            }

            /*if(itemstack != null)
            {
                int j = itemstack.getIconIndex();
                loadTexture("/war/items.png");
                float f3 = (float)((j % 16) * 16 + 0) / 256F;
                float f4 = (float)((j % 16) * 16 + 16) / 256F;
                float f5 = (float)((j / 16) * 16 + 0) / 256F;
                float f6 = (float)((j / 16) * 16 + 16) / 256F;
                float f7 = 1.0F;
                float f8 = 0.5F;
                float f9 = 0.25F;
                GL11.glPushMatrix();
                tessellator.startDrawingQuads();
                tessellator.setColorOpaque_F(f1, f1, f1);
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                tessellator.addVertexWithUV(0.0F - f8, 0.0F - f9, 0.0D, f3, f6);
                tessellator.addVertexWithUV(f7 - f8, 0.0F - f9, 0.0D, f4, f6);
                tessellator.addVertexWithUV(f7 - f8, 1.0F - f9, 0.0D, f4, f5);
                tessellator.addVertexWithUV(0.0F - f8, 1.0F - f9, 0.0D, f3, f5);
                tessellator.addVertexWithUV(0.0F - f8, 1.0F - f9, 0.0D, f3, f5);
                tessellator.addVertexWithUV(f7 - f8, 1.0F - f9, 0.0D, f4, f5);
                tessellator.addVertexWithUV(f7 - f8, 0.0F - f9, 0.0D, f4, f6);
                tessellator.addVertexWithUV(0.0F - f8, 0.0F - f9, 0.0D, f3, f6);
                tessellator.draw();
                GL11.glPopMatrix();
            }*/
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
    
    @Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        render((EntityAtv)entity, d, d1, d2, f, f1);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return new ResourceLocation("modernwarfare:render/Atv.png");
    }
}
