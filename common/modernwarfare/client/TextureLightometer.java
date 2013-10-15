package modernwarfare.client;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import modernwarfare.common.ModernWarfare;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.MathHelper;

public class TextureLightometer extends TextureAtlasSprite
{
    private double field_94239_h;
    private double field_94240_i;
    
    public TextureLightometer(String s)
    {
    	super(s);
    }
    
    @Override
    public void updateAnimation()
    {
        if (!this.framesTextureData.isEmpty())
        {
            Minecraft minecraft = Minecraft.getMinecraft();
            double d0 = 0.0D;

            if (minecraft.theWorld != null && minecraft.thePlayer != null)
            {
                int i = MathHelper.floor_double(minecraft.thePlayer.posX);
                int j = MathHelper.floor_double(minecraft.thePlayer.posY);
                int k = MathHelper.floor_double(minecraft.thePlayer.posZ);
                
                int l = minecraft.theWorld.getBlockLightValue(i, j - 1, k);
                float f = getRotationValue(l);
                
                d0 = (double)f;
            }

            double d1;

            for (d1 = d0 - this.field_94239_h; d1 < -0.5D; ++d1)
            {
                ;
            }

            while (d1 >= 0.5D)
            {
                --d1;
            }

            if (d1 < -1.0D)
            {
                d1 = -1.0D;
            }

            if (d1 > 1.0D)
            {
                d1 = 1.0D;
            }

            this.field_94240_i += d1 * 0.1D;
            this.field_94240_i *= 0.8D;
            this.field_94239_h += this.field_94240_i;
            int i;

            for (i = (int)((this.field_94239_h + 1.0D) * (double)this.framesTextureData.size()) % this.framesTextureData.size(); i < 0; i = (i + this.framesTextureData.size()) % this.framesTextureData.size())
            {
                ;
            }

            if (i != this.frameCounter)
            {
                this.frameCounter = i;
                TextureUtil.uploadTextureSub((int[])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
            }
        }
    }

    private float getRotationValue(int i)
    {
        float f;

        if (i > 7)
        {
            f = 0.0F;
        }
        else
        {
            f = 0.5F;
        }

        float f1 = f;
        f = 1.0F - (float)((Math.cos((double)f * Math.PI) + 1.0D) / 2D);
        f = f1 + (f - f1) / 3F;
        return f;
    }
}
