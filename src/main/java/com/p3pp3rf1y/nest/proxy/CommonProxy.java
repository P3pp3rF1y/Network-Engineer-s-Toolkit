package com.p3pp3rf1y.nest.proxy;


import com.p3pp3rf1y.nest.handler.BackpackResupplyHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;


public abstract class CommonProxy implements IProxy
{
	public void registerEventHandlers()
	{
		BackpackResupplyHandler backpackResupplyHandler = new BackpackResupplyHandler();

		FMLCommonHandler.instance().bus().register( backpackResupplyHandler );
		MinecraftForge.EVENT_BUS.register( backpackResupplyHandler );
	}
}