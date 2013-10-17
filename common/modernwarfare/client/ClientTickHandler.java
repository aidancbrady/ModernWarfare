package modernwarfare.client;

import java.util.EnumSet;

import modernwarfare.common.ModernWarfare;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class ClientTickHandler implements ITickHandler
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
			if(!mc.thePlayer.isEntityAlive()) 
			{
				ModernWarfareClient.currentGunZoom = 1.0F;
	
				if(ModernWarfareClient.reloadTimes.containsKey(mc.thePlayer))
				{
					ModernWarfareClient.reloadTimes.remove(mc.thePlayer);
				}
	
				ModernWarfareClient.currentRecoilV = 0.0D;
				ModernWarfareClient.currentRecoilH = 0.0D;
			}
	
			ModernWarfareClient.handleRecoil(mc);
			ModernWarfareClient.handleGunZoom(mc);
			ModernWarfareClient.handleBurstShots(mc.theWorld);
	
			ModernWarfareClient.handleGuns(mc);
			ModernWarfareClient.setJetPack(ModernWarfareClient.handleJetPack(mc));
	
	        ItemStack itemstack1 = mc.thePlayer.inventory.armorItemInSlot(3);
	
	        if (itemstack1 == null || itemstack1.itemID != ModernWarfare.itemNightvisionGoggles.itemID)
	        {
	            ModernWarfareClient.nightvisionEnabled = false;
	        }
	
	        ModernWarfareClient.handleUtilityZoom(mc);
		}
	}

	@Override
	public EnumSet<TickType> ticks() 
	{
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() 
	{
		return "MWClient";
	}
}
