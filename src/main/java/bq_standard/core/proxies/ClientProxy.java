package bq_standard.core.proxies;

import java.awt.Color;

import betterquesting.client.themes.ThemeRegistry;
import betterquesting.client.themes.ThemeStandard;
import betterquesting.importers.ImporterRegistry;
import betterquesting.network.PacketTypeRegistry;
import bq_standard.importers.NativeFileImporter;
import bq_standard.importers.NativeUrlImporter;
import bq_standard.importers.hqm.HQMBagImporter;
import bq_standard.importers.hqm.HQMQuestImporter;
import bq_standard.network.StandardPacketType;
import bq_standard.network.handlers.PktHandlerLootClaim;
import net.minecraft.util.ResourceLocation;

public class ClientProxy extends CommonProxy
{
	@Override
	public boolean isClient()
	{
		return true;
	}

	@Override
	public void registerHandlers()
	{
		super.registerHandlers();

    	ImporterRegistry.registerImporter(HQMQuestImporter.instance);
    	ImporterRegistry.registerImporter(HQMBagImporter.instance);
    	ImporterRegistry.registerImporter(NativeFileImporter.instance);
    	ImporterRegistry.registerImporter(NativeUrlImporter.instance);

    	PacketTypeRegistry.RegisterType(new PktHandlerLootClaim(), StandardPacketType.LOOT_CLAIM.GetLocation());
	}

	@Override
	public void registerThemes()
	{
		ThemeRegistry.RegisterTheme(new ThemeStandard("Standard Light", new ResourceLocation("betterquesting", "textures/gui/editor_gui.png")), "light");
		ThemeRegistry.RegisterTheme(new ThemeStandard("Standard Dark", new ResourceLocation("bq_standard", "textures/gui/editor_gui_dark.png")).setTextColor(Color.WHITE), "dark");
		ThemeRegistry.RegisterTheme(new ThemeStandard("Stronghold", new ResourceLocation("bq_standard", "textures/gui/editor_gui_stronghold.png")).setTextColor(Color.WHITE), "stronghold");
		ThemeRegistry.RegisterTheme(new ThemeStandard("Overworld", new ResourceLocation("bq_standard", "textures/gui/editor_gui_overworld.png")).setTextColor(Color.WHITE), "overworld");
		ThemeRegistry.RegisterTheme(new ThemeStandard("Nether", new ResourceLocation("bq_standard", "textures/gui/editor_gui_nether.png")).setTextColor(Color.WHITE), "nether");
		ThemeRegistry.RegisterTheme(new ThemeStandard("End", new ResourceLocation("bq_standard", "textures/gui/editor_gui_end.png")).setTextColor(Color.WHITE), "end");
		ThemeRegistry.RegisterTheme(new ThemeStandard("Vanilla", new ResourceLocation("bq_standard", "textures/gui/editor_gui_vanilla.png")), "vanilla");
	}
}
