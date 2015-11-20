package com.p3pp3rf1y.nest;


import com.p3pp3rf1y.nest.configuration.ConfigurationHandler;
import com.p3pp3rf1y.nest.handler.GuiHandler;
import com.p3pp3rf1y.nest.handler.ItemPickupHandler;
import com.p3pp3rf1y.nest.init.ModItems;
import com.p3pp3rf1y.nest.proxy.IProxy;
import com.p3pp3rf1y.nest.reference.Reference;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraftforge.common.MinecraftForge;


@Mod( modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION )
public class NEsT
{
	@Mod.Instance( "NEsT" )
	public static NEsT instance;

	@SidedProxy( clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS )
	public static IProxy proxy;

	@Mod.EventHandler
	public void preInit( FMLPreInitializationEvent event )
	{
		MinecraftForge.EVENT_BUS.register( new ItemPickupHandler() );

		ConfigurationHandler.init( event.getSuggestedConfigurationFile() );

		ModItems.init();
	}

	@Mod.EventHandler
	public void init( FMLInitializationEvent event )
	{
		NetworkRegistry.INSTANCE.registerGuiHandler( instance, new GuiHandler() );

		proxy.registerEventHandlers();

	}

	@Mod.EventHandler
	public void postInit( FMLPostInitializationEvent event )
	{

	}
}
