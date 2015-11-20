package com.p3pp3rf1y.nest.handler;


import com.p3pp3rf1y.nest.inventory.ContainerBackpack;
import com.p3pp3rf1y.nest.item.BackpackDefinition;
import com.p3pp3rf1y.nest.item.ItemNEBackpack;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;


public class ItemPickupHandler
{
	@SubscribeEvent
	public void handleItemPickup( EntityItemPickupEvent event )
	{

		if( event.isCanceled() )
		{
			return;
		}

		if( onItemPickup( event.entityPlayer, event.item ) )
		{
			event.setResult( Event.Result.ALLOW );
			return;
		}
	}

	public boolean onItemPickup( EntityPlayer player, EntityItem entityitem )
	{

		ItemStack itemstack = entityitem.getEntityItem();
		if( itemstack == null || itemstack.stackSize <= 0 )
		{
			return false;
		}

		// Do not pick up if a backpack is open
		if( player.openContainer instanceof ContainerBackpack )
		{
			return false;
		}

		// Make sure to top off manually placed itemstacks in player inventory first
		topOffPlayerInventory( player, itemstack );

		for( ItemStack pack : player.inventory.mainInventory )
		{

			if( pack == null || pack.stackSize <= 0 )
			{
				continue;
			}

			if( itemstack.stackSize <= 0 )
			{
				break;
			}

			if( !( pack.getItem() instanceof ItemNEBackpack ) )
			{
				continue;
			}

			ItemNEBackpack backpack = ( (ItemNEBackpack) pack.getItem() );
			BackpackDefinition backpackDefinition = backpack.getDefinition();
			if( backpackDefinition.isValidItem( itemstack ) )
			{
				ItemNEBackpack.tryStowing( player, pack, itemstack );
			}
		}

		return itemstack.stackSize == 0;
	}

	/**
	 * This tops off existing stacks in the player's inventory. That way you can keep f.e. a stack of dirt or cobblestone in your inventory which gets refreshed
	 * constantly by picked up items.
	 */
	private static void topOffPlayerInventory( EntityPlayer player, ItemStack itemstack )
	{

		// Add to player inventory first, if there is an incomplete stack in
		// there.
		for( int i = 0; i < player.inventory.getSizeInventory(); i++ )
		{
			ItemStack inventoryStack = player.inventory.getStackInSlot( i );
			// We only add to existing stacks.
			if( inventoryStack == null )
			{
				continue;
			}

			// Already full
			if( inventoryStack.stackSize >= inventoryStack.getMaxStackSize() )
			{
				continue;
			}

			if( inventoryStack.isItemEqual( itemstack ) && ItemStack.areItemStackTagsEqual( inventoryStack, itemstack ) )
			{
				int space = inventoryStack.getMaxStackSize() - inventoryStack.stackSize;

				// Enough space to add all
				if( space > itemstack.stackSize )
				{
					inventoryStack.stackSize += itemstack.stackSize;
					itemstack.stackSize = 0;
					break;
					// Only part can be added
				}
				else
				{
					inventoryStack.stackSize = inventoryStack.getMaxStackSize();
					itemstack.stackSize -= space;
				}
			}
		}
	}
}
