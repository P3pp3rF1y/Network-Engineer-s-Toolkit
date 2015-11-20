package com.p3pp3rf1y.nest.creativetab;


import com.p3pp3rf1y.nest.init.ModItems;
import com.p3pp3rf1y.nest.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;


public class CreativeTab
{
	public static final CreativeTabs NEST_TAB = new CreativeTabs( Reference.MOD_ID.toLowerCase() )
	{
		@Override
		public Item getTabIconItem()
		{
			return ModItems.backpackT1;
		}
	};
}
