package com.p3pp3rf1y.nest.utility.inventory;


import com.p3pp3rf1y.nest.inventory.slots.IInvSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

import java.util.Iterator;


public class InventoryIterator implements Iterable<IInvSlot>
{

	public static Iterable<IInvSlot> getIterable( IInventory inv )
	{
		if( inv instanceof ISidedInventory )
		{
			return new SidedInventoryIterator( (ISidedInventory) inv );
		}
		return new InventoryIterator( inv );
	}

	private final IInventory inv;
	private final int invSize;

	private InventoryIterator( IInventory inv )
	{
		this.inv = inv;
		this.invSize = inv != null ? inv.getSizeInventory() : 0;
	}

	@Override
	public Iterator<IInvSlot> iterator()
	{
		return new Iterator<IInvSlot>()
		{
			int slot = 0;

			@Override
			public boolean hasNext()
			{
				return slot < invSize;
			}

			@Override
			public IInvSlot next()
			{
				return new InvSlot( slot++ );
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
			return inv.isItemValidForSlot( slot, stack );
		}
	}
}
