package modernwarfare.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) 
	{
		if(packet.channel.equals("MDWF"))
		{
			ByteArrayDataInput dataStream = ByteStreams.newDataInput(packet.data);
	        EntityPlayer entityplayer = (EntityPlayer)player;
	        
			try {
                int packetType = dataStream.readInt();

                if(packetType == 0)
                {
                    ModernWarfare.isSniperZoomedIn.put(player, Boolean.valueOf(false));
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '0' packet from ").append(entityplayer.username).append(".").toString());
                }
                else if(packetType == 1)
                {
                	ModernWarfare.isSniperZoomedIn.put(player, Boolean.valueOf(true));
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '1' packet from ").append(entityplayer.username).append(".").toString());
                }
                else if(packetType == 2)
                {
                    ModernWarfare.handleParachuteKey(entityplayer);
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '2' packet from ").append(entityplayer.username).append(".").toString());
                }
                else if(packetType == 3)
                {
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '3' packet from ").append(entityplayer.username).append(".").toString());

                    if(entityplayer.openContainer instanceof ContainerCraftingPack)
                    {
                        entityplayer.closeScreen();
                    }
                    else if(entityplayer.inventory.hasItem(ModernWarfare.itemCraftingPack.itemID))
                    {
                        entityplayer.openGui(ModernWarfare.instance, 0, entityplayer.worldObj, 0, 0, 0);
                    }
                }
                else if(packetType == 4)
                {
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '4' packet from ").append(entityplayer.username).append(".").toString());

                    if(entityplayer.ridingEntity instanceof EntityAtv)
                    {
                        entityplayer.openGui(ModernWarfare.instance, 1, entityplayer.worldObj, 0, 0, 0);
                    }
                    else if(entityplayer.openContainer instanceof ContainerAtv)
                    {
                        entityplayer.closeScreen();
                    }
                }
                else if(packetType == 5)
                {
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '5' packet from ").append(entityplayer.username).append(".").toString());

                    if(entityplayer.ridingEntity instanceof EntityAtv)
                    {
                        ((EntityAtv)entityplayer.ridingEntity).fireGuns();
                    }
                }
                else if(packetType == 6)
                {
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '6' packet from ").append(entityplayer.username).append(".").toString());
                    ModernWarfare.handleReload(entityplayer.worldObj, entityplayer, false);
                }
                else if(packetType == 7)
                {
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '7' packet from ").append(entityplayer.username).append(".").toString());

                    if(ModernWarfare.isJetpackOn.containsKey(entityplayer))
                    {
                    	ModernWarfare.isJetpackOn.remove(entityplayer);
                    }

                    ModernWarfare.isJetpackOn.put(entityplayer, Boolean.valueOf(true));
                }
                else if(packetType == 8)
                {
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '8' packet from ").append(entityplayer.username).append(".").toString());

                    if(ModernWarfare.isJetpackOn.containsKey(entityplayer))
                    {
                        ModernWarfare.isJetpackOn.remove(entityplayer);
                    }

                    ModernWarfare.isJetpackOn.put(entityplayer, Boolean.valueOf(false));
                }
                else if(packetType == 9)
                {
                	boolean shooting = dataStream.readBoolean();
                	
                	if(shooting && entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().getItem() instanceof ItemGun)
                	{
                		ModernWarfare.shooting.add(entityplayer);
                	}
                	else {
                		ModernWarfare.shooting.remove(entityplayer);
                	}
                }
                else if(packetType == 10)
                {
                	ModernWarfare.proxy.doParticles(dataStream.readDouble(), dataStream.readDouble(), dataStream.readDouble(), dataStream.readDouble(), dataStream.readDouble());
                }
                else if(packetType == 11)
                {
                	ModernWarfare.proxy.doExplosionFX(dataStream.readDouble(), dataStream.readDouble(), dataStream.readDouble());
                }
			} catch(Exception e) {
				System.err.println("Error while handling packet.");
				e.printStackTrace();
			}
		}
	}
}
