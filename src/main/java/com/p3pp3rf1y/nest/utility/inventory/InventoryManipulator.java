package com.p3pp3rf1y.nest.utility.inventory;


import com.p3pp3rf1y.nest.inventory.slots.IInvSlot;
import com.p3pp3rf1y.nest.utility.InventoryHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class InventoryManipulator
{
	private final IInventory inv;

	public static InventoryManipulator get( IInventory inv )
	{
		return new InventoryManipulator( inv );
	}

	protected InventoryManipulator( IInventory inv )
	{
		this.inv = inv;
	}

	/**
	 * Attempt to add the stack to the inventory.
	 *
	 * @return The remainder
	 */
	public ItemStack addStack( ItemStack stack )
	{
		return addStack( stack, true );
	}

	private ItemStack addStack( ItemStack stack, boolean doAdd )
	{
		if( stack == null || stack.stackSize <= 0 )
		{
			return null;
		}
		stack = stack.copy();
		List<IInvSlot> filledSlots = new ArrayList<IInvSlot>( inv.getSizeInventory() );
		List<IInvSlot> emptySlots = new ArrayList<IInvSlot>( inv.getSizeInventory() );
		for( IInvSlot slot : InventoryIterator.getIterable( inv ) )
		{
			if( slot.canPutStackInSlot( stack ) )
			{
				if( slot.getStackInSlot() == null )
				{
					emptySlots.add( slot );
				}
				else
				{
					filledSlots.add( slot );
				}
			}
		}

		int injected = 0;
		injected = tryPut( filledSlots, stack, injected, doAdd );
		injected = tryPut( emptySlots, stack, injected, doAdd );
		stack.stackSize -= injected;
		if( stack.stackSize <= 0 )
		{
			return null;
		}
		return stack;
	}

	private int tryPut( List<IInvSlot> slots, ItemStack stack, int injected, boolean doAdd )
	{
		if( injected >= stack.stackSize )
		{
			return injected;
		}
		for( IInvSlot slot : slots )
		{
			ItemStack stackInSlot = slot.getStackInSlot();
			if( stackInSlot == null || InventoryHelper.isItemEqual( stackInSlot, stack ) )
			{
				int used = addToSlot( slot, stack, stack.stackSize - injected, doAdd );
				if( used > 0 )
				{
					injected += used;
					if( injected >= stack.stackSize )
					{
						return injected;
					}
				}
			}
		}
		return injected;
	}

	/**
	 * @param available Amount we can move
	 * @return Return the number of items moved.
	 */
	private int addToSlot( IInvSlot slot, ItemStack stack, int available, boolean doAdd )
	{
		int max = Math.min( stack.getMaxStackSize(), inv.getInventoryStackLimit() );

		ItemStack stackInSlot = slot.getStackInSlot();
		if( stackInSlot == null )
		{
			int wanted = Math.min( available, max );
			if( doAdd )
			{
				stackInSlot = stack.copy();
				stackInSlot.stackSize = wanted;
				slot.setStackInSlot( stackInSlot );
			}
			return wanted;
		}

		if( !InventoryHelper.isItemEqual( stack, stackInSlot ) )
		{
			return 0;
		}

		int wanted = max - stackInSlot.stackSize;
		if( wanted <= 0 )
		{
			return 0;
		}

		if( wanted > available )
		{
			wanted = available;
		}

		if( doAdd )
		{
			stackInSlot.stackSize += wanted;
			slot.setStackInSlot( stackInSlot );
		}
		return wanted;
	}
}
