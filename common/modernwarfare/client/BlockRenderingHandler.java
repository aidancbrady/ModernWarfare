package modernwarfare.client;

import org.lwjgl.opengl.GL11;

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
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) 
	{
        try {
            if(block.blockID == ModernWarfare.blockRope.blockID)
            {
                return renderer.renderBlockLadder(block, x, y, z);
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

    public static boolean renderGrapplingHook(RenderBlocks renderblocks, Block block, int x, int y, int z, IBlockAccess iblockaccess)
    {
        int l = block.colorMultiplier(iblockaccess, x, y, z);
        float colorR = (float)(l >> 16 & 0xff) / 255F;
        float colorG = (float)(l >> 8 & 0xff) / 255F;
        float colorB = (float)(l & 0xff) / 255F;
        return renderGrapplingHook2(renderblocks, block, x, y, z, colorR, colorG, colorB, iblockaccess);
    }

    public static boolean renderGrapplingHook2(RenderBlocks renderblocks, Block block, int x, int y, int z, float colorR, float colorG, float colorB, IBlockAccess iblockaccess)
    {
        Tessellator tessellator = Tessellator.instance;
        boolean flag = false;
        float f3 = 1.0F;
        float f4 = f3 * colorR;
        float f5 = f3 * colorG;
        float f6 = f3 * colorB;

        if(block == Block.grass)
        {
            colorR = colorG = colorB = 1.0F;
        }

        tessellator.setBrightness(block.getMixedBrightnessForBlock(iblockaccess, x, y, z));
        float f7 = 1.0F;
        int metadata = iblockaccess.getBlockMetadata(x, y, z);

        if(block.shouldSideBeRendered(iblockaccess, x, y + 1, z, 1))
        {
            float f8 = block.getBlockBrightness(iblockaccess, x, y + 1, z);

            if(block.getBlockBoundsMaxY() != 1.0D && !block.blockMaterial.isLiquid())
            {
                f8 = f7;
            }

            tessellator.setColorOpaque_F(f4 * f8, f5 * f8, f6 * f8);
            Icon icon = block.getBlockTexture(iblockaccess, x, y, z, 1);

            if(renderblocks.hasOverrideBlockTexture())
            {
                icon = renderblocks.overrideBlockTexture;
            }

            renderGrapplingHook3(block, x, y, z, icon, metadata);
            flag = true;
        }

        return flag;
    }

    public static void renderGrapplingHook3(Block block, int x, int y, int z, Icon icon, int metadata)
    {
    	GL11.glPushMatrix();
    	GL11.glTranslatef(x, y, z);
        Tessellator tessellator = Tessellator.instance;
        
        double minU = icon.getMinU();
        double maxU = icon.getMaxU();
        double minV = icon.getMinV();
        double maxV = icon.getMaxV();

        double minX = x + block.getBlockBoundsMinX();
        double maxX = x + block.getBlockBoundsMaxX();
        double maxY = y + block.getBlockBoundsMaxY();
        double minZ = z + block.getBlockBoundsMinZ();
        double maxZ = z + block.getBlockBoundsMaxZ();
        
        switch(metadata)
        {
            case 2:
            	double tempMinU = minU;
            	minU = maxU;
            	maxU = tempMinU;
            	
            	double tempMinV = minV;
            	minV = maxV;
            	maxV = tempMinV;
                break;
            case 4:
            	double tempMinV1 = minV;
            	minV = maxV;
            	maxV = tempMinV1;
                break;
            case 5:
            	double tempMinU1 = minU;
            	minU = maxU;
            	maxU = tempMinU1;
                break;
        }

        tessellator.addVertexWithUV(maxX, maxY, maxZ, maxU, maxV);
        tessellator.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
        tessellator.addVertexWithUV(minX, maxY, minZ, minU, minV);
        tessellator.addVertexWithUV(minX, maxY, maxZ, minU, maxV);
        
        GL11.glPopMatrix();
    }
}
