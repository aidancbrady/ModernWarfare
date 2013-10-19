package modernwarfare.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "ModernWarfare", name = ModernWarfare.NAME, version = ModernWarfare.VERSION)
@NetworkMod(channels = {"MDWF"}, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class ModernWarfare
{
    public static final String NAME = "Modern Warfare";
    
    public static final String VERSION = "2.0.0";
    
    public static Logger logger = Logger.getLogger("Minecraft");
    
    @Instance("ModernWarfare")
    public static ModernWarfare instance;
    
    @SidedProxy(clientSide = "modernwarfare.client.ClientProxy", serverSide = "modernwarfare.common.CommonProxy")
    public static CommonProxy proxy;
    
    public static Configuration configuration;
    
    public static CreativeTabModernWarfare tabModernWarfare = new CreativeTabModernWarfare();
    
    public static Item itemLightometer;
    public static Item itemNightvisionGoggles;
    public static Item itemScubaTank;
    public static Item itemParachute;
    public static Item itemTelescope;
    public static Item itemGhillieHelmet;
    public static Item itemGhillieChest;
    public static Item itemGhilliePants;
    public static Item itemGhillieBoots;
    public static Item itemBulletCasing;
    public static Item itemBulletLight;
    public static Item itemBulletMedium;
    public static Item itemBulletShell;
    public static Item itemGunAk47;
    public static Item itemGunMp5;
    public static Item itemGunShotgun;
    public static Item itemGunDeagle;
    public static Item itemGrenade;
    public static Item itemBulletRocket;
    public static Item itemGunRocketLauncher;
    public static Item itemBulletRocketLaser;
    public static Item itemGunRocketLauncherLaser;
    public static Item itemBulletHeavy;
    public static Item itemGunSniper;
    public static Item itemGrenadeStun;
    public static Item itemGrenadeSmoke;
    public static Item itemBulletCasingShell;
    public static Item itemOil;
    public static Item itemGunFlamethrower;
    public static Item itemGrenadeSticky;
    public static Item itemGunSg552;
    public static Item itemGunMinigun;
    public static Item itemGunLaser;
    public static Item itemGunM4;
    public static Item itemGrenadeIncendiary;
    public static Item itemGrenadeIncendiaryLit;
    public static Item itemScope;
    public static Item itemWrench;
    public static Item itemSentry;
    public static Item itemJetPack;
    public static Item itemRope;
    public static Item itemGrapplingHook;
    public static Item itemCraftingPack;
    public static Item itemSmallBarrel;
    public static Item itemMediumBarrel;
    public static Item itemLongBarrel;
    public static Item itemFatBarrel;
    public static Item itemMetalGrip;
    public static Item itemWoodGrip;
    public static Item itemFuelTank;
    public static Item itemAtv;
    public static Item itemAtvBody;
    public static Item itemAtvWheel;
    
    public static Block blockRope;
    public static Block blockGrapplingHook;
    
    public static boolean guns = true;
    public static boolean bulletsDestroyGlass = true;
    public static boolean showAmmoBar = true;
    public static boolean grenades = true;
    public static boolean explosionsDestroyBlocks = true;
    public static boolean laserSetsFireToBlocks = true;
    public static boolean lighter = true;
    public static boolean sentries = true;
    public static boolean sentriesKillAnimals = false;
    public static boolean atv = true;
    public static boolean jetPack = true;
    public static boolean ammoRestrictions = true;
    public static boolean ammoCasings = true;
    
    public static int ropeID = 3450;
    public static int grapplingHookID = 3451;
    
    public static Map grapplingHooks = new HashMap();
    public static Map reloadTimes = new HashMap();
    public static Map isSniperZoomedIn = new HashMap();
    public static Map isJetpackOn = new HashMap();
    
    public static Map<String, Long> jetPackLastSound = new HashMap<String, Long>();
    
    public static Set<EntityPlayer> shooting = new HashSet<EntityPlayer>();
    
    public static Class sentryEntityClasses[] = (new Class[] 
    {
        EntitySentryAk47.class, EntitySentryMp5.class, EntitySentryShotgun.class, EntitySentryDeagle.class, EntitySentryRocketLauncher.class, EntitySentryRocketLauncherLaser.class, EntitySentrySniper.class, EntitySentryFlamethrower.class, EntitySentrySg552.class, EntitySentryMinigun.class,
        EntitySentryLaser.class, EntitySentryM4.class
    });
    
    public static String sentryNames[] =
    {
        "AK47", "MP5", "Shotgun", "Desert Eagle", "Rocket Launcher", "Laser-Guided Rocket Launcher", "Sniper Rifle", "Flamethrower", "SG552", "Minigun",
        "Laser", "M4"
    };
    
    public static int monsterSpawns = 70;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	proxy.initSounds();
    	configuration = new Configuration(event.getSuggestedConfigurationFile());
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        addItems();
        addBlocks();
        addEntities();
        addTileEntities();
        addBulletRestrictions();
        addItemDamage();
        addRecipes();
        addNames();
        
        proxy.loadConfiguration();
        proxy.loadUtilities();
        proxy.loadRenderers();
        
        TickRegistry.registerTickHandler(new CommonTickHandler(), Side.SERVER);
        
        NetworkRegistry.instance().registerConnectionHandler(new CommonConnectionHandler());
        NetworkRegistry.instance().registerGuiHandler(this, proxy);
    }
    
    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event)
    {
    	grapplingHooks = new HashMap();
    	reloadTimes = new HashMap();
    	isSniperZoomedIn = new HashMap();
    	isJetpackOn = new HashMap();
    	
    	jetPackLastSound = new HashMap<String, Long>();
    }

    public void addTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityRope.class, "Rope");
    }

    public void addItemDamage()
    {
        itemBulletLight.setMaxDamage(0);
        itemBulletMedium.setMaxDamage(0);
        itemBulletShell.setMaxDamage(0);
        itemBulletRocket.setMaxDamage(0);
        itemBulletRocketLaser.setMaxDamage(0);
        itemBulletHeavy.setMaxDamage(0);
        Item.redstone.setMaxDamage(0);
    }

    public void addEntities()
    {
    	EntityRegistry.registerGlobalEntityID(EntityBulletAk47.class, "BulletAk47", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityBulletCasing.class, "BulletCasing", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityBulletCasingShell.class, "BulletCasingShell", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityBulletDeagle.class, "BulletDeagle", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityBulletFlame.class, "BulletFlame", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityBulletLaser.class, "BulletLaser", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityBulletM4.class, "BulletM4", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityBulletMinigun.class, "BulletMinigun", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityBulletMp5.class, "BulletMp5", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityBulletRocket.class, "BulletRocket", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityBulletRocketLaser.class, "BulletRocketLaser", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityBulletSg552.class, "BulletSg552", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityBulletShotgun.class, "BulletShot", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityBulletSniper.class, "BulletSniper", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityGrenade.class, "Grenade", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityGrenadeIncendiary.class, "GrenadeIncendiary", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityGrenadeSmoke.class, "GrenadeSmoke", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityGrenadeSticky.class, "GrenadeSticky", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityGrenadeStun.class, "GrenadeStun", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityParachute.class, "Parachute", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityGrapplingHook.class, "GrapplingHook", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntityAtv.class, "ATV", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntitySentryAk47.class, "SentryAk47", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntitySentryDeagle.class, "SentryDeagle", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntitySentryMp5.class, "SentryMp5", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntitySentryRocketLauncher.class, "SentryRocketLauncher", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntitySentryRocketLauncherLaser.class, "SentryRocketLauncherLaser", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntitySentryShotgun.class, "SentryShotgun", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntitySentrySniper.class, "SentrySniper", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntitySentryFlamethrower.class, "SentryFlamethrower", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntitySentrySg552.class, "SentrySg552", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntitySentryMinigun.class, "SentryMinigun", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntitySentryLaser.class, "SentryLaser", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntitySentryM4.class, "SentryM4", EntityRegistry.findGlobalUniqueEntityId());
        
        EntityRegistry.registerModEntity(EntityBulletAk47.class, "BulletAk47", 0, this, 40, 100, true);
        EntityRegistry.registerModEntity(EntityBulletCasing.class, "BulletCasing", 1, this, 40, 100, true);
        EntityRegistry.registerModEntity(EntityBulletCasingShell.class, "BulletCasingShell", 2, this, 40, 100, true);
        EntityRegistry.registerModEntity(EntityBulletDeagle.class, "BulletDeagle", 3, this, 40, 100, true);
        EntityRegistry.registerModEntity(EntityBulletFlame.class, "BulletFlame", 4, this, 40, 100, true);
        EntityRegistry.registerModEntity(EntityBulletLaser.class, "BulletLaser", 5, this, 40, 100, true);
        EntityRegistry.registerModEntity(EntityBulletM4.class, "BulletM4", 6, this, 40, 100, true);
        EntityRegistry.registerModEntity(EntityBulletMinigun.class, "BulletMinigun", 7, this, 40, 100, true);
        EntityRegistry.registerModEntity(EntityBulletMp5.class, "BulletMp5", 8, this, 40, 100, true);
        EntityRegistry.registerModEntity(EntityBulletRocket.class, "BulletRocket", 9, this, 80, 100, true);
        EntityRegistry.registerModEntity(EntityBulletRocketLaser.class, "BulletRocketLaser", 10, this, 80, 100, true);
        EntityRegistry.registerModEntity(EntityBulletSg552.class, "BulletSg552", 11, this, 40, 100, true);
        EntityRegistry.registerModEntity(EntityBulletShotgun.class, "BulletShotgun", 12, this, 40, 100, true);
        EntityRegistry.registerModEntity(EntityBulletSniper.class, "BulletSniper", 13, this, 40, 100, true);
        EntityRegistry.registerModEntity(EntityGrenade.class, "Grenade", 14, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntityGrenadeIncendiary.class, "GrenadeIncendiary", 15, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntityGrenadeSmoke.class, "GrenadeSmoke", 16, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntityGrenadeSticky.class, "GrenadeSticky", 17, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntityGrenadeStun.class, "GrenadeStun", 18, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntityParachute.class, "Parachute", 19, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntityGrapplingHook.class, "GrapplingHook", 20, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntityAtv.class, "ATV", 21, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntitySentryAk47.class, "SentryAk47", 22, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntitySentryDeagle.class, "SentryDeagle", 23, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntitySentryMp5.class, "SentryMp5", 24, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntitySentryRocketLauncher.class, "SentryRocketLauncher", 25, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntitySentryRocketLauncherLaser.class, "SentryRocketLauncherLaser", 26, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntitySentryShotgun.class, "SentryShotgun", 27, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntitySentrySniper.class, "SentrySniper", 28, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntitySentryFlamethrower.class, "SentryFlamethrower", 29, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntitySentrySg552.class, "SentrySg552", 30, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntitySentryMinigun.class, "SentryMinigun", 31, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntitySentryLaser.class, "SentryLaser", 32, this, 40, 5, true);
        EntityRegistry.registerModEntity(EntitySentryM4.class, "SentryM4", 33, this, 40, 5, true);
    }

    public void addBulletRestrictions()
    {
        itemBulletLight.setMaxStackSize(32);
        itemBulletMedium.setMaxStackSize(ammoRestrictions ? 8 : 32);
        itemBulletShell.setMaxStackSize(ammoRestrictions ? 8 : 32);
        itemGrenade.setMaxStackSize(ammoRestrictions ? 4 : 64);
        itemBulletRocket.setMaxStackSize(ammoRestrictions ? 4 : 32);
        itemBulletRocketLaser.setMaxStackSize(ammoRestrictions ? 4 : 32);
        itemBulletHeavy.setMaxStackSize(ammoRestrictions ? 4 : 32);
        itemGrenadeStun.setMaxStackSize(ammoRestrictions ? 4 : 64);
        itemGrenadeSmoke.setMaxStackSize(ammoRestrictions ? 4 : 64);
        itemGrenadeSticky.setMaxStackSize(ammoRestrictions ? 4 : 64);
        itemGrenadeIncendiary.setMaxStackSize(ammoRestrictions ? 4 : 64);
    }

    public void addRecipes()
    {
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(Item.ingotIron, 1), new Object[] {
            "XX", "XX", 'X', itemBulletCasing
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemBulletLight, ammoRestrictions ? 4 : itemBulletLight.getItemStackLimit()), new Object[] {
            "X", "#", 'X', Item.ingotIron, '#', Item.gunpowder
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemBulletMedium, ammoRestrictions ? 4 : itemBulletMedium.getItemStackLimit()), new Object[] {
            "X ", "##", 'X', Item.ingotIron, '#', Item.gunpowder
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemBulletShell, ammoRestrictions ? 4 : itemBulletShell.getItemStackLimit()), new Object[] {
            "X ", "#Y", 'X', Item.ingotIron, '#', Item.gunpowder, 'Y', Item.paper
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemBulletShell, ammoRestrictions ? 4 : itemBulletShell.getItemStackLimit()), new Object[] {
            " X", "#Y", 'X', Item.ingotIron, '#', Item.gunpowder, 'Y', Item.paper
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemBulletRocket, ammoRestrictions ? 1 : itemBulletRocket.getItemStackLimit()), new Object[] {
            "###", "#X#", "XXX", 'X', Item.gunpowder, '#', Item.ingotIron
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemBulletRocketLaser, ammoRestrictions ? 1 : itemBulletRocketLaser.getItemStackLimit()), new Object[] {
            "#Y#", "#X#", "XXX", 'X', Item.gunpowder, '#', Item.ingotIron, 'Y', Item.redstone
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemBulletHeavy, itemBulletHeavy.getItemStackLimit()), new Object[] {
            "XX", "##", 'X', Item.ingotIron, '#', Item.gunpowder
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(Item.ingotIron, 1), new Object[] {
            "XX", "XX", 'X', itemBulletCasingShell
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemScope), new Object[] {
            "X#X", 'X', Item.diamond, '#', Item.ingotIron
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGrenade, 4), new Object[] {
            "X#X", "#X#", "X#X", 'X', Item.gunpowder, '#', Item.ingotIron
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGrenadeStun, 4), new Object[] {
            "X#X", "#Y#", "X#X", 'X', Item.gunpowder, '#', Item.ingotIron, 'Y', Item.glowstone
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGrenadeSmoke, 4), new Object[] {
            "X#X", "#Y#", "X#X", 'X', Item.gunpowder, '#', Item.ingotIron, 'Y', Item.flint
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGrenadeIncendiary, 4), new Object[] {
            " X ", "#Y#", " # ", 'X', Item.paper, '#', Block.glass, 'Y', itemOil
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemWrench), new Object[] {
            "X", "X", "X", 'X', Item.ingotIron
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemJetPack), new Object[] {
            "###", "#X#", "###", '#', Item.ingotIron, 'X', itemOil
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGrapplingHook, 1), new Object[] {
            "X", "#", "#", '#', itemRope, 'X', Item.ingotIron
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemRope, 1), new Object[] {
            "##", "##", "##", '#', Item.silk
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemLightometer, 1), new Object[] {
            " # ", "#X#", " # ", '#', Item.ingotIron, 'X', Item.glowstone
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemNightvisionGoggles, 1), new Object[] {
            "#X#", '#', Item.diamond, 'X', Item.silk
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemScubaTank, 1), new Object[] {
            "TLT", 'T', itemFuelTank, 'L', Item.leather
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemParachute, 1), new Object[] {
            "###", "XYX", "YYY", '#', Block.cloth, 'X', Item.silk, 'Y', Item.leather
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemTelescope), new Object[] {
            "#", "X", "X", '#', Item.diamond, 'X', Item.ingotIron
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGhillieHelmet), new Object[] {
            "###", "# #", '#', Block.vine
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGhillieChest), new Object[] {
            "# #", "###", "###", '#', Block.vine
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGhilliePants), new Object[] {
            "###", "# #", "# #", '#', Block.vine
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGhillieBoots), new Object[] {
            "# #", "# #", '#', Block.vine
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemCraftingPack, 1), new Object[] {
            "L L", "LCL", "LLL", 'L', Item.leather, 'C', Block.workbench
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGunSg552), new Object[] {
            "IM", "W ", 'I', Item.ingotIron, 'M', itemMediumBarrel, 'W', itemWoodGrip
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGunMinigun), new Object[] {
            "IF", "M ", 'I', Item.ingotIron, 'F', itemFatBarrel, 'B', itemMetalGrip
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGunLaser), new Object[] {
            "R ", "IL", "M ", 'R', Item.redstone, 'I', Item.ingotIron, 'L', itemLongBarrel, 'M', itemMetalGrip
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGunM4), new Object[] {
            "IB", "M ", 'I', Item.ingotIron, 'L', itemMediumBarrel, 'M', itemMetalGrip
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGunAk47), new Object[] {
            "IS", "W ", 'I', Item.ingotIron, 'S', itemSmallBarrel, 'W', itemWoodGrip
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGunMp5), new Object[] {
            "IS", "M ", 'I', Item.ingotIron, 'S', itemSmallBarrel, 'M', itemMetalGrip
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGunShotgun), new Object[] {
            "IL", "W ", 'I', Item.ingotIron, 'L', itemLongBarrel, 'W', itemWoodGrip
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGunDeagle), new Object[] {
            "II", "M ", 'I', Item.ingotIron, 'M', itemMetalGrip
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGunRocketLauncherLaser), new Object[] {
            "R ", "OF", "M ", 'R', Item.redstone, 'O', Block.obsidian, 'F', itemFatBarrel, 'M', itemMetalGrip
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGunRocketLauncher), new Object[] {
            "OF", "M ", 'O', Block.obsidian, 'F', itemFatBarrel, 'M', itemMetalGrip
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGunSniper), new Object[] {
            "S ", "IL", "M ", 'S', itemScope, 'I', Item.ingotIron, 'L', itemLongBarrel, 'M', itemMetalGrip
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemGunFlamethrower), new Object[] {
            "IF", "GT", 'I', Item.ingotIron, 'F', itemFatBarrel, 'G', itemMetalGrip, 'T', itemFuelTank
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemWoodGrip), new Object[] {
            "PP", "P ", 'P', Block.planks
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemMetalGrip), new Object[] {
            "II", "I ", 'I', Item.ingotIron
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemSmallBarrel), new Object[] {
            "II", 'I', Item.ingotIron
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemMediumBarrel), new Object[] {
            "SI", 'S', itemSmallBarrel, 'I', Item.ingotIron
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemLongBarrel), new Object[] {
            "MI", 'M', itemMediumBarrel, 'I', Item.ingotIron
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemFatBarrel), new Object[] {
            "III", "III", 'I', Item.ingotIron
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemFuelTank), new Object[] {
            "II", "II", "II", 'I', Item.ingotIron
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemAtvWheel), new Object[] {
            " X ", "XYX", " X ", 'X', Item.leather, 'Y', Item.ingotIron
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemAtvBody), new Object[] {
            "ZXZ", "XYX", "ZXZ", 'X', Item.ingotIron, 'Y', Block.furnaceIdle, 'Z', Item.redstone
        }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(itemAtv), new Object[] {
            " X ", "XYX", " X ", 'X', itemAtvWheel, 'Y', itemAtvBody
        }));
        
        CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(itemGrenadeSticky), new Object[] {
            new ItemStack(itemGrenade), new ItemStack(Item.slimeBall)
        }));
    }

    public void addNames()
    {
        LanguageRegistry.addName(itemBulletCasing, "Bullet Casing");
        LanguageRegistry.addName(itemBulletLight, "Light Bullet");
        LanguageRegistry.addName(itemBulletMedium, "Medium Bullet");
        LanguageRegistry.addName(itemBulletShell, "Shotgun Shell");
        LanguageRegistry.addName(itemGunAk47, "AK47");
        LanguageRegistry.addName(itemGunMp5, "MP5");
        LanguageRegistry.addName(itemGunShotgun, "Shotgun");
        LanguageRegistry.addName(itemGunDeagle, "Desert Eagle");
        LanguageRegistry.addName(itemGrenade, "Grenade");
        LanguageRegistry.addName(itemBulletRocket, "Rocket");
        LanguageRegistry.addName(itemGunRocketLauncher, "Rocket Launcher");
        LanguageRegistry.addName(itemBulletRocketLaser, "Laser-Guided Rocket");
        LanguageRegistry.addName(itemGunRocketLauncherLaser, "Laser-Guided Rocket Launcher");
        LanguageRegistry.addName(itemBulletHeavy, "Heavy Bullet");
        LanguageRegistry.addName(itemGunSniper, "Sniper Rifle");
        LanguageRegistry.addName(itemGrenadeStun, "Stun Grenade");
        LanguageRegistry.addName(itemGrenadeSmoke, "Smoke Grenade");
        LanguageRegistry.addName(itemBulletCasingShell, "Shell Casing");
        LanguageRegistry.addName(itemOil, "Oil Bucket");
        LanguageRegistry.addName(itemGunFlamethrower, "Flamethrower");
        LanguageRegistry.addName(itemGrenadeSticky, "Sticky Grenade");
        LanguageRegistry.addName(itemGunSg552, "SG552");
        LanguageRegistry.addName(itemGunMinigun, "Minigun");
        LanguageRegistry.addName(itemGunLaser, "Laser");
        LanguageRegistry.addName(itemGunM4, "M4");
        LanguageRegistry.addName(itemGrenadeIncendiary, "Molotov Cocktail");
        LanguageRegistry.addName(itemScope, "Scope");
        LanguageRegistry.addName(itemWrench, "Wrench");
        LanguageRegistry.addName(new ItemStack(itemSentry, 1, 0), itemSentry.getUnlocalizedName(new ItemStack(itemSentry, 1, 0)));
        LanguageRegistry.addName(new ItemStack(itemSentry, 1, 1), itemSentry.getUnlocalizedName(new ItemStack(itemSentry, 1, 1)));
        LanguageRegistry.addName(new ItemStack(itemSentry, 1, 2), itemSentry.getUnlocalizedName(new ItemStack(itemSentry, 1, 2)));
        LanguageRegistry.addName(new ItemStack(itemSentry, 1, 3), itemSentry.getUnlocalizedName(new ItemStack(itemSentry, 1, 3)));
        LanguageRegistry.addName(new ItemStack(itemSentry, 1, 4), itemSentry.getUnlocalizedName(new ItemStack(itemSentry, 1, 4)));
        LanguageRegistry.addName(new ItemStack(itemSentry, 1, 5), itemSentry.getUnlocalizedName(new ItemStack(itemSentry, 1, 5)));
        LanguageRegistry.addName(new ItemStack(itemSentry, 1, 6), itemSentry.getUnlocalizedName(new ItemStack(itemSentry, 1, 6)));
        LanguageRegistry.addName(new ItemStack(itemSentry, 1, 7), itemSentry.getUnlocalizedName(new ItemStack(itemSentry, 1, 7)));
        LanguageRegistry.addName(new ItemStack(itemSentry, 1, 8), itemSentry.getUnlocalizedName(new ItemStack(itemSentry, 1, 8)));
        LanguageRegistry.addName(new ItemStack(itemSentry, 1, 9), itemSentry.getUnlocalizedName(new ItemStack(itemSentry, 1, 9)));
        LanguageRegistry.addName(new ItemStack(itemSentry, 1, 10), itemSentry.getUnlocalizedName(new ItemStack(itemSentry, 1, 10)));
        LanguageRegistry.addName(new ItemStack(itemSentry, 1, 11), itemSentry.getUnlocalizedName(new ItemStack(itemSentry, 1, 11)));
        LanguageRegistry.addName(itemJetPack, "Jet Pack");
        LanguageRegistry.addName(itemRope, "Rope");
        LanguageRegistry.addName(itemGrapplingHook, "Grappling Hook");
        LanguageRegistry.addName(itemLightometer, "Lightometer");
        LanguageRegistry.addName(itemNightvisionGoggles, "Nightvision Goggles");
        LanguageRegistry.addName(itemScubaTank, "Scuba Tank");
        LanguageRegistry.addName(itemParachute, "Parachute");
        LanguageRegistry.addName(itemTelescope, "Telescope");
        LanguageRegistry.addName(itemGhillieHelmet, "Ghillie Suit Helmet");
        LanguageRegistry.addName(itemGhillieChest, "Ghillie Suit Shirt");
        LanguageRegistry.addName(itemGhilliePants, "Ghillie Suit Pants");
        LanguageRegistry.addName(itemGhillieBoots, "Ghillie Suit Boots");
        LanguageRegistry.addName(itemCraftingPack, "CraftingPack");
        LanguageRegistry.addName(itemWoodGrip, "Wood Grip");
        LanguageRegistry.addName(itemMetalGrip, "Metal Grip");
        LanguageRegistry.addName(itemSmallBarrel, "Small Barrel");
        LanguageRegistry.addName(itemMediumBarrel, "Medium Barrel");
        LanguageRegistry.addName(itemLongBarrel, "Long Barrel");
        LanguageRegistry.addName(itemFatBarrel, "Fat Barrel");
        LanguageRegistry.addName(itemAtv, "ATV");
        LanguageRegistry.addName(itemAtvBody, "ATV Body");
        LanguageRegistry.addName(itemAtvWheel, "ATV Wheel");
        LanguageRegistry.addName(itemFuelTank, "Fuel Tank");
        LanguageRegistry.addName(blockRope, "Rope");
        LanguageRegistry.addName(blockGrapplingHook, "Grappling Hook");
        
        LanguageRegistry.instance().addStringLocalization("itemGroup.tabModernWarfare", "Modern Warfare");
    }

    public void addBlocks()
    {
        blockRope = (new BlockRope(ropeID, 4)).setHardness(-1F).setResistance(6000000F).setStepSound(Block.soundClothFootstep).setUnlocalizedName("blockRope");
        blockGrapplingHook = (new BlockGrapplingHook(grapplingHookID, 0)).setHardness(0.0F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("blockGrapplingHook");
        
        GameRegistry.registerBlock(blockRope, "Rope");
        GameRegistry.registerBlock(blockGrapplingHook, "GrapplingHook");
    }

    public void addItems()
    {
        itemLightometer = (new ItemWar(13236)).setMaxStackSize(1).setUnlocalizedName("itemLightometer");
        itemParachute = (new ItemParachute(13237, proxy.getArmorIndex("parachute"))).setUnlocalizedName("itemParachute");
        itemTelescope = (new ItemTelescope(13238)).setUnlocalizedName("itemTelescope");
        itemBulletCasing = (new ItemWar(13239)).setUnlocalizedName("itemBulletCasing");
        itemBulletLight = (new ItemWar(13240)).setMaxStackSize(32).setUnlocalizedName("itemBullet9mm");
        itemBulletMedium = (new ItemWar(13241)).setMaxStackSize(8).setUnlocalizedName("itemBullet357");
        itemBulletShell = (new ItemWar(13242)).setMaxStackSize(8).setUnlocalizedName("itemBulletShell");
        itemGunAk47 = (new ItemGunAk47(13243)).setUnlocalizedName("itemGunAk47");
        itemGunMp5 = (new ItemGunMp5(13244)).setUnlocalizedName("itemGunMp5");
        itemGunShotgun = (new ItemGunShotgun(13245)).setUnlocalizedName("itemGunShotgun");
        itemGunDeagle = (new ItemGunDeagle(13246)).setUnlocalizedName("itemGunDeagle");
        itemGrenade = (new ItemGrenade(13247)).setMaxStackSize(4).setUnlocalizedName("itemGrenade");
        itemBulletRocket = (new ItemWar(13248)).setMaxStackSize(4).setUnlocalizedName("itemBulletRocket");
        itemGunRocketLauncher = (new ItemGunRocketLauncher(13249)).setUnlocalizedName("itemGunRocketLauncher");
        itemBulletRocketLaser = (new ItemWar(13250)).setMaxStackSize(4).setUnlocalizedName("itemBulletRocketLaser");
        itemGunRocketLauncherLaser = (new ItemGunRocketLauncherLaser(13251)).setUnlocalizedName("itemGunRocketLauncherLaser");
        itemBulletHeavy = (new ItemWar(13252)).setMaxStackSize(4).setUnlocalizedName("itemBullet50Cal");
        itemGunSniper = (new ItemGunSniper(13253)).setUnlocalizedName("itemGunSniper");
        itemGrenadeStun = (new ItemGrenadeStun(13254)).setMaxStackSize(4).setUnlocalizedName("itemGrenadeStun");
        itemGrenadeSmoke = (new ItemGrenadeSmoke(13255)).setMaxStackSize(4).setUnlocalizedName("itemGrenadeSmoke");
        itemBulletCasingShell = (new ItemWar(13256)).setUnlocalizedName("itemBulletCasingShell");
        itemOil = (new ItemOil(13257)).setUnlocalizedName("itemOil");
        itemGunFlamethrower = (new ItemGunFlamethrower(13258)).setUnlocalizedName("itemGunFlamethrower");
        itemGrenadeSticky = (new ItemGrenadeSticky(13259)).setMaxStackSize(4).setUnlocalizedName("itemGrenadeSticky");
        itemGunSg552 = (new ItemGunSg552(13260)).setUnlocalizedName("itemGunSg552");
        itemGunMinigun = (new ItemGunMinigun(13261)).setUnlocalizedName("itemGunMinigun");
        itemGunLaser = (new ItemGunLaser(13262)).setUnlocalizedName("itemGunLaser");
        itemGunM4 = (new ItemGunM4(13263)).setUnlocalizedName("itemGunM4");
        itemGrenadeIncendiary = (new ItemGrenadeIncendiary(13264)).setMaxStackSize(4).setUnlocalizedName("itemGrenadeIncendiary");
        itemGrenadeIncendiaryLit = (new ItemGrenadeIncendiary(13265)).setMaxStackSize(4).setCreativeTab(null).setUnlocalizedName("itemGrenadeIncendiaryLit");
        itemScope = (new ItemWar(13277)).setUnlocalizedName("itemScope");
        itemWrench = (new ItemWar(13278)).setMaxStackSize(1).setMaxDamage(15).setUnlocalizedName("itemWrench");
        itemSentry = (new ItemSentry(13279)).setMaxStackSize(1).setMaxDamage(0).setUnlocalizedName("itemSentry");
        itemNightvisionGoggles = (new ItemArmorWar(13281, EnumArmorMaterial.CLOTH, "nightvision", proxy.getArmorIndex("nightvision"), 0)).setUnlocalizedName("itemNightvisionGoggles");
        itemScubaTank = (new ItemArmorWar(13282, EnumArmorMaterial.CLOTH, "scubaTank", proxy.getArmorIndex("scubaTank"), 1)).setUnlocalizedName("itemScubaTank");
        itemGhillieHelmet = (new ItemArmorWar(13283, EnumArmorMaterial.CLOTH, "ghillie", proxy.getArmorIndex("ghillie"), 0)).setUnlocalizedName("itemGhillieHelmet");
        itemGhillieChest = (new ItemArmorWar(13284, EnumArmorMaterial.CLOTH, "ghillie", proxy.getArmorIndex("ghillie"), 1)).setUnlocalizedName("itemGhillieChest");
        itemGhilliePants = (new ItemArmorWar(13285, EnumArmorMaterial.CLOTH, "ghillie", proxy.getArmorIndex("ghillie"), 2)).setUnlocalizedName("itemGhilliePants");
        itemGhillieBoots = (new ItemArmorWar(13286, EnumArmorMaterial.CLOTH, "ghillie", proxy.getArmorIndex("ghillie"), 3)).setUnlocalizedName("itemGhillieBoots");
        itemRope = (new ItemWar(13287)).setUnlocalizedName("itemRope");
        itemGrapplingHook = (new ItemGrapplingHook(13288)).setUnlocalizedName("itemGrapplingHook");
        itemJetPack = (new ItemArmorWar(13289, EnumArmorMaterial.CLOTH, "jetPack", proxy.getArmorIndex("itemJetPack"), 1)).setUnlocalizedName("itemJetPack");
        itemCraftingPack = (new ItemCraftingPack(13290)).setUnlocalizedName("itemCraftingPack");
        itemWoodGrip = (new ItemWar(13291)).setUnlocalizedName("itemWoodGrip");
        itemMetalGrip = (new ItemWar(13292)).setUnlocalizedName("itemMetalGrip");
        itemSmallBarrel = (new ItemWar(13293)).setUnlocalizedName("itemSmallBarrel");
        itemMediumBarrel = (new ItemWar(13294)).setUnlocalizedName("itemMediumBarrel");
        itemLongBarrel = (new ItemWar(13295)).setUnlocalizedName("itemLongBarrel");
        itemFatBarrel = (new ItemWar(13296)).setUnlocalizedName("itemFatBarrel");
        itemFuelTank = (new ItemWar(13297)).setUnlocalizedName("itemFuelTank");
        itemAtv = (new ItemAtv(13298)).setUnlocalizedName("itemAtv");
        itemAtvBody = (new ItemWar(13299)).setUnlocalizedName("itemAtvBody");
        itemAtvWheel = (new ItemWar(13300)).setUnlocalizedName("itemAtvWheel");
        
        GameRegistry.registerItem(itemLightometer, "itemLightometer");
        GameRegistry.registerItem(itemParachute, "itemParachute");
        GameRegistry.registerItem(itemTelescope, "itemTelescope");
        GameRegistry.registerItem(itemBulletCasing, "itemBulletCasing");
        GameRegistry.registerItem(itemBulletLight, "itemBullet9mm");
        GameRegistry.registerItem(itemBulletMedium, "itemBullet357");
        GameRegistry.registerItem(itemBulletShell, "itemBulletShell");
        GameRegistry.registerItem(itemGunAk47, "itemGunAk47");
        GameRegistry.registerItem(itemGunMp5, "itemGunMp5");
        GameRegistry.registerItem(itemGunShotgun, "itemGunShotgun");
        GameRegistry.registerItem(itemGunDeagle, "itemGunDeagle");
        GameRegistry.registerItem(itemGrenade, "itemGrenade");
        GameRegistry.registerItem(itemBulletRocket, "itemBulletRocket");
        GameRegistry.registerItem(itemGunRocketLauncher, "itemGunRocketLauncher");
        GameRegistry.registerItem(itemBulletRocketLaser, "itemBulletRocketLaser");
        GameRegistry.registerItem(itemGunRocketLauncherLaser, "itemGunRocketLauncherLaser");
        GameRegistry.registerItem(itemBulletHeavy, "itemBullet50Cal");
        GameRegistry.registerItem(itemGunSniper, "itemGunSniper");
        GameRegistry.registerItem(itemGrenadeStun, "itemGrenadeStun");
        GameRegistry.registerItem(itemGrenadeSmoke, "itemGrenadeSmoke");
        GameRegistry.registerItem(itemBulletCasingShell, "itemBulletCasingShell");
        GameRegistry.registerItem(itemOil, "itemOil");
        GameRegistry.registerItem(itemGunFlamethrower, "itemGunFlamethrower");
        GameRegistry.registerItem(itemGrenadeSticky, "itemGrenadeSticky");
        GameRegistry.registerItem(itemGunSg552, "itemGunSg552");
        GameRegistry.registerItem(itemGunMinigun, "itemGunMinigun");
        GameRegistry.registerItem(itemGunLaser, "itemGunLaser");
        GameRegistry.registerItem(itemGunM4, "itemGunM4");
        GameRegistry.registerItem(itemGrenadeIncendiary, "itemGrenadeIncendiary");
        GameRegistry.registerItem(itemGrenadeIncendiaryLit, "itemGrenadeIncendiaryLit");
        GameRegistry.registerItem(itemScope, "itemScope");
        GameRegistry.registerItem(itemWrench, "itemWrench");
        GameRegistry.registerItem(itemSentry, "itemSentry");
        GameRegistry.registerItem(itemJetPack, "itemJetPack");
        GameRegistry.registerItem(itemRope, "itemRope");
        GameRegistry.registerItem(itemGrapplingHook, "itemGrapplingHook");
        GameRegistry.registerItem(itemNightvisionGoggles, "itemNightvisionGoggles");
        GameRegistry.registerItem(itemScubaTank, "itemScubaTank");
        GameRegistry.registerItem(itemGhillieHelmet, "itemGhillieHelmet");
        GameRegistry.registerItem(itemGhillieChest, "itemGhillieChest");
        GameRegistry.registerItem(itemGhilliePants, "itemGhilliePants");
        GameRegistry.registerItem(itemGhillieBoots, "itemGhillieBoots");
        GameRegistry.registerItem(itemCraftingPack, "itemCraftingPack");
        GameRegistry.registerItem(itemWoodGrip, "itemWoodGrip");
        GameRegistry.registerItem(itemMetalGrip, "itemMetalGrip");
        GameRegistry.registerItem(itemSmallBarrel, "itemSmallBarrel");
        GameRegistry.registerItem(itemMediumBarrel, "itemMediumBarrel");
        GameRegistry.registerItem(itemLongBarrel, "itemLongBarrel");
        GameRegistry.registerItem(itemFatBarrel, "itemFatBarrel");
        GameRegistry.registerItem(itemAtv, "itemATV");
        GameRegistry.registerItem(itemAtvBody, "itemATVBody");
        GameRegistry.registerItem(itemAtvWheel, "itemATVWheel");
        GameRegistry.registerItem(itemFuelTank, "itemFuelTank");
    }

    public static void useParachute(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if(!WarTools.onGroundOrInWater(world, entityplayer))
        {
            itemstack.damageItem(1, entityplayer);

            if(itemstack.stackSize == 0)
            {
                boolean flag = false;
                int i = 0;

                do {
                    if(i >= entityplayer.inventory.armorInventory.length)
                    {
                        break;
                    }

                    if(entityplayer.inventory.armorInventory[i] == itemstack)
                    {
                        entityplayer.inventory.armorInventory[i] = null;
                        flag = true;
                        break;
                    }

                    i++;
                }
                while(true);

                if(!flag)
                {
                    int j = 0;

                    do {
                        if(j >= entityplayer.inventory.mainInventory.length)
                        {
                            break;
                        }

                        if(entityplayer.inventory.mainInventory[j] == itemstack)
                        {
                            entityplayer.inventory.mainInventory[j] = null;
                            boolean flag1 = true;
                            break;
                        }

                        j++;
                    }
                    while(true);
                }
            }

            world.playSoundAtEntity(entityplayer, "modernwarfare:parachute", 0.5F, 1.0F / (WarTools.random.nextFloat() * 0.1F + 0.95F));

            if(!world.isRemote)
            {
                world.spawnEntityInWorld(new EntityParachute(world, entityplayer));
            }
        }
    }
    
    public static void handleReload()
    {
        for(Iterator iterator = reloadTimes.entrySet().iterator(); iterator.hasNext();)
        {
            Map.Entry entry = (Map.Entry)iterator.next();
            int i = ((Integer)entry.getValue()).intValue() - 1;

            if(i <= 0)
            {
                iterator.remove();
            }
            else {
                entry.setValue(Integer.valueOf(i));
            }
        }
    }

    public static void handleReload(World world, EntityPlayer entityplayer, boolean flag)
    {
        if(!reloadTimes.containsKey(entityplayer))
        {
            ItemStack itemstack = entityplayer.getCurrentEquippedItem();

            if(itemstack != null && (itemstack.getItem() instanceof ItemGun))
            {
                Item item = ((ItemGun)itemstack.getItem()).requiredBullet;
                int i = -1;
                int j = -1;
                int k = -1;
                boolean flag1 = false;

                do {
                    k = -1;
                    InventoryPlayer inventoryplayer = entityplayer.inventory;

                    for(int l = i + 1; l < inventoryplayer.mainInventory.length; l++)
                    {
                        if(inventoryplayer.mainInventory[l] == null || inventoryplayer.mainInventory[l].itemID != item.itemID)
                        {
                            continue;
                        }

                        if(item.getMaxDamage() > 0)
                        {
                            int i1 = item.getMaxDamage() + 1;

                            if(i == -1)
                            {
                                j = i1 - inventoryplayer.mainInventory[l].getItemDamage();

                                if (!flag && item.getMaxDamage() > 0 && j == item.getMaxDamage() + 1)
                                {
                                    break;
                                }
                            }
                            else {
                                if(!flag1)
                                {
                                    reload(world, entityplayer);
                                    flag1 = true;
                                }

                                k = i1 - inventoryplayer.mainInventory[l].getItemDamage();
                                int k1 = Math.min(i1 - j, k);
                                j += k1;
                                k -= k1;
                                inventoryplayer.mainInventory[i].setItemDamage(i1 - j);
                                inventoryplayer.mainInventory[l].setItemDamage(i1 - k);

                                if(k == 0)
                                {
                                    inventoryplayer.mainInventory[l] = new ItemStack(Item.bucketEmpty);
                                }

                                break;
                            }
                        }
                        else if(i == -1)
                        {
                            j = inventoryplayer.mainInventory[l].stackSize;

                            if(!flag && item.getMaxDamage() == 0 && j == item.getItemStackLimit(itemstack))
                            {
                                break;
                            }
                        }
                        else {
                            if(!flag1)
                            {
                                reload(world, entityplayer);
                                flag1 = true;
                            }

                            k = inventoryplayer.mainInventory[l].stackSize;
                            int j1 = Math.min(item.getItemStackLimit(itemstack) - j, k);
                            j += j1;
                            k -= j1;
                            inventoryplayer.mainInventory[i].stackSize = j;
                            inventoryplayer.mainInventory[l].stackSize = k;

                            if(k == 0)
                            {
                                inventoryplayer.mainInventory[l] = null;
                            }

                            break;
                        }

                        if(i == -1)
                        {
                            i = l;
                        }
                    }

                    if(i == -1)
                    {
                        break;
                    }

                    if(flag1 || !flag)
                    {
                        continue;
                    }

                    reload(world, entityplayer);
                    break;
                }
                while(k != -1 && (item.getMaxDamage() != 0 || j != item.getItemStackLimit(itemstack)) && (item.getMaxDamage() <= 0 || j != item.getMaxDamage() + 1));
            }
        }
    }
    
    public static void handleParachuteKey(EntityPlayer entityplayer)
    {
    	MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        ItemStack itemstack = entityplayer.inventory.armorInventory[2];

        if (itemstack != null && itemstack.itemID == itemParachute.itemID)
        {
            for(Object obj : server.getConfigurationManager().playerEntityList)
            {
            	if(obj instanceof EntityPlayerMP)
            	{
            		EntityPlayerMP player = (EntityPlayerMP)obj;
            		useParachute(itemstack, player.worldObj, player);
            	}
            }
        }
    }
    
    public static boolean handleJetPack(World world, EntityPlayerMP entityplayermp)
    {
        ItemStack itemstack = entityplayermp.inventory.armorInventory[2];

        if(itemstack != null && itemstack.itemID == itemJetPack.itemID && !WarTools.onGroundOrInWater(world, entityplayermp) && entityplayermp.ridingEntity == null && isJetpackOn.get(entityplayermp) != null && (Boolean)isJetpackOn.get(entityplayermp) && useJetPackFuel(entityplayermp))
        {
            entityplayermp.motionY = Math.min(entityplayermp.motionY + 0.06D + 0.06D, 0.3D);
            entityplayermp.fallDistance = 0.0F;
            
    		if(world.getWorldTime() - getJetPackSound(entityplayermp) < 0L) 
			{
				jetPackLastSound.put(entityplayermp.username, world.getWorldTime() - 15L);
			}

			if(getJetPackSound(entityplayermp) == 0L || world.getWorldTime() - getJetPackSound(entityplayermp) > 15L) 
			{
				world.playSoundAtEntity(entityplayermp, "modernwarfare:jetpack", 0.25F, 1.0F / (WarTools.random.nextFloat() * 0.1F + 0.95F));
				jetPackLastSound.put(entityplayermp.username, world.getWorldTime());
			}
			
            return true;
        }
        else {
            return false;
        }
    }
    
    public static boolean useJetPackFuel(EntityPlayerMP entityplayermp)
    {
        return WarTools.useItemInInventory(entityplayermp, itemOil.itemID, true) > 0;
    }

    public static void reload(World world, EntityPlayer entityplayer)
    {
        world.playSoundAtEntity(entityplayer, "modernwarfare:reload", 1.0F, 1.0F / (entityplayer.getRNG().nextFloat() * 0.1F + 0.95F));
        reloadTimes.put(entityplayer, Integer.valueOf(40));
    }
    
    public static boolean getSniperZoomedIn(EntityPlayer entityplayer)
    {
        Boolean boolean1 = (Boolean)isSniperZoomedIn.get(entityplayer);
        return boolean1 == null ? false : boolean1.booleanValue();
    }
    
    public static long getJetPackSound(EntityPlayerMP player)
    {
    	if(jetPackLastSound.get(player.username) == null)
    	{
    		return 0;
    	}
    	
    	return jetPackLastSound.get(player.username);
    }
}
