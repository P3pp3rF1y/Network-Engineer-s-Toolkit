package com.p3pp3rf1y.nest.utility;


import com.p3pp3rf1y.nest.utility.inventory.InventoryManipulator;
import com.p3pp3rf1y.nest.utility.inventory.SidedInventoryMapper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;


public abstract class InventoryHelper
{
	/**
	 * Places an ItemStack in a destination IInventory. Will attempt to move as
	 * much of the stack as possible, returning any remainder.
	 *
	 * @param stack The ItemStack to put in the inventory.
	 * @param dest  The destination IInventory.
	 * @return Null if itemStack was completely moved, a new itemStack with
	 * remaining stackSize if part or none of the stack was moved.
	 */
	public static ItemStack moveItemStack( ItemStack stack, IInventory dest )
	{
		InventoryManipulator im = InventoryManipulator.get( dest );
		return im.addStack( stack );
	}

	/**
	 * A more robust item comparison function. Supports items with damage = -1
	 * matching any sub-type.
	 *
	 * @param a An ItemStack
	 * @param b An ItemStack
	 * @return True if equal
	 */
	public static boolean isItemEqual( ItemStack a, ItemStack b )
	{
		return isItemEqual( a, b, true, true );
	}

	public static boolean isItemEqual( final ItemStack a, final ItemStack b, final boolean matchDamage, final boolean matchNBT )
	{
		if( a == null || b == null )
		{
			return false;
		}
		if( a.getItem() != b.getItem() )
		{
			return false;
		}
		if( matchNBT && !ItemStack.areItemStackTagsEqual( a, b ) )
		{
			return false;
		}
		if( matchDamage && a.getHasSubtypes() )
		{
			if( isWildcard( a ) || isWildcard( b ) )
			{
				return true;
			}
			if( a.getItemDamage() != b.getItemDamage() )
			{
				return false;
			}
		}
		return true;
	}

	public static boolean isWildcard( ItemStack stack )
	{
		return isWildcard( stack.getItemDamage() );
	}

	public static boolean isWildcard( int damage )
	{
		return damage == -1 || damage == OreDictionary.WILDCARD_VALUE;
	}

	public static IInventory getInventory( IInventory inv, ForgeDirection side )
	{
		if( inv == null )
		{
			return null;
		}
		if( inv instanceof ISidedInventory )
		{
			inv = new SidedInventoryMapper( (ISidedInventory) inv, side );
		}
		return inv;
	}

}
