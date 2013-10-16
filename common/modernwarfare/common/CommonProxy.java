package modernwarfare.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
	public void loadConfiguration()
	{
		ModernWarfare.configuration.load();
		ModernWarfare.ropeID = ModernWarfare.configuration.getBlock("Rope", 3450).getInt();
		ModernWarfare.grapplingHookID = ModernWarfare.configuration.getBlock("GrapplingHook", 3451).getInt();
		
		ModernWarfare.guns = ModernWarfare.configuration.get(Configuration.CATEGORY_GENERAL, "guns", true).getBoolean(true);
		ModernWarfare.bulletsDestroyGlass = ModernWarfare.configuration.get(Configuration.CATEGORY_GENERAL, "bulletsDestroyGlass", true).getBoolean(true);
		ModernWarfare.showAmmoBar = ModernWarfare.configuration.get(Configuration.CATEGORY_GENERAL, "showAmmoBar", true).getBoolean(true);
		ModernWarfare.grenades = ModernWarfare.configuration.get(Configuration.CATEGORY_GENERAL, "grenades", true).getBoolean(true);
		ModernWarfare.explosionsDestroyBlocks = ModernWarfare.configuration.get(Configuration.CATEGORY_GENERAL, "explosionsDestroyBlocks", true).getBoolean(true);
		ModernWarfare.laserSetsFireToBlocks = ModernWarfare.configuration.get(Configuration.CATEGORY_GENERAL, "laserSetsFireToBlocks", true).getBoolean(true);
		ModernWarfare.lighter = ModernWarfare.configuration.get(Configuration.CATEGORY_GENERAL, "lighter", true).getBoolean(true);
		ModernWarfare.sentries = ModernWarfare.configuration.get(Configuration.CATEGORY_GENERAL, "sentries", true).getBoolean(true);
		ModernWarfare.sentriesKillAnimals = ModernWarfare.configuration.get(Configuration.CATEGORY_GENERAL, "sentriesKillAnimals", true).getBoolean(true);
		ModernWarfare.atv = ModernWarfare.configuration.get(Configuration.CATEGORY_GENERAL, "atv", true).getBoolean(true);
		ModernWarfare.jetPack = ModernWarfare.configuration.get(Configuration.CATEGORY_GENERAL, "jetPack", true).getBoolean(true);
		ModernWarfare.ammoRestrictions = ModernWarfare.configuration.get(Configuration.CATEGORY_GENERAL, "ammoRestrictions", true).getBoolean(true);
		ModernWarfare.ammoCasings = ModernWarfare.configuration.get(Configuration.CATEGORY_GENERAL, "ammoCasings", true).getBoolean(true);
		ModernWarfare.monsterSpawns = ModernWarfare.configuration.get(Configuration.CATEGORY_GENERAL, "monsterSpawns", 70).getInt();
		ModernWarfare.configuration.save();
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		switch(ID) 
		{
			case 0:
				return new ContainerCraftingPack(player.inventory, world);
			case 1:
				return new ContainerAtv(player.inventory, (EntityAtv)player.ridingEntity);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
	
	public void initSounds() {}
	
	public void loadRenderers() {}
	
	public void loadUtilities() {}
	
	public void resetData() {}
	
	public void useZoom() {}
	
	public Object reloadLightometerIcon() 
	{
		return null;
	}
	
	public int getArmorIndex(String string)
	{
		return 0;
	}
}
