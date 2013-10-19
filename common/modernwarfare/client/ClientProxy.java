package modernwarfare.client;

import java.util.List;
import java.util.Random;

import modernwarfare.common.CommonProxy;
import modernwarfare.common.EntityAtv;
import modernwarfare.common.EntityBullet;
import modernwarfare.common.EntityBulletCasing;
import modernwarfare.common.EntityBulletCasingShell;
import modernwarfare.common.EntityBulletFlame;
import modernwarfare.common.EntityBulletLaser;
import modernwarfare.common.EntityBulletRocket;
import modernwarfare.common.EntityBulletRocketLaser;
import modernwarfare.common.EntityBulletShotgun;
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
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy
{
	public static int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
	public static Minecraft mc = Minecraft.getMinecraft();
	public static Random rand = new Random();
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch(ID) 
		{
			case 0:
				return new GuiCraftingPack(WarTools.minecraft.thePlayer.inventory, world);
			case 1:
				return new GuiAtv(WarTools.minecraft.thePlayer.inventory, (EntityAtv)WarTools.minecraft.theWorld.getEntityByID(x));
		}

		return null;
	}
	
	@Override
	public void loadRenderers()
	{
        RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, new RenderBullet());
        RenderingRegistry.registerEntityRenderingHandler(EntityBulletShotgun.class, new RenderBulletShotgun());
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
			List l = (List)WarTools.getPrivateValue(mc.renderEngine, TextureManager.class, ObfuscatedNames.TextureManager_listTickables);

			for(Object obj : l) 
			{
				if(obj instanceof TextureMap) 
				{
					if(((TextureMap)obj).textureType == type) 
					{
						return (TextureMap)obj;
					}
				}
			}
		} catch(Exception e) {}

		return null;
	}
	
	@Override
	public void loadUtilities()
	{		
		TickRegistry.registerTickHandler(new RenderTickHandler(), Side.CLIENT);
		TickRegistry.registerTickHandler(new ClientTickHandler(), Side.CLIENT);
		
		KeyBindingRegistry.registerKeyBinding(new KeyBindingHandler());
	}
	
	@Override
	public void initSounds()
	{
		mc.sndManager.addSound("modernwarfare:ak.ogg");
		mc.sndManager.addSound("modernwarfare:deagle.ogg");
		mc.sndManager.addSound("modernwarfare:flamethrower.ogg");
		mc.sndManager.addSound("modernwarfare:grenadebounce.ogg");
		mc.sndManager.addSound("modernwarfare:grunt.ogg");
		mc.sndManager.addSound("modernwarfare:gunempty.ogg");
		mc.sndManager.addSound("modernwarfare:impact.ogg");
		mc.sndManager.addSound("modernwarfare:jetpack.ogg");
		mc.sndManager.addSound("modernwarfare:laser.ogg");
		mc.sndManager.addSound("modernwarfare:m.ogg");
		mc.sndManager.addSound("modernwarfare:mechhurt.ogg");
		mc.sndManager.addSound("modernwarfare:minigun.ogg");
		mc.sndManager.addSound("modernwarfare:mp.ogg");
		mc.sndManager.addSound("modernwarfare:parachute.ogg");
		mc.sndManager.addSound("modernwarfare:reload.ogg");
		mc.sndManager.addSound("modernwarfare:rocket.ogg");
		mc.sndManager.addSound("modernwarfare:sg.ogg");
		mc.sndManager.addSound("modernwarfare:shotgun.ogg");
		mc.sndManager.addSound("modernwarfare:smokegrenade.ogg");
		mc.sndManager.addSound("modernwarfare:smokegrenadebounce.ogg");
		mc.sndManager.addSound("modernwarfare:sniper.ogg");
		mc.sndManager.addSound("modernwarfare:stungrenade.ogg");
		mc.sndManager.addSound("modernwarfare:stungrenadebounce.ogg");
		mc.sndManager.addSound("modernwarfare:wrench.ogg");
	}
	
	@Override
	public void resetData()
	{
		ModernWarfareClient.jetPackReady = false;
		ModernWarfareClient.jetPackOn = false;
		ModernWarfareClient.currentUtilityZoom = 1.0F;
		ModernWarfareClient.lastUtilityZoom = ModernWarfareClient.currentUtilityZoom;
		ModernWarfareClient.currentGunZoom = 1.0F;
		ModernWarfareClient.lastGunZoom = ModernWarfareClient.currentGunZoom;
	}
	
	@Override
	public TextureLightometer getLightometerIcon()
	{
		getTextureMap(1).setTextureEntry("modernwarfare:itemLightometer", new TextureLightometer("modernwarfare:itemLightometer"));
		return (TextureLightometer)getTextureMap(1).getTextureExtry("modernwarfare:itemLightometer");
	}
	
	@Override
	public int getArmorIndex(String string)
	{
		return RenderingRegistry.addNewArmourRendererPrefix(string);
	}
	
	@Override
	public void doRecoil(double v, double h)
	{
        ModernWarfareClient.currentRecoilV += v;
        ModernWarfareClient.currentRecoilH += h;
	}
	
	@Override
	public EntityPlayer getClientPlayer()
	{
		return mc.thePlayer;
	}
	
	@Override
	public void doParticles(double x, double y, double z, double width, double height)
	{
        double xPos = (x + rand.nextDouble() * width * 2D) - width;
        double yPos = (y + rand.nextDouble() * height * 2D) - height;
        double zPos = (z + rand.nextDouble() * width * 2D) - width;
        mc.effectRenderer.addEffect(new EntityWarSmokeFX(mc.theWorld, xPos, yPos, zPos, 2.5F, 1.0F, 1.0F, 1.0F));
	}
	
	@Override
	public void doExplosionFX(double x, double y, double z)
	{
		for(int i = 0; i < 32; i++) 
		{
			mc.theWorld.spawnParticle("explode", x, y, z, rand.nextDouble() - 0.5D, rand.nextDouble() - 0.5D, rand.nextDouble() - 0.5D);
			mc.theWorld.spawnParticle("smoke", x, y, z, rand.nextDouble() - 0.5D, rand.nextDouble() - 0.5D, rand.nextDouble() - 0.5D);
		}
	}
	
	@Override
	public void useZoom()
	{
		ModernWarfareClient.useZoom();
	}
}
