package com.p3pp3rf1y.nest.handler;


import com.p3pp3rf1y.nest.inventory.ItemInventory;
import com.p3pp3rf1y.nest.inventory.ItemInventoryBackpack;
import com.p3pp3rf1y.nest.item.ItemNEBackpack;
import com.p3pp3rf1y.nest.reference.BackpackMode;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class BackpackResupplyHandler
{

	@SubscribeEvent
	public void onWorldTick( TickEvent.WorldTickEvent event )
	{
		if( event.phase != TickEvent.Phase.END )
		{
			return;
		}

		for( Object obj : event.world.playerEntities )
		{
			EntityPlayer player = (EntityPlayer) obj;
			resupply( player );
		}
	}

	private static List<ItemStack> backpacks( InventoryPlayer playerInventory )
	{
		List<ItemStack> backpacks = new ArrayList<ItemStack>();
		for( ItemStack itemStack : playerInventory.mainInventory )
		{
			if( itemStack != null && itemStack.stackSize > 0 && ( itemStack.getItem() instanceof ItemNEBackpack ) )
			{
				backpacks.add( itemStack );
			}
		}
		return backpacks;
	}

	public void resupply( EntityPlayer player )
	{

		// Do not attempt resupplying if this backpack is already opened.
		if( !( player.openContainer instanceof ContainerPlayer ) )
		{
			return;
		}

		for( ItemStack backpack : backpacks( player.inventory ) )
		{

			// Only handle those in resupply mode
			if( ItemNEBackpack.getMode( backpack ) != BackpackMode.RESUPPLY )
			{
				continue;
			}

			// Delay before resupplying
			if( backpack.getItemDamage() < 40 )
			{
				backpack.setItemDamage( backpack.getItemDamage() + 1 );
				continue;
			}

			// Load their inventory
			ItemNEBackpack backpackItem = ( (ItemNEBackpack) backpack.getItem() );
			ItemInventory backpackInventory = new ItemInventoryBackpack( player, backpackItem.getBackpackSize(), backpack );

			// Cycle through their contents
			for( int i = 0; i < backpackInventory.getSizeInventory(); i++ )
			{

				ItemStack itemStack = backpackInventory.getStackInSlot( i );
				if( itemStack == null || itemStack.stackSize <= 0 )
				{
					continue;
				}

				// Try to add it to the player's inventory and note any change
				boolean change = topOffPlayerInventory( player, itemStack );

				if( change )
				{
					backpackInventory.setInventorySlotContents( i, itemStack );
				}
			}
		}
	}

	/**
	 * This tops off existing stacks in the player's inventory.
	 */
	private static boolean topOffPlayerInventory( EntityPlayer player, ItemStack itemstack )
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
				inventoryStack.stackSize++;
				itemstack.stackSize--;
				return true;
			}
		}
		return false;

	}
}
