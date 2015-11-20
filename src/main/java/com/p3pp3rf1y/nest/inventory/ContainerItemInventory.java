package com.p3pp3rf1y.nest.inventory;


import com.p3pp3rf1y.nest.utility.SlotHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public abstract class ContainerItemInventory<I extends ItemInventory> extends Container
{

	protected final I inventory;

	protected ContainerItemInventory( I inventory, InventoryPlayer playerInventory, int xInv, int yInv )
	{
		this.inventory = inventory;

		addPlayerInventory( playerInventory, xInv, yInv );
	}

	protected final void addPlayerInventory( InventoryPlayer playerInventory, int xInv, int yInv )
	{
		// Player inventory
		for( int row = 0; row < 3; row++ )
		{
			for( int column = 0; column < 9; column++ )
			{
				addSlotToContainer( new Slot( playerInventory, column + row * 9 + 9, xInv + column * 18, yInv + row * 18 ) );
			}
		}
		// Player hotbar
		for( int column = 0; column < 9; column++ )
		{
			addHotbarSlot( playerInventory, column, xInv + column * 18, yInv + 58 );
		}
	}

	protected void addHotbarSlot( InventoryPlayer playerInventory, int slot, int x, int y )
	{
		addSlotToContainer( new Slot( playerInventory, slot, x, y ) );
	}

	@Override
	public final ItemStack transferStackInSlot( EntityPlayer player, int slotIndex )
	{
		return SlotHelper.transferStackInSlot( inventorySlots, player, slotIndex );
	}

	@Override
	public final boolean canInteractWith( EntityPlayer entityplayer )
	{
		return inventory.isUseableByPlayer( entityplayer );
	}
}
