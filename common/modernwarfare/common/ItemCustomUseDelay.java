package modernwarfare.common;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ItemCustomUseDelay extends ItemWar
{
    public boolean stopArmSwing;
    public int useDelay;
    private Map lastUses;
    public static long doNotUseThisTick = 0L;

    public ItemCustomUseDelay(int i)
    {
        super(i);
        stopArmSwing = false;
        useDelay = 5;
        lastUses = new HashMap();
    }

    public boolean isUseable(World world, ItemStack itemstack)
    {
        if (world.getWorldTime() == doNotUseThisTick)
        {
            return false;
        }

        int i = getMinecraftRightClick();
        int j = getMinecraftTicksRan();

        if (lastUses.containsKey(itemstack))
        {
            CustomUseDelayEntry customusedelayentry = (CustomUseDelayEntry)lastUses.get(itemstack);
            return j - customusedelayentry.lastMinecraftUse == useDelay;
        }
        else
        {
            return false;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (world.getWorldTime() == doNotUseThisTick)
        {
            return itemstack;
        }
        else
        {
            return onItemRightClickEntity(itemstack, world, entityplayer, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    public ItemStack onItemRightClickEntity(ItemStack itemstack, World world, Entity entity, float f, float f1, float f2, float f3, float f4)
    {
        long l = 0L;
        int i = 0;

        if (lastUses.containsKey(itemstack))
        {
            CustomUseDelayEntry customusedelayentry = (CustomUseDelayEntry)lastUses.get(itemstack);
            l = customusedelayentry.lastUse;
            i = customusedelayentry.lastMinecraftUse;
        }

        if (world.getWorldTime() - l < 0L)
        {
            l = world.getWorldTime() - (long)useDelay;
            i = getMinecraftTicksRan() - useDelay;
        }

        if (world.getWorldTime() - l >= (long)useDelay && use(itemstack, world, entity, f, f1, f2, f3, f4))
        {
            l = world.getWorldTime();
            i = getMinecraftTicksRan();
        }

        lastUses.put(itemstack, new CustomUseDelayEntry(l, i));
        return itemstack;
    }

    public abstract boolean use(ItemStack itemstack, World world, Entity entity, float f, float f1, float f2, float f3, float f4);

    private int getMinecraftRightClick()
    {
        try {
            int ret = (Integer)WarTools.getPrivateValue(WarTools.minecraft, Minecraft.class, ObfuscatedNames.Minecraft_rightClickDelayTimer);
            return ret;
        } catch (Exception e) {
            return 0;
        }
    }

    private int getMinecraftTicksRan()
    {
        try {
        	int ret = (Integer)WarTools.getPrivateValue(WarTools.minecraft, Minecraft.class, ObfuscatedNames.Minecraft_ticksRan);
            return ret;
        } catch (Exception e) {
            return 0;
        }
    }
}
