package modernwarfare.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.EnumSet;

import modernwarfare.common.ModernWarfare;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.src.ModLoader;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class KeyBindingHandler extends KeyHandler
{
	private static Minecraft mc = Minecraft.getMinecraft();
	
	public static KeyBinding zoomKey = new KeyBinding("MW Zoom", Keyboard.KEY_Z);
	public static KeyBinding reloadKey = new KeyBinding("MW Reload", Keyboard.KEY_R);
	public static KeyBinding parachuteKey = new KeyBinding("MW Parachute", Keyboard.KEY_P);
	public static KeyBinding nightvisionKey = new KeyBinding("MW Nightvision", Keyboard.KEY_N);
	public static KeyBinding craftingPackKey = new KeyBinding("MW Crafting Pack", Keyboard.KEY_C);
	public static KeyBinding atvFireKey = new KeyBinding("MW ATF Fire", Keyboard.KEY_SPACE);
	public static KeyBinding atvInventoryKey = new KeyBinding("MW ATV Inventory", Keyboard.KEY_I);
	
	public KeyBindingHandler()
	{
		super(new KeyBinding[] {zoomKey, reloadKey, parachuteKey, nightvisionKey, craftingPackKey, atvFireKey, atvInventoryKey},
				new boolean[] {false, false, false, false, false, true, false});
	}

	@Override
	public String getLabel() 
	{
		return null;
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) 
	{
        if(mc.currentScreen == null)
        {
        	if(kb.keyCode == zoomKey.keyCode)
        	{
        		ModernWarfareClient.toggleZoomEnabled();
        	}
        	else if(kb.keyCode == reloadKey.keyCode)
        	{
                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

                try
                {
                    dataoutputstream.writeInt(6);
                }
                catch (IOException ioexception)
                {
                    System.out.println("[ModernWarfare] An error occured while writing packet data.");
                    ioexception.printStackTrace();
                }

                Packet250CustomPayload packet250custompayload = new Packet250CustomPayload();
                packet250custompayload.channel = "ModernWarfare";
                packet250custompayload.data = bytearrayoutputstream.toByteArray();
                packet250custompayload.length = packet250custompayload.data.length;
                ModLoader.sendPacket(packet250custompayload);
                ModernWarfare.handleReload(mc.theWorld, mc.thePlayer, false);
                System.out.println("[ModernWarfare] Sent '6' packet to server");
        	}
        	else if(kb.keyCode == parachuteKey.keyCode)
        	{
        		ModernWarfareClient.handleParachuteKey(mc);
        	}
        	else if(kb.keyCode == nightvisionKey.keyCode)
        	{
        		ModernWarfareClient.handleNightvisionKey();
        	}
        	else if(kb.keyCode == craftingPackKey.keyCode)
        	{
        		ModernWarfareClient.handleCraftingPackKey(mc, mc.thePlayer);
        	}
        	else if(kb.keyCode == atvFireKey.keyCode)
        	{
        		ModernWarfareClient.handleAtvFireKey(mc, mc.thePlayer);
        	}
        	else if(kb.keyCode == atvInventoryKey.keyCode)
        	{
        		ModernWarfareClient.handleInventoryAtvKey(mc, mc.thePlayer);
        	}
        }
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) 
	{
		
	}

	@Override
	public EnumSet<TickType> ticks() 
	{
		return EnumSet.of(TickType.CLIENT);
	}
}
