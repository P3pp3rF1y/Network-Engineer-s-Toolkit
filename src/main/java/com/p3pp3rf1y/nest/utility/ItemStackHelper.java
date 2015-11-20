package com.p3pp3rf1y.nest.utility;


import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;


public abstract class ItemStackHelper
{
	/**
	 * Compares item id, damage and NBT. Accepts wildcard damage.
	 */
	public static boolean isIdenticalItem( ItemStack lhs, ItemStack rhs )
	{
		if( lhs == null || rhs == null )
		{
			return false;
		}

		if( lhs.getItem() != rhs.getItem() )
		{
			return false;
		}

		if( lhs.getItemDamage() != OreDictionary.WILDCARD_VALUE )
		{
			if( lhs.getItemDamage() != rhs.getItemDamage() )
			{
				return false;
			}
		}

		return ItemStack.areItemStackTagsEqual( lhs, rhs );
	}
}
