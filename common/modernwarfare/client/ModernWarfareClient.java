package modernwarfare.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import modernwarfare.common.EntityAtv;
import modernwarfare.common.ModernWarfare;
import modernwarfare.common.ObfuscatedNames;
import modernwarfare.common.WarTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.network.PacketDispatcher;

public class ModernWarfareClient extends ModernWarfare
{
    public static float currentUtilityZoom;
    public static float currentGunZoom;
    
    public static int currentUtilityZoomIndex = 0;
    
    public static float lastUtilityZoom;
    public static float lastGunZoom;
    
    public static int lastUtilityZoomSlot = 0;
    public static int lastGunZoomSlot = 0;
    
    public static int currentZoomDelay = 0;
    public static int ZOOM_DELAY = 10;
    
    public static boolean zoomEnabled = false;
    public static boolean sniperZoomedIn = false;
    
    public static final float DEFAULT_GUN_ZOOM = 1F;
    public static final float MAX_ZOOM_SNIPER = 0.125F;
    public static final float MAX_ZOOM_SG552 = 0.25F;
    public static final float GUN_ZOOM_INCREMENT = 0.075F;
    
    public static boolean nightvisionEnabled = false;
    
    public static boolean jetPackReady;
    public static boolean jetPackOn;
    public static long jetPackLastSound;
    
    public static final int JET_PACK_SOUND_INTERVAL = 15;
    
    private static final float DEFAULT_ZOOM = 1F;
    private static final float ZOOM_INCREMENT = 0.0625F;
    private static final float MAX_ZOOMS[] =
    {
        1.0F, 0.5F, 0.25F, 0.125F, 0.0625F
    };
    
    public static final double RECOIL_FIX_FACTOR = 0.1D;
    public static final double MIN_RECOIL_FIX = 0.5D;
    
    public static double currentRecoilV = 0.0D;
    public static double currentRecoilH = 0.0D;
    
    public static void spawnJetPackParticle(Minecraft minecraft, String s)
    {
        spawnJetPackParticle(minecraft, s, 0.175D, -0.9D, -0.3D);
        spawnJetPackParticle(minecraft, s, -0.175D, -0.9D, -0.3D);
    }

    public static void spawnJetPackParticle(Minecraft minecraft, String s, double d, double d1, double d2)
    {
        float f = -(minecraft.thePlayer.renderYawOffset * 0.01745329F);
        double d3 = d2 * (double)MathHelper.sin(f) + d * (double)MathHelper.cos(f);
        double d4 = d2 * (double)MathHelper.cos(f) - d * (double)MathHelper.sin(f);
        minecraft.theWorld.spawnParticle(s, minecraft.thePlayer.posX + d3, minecraft.thePlayer.posY + d1, minecraft.thePlayer.posZ + d4, WarTools.random.nextDouble() * 0.10000000000000001D - 0.050000000000000003D, ((WarTools.random.nextDouble() * 0.10000000000000001D - 0.050000000000000003D) + minecraft.thePlayer.motionY) - 0.5D, WarTools.random.nextDouble() * 0.10000000000000001D - 0.050000000000000003D);
    }
    
    public static void setJetPack(boolean flag)
    {
        if(flag != jetPackOn)
        {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

            try
            {
                dataoutputstream.writeInt(flag ? 7 : 8);
            } catch (IOException ioexception) {
                System.out.println("[ModernWarfare] An error occured while writing packet data.");
                ioexception.printStackTrace();
            }

            Packet250CustomPayload packet250custompayload = new Packet250CustomPayload();
            packet250custompayload.channel = "MDWF";
            packet250custompayload.data = bytearrayoutputstream.toByteArray();
            packet250custompayload.length = packet250custompayload.data.length;
            PacketDispatcher.sendPacketToServer(packet250custompayload);
            System.out.println("[ModernWarfare] Sent '" + (flag ? 7 : 8) + "' packet to server");
            
            jetPackOn = flag;
        }
    }
    
    public static boolean useJetPackFuel(Minecraft minecraft)
    {
        if(WarTools.useItemInInventory(minecraft.thePlayer, itemOil.itemID) > 0)
        {
            setJetPack(true);
            return true;
        }
        else {
            return false;
        }
    }
    
