package bq_standard.core;

import org.apache.logging.log4j.Logger;

import betterquesting.network.PacketTypeRegistry;
import betterquesting.quests.rewards.RewardRegistry;
import betterquesting.quests.tasks.TaskRegistry;
import bq_standard.commands.BQS_Commands;
import bq_standard.core.proxies.CommonProxy;
import bq_standard.handlers.ConfigHandler;
import bq_standard.handlers.GuiHandler;
import bq_standard.items.ItemLootChest;
import bq_standard.network.StandardPacketType;
import bq_standard.network.handlers.PktHandlerCheckbox;
import bq_standard.network.handlers.PktHandlerLootDatabase;
import bq_standard.network.handlers.PktHandlerScoreboard;
import bq_standard.rewards.RewardChoice;
import bq_standard.rewards.RewardCommand;
import bq_standard.rewards.RewardItem;
import bq_standard.rewards.RewardScoreboard;
import bq_standard.rewards.RewardXP;
import bq_standard.tasks.TaskBlockBreak;
import bq_standard.tasks.TaskCheckbox;
import bq_standard.tasks.TaskCrafting;
import bq_standard.tasks.TaskFluid;
import bq_standard.tasks.TaskHunt;
import bq_standard.tasks.TaskLocation;
import bq_standard.tasks.TaskMeeting;
import bq_standard.tasks.TaskRetrieval;
import bq_standard.tasks.TaskScoreboard;
import bq_standard.tasks.TaskXP;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = BQ_Standard.MODID, version = BQ_Standard.VERSION, name = BQ_Standard.NAME, guiFactory = "bq_standard.handlers.ConfigGuiFactory")
public class BQ_Standard {
	public static final String MODID = "bq_standard";
	public static final String HASH = "1.0.108b";
	public static final String BRANCH = "1.0.108b";
	public static final String VERSION = "1.0.108b";
	public static final String NAME = "Standard Expansion";
	public static final String PROXY = "bq_standard.core.proxies";
	public static final String CHANNEL = "BQ_STANDARD";

	@Instance(MODID)
	public static BQ_Standard instance;

	@SidedProxy(clientSide = PROXY + ".ClientProxy", serverSide = PROXY + ".CommonProxy")
	public static CommonProxy proxy;
	public SimpleNetworkWrapper network;
	public static Logger logger;

	public static Item lootChest = new ItemLootChest();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		network = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL);

		ConfigHandler.config = new Configuration(event.getSuggestedConfigurationFile());
		ConfigHandler.initConfigs();

		proxy.registerHandlers();

		PacketTypeRegistry.RegisterType(new PktHandlerLootDatabase(), StandardPacketType.LOOT_SYNC.GetLocation());
		PacketTypeRegistry.RegisterType(new PktHandlerCheckbox(), StandardPacketType.CHECKBOX.GetLocation());
		PacketTypeRegistry.RegisterType(new PktHandlerScoreboard(), StandardPacketType.SCORE_SYNC.GetLocation());

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		GameRegistry.registerItem(lootChest, "loot_chest");

		TaskRegistry.RegisterTask(TaskRetrieval.class, new ResourceLocation(MODID + ":retrieval"));
		TaskRegistry.RegisterTask(TaskHunt.class, new ResourceLocation(MODID + ":hunt"));
		TaskRegistry.RegisterTask(TaskLocation.class, new ResourceLocation(MODID + ":location"));
		TaskRegistry.RegisterTask(TaskCrafting.class, new ResourceLocation(MODID + ":crafting"));
		TaskRegistry.RegisterTask(TaskScoreboard.class, new ResourceLocation(MODID + ":scoreboard"));
		TaskRegistry.RegisterTask(TaskFluid.class, new ResourceLocation(MODID + ":fluid"));
		TaskRegistry.RegisterTask(TaskMeeting.class, new ResourceLocation(MODID + ":meeting"));
		TaskRegistry.RegisterTask(TaskXP.class, new ResourceLocation(MODID + ":xp"));
		TaskRegistry.RegisterTask(TaskBlockBreak.class, new ResourceLocation(MODID + ":block_break"));
		TaskRegistry.RegisterTask(TaskCheckbox.class, new ResourceLocation(MODID + ":checkbox"));

		RewardRegistry.RegisterReward(RewardItem.class, new ResourceLocation(MODID + ":item"));
		RewardRegistry.RegisterReward(RewardChoice.class, new ResourceLocation(MODID + ":choice"));
		RewardRegistry.RegisterReward(RewardScoreboard.class, new ResourceLocation(MODID + ":scoreboard"));
		RewardRegistry.RegisterReward(RewardCommand.class, new ResourceLocation(MODID + ":command"));
		RewardRegistry.RegisterReward(RewardXP.class, new ResourceLocation(MODID + ":xp"));

		proxy.registerThemes();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}

	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		MinecraftServer server = MinecraftServer.getServer();
		ICommandManager command = server.getCommandManager();
		ServerCommandManager manager = (ServerCommandManager) command;

		manager.registerCommand(new BQS_Commands());
	}
}
