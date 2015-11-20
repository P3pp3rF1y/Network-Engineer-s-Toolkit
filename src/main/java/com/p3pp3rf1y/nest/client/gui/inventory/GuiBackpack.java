package com.p3pp3rf1y.nest.client.gui.inventory;


import com.p3pp3rf1y.nest.inventory.ContainerBackpack;
import com.p3pp3rf1y.nest.reference.Textures;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;


public class GuiBackpack extends GuiNEsT<ContainerBackpack, IInventory>
{

	public GuiBackpack( ContainerBackpack container )
	{
		this( Textures.Gui.BACKPACK, container );
	}

	protected GuiBackpack( ResourceLocation textureLocation, ContainerBackpack container )
	{
		super( textureLocation, container, null );
	}

	@Override
	protected boolean checkHotbarKeys( int key )
	{
		return false;
	}
}
