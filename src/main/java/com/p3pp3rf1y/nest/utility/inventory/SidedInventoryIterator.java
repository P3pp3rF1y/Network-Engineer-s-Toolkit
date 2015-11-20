package com.p3pp3rf1y.nest.utility.inventory;


import com.p3pp3rf1y.nest.inventory.slots.IInvSlot;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

import java.util.Iterator;


public class SidedInventoryIterator implements Iterable<IInvSlot>
{

	private final ISidedInventory inv;

	SidedInventoryIterator( ISidedInventory inv )
	{
		this.inv = inv;
	}

	@Override
	public Iterator<IInvSlot> iterator()
	{
		return new Iterator<IInvSlot>()
		{
			final int[] slots = inv.getAccessibleSlotsFromSide( 0 );
			int index = 0;

			@Override
			public boolean hasNext()
			{
				return slots != null && index < slots.length;
			}

			@Override
			public IInvSlot next()
			{
				return new InvSlot( slots[index++] );
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException( "Remove not supported." );
			}

		};
	}

	private class InvSlot implements IInvSlot
	{

		private final int slot;

		public InvSlot( int slot )
		{
			this.slot = slot;
		}

		@Override
		public ItemStack getStackInSlot()
		{
			return inv.getStackInSlot( slot );
		}

		@Override
		public void setStackInSlot( ItemStack stack )
		{
			inv.setInventorySlotContents( slot, stack );
		}

		@Override
		public boolean canPutStackInSlot( ItemStack stack )
		{
			return inv.canInsertItem( slot, stack, 0 );
		}
	}
}
