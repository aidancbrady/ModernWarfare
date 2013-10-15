package modernwarfare.common;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;

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

        for(int i = 0; i < server.worldServerForDimension(0).playerEntities.size(); i++)
        {
            ModernWarfare.handleJetPack(server.worldServerForDimension(0), (EntityPlayerMP)server.worldServerForDimension(0).playerEntities.get(i));
        }

        for(int j = 0; j < server.worldServerForDimension(-1).playerEntities.size(); j++)
        {
        	ModernWarfare.handleJetPack(server.worldServerForDimension(-1), (EntityPlayerMP)server.worldServerForDimension(-1).playerEntities.get(j));
        }

        for(int k = 0; k < server.worldServerForDimension(1).playerEntities.size(); k++)
        {
        	ModernWarfare.handleJetPack(server.worldServerForDimension(1), (EntityPlayerMP)server.worldServerForDimension(1).playerEntities.get(k));
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
