package modernwarfare.common;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
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
		
		Set<ItemStack> toRemove = new HashSet<ItemStack>();
		
		for(Map.Entry<ItemStack, Integer> entry : ItemGun.fireDelays.entrySet())
		{
			if(entry.getKey() == null || entry.getValue() <= 0)
			{
				toRemove.add(entry.getKey());
			}
			
			ItemGun.fireDelays.put(entry.getKey(), entry.getValue()-1);
		}
		
		for(ItemStack stack : toRemove)
		{
			ItemGun.fireDelays.remove(stack);
		}
		
		for(Map.Entry<ItemStack, Integer> entry : ItemGun.fireDelays.entrySet())
		{
			System.out.println(entry.getKey().getItem().getUnlocalizedName() + " - " + entry.getValue());
		}
		
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
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel() 
	{
		return "MWCommon";
	}
}
