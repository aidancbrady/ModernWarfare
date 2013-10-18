package modernwarfare.common;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class CommonTickHandler implements ITickHandler
{
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) 
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		
		if(FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			for(Object obj : server.getConfigurationManager().playerEntityList)
			{
				if(obj instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer)obj;
					
					if(!player.worldObj.isRemote && ModernWarfare.shooting.contains(player))
					{
						if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemGun)
						{
							ItemGun gun = (ItemGun)player.getCurrentEquippedItem().getItem();
							
							if(ItemGun.canFire(player.getCurrentEquippedItem()))
							{
								ItemGun.addDelay(player.getCurrentEquippedItem());
								gun.fireBullet(player.worldObj, player, player.getCurrentEquippedItem());
							}
						}
						else {
							ModernWarfare.shooting.remove(player);
						}
					}
				}
			}
		}
		
		Set<ItemStack> toRemove = new HashSet<ItemStack>();
		
        Iterator iterator = (new ArrayList(server.getConfigurationManager().playerEntityList)).iterator();

        do {
            if(!iterator.hasNext())
            {
                break;
            }

            EntityPlayerMP entityplayermp = (EntityPlayerMP)iterator.next();
            ItemStack itemstack = entityplayermp.inventory.armorInventory[2];

            if(itemstack != null && itemstack.itemID == ModernWarfare.itemScubaTank.itemID)
            {
                entityplayermp.setAir(300);
            }
        }
        while(true);
        
        ModernWarfare.handleReload();

        for(Object obj : server.getConfigurationManager().playerEntityList)
        {
        	if(obj instanceof EntityPlayerMP)
        	{
        		EntityPlayerMP player = (EntityPlayerMP)obj;
        		ModernWarfare.handleJetPack(player.worldObj, player);
        	}
        }
	}

	@Override
	public EnumSet<TickType> ticks() 
	{
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel() 
	{
		return "MWCommon";
	}
}