	public static boolean handleJetPack(Minecraft minecraft) 
	{
		ItemStack itemstack = minecraft.thePlayer.inventory.armorItemInSlot(2);

		if(minecraft.currentScreen == null && itemstack != null && itemstack.itemID == ModernWarfare.itemJetPack.itemID) 
		{
			if(WarTools.onGroundOrInWater(minecraft.theWorld, minecraft.thePlayer) || minecraft.thePlayer.ridingEntity != null)
			{
				jetPackReady = false;
			} 
			else if(!Keyboard.isKeyDown(minecraft.gameSettings.keyBindJump.keyCode))
			{
				jetPackReady = true;
			} 
			else if(jetPackReady)
			{
				setJetPack(true);
				
	            minecraft.thePlayer.motionY = Math.min(minecraft.thePlayer.motionY + 0.06D + 0.06D, 0.3D);
	            minecraft.thePlayer.fallDistance = 0.0F;
				
				for(int i = 0; i < 16; i++) 
				{
					spawnJetPackParticle(minecraft, "flame");
					spawnJetPackParticle(minecraft, "smoke");
				}

				if(minecraft.theWorld.getWorldTime() - jetPackLastSound < 0L) 
				{
					jetPackLastSound = minecraft.theWorld.getWorldTime() - 15L;
				}

				if(jetPackLastSound == 0L || minecraft.theWorld.getWorldTime() - jetPackLastSound > 15L) 
				{
					minecraft.theWorld.playSoundAtEntity(minecraft.thePlayer, "modernwarfare:jetpack", 0.25F, 1.0F / (WarTools.random.nextFloat() * 0.1F + 0.95F));
					jetPackLastSound = minecraft.theWorld.getWorldTime();
				}

				return true;
			}
		}

		return false;
	}
	
    public static void handleUtilityZoom(Minecraft minecraft)
    {
        ItemStack itemstack = minecraft.thePlayer.inventory.getCurrentItem();

        if (itemstack == null || itemstack.getItem() != itemTelescope || minecraft.thePlayer.inventory.currentItem != lastUtilityZoomSlot || minecraft.gameSettings.thirdPersonView > 0 || minecraft.currentScreen != null)
        {
            currentUtilityZoomIndex = 0;
        }

        lastUtilityZoomSlot = minecraft.thePlayer.inventory.currentItem;
        float f = MAX_ZOOMS[currentUtilityZoomIndex];

        if(currentUtilityZoom > f)
        {
            currentUtilityZoom = Math.max(f, currentUtilityZoom - 0.0625F);
        }
        else if(currentUtilityZoom < f)
        {
            currentUtilityZoom = Math.min(f, currentUtilityZoom + 0.0625F);
        }

        if(currentUtilityZoom != lastUtilityZoom)
        {
            try {
            	WarTools.setPrivateValue(minecraft.entityRenderer, Float.valueOf(1.0F / currentUtilityZoom), EntityRenderer.class, ObfuscatedNames.EntityRenderer_cameraZoom);
            } catch (Exception exception1) {
            	return;
            }
        }

        lastUtilityZoom = currentUtilityZoom;
    }
	
    public static void handleNightvisionKey()
    {
        nightvisionEnabled = !nightvisionEnabled;
    }
    
