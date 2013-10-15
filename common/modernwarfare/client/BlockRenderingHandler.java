package modernwarfare.client;

import modernwarfare.common.ModernWarfare;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockRenderingHandler implements ISimpleBlockRenderingHandler
{
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) 
	{
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) 
	{
        try {
            if(block.blockID == ModernWarfare.blockRope.blockID)
            {
                return renderRope(renderer, block, x, y, z, world);
            }
            else if(block.blockID == ModernWarfare.blockGrapplingHook.blockID)
            {
                return renderGrapplingHook(renderer, block, x, y, z, world);
            }
        } catch(Exception exception) {
            return false;
        }

        return false;
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return true;
	}

	@Override
	public int getRenderId() 
	{
		return ClientProxy.RENDER_ID;
	}
	
    public static boolean renderRope(RenderBlocks renderblocks, Block block, int i, int j, int k, IBlockAccess iblockaccess)
    {
        /*Tessellator tessellator = Tessellator.instance;
        int l = block.getBlockTextureFromSide(0);

        if (renderblocks.overrideBlockTexture != null)
        {
            l = renderblocks.overrideBlockTexture;
        }

        tessellator.setBrightness(block.getMixedBrightnessForBlock(iblockaccess, i, j, k));
        float f = 1.0F;
        tessellator.setColorOpaque_F(f, f, f);
        int i1 = (l & 0xf) << 4;
        int j1 = l & 0xf0;
        double d = (float)i1 / 256F;
        double d1 = ((float)i1 + 15.99F) / 256F;
        double d2 = (float)j1 / 256F;
        double d3 = ((float)j1 + 15.99F) / 256F;
        int k1 = iblockaccess.getBlockMetadata(i, j, k);
        float f1 = 0.0F;
        float f2 = 0.05F;

        if (k1 == 5)
        {
            tessellator.addVertexWithUV((float)i + f2, (float)(j + 1) + f1, (float)(k + 1) + f1, d, d2);
            tessellator.addVertexWithUV((float)i + f2, (float)(j + 0) - f1, (float)(k + 1) + f1, d, d3);
            tessellator.addVertexWithUV((float)i + f2, (float)(j + 0) - f1, (float)(k + 0) - f1, d1, d3);
            tessellator.addVertexWithUV((float)i + f2, (float)(j + 1) + f1, (float)(k + 0) - f1, d1, d2);
            tessellator.addVertexWithUV((float)i + f2, (float)(j + 0) - f1, (float)(k + 1) + f1, d1, d3);
            tessellator.addVertexWithUV((float)i + f2, (float)(j + 1) + f1, (float)(k + 1) + f1, d1, d2);
            tessellator.addVertexWithUV((float)i + f2, (float)(j + 1) + f1, (float)(k + 0) - f1, d, d2);
            tessellator.addVertexWithUV((float)i + f2, (float)(j + 0) - f1, (float)(k + 0) - f1, d, d3);
        }

        if (k1 == 4)
        {
            tessellator.addVertexWithUV((float)(i + 1) - f2, (float)(j + 1) + f1, (float)(k + 1) + f1, d, d2);
            tessellator.addVertexWithUV((float)(i + 1) - f2, (float)(j + 0) - f1, (float)(k + 1) + f1, d, d3);
            tessellator.addVertexWithUV((float)(i + 1) - f2, (float)(j + 0) - f1, (float)(k + 0) - f1, d1, d3);
            tessellator.addVertexWithUV((float)(i + 1) - f2, (float)(j + 1) + f1, (float)(k + 0) - f1, d1, d2);
            tessellator.addVertexWithUV((float)(i + 1) - f2, (float)(j + 0) - f1, (float)(k + 1) + f1, d1, d3);
            tessellator.addVertexWithUV((float)(i + 1) - f2, (float)(j + 1) + f1, (float)(k + 1) + f1, d1, d2);
            tessellator.addVertexWithUV((float)(i + 1) - f2, (float)(j + 1) + f1, (float)(k + 0) - f1, d, d2);
            tessellator.addVertexWithUV((float)(i + 1) - f2, (float)(j + 0) - f1, (float)(k + 0) - f1, d, d3);
        }

        if (k1 == 3)
        {
            tessellator.addVertexWithUV((float)(i + 1) + f1, (float)(j + 0) - f1, (float)k + f2, d1, d3);
            tessellator.addVertexWithUV((float)(i + 1) + f1, (float)(j + 1) + f1, (float)k + f2, d1, d2);
            tessellator.addVertexWithUV((float)(i + 0) - f1, (float)(j + 1) + f1, (float)k + f2, d, d2);
            tessellator.addVertexWithUV((float)(i + 0) - f1, (float)(j + 0) - f1, (float)k + f2, d, d3);
            tessellator.addVertexWithUV((float)(i + 1) + f1, (float)(j + 1) + f1, (float)k + f2, d, d2);
            tessellator.addVertexWithUV((float)(i + 1) + f1, (float)(j + 0) - f1, (float)k + f2, d, d3);
            tessellator.addVertexWithUV((float)(i + 0) - f1, (float)(j + 0) - f1, (float)k + f2, d1, d3);
            tessellator.addVertexWithUV((float)(i + 0) - f1, (float)(j + 1) + f1, (float)k + f2, d1, d2);
        }

        if (k1 == 2)
        {
            tessellator.addVertexWithUV((float)(i + 1) + f1, (float)(j + 0) - f1, (float)(k + 1) - f2, d1, d3);
            tessellator.addVertexWithUV((float)(i + 1) + f1, (float)(j + 1) + f1, (float)(k + 1) - f2, d1, d2);
            tessellator.addVertexWithUV((float)(i + 0) - f1, (float)(j + 1) + f1, (float)(k + 1) - f2, d, d2);
            tessellator.addVertexWithUV((float)(i + 0) - f1, (float)(j + 0) - f1, (float)(k + 1) - f2, d, d3);
            tessellator.addVertexWithUV((float)(i + 1) + f1, (float)(j + 1) + f1, (float)(k + 1) - f2, d, d2);
            tessellator.addVertexWithUV((float)(i + 1) + f1, (float)(j + 0) - f1, (float)(k + 1) - f2, d, d3);
            tessellator.addVertexWithUV((float)(i + 0) - f1, (float)(j + 0) - f1, (float)(k + 1) - f2, d1, d3);
            tessellator.addVertexWithUV((float)(i + 0) - f1, (float)(j + 1) + f1, (float)(k + 1) - f2, d1, d2);
        }*/

        return true;
    }

    public static boolean renderGrapplingHook(RenderBlocks renderblocks, Block block, int i, int j, int k, IBlockAccess iblockaccess)
    {
        int l = block.colorMultiplier(iblockaccess, i, j, k);
        float f = (float)(l >> 16 & 0xff) / 255F;
        float f1 = (float)(l >> 8 & 0xff) / 255F;
        float f2 = (float)(l & 0xff) / 255F;
        return renderGrapplingHook2(renderblocks, block, i, j, k, f, f1, f2, iblockaccess);
    }

    public static boolean renderGrapplingHook2(RenderBlocks renderblocks, Block block, int i, int j, int k, float f, float f1, float f2, IBlockAccess iblockaccess)
    {
        Tessellator tessellator = Tessellator.instance;
        boolean flag = false;
        float f3 = 1.0F;
        float f4 = f3 * f;
        float f5 = f3 * f1;
        float f6 = f3 * f2;

        if (block == Block.grass)
        {
            f = f1 = f2 = 1.0F;
        }

        tessellator.setBrightness(block.getMixedBrightnessForBlock(iblockaccess, i, j, k));
        float f7 = 1.0F;
        int l = iblockaccess.getBlockMetadata(i, j, k);

        if (block.shouldSideBeRendered(iblockaccess, i, j + 1, k, 1))
        {
            float f8 = block.getBlockBrightness(iblockaccess, i, j + 1, k);

            if (block.getBlockBoundsMaxY() != 1.0D && !block.blockMaterial.isLiquid())
            {
                f8 = f7;
            }

            tessellator.setColorOpaque_F(f4 * f8, f5 * f8, f6 * f8);
            Icon i1 = block.getBlockTexture(iblockaccess, i, j, k, 1);

            if(renderblocks.overrideBlockTexture != null)
            {
                i1 = renderblocks.overrideBlockTexture;
            }

            renderGrapplingHook3(block, i, j, k, i1, l);
            flag = true;
        }

        return flag;
    }

    public static void renderGrapplingHook3(Block block, double d, double d1, double d2, Icon i, int j)
    {
        /*Tessellator tessellator = Tessellator.instance;
        int k = (i & 0xf) << 4;
        int l = i & 0xf0;
        double d3 = (k + block.getBlockBoundsMinX() * 16D) / 256D;
        double d4 = ((k + block.getBlockBoundsMaxX() * 16D) - 0.01D) / 256D;
        double d5 = (l + block.getBlockBoundsMinZ() * 16D) / 256D;
        double d6 = ((l + block.getBlockBoundsMaxZ() * 16D) - 0.01D) / 256D;

        if (block.getBlockBoundsMinX() < 0.0D || block.getBlockBoundsMaxX() > 1.0D)
        {
            d3 = (k + 0.0F) / 256F;
            d4 = (k + 15.99F) / 256F;
        }

        if (block.getBlockBoundsMinZ() < 0.0D || block.getBlockBoundsMaxZ() > 1.0D)
        {
            d5 = (l + 0.0F) / 256F;
            d6 = (l + 15.99F) / 256F;
        }

        double d7 = d + block.getBlockBoundsMinX();
        double d8 = d + block.getBlockBoundsMaxX();
        double d9 = d1 + block.getBlockBoundsMaxY();
        double d10 = d2 + block.getBlockBoundsMinZ();
        double d11 = d2 + block.getBlockBoundsMaxZ();

        switch (j)
        {
            case 2:
                double d12 = d5;
                d5 = d6;
                d6 = d12;
                d12 = d3;
                d3 = d4;
                d4 = d12;
                break;

            case 4:
                double d13 = d5;
                d5 = d6;
                d6 = d13;
                break;

            case 5:
                double d14 = d3;
                d3 = d4;
                d4 = d14;
                break;
        }

        tessellator.addVertexWithUV(d8, d9, d11, d4, d6);
        tessellator.addVertexWithUV(d8, d9, d10, d4, d5);
        tessellator.addVertexWithUV(d7, d9, d10, d3, d5);
        tessellator.addVertexWithUV(d7, d9, d11, d3, d6);*/
    }
}
