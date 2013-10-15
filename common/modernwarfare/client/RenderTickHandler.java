package modernwarfare.client;

import java.util.EnumSet;

import modernwarfare.common.ItemCustomUseDelay;
import modernwarfare.common.ItemGun;
import modernwarfare.common.ItemGunFlamethrower;
import modernwarfare.common.ItemGunLaser;
import modernwarfare.common.ModernWarfare;
import modernwarfare.common.ObfuscatedNames;
import modernwarfare.common.WarTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class RenderTickHandler implements ITickHandler
{
	public Minecraft mc = Minecraft.getMinecraft();
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) 
	{
		
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) 
	{
		if(mc.theWorld != null)
		{
	        renderNightvisionOverlay(mc);
	        renderUtilityScopeOverlay(mc);
	        renderGunScopeOverlay(mc);
	        renderAmmo(mc);
	        handleArmSwing(mc);
		}
	}

	@Override
	public EnumSet<TickType> ticks() 
	{
		return EnumSet.of(TickType.RENDER);
	}

	@Override
	public String getLabel() 
	{
		return "MWRender";
	}
	
	private void renderAmmo(Minecraft minecraft)
    {
        if (ModernWarfare.showAmmoBar && minecraft.currentScreen == null && (minecraft.gameSettings.thirdPersonView > 0 || ModernWarfareClient.currentGunZoom == 1.0F))
        {
            ItemStack itemstack = minecraft.thePlayer.getCurrentEquippedItem();

            if (itemstack != null)
            {
                Item item = itemstack.getItem();

                if (item instanceof ItemGun)
                {
                    int i = WarTools.getNumberInInventory(minecraft.thePlayer.inventory, ((ItemGun)item).requiredBullet.itemID);
                    ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
                    int j = scaledresolution.getScaledWidth();
                    int k = scaledresolution.getScaledHeight();
                    int l = k - 32 - 8 - 18;
                    String s = Integer.valueOf(i > 0 ? i - 1 : 0).toString();
                    int i1 = minecraft.fontRenderer.getStringWidth(s);
                    minecraft.fontRenderer.drawString(s, (j / 2 + 91) - i1, l, 0xffffff);
                    int j1 = WarTools.getNumberInFirstStackInInventory(minecraft.thePlayer.inventory, ((ItemGun)item).requiredBullet.itemID);
                    Tessellator tessellator = Tessellator.instance;
                    tessellator.startDrawingQuads();

                    if (item instanceof ItemGunFlamethrower)
                    {
                        minecraft.renderEngine.bindTexture(new ResourceLocation("modernwarfare:gui/AmmoOil.png"));

                        for (int k1 = j1 - 1; k1 >= 0; k1--)
                        {
                            int j2 = (j / 2 + 91) - k1 - 1 - 14;
                            int i3 = l - 1;
                            tessellator.addVertexWithUV(j2, i3 + 9, -90D, 0.0D, 1.0D);
                            tessellator.addVertexWithUV(j2 + 1, i3 + 9, -90D, 1.0D, 1.0D);
                            tessellator.addVertexWithUV(j2 + 1, i3, -90D, 1.0D, 0.0D);
                            tessellator.addVertexWithUV(j2, i3, -90D, 0.0D, 0.0D);
                        }
                    }
                    else if (item instanceof ItemGunLaser)
                    {
                        minecraft.renderEngine.bindTexture(new ResourceLocation("modernwarfare:gui/AmmoRedstone.png"));

                        for (int l1 = j1 - 1; l1 >= 0; l1--)
                        {
                            int k2 = (j / 2 + 91) - l1 - 1 - 14;
                            int j3 = l - 1;
                            tessellator.addVertexWithUV(k2, j3 + 9, -90D, 0.0D, 1.0D);
                            tessellator.addVertexWithUV(k2 + 1, j3 + 9, -90D, 1.0D, 1.0D);
                            tessellator.addVertexWithUV(k2 + 1, j3, -90D, 1.0D, 0.0D);
                            tessellator.addVertexWithUV(k2, j3, -90D, 0.0D, 0.0D);
                        }
                    }
                    else
                    {
                        minecraft.renderEngine.bindTexture(new ResourceLocation("modernwarfare:gui/AmmoBullet.png"));

                        for (int i2 = j1 - 1; i2 >= 0; i2--)
                        {
                            int l2 = (j / 2 + 91) - i2 * 2 - 3 - 14;
                            int k3 = l - 1;
                            tessellator.addVertexWithUV(l2, k3 + 9, -90D, 0.0D, 1.0D);
                            tessellator.addVertexWithUV(l2 + 3, k3 + 9, -90D, 1.0D, 1.0D);
                            tessellator.addVertexWithUV(l2 + 3, k3, -90D, 1.0D, 0.0D);
                            tessellator.addVertexWithUV(l2, k3, -90D, 0.0D, 0.0D);
                        }
                    }

                    tessellator.draw();
                }
            }
        }
    }

    private void renderGunScopeOverlay(Minecraft minecraft)
    {
        if(minecraft.gameSettings.thirdPersonView == 0 && ModernWarfareClient.currentGunZoom != 1.0F && minecraft.currentScreen == null)
        {
            WarTools.renderTextureOverlay("gui/Scope.png", 1.0F);
        }
    }

    private void renderNightvisionOverlay(Minecraft minecraft)
    {
        if(ModernWarfareClient.nightvisionEnabled())
        {
            WarTools.renderTextureOverlay("gui/Nightvision.png", 1.0F);
        }
    }
    
    private void renderUtilityScopeOverlay(Minecraft minecraft)
    {
        if (minecraft.gameSettings.thirdPersonView == 0 && ModernWarfareClient.currentUtilityZoom != 1.0F && minecraft.currentScreen == null)
        {
            WarTools.renderTextureOverlay("gui/Telescope.png", 1.0F);
        }
    }
    
    private void handleArmSwing(Minecraft minecraft)
    {
        if (minecraft.thePlayer != null && minecraft.thePlayer.inventory != null)
        {
            ItemStack itemstack = minecraft.thePlayer.inventory.getCurrentItem();

            if (itemstack != null && (itemstack.getItem() instanceof ItemCustomUseDelay))
            {
                ItemCustomUseDelay itemcustomusedelay = (ItemCustomUseDelay)itemstack.getItem();

                if (itemcustomusedelay.stopArmSwing)
                {
                    WarTools.setPrivateValue(minecraft, Integer.valueOf(2), Minecraft.class, ObfuscatedNames.Minecraft_leftClickCounter);
                }
            }
        }
    }
}