    public static void handleGunZoom(Minecraft minecraft)
    {
        boolean flag = false;
        float f = 1.0F;
        ItemStack itemstack = minecraft.thePlayer.inventory.getCurrentItem();

        if(minecraft.gameSettings != null && minecraft.thePlayer != null && minecraft.thePlayer.inventory != null)
        {
            if(itemstack == null || itemstack.getItem() != itemGunSniper && itemstack.getItem() != itemGunSg552 || minecraft.thePlayer.inventory.currentItem != lastGunZoomSlot || minecraft.gameSettings.thirdPersonView > 0 || minecraft.currentScreen != null)
            {
                zoomEnabled = false;
            }

            lastGunZoomSlot = minecraft.thePlayer.inventory.currentItem;

            if(zoomEnabled && minecraft.gameSettings.thirdPersonView == 0)
            {
                if(itemstack.getItem() == itemGunSniper)
                {
                    f = 0.125F;
                }
                else {
                    f = 0.25F;
                }

                if(currentGunZoom > f)
                {
                    flag = true;
                }
            }
        }

        if(zoomEnabled && flag)
        {
            currentGunZoom = Math.max(f, currentGunZoom - 0.075F);
        }
        else if(!zoomEnabled && currentGunZoom < 1.0F)
        {
            currentGunZoom = Math.min(1.0F, currentGunZoom + 0.075F);
        }

        if(currentGunZoom == 0.125F && !sniperZoomedIn && itemstack != null && itemstack.getItem() == itemGunSniper)
        {
            sniperZoomedIn = true;
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

            try
            {
                dataoutputstream.writeInt(1);
            } catch (IOException ioexception) {
                System.out.println("[ModernWarfare] An error occured while writing packet data.");
                ioexception.printStackTrace();
            }

            Packet250CustomPayload packet250custompayload = new Packet250CustomPayload();
            packet250custompayload.channel = "MDWF";
            packet250custompayload.data = bytearrayoutputstream.toByteArray();
            packet250custompayload.length = packet250custompayload.data.length;
            PacketDispatcher.sendPacketToServer(packet250custompayload);
            System.out.println("[ModernWarfare] Sent '1' packet to server");
        }
        else if(currentGunZoom != 0.125F && sniperZoomedIn)
        {
            sniperZoomedIn = false;
            ByteArrayOutputStream bytearrayoutputstream1 = new ByteArrayOutputStream();
            DataOutputStream dataoutputstream1 = new DataOutputStream(bytearrayoutputstream1);

            try
            {
                dataoutputstream1.writeInt(0);
            } catch (IOException ioexception1) {
                System.out.println("[ModernWarfare] An error occured while writing packet data.");
                ioexception1.printStackTrace();
            }

            Packet250CustomPayload packet250custompayload1 = new Packet250CustomPayload();
            packet250custompayload1.channel = "MDWF";
            packet250custompayload1.data = bytearrayoutputstream1.toByteArray();
            packet250custompayload1.length = packet250custompayload1.data.length;
            PacketDispatcher.sendPacketToServer(packet250custompayload1);
            System.out.println("[ModernWarfare] Sent '0' packet to server");
        }

        if(currentGunZoom != lastGunZoom)
        {
            try {
            	WarTools.setPrivateValue(minecraft.entityRenderer, Float.valueOf(1.0F / currentGunZoom), EntityRenderer.class, ObfuscatedNames.EntityRenderer_cameraZoom);
            } catch (Exception exception1) {
            	return;
            }
        }

        lastGunZoom = currentGunZoom;
    }
    
    public static boolean nightvisionEnabled()
    {
        ItemStack itemstack = WarTools.minecraft.thePlayer.inventory.armorItemInSlot(3);
        return itemstack != null && itemstack.itemID == itemNightvisionGoggles.itemID && WarTools.minecraft.gameSettings.thirdPersonView == 0 && WarTools.minecraft.currentScreen == null && nightvisionEnabled;
    }
    
    public static void useZoom()
    {
    	if(currentZoomDelay == 0)
    	{
    		currentUtilityZoomIndex = (currentUtilityZoomIndex + 1) % MAX_ZOOMS.length;
    	}
    }

    public static void toggleZoomEnabled()
    {
    	if(currentZoomDelay == 0)
    	{
    		zoomEnabled = !zoomEnabled;
    	}
    }
    
    public static boolean getSniperZoomedIn()
    {
        return sniperZoomedIn;
    }
    
    public static ModelBase getMainModel(RenderLiving renderliving)
    {
        return (ModelBase)WarTools.getPrivateValue(renderliving, RenderLiving.class, ObfuscatedNames.RenderLiving_mainModel);
    }
    
    public static void handleCraftingPackKey(Minecraft minecraft, EntityPlayer entityplayer)
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

        try
        {
            dataoutputstream.writeInt(3);
        } catch (IOException ioexception) {
            System.out.println("[ModernWarfare] An error occured while writing packet data.");
            ioexception.printStackTrace();
        }

