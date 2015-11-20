package com.p3pp3rf1y.nest.client.gui.inventory;


import com.p3pp3rf1y.nest.inventory.ContainerBackpack;
import com.p3pp3rf1y.nest.reference.Textures;


public class GuiBackpackT2 extends GuiBackpack
{

	public GuiBackpackT2( ContainerBackpack container )
	{
		super( Textures.Gui.BACKPACK_T2, container );

		xSize = 176;
		ySize = 192;
	}
}
