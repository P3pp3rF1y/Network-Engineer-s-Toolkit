package com.p3pp3rf1y.nest.utility;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;


public abstract class SlotHelper
{
	public static boolean isSlotInRange( int slotIndex, int start, int count )
	{
		return ( slotIndex >= start ) && ( slotIndex < start + count );
	}

	public static ItemStack transferStackInSlot( List inventorySlots, EntityPlayer player, int slotIndex )
	{
		Slot slot = (Slot) inventorySlots.get( slotIndex );
		if( slot == null || !slot.getHasStack() )
		{
			return null;
		}

		int numSlots = inventorySlots.size();
		ItemStack stackInSlot = slot.getStack();
		ItemStack originalStack = stackInSlot.copy();

		if( !shiftItemStack( inventorySlots, stackInSlot, slotIndex, numSlots ) )
		{
			return null;
		}

		slot.onSlotChange( stackInSlot, originalStack );
		if( stackInSlot.stackSize <= 0 )
		{
			slot.putStack( null );
		}
		else
		{
			slot.onSlotChanged();
		}

		if( stackInSlot.stackSize == originalStack.stackSize )
		{
			return null;
		}

		slot.onPickupFromSlot( player, stackInSlot );
		return originalStack;
	}

	private static boolean shiftItemStack( List inventorySlots, ItemStack stackInSlot, int slotIndex, int numSlots )
	{
		if( isInPlayerInventory( slotIndex ) )
		{
			if( shiftToContainer( inventorySlots, stackInSlot, numSlots ) )
			{
				return true;
			}

			if( isInPlayerHotbar( slotIndex ) )
			{
				return shiftToPlayerInventoryNoHotbar( inventorySlots, stackInSlot );
			}
			else
			{
				return shiftToHotbar( inventorySlots, stackInSlot );
			}
		}
		else
		{
			boolean shifted = shiftToHotbar( inventorySlots, stackInSlot );
			if( !shifted )
			{
				shifted = shiftToPlayerInventoryNoHotbar( inventorySlots, stackInSlot );
			}
			return shifted;
		}
	}

	private static boolean shiftItemStackToRange( List inventorySlots, ItemStack stackToShift, int start, int count )
	{
		boolean changed = false;
		if( stackToShift.isStackable() )
		{
			for( int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < start + count; slotIndex++ )
			{
				Slot slot = (Slot) inventorySlots.get( slotIndex );
				ItemStack stackInSlot = slot.getStack();
				if( stackInSlot != null && ItemStackHelper.isIdenticalItem( stackInSlot, stackToShift ) )
				{
					int resultingStackSize = stackInSlot.stackSize + stackToShift.stackSize;
					int max = Math.min( stackToShift.getMaxStackSize(), slot.getSlotStackLimit() );
					if( resultingStackSize <= max )
					{
						stackToShift.stackSize = 0;
						stackInSlot.stackSize = resultingStackSize;
						slot.onSlotChanged();
						changed = true;
					}
					else if( stackInSlot.stackSize < max )
					{
						stackToShift.stackSize -= max - stackInSlot.stackSize;
						stackInSlot.stackSize = max;
						slot.onSlotChanged();
						changed = true;
					}
				}
			}
		}
		if( stackToShift.stackSize > 0 )
		{
			for( int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < start + count; slotIndex++ )
			{
				Slot slot = (Slot) inventorySlots.get( slotIndex );
				ItemStack stackInSlot = slot.getStack();
				if( stackInSlot == null )
				{
					int max = Math.min( stackToShift.getMaxStackSize(), slot.getSlotStackLimit() );
					stackInSlot = stackToShift.copy();
					stackInSlot.stackSize = Math.min( stackToShift.stackSize, max );
					stackToShift.stackSize -= stackInSlot.stackSize;
					slot.putStack( stackInSlot );
					slot.onSlotChanged();
					changed = true;
				}
			}
		}
		return changed;
	}

	private static final int playerInventorySize = 9 * 4;
	private static final int playerHotbarSize = 9;

	private static boolean isInPlayerInventory( int slotIndex )
	{
		return slotIndex < playerInventorySize;
	}

	private static boolean isInPlayerHotbar( int slotIndex )
	{
		return SlotHelper.isSlotInRange( slotIndex, playerInventorySize - playerHotbarSize, playerInventorySize );
	}

	private static boolean shiftToPlayerInventoryNoHotbar( List inventorySlots, ItemStack stackInSlot )
	{
		int playerHotbarStart = playerInventorySize - playerHotbarSize;
		return shiftItemStackToRange( inventorySlots, stackInSlot, 0, playerHotbarStart );
	}

	private static boolean shiftToHotbar( List inventorySlots, ItemStack stackInSlot )
	{
		int playerHotbarStart = playerInventorySize - playerHotbarSize;
		return shiftItemStackToRange( inventorySlots, stackInSlot, playerHotbarStart, playerHotbarSize );
	}

	private static boolean shiftToContainer( List inventorySlots, ItemStack stackToShift, int numSlots )
	{
		boolean success = false;
		if( stackToShift.isStackable() )
		{
			success = shiftToContainer( inventorySlots, stackToShift, numSlots, true );
		}
		if( stackToShift.stackSize > 0 )
		{
			success |= shiftToContainer( inventorySlots, stackToShift, numSlots, false );
		}
		return success;
	}

	// if mergeOnly = true, don't shift into empty slots.
	private static boolean shiftToContainer( List inventorySlots, ItemStack stackToShift, int numSlots, boolean mergeOnly )
	{
		for( int containerSlotIndex = playerInventorySize; containerSlotIndex < numSlots; containerSlotIndex++ )
		{
			Slot slot = (Slot) inventorySlots.get( containerSlotIndex );
			if( mergeOnly && slot.getStack() == null )
			{
				continue;
			}
			if( !slot.isItemValid( stackToShift ) )
			{
				continue;
			}
			if( shiftItemStackToRange( inventorySlots, stackToShift, containerSlotIndex, 1 ) )
			{
				return true;
			}
		}
		return false;
	}
}