        Packet250CustomPayload packet250custompayload = new Packet250CustomPayload();
        packet250custompayload.channel = "MDWF";
        packet250custompayload.data = bytearrayoutputstream.toByteArray();
        packet250custompayload.length = packet250custompayload.data.length;
        PacketDispatcher.sendPacketToServer(packet250custompayload);
        System.out.println("[ModernWarfare] Sent '3' packet to server");
    }

    public static void handleInventoryAtvKey(Minecraft minecraft, EntityPlayer entityplayer)
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

        try
        {
            dataoutputstream.writeInt(4);
        } catch (IOException ioexception) {
            System.out.println("[ModernWarfare] An error occured while writing packet data.");
            ioexception.printStackTrace();
        }

        Packet250CustomPayload packet250custompayload = new Packet250CustomPayload();
        packet250custompayload.channel = "MDWF";
        packet250custompayload.data = bytearrayoutputstream.toByteArray();
        packet250custompayload.length = packet250custompayload.data.length;
        PacketDispatcher.sendPacketToServer(packet250custompayload);
        System.out.println("[ModernWarfare] Sent '4' packet to server");
    }

    public static void handleAtvFireKey(Minecraft minecraft, EntityPlayer entityplayer)
    {
        if(entityplayer.ridingEntity instanceof EntityAtv)
        {
        	EntityAtv entityAtv = (EntityAtv)entityplayer.ridingEntity;
        	
        	if(entityAtv.gunA != null || entityAtv.gunB != null)
        	{
	            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
	            DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
	
	            try {
	                dataoutputstream.writeInt(5);
	            } catch (IOException ioexception) {
	                System.out.println("[ModernWarfare] An error occured while writing packet data.");
	                ioexception.printStackTrace();
	            }
	
	            Packet250CustomPayload packet250custompayload = new Packet250CustomPayload();
	            packet250custompayload.channel = "MDWF";
	            packet250custompayload.data = bytearrayoutputstream.toByteArray();
	            packet250custompayload.length = packet250custompayload.data.length;
	            PacketDispatcher.sendPacketToServer(packet250custompayload);
        	}
        }
    }
    
    public static void sendShootPacket(boolean shooting)
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

        try {
            dataoutputstream.writeInt(9);
            dataoutputstream.writeBoolean(shooting);
        } catch (IOException ioexception) {
            System.out.println("[ModernWarfare] An error occured while writing packet data.");
            ioexception.printStackTrace();
        }

        Packet250CustomPayload packet250custompayload = new Packet250CustomPayload();
        packet250custompayload.channel = "MDWF";
        packet250custompayload.data = bytearrayoutputstream.toByteArray();
        packet250custompayload.length = packet250custompayload.data.length;
        PacketDispatcher.sendPacketToServer(packet250custompayload);
    }
    
    public static void handleParachuteKey(Minecraft minecraft)
    {
        ItemStack itemstack = minecraft.thePlayer.inventory.armorInventory[2];

        if(itemstack != null && itemstack.itemID == itemParachute.itemID)
        {
            useParachute(itemstack, minecraft.theWorld, minecraft.thePlayer);
            
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

            try {
                dataoutputstream.writeInt(2);
            } catch (IOException ioexception) {
                System.out.println("[ModernWarfare] An error occured while writing packet data.");
                ioexception.printStackTrace();
            }

            Packet250CustomPayload packet250custompayload = new Packet250CustomPayload();
            packet250custompayload.channel = "MDWF";
            packet250custompayload.data = bytearrayoutputstream.toByteArray();
            packet250custompayload.length = packet250custompayload.data.length;
            PacketDispatcher.sendPacketToServer(packet250custompayload);
            System.out.println("[ModernWarfare] Sent '2' packet to server");
        }
    }
    
    public static void handleRecoil(Minecraft minecraft)
    {
        double d = 0.0D;
        double d1 = currentRecoilV;

        if (minecraft.thePlayer != null && currentRecoilV > 0.0D)
        {
            d = Math.min(Math.max(currentRecoilV * 0.10000000000000001D, 0.5D), currentRecoilV);
            currentRecoilV -= d;
            minecraft.thePlayer.rotationPitch += d;
        }

        if (minecraft.thePlayer != null && Math.abs(currentRecoilH) > 0.0D)
        {
            double d2;

            if (currentRecoilH > 0.0D)
            {
                d2 = Math.min(Math.max((currentRecoilH * 0.10000000000000001D) / 2D, 0.25D), currentRecoilH);
            }
            else
            {
                d2 = Math.max(Math.min((currentRecoilH * 0.10000000000000001D) / 2D, -0.25D), currentRecoilH);
            }

            if (d != 0.0D)
            {
                double d3 = (d / d1) * currentRecoilH;

                if (currentRecoilH > 0.0D)
                {
                    d2 = Math.min(d3, d2);
                }
                else
                {
                    d2 = Math.max(d3, d2);
                }
            }

            currentRecoilH -= d2;
            minecraft.thePlayer.rotationYaw -= d2;
        }
    }
}
