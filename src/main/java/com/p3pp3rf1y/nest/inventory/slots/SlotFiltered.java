package com.p3pp3rf1y.nest.inventory.slots;


import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


/**
 * Slot which only takes specific items, specified by the IFilterSlotDelegate.
 */
public class SlotFiltered extends Slot
{

	private final IFilterSlotDelegate filterSlotDelegate;

	public <T extends IInventory & IFilterSlotDelegate> SlotFiltered( T filterSlotDelegateInventory, int slotIndex, int xPos, int yPos )
	{
		this( filterSlotDelegateInventory, filterSlotDelegateInventory, slotIndex, xPos, yPos );
	}

	public SlotFiltered( IFilterSlotDelegate filterSlotDelegate, IInventory inventory, int slotIndex, int xPos, int yPos )
	{
		super( inventory, slotIndex, xPos, yPos );
		this.filterSlotDelegate = filterSlotDelegate;
	}

	@Override
	public boolean isItemValid( ItemStack itemstack )
	{
		int slotIndex = getSlotIndex();
		if( filterSlotDelegate.isLocked( slotIndex ) )
		{
			return false;
		}
		if( itemstack != null )
		{
			return filterSlotDelegate.canSlotAccept( slotIndex, itemstack );
		}
		return true;
	}
}
