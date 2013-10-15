package modernwarfare.client;

import java.util.List;
import java.util.Map;

import modernwarfare.common.CommonProxy;
import modernwarfare.common.EntityAtv;
import modernwarfare.common.EntityBullet;
import modernwarfare.common.EntityBulletCasing;
import modernwarfare.common.EntityBulletCasingShell;
import modernwarfare.common.EntityBulletFlame;
import modernwarfare.common.EntityBulletLaser;
import modernwarfare.common.EntityBulletRocket;
import modernwarfare.common.EntityBulletRocketLaser;
import modernwarfare.common.EntityBulletShot;
import modernwarfare.common.EntityGrapplingHook;
import modernwarfare.common.EntityParachute;
import modernwarfare.common.EntitySentry;
import modernwarfare.common.ObfuscatedNames;
import modernwarfare.common.WarTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy
{
	public static int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch(ID) 
		{
			case 0:
				return new GuiCraftingPack(WarTools.minecraft.thePlayer.inventory, world);
			case 1:
				return new GuiAtv(WarTools.minecraft.thePlayer.inventory, new EntityAtv(WarTools.minecraft.theWorld));
		}

		return null;
	}
	
	@Override
	public void loadRenderers()
	{
		Map spritesMap = (Map)WarTools.getPrivateValue(getTextureMap(1), TextureMap.class, ObfuscatedNames.TextureMap_mapRegisteredSprites);
		spritesMap.put("modernwarfare:itemLightometer", new TextureLightometer("modernwarfare:itemLightometer"));
		
        RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, new RenderBullet());
        RenderingRegistry.registerEntityRenderingHandler(EntityBulletShot.class, new RenderBulletShot());
        RenderingRegistry.registerEntityRenderingHandler(EntityBulletCasing.class, new RenderBulletCasing());
        RenderingRegistry.registerEntityRenderingHandler(EntityBulletCasingShell.class, new RenderBulletCasingShell());
        RenderingRegistry.registerEntityRenderingHandler(EntitySentry.class, new RenderSentry());
        RenderingRegistry.registerEntityRenderingHandler(EntityBulletRocket.class, new RenderBulletRocket());
        RenderingRegistry.registerEntityRenderingHandler(EntityBulletRocketLaser.class, new RenderBulletRocketLaser());
        RenderingRegistry.registerEntityRenderingHandler(EntityBulletFlame.class, new RenderBulletFlame());
        RenderingRegistry.registerEntityRenderingHandler(EntityBulletLaser.class, new RenderBulletLaser());
        RenderingRegistry.registerEntityRenderingHandler(EntityGrapplingHook.class, new RenderGrapplingHook());
        RenderingRegistry.registerEntityRenderingHandler(EntityParachute.class, new RenderParachute());
        RenderingRegistry.registerEntityRenderingHandler(EntityAtv.class, new RenderAtv());
        
        RenderingRegistry.registerBlockHandler(new BlockRenderingHandler());
	}
	
	public static TextureMap getTextureMap(int type) 
	{
		try {
			List l = (List)WarTools.getPrivateValue(Minecraft.getMinecraft().renderEngine, TextureManager.class, ObfuscatedNames.TextureManager_listTickables);

			for(Object obj : l) 
			{
				if(obj instanceof TextureMap) 
				{
					if(((TextureMap)obj).textureType == type) 
					{
						return (TextureMap) obj;
					}
				}
			}
		} catch(Exception e) {}

		return null;
	}
	
	@Override
	public void loadUtilities()
	{
		MinecraftForge.EVENT_BUS.register(new SoundHandler());
		
		TickRegistry.registerTickHandler(new RenderTickHandler(), Side.CLIENT);
		TickRegistry.registerTickHandler(new RenderTickHandler(), Side.CLIENT);
		
		KeyBindingRegistry.registerKeyBinding(new KeyBindingHandler());
	}
	
	@Override
	public void resetData()
	{
		ModernWarfareClient.minecraft_clickMouse = null;
		ModernWarfareClient.minecraft_aa = null;
		ModernWarfareClient.minecraft_ticksRan = null;
		ModernWarfareClient.jetPackReady = false;
		ModernWarfareClient.jetPackOn = false;
		ModernWarfareClient.jetPackLastSound = 0L;
		ModernWarfareClient.currentUtilityZoom = 1.0F;
		ModernWarfareClient.lastUtilityZoom = ModernWarfareClient.currentUtilityZoom;
		ModernWarfareClient.currentGunZoom = 1.0F;
		ModernWarfareClient.lastGunZoom = ModernWarfareClient.currentGunZoom;
	}
	
	@Override
	public int getArmorIndex(String string)
	{
		return RenderingRegistry.addNewArmourRendererPrefix(string);
	}
	
	@Override
	public void useZoom()
	{
		ModernWarfareClient.useZoom();
	}
}
