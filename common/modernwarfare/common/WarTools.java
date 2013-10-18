package modernwarfare.common;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public final class WarTools
{
    public static Minecraft minecraft = Minecraft.getMinecraft();
    public static Random random = new Random();
    private static List recipes;

    public static int getNumberInFirstStackInInventory(InventoryPlayer inventoryplayer, int i)
    {
        for(int j = 0; j < inventoryplayer.mainInventory.length; j++)
        {
            if(inventoryplayer.mainInventory[j] != null && inventoryplayer.mainInventory[j].itemID == i)
            {
                if(inventoryplayer.mainInventory[j].getItem().getMaxDamage() > 0)
                {
                    return (inventoryplayer.mainInventory[j].getItem().getMaxDamage() - inventoryplayer.mainInventory[j].getItemDamage()) + 1;
                }
                else {
                    return inventoryplayer.mainInventory[j].stackSize;
                }
            }
        }

        return -1;
    }

    public static int getNumberInInventory(InventoryPlayer inventoryplayer, int i)
    {
        int j = 0;

        for (int k = 0; k < inventoryplayer.mainInventory.length; k++)
        {
            if (inventoryplayer.mainInventory[k] != null && inventoryplayer.mainInventory[k].itemID == i)
            {
                j++;
            }
        }

        return j;
    }

    public static int getNumberInFirstStackInHotbar(InventoryPlayer inventoryplayer, int i)
    {
        for (int j = 0; j < 9; j++)
        {
            if (inventoryplayer.mainInventory[j] != null && inventoryplayer.mainInventory[j].itemID == i)
            {
                return inventoryplayer.mainInventory[j].stackSize;
            }
        }

        return -1;
    }

    public static boolean playerInventoryEmpty(InventoryPlayer inventoryplayer)
    {
        for (int i = 0; i < inventoryplayer.mainInventory.length; i++)
        {
            if (inventoryplayer.mainInventory[i] != null)
            {
                return false;
            }
        }

        for (int j = 0; j < inventoryplayer.armorInventory.length; j++)
        {
            if (inventoryplayer.armorInventory[j] != null)
            {
                return false;
            }
        }

        return true;
    }

    public static void renderTextureOverlay(String s, float f)
    {
        ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, f);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        minecraft.renderEngine.bindTexture(new ResourceLocation("modernwarfare:" + s));
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0D, j, -90D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(i, j, -90D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(i, 0.0D, -90D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(0.0D, 0.0D, -90D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, f);
    }

    public static void setBurnRate(int i, int j, int k)
    {
        try
        {
        	Method m = getPrivateMethod(BlockFire.class, ObfuscatedNames.BlockFire_setBurnRate, new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE});
            m.invoke(Block.fire, Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(k));
        } catch (Exception e) {
            return;
        }
    }

    public static void attackEntityIgnoreDelay(EntityLiving entityliving, DamageSource damagesource, int i)
    {
        entityliving.attackEntityFrom(damagesource, i);
    }

    public static boolean isFlammable(int i)
    {
        try
        {
            int[] ai = (int[])getPrivateValue(Block.fire, BlockFire.class, ObfuscatedNames.BlockFire_abilityToCatchFire);
            return ai[i] != 0 || i == Block.netherrack.blockID;
        } catch (Exception exception) {
            return false;
        }
    }

    public static int useItemInInventory(EntityPlayer entityplayer, int i)
    {
        int j = getInventorySlotContainItem(entityplayer.inventory, i);

        if (j < 0)
        {
            return 0;
        }

        if (Item.itemsList[i].getMaxDamage() > 0)
        {
            if (entityplayer.inventory.mainInventory[j].getItemDamage() + 1 > entityplayer.inventory.mainInventory[j].getMaxDamage())
            {
                entityplayer.inventory.mainInventory[j] = new ItemStack(Item.bucketEmpty);

                if (getInventorySlotContainItem(entityplayer.inventory, i) >= 0)
                {
                    return 2;
                }
            }
            else {
                entityplayer.inventory.mainInventory[j].damageItem(1, entityplayer);
            }
        }
        else if (--entityplayer.inventory.mainInventory[j].stackSize <= 0)
        {
            entityplayer.inventory.mainInventory[j] = null;

            if (getInventorySlotContainItem(entityplayer.inventory, i) >= 0)
            {
                return 2;
            }
        }

        return 1;
    }

    private static int getInventorySlotContainItem(InventoryPlayer inventoryplayer, int i)
    {
        for (int j = 0; j < inventoryplayer.mainInventory.length; j++)
        {
            if (inventoryplayer.mainInventory[j] != null && inventoryplayer.mainInventory[j].itemID == i)
            {
                return j;
            }
        }

        return -1;
    }

    public static int useItemInHotbar(EntityPlayer entityplayer, int i)
    {
        int j = getHotbarSlotContainItem(entityplayer.inventory, i);

        if (j < 0)
        {
            return 0;
        }

        if (Item.itemsList[i].getMaxDamage() > 0)
        {
            entityplayer.inventory.mainInventory[j].damageItem(1, entityplayer);

            if (entityplayer.inventory.mainInventory[j].stackSize == 0)
            {
                entityplayer.inventory.mainInventory[j] = new ItemStack(Item.bucketEmpty);

                if (getHotbarSlotContainItem(entityplayer.inventory, i) >= 0)
                {
                    return 2;
                }
            }
        }
        else if (--entityplayer.inventory.mainInventory[j].stackSize <= 0)
        {
            entityplayer.inventory.mainInventory[j] = null;

            if (getHotbarSlotContainItem(entityplayer.inventory, i) >= 0)
            {
                return 2;
            }
        }

        return 1;
    }

    private static int getHotbarSlotContainItem(InventoryPlayer inventoryplayer, int i)
    {
        for (int j = 0; j < 9; j++)
        {
            if (inventoryplayer.mainInventory[j] != null && inventoryplayer.mainInventory[j].itemID == i)
            {
                return j;
            }
        }

        return -1;
    }

    public static boolean onGroundOrInWater(World world, Entity entity)
    {
        return entity.onGround || world.handleMaterialAcceleration(entity.boundingBox, Material.water, entity);
    }
    
    /**
     * Retrieves a private value from a defined class and field.
     * @param obj - the Object to retrieve the value from, null if static
     * @param c - Class to retrieve field value from
     * @param fields - possible names of field to iterate through
     * @return value as an Object, cast as necessary
     */
    public static Object getPrivateValue(Object obj, Class c, String[] fields)
    {
    	for(String field : fields)
    	{
	    	try {
		    	Field f = c.getDeclaredField(field);
		    	f.setAccessible(true);
		    	return f.get(obj);
	    	} catch(Exception e) {
	    		continue;
	    	}
    	}
    	
    	return null;
    }
    
    /**
     * Sets a private value from a defined class and field to a new value.
     * @param obj - the Object to perform the operation on, null if static
     * @param value - value to set the field to
     * @param c - Class the operation will be performed on
     * @param fields - possible names of field to iterate through
     */
    public static void setPrivateValue(Object obj, Object value, Class c, String[] fields)
    {
    	for(String field : fields)
    	{
	    	try {
	    		Field f = c.getDeclaredField(field);
	    		f.setAccessible(true);
	    		f.set(obj, value);
	    	} catch(Exception e) {
	    		continue;
	    	}
    	}
    }
    
    /**
     * Retrieves a private method from a class, sets it as accessible, and returns it.
     * @param c - Class the method is located in
     * @param methods - possible names of the method to iterate through
     * @param params - the Types inserted as parameters into the method
     * @return private method
     */
    public static Method getPrivateMethod(Class c, String[] methods, Class... params)
    {
    	for(String method : methods)
    	{
    		try {
    			Method m = c.getDeclaredMethod(method, params);
    			m.setAccessible(true);
    			return m;
    		} catch(Exception e) {
    			continue;
    		}
    	}
    	
    	return null;
    }
}
