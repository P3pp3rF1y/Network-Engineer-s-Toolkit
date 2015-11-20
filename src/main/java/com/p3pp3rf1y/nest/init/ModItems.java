package com.p3pp3rf1y.nest.init;


import com.p3pp3rf1y.nest.item.ItemNEBackpack;
import com.p3pp3rf1y.nest.item.ItemNEsT;
import com.p3pp3rf1y.nest.reference.BackpackType;
import com.p3pp3rf1y.nest.reference.Names;
import cpw.mods.fml.common.registry.GameRegistry;


public class ModItems
{
	public static final ItemNEsT backpackT1 = new ItemNEBackpack( BackpackType.T1 );
	public static final ItemNEsT backpackT2 = new ItemNEBackpack( BackpackType.T2 );

	public static void init()
	{
		GameRegistry.registerItem( backpackT1, Names.Items.BACKPACK_T1 );
		GameRegistry.registerItem( backpackT2, Names.Items.BACKPACK_T2 );

	}
}
