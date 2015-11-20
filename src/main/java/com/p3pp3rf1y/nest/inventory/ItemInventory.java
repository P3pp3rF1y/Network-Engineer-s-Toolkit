package com.p3pp3rf1y.nest.inventory;


import com.p3pp3rf1y.nest.inventory.slots.IFilterSlotDelegate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;


public abstract class ItemInventory implements IInventory, IFilterSlotDelegate
{

	private static final String KEY_SLOTS = "Slots";
	private static final String KEY_UID = "UID";
	private static final Random rand = new Random();

	private final EntityPlayer player;
	private final ItemStack parent;
	private final ItemStack[] inventoryStacks;

	public ItemInventory( EntityPlayer player, int size, ItemStack parent )
	{
		this.player = player;
		this.parent = parent;
		this.inventoryStacks = new ItemStack[size];

		setUID(); // Set a uid to identify the itemstack on SMP

		readFromNBT( parent.getTagCompound() );
	}

	public static int getOccupiedSlotCount( ItemStack itemStack )
	{
		NBTTagCompound nbt = itemStack.getTagCompound();
		if( nbt == null )
		{
			return 0;
		}

		if( nbt.hasKey( KEY_SLOTS ) )
		{
			NBTTagCompound slotNbt = nbt.getCompoundTag( KEY_SLOTS );
			return slotNbt.func_150296_c().size();
		}

		return 0;
	}

	private void setUID()
	{
		ItemStack parent = getParent();

		if( parent.getTagCompound() == null )
		{
			parent.setTagCompound( new NBTTagCompound() );
		}

		NBTTagCompound nbt = parent.getTagCompound();
		if( !nbt.hasKey( KEY_UID ) )
		{
			nbt.setInteger( KEY_UID, rand.nextInt() );
		}
	}

	protected ItemStack getParent()
	{
		ItemStack equipped = player.getCurrentEquippedItem();
		if( isSameItemInventory( equipped, parent ) )
		{
			return equipped;
		}
		return parent;
	}

	private static boolean isSameItemInventory( ItemStack base, ItemStack comparison )
	{
		if( base == null || comparison == null )
		{
			return false;
		}

		if( base.getItem() != comparison.getItem() )
		{
			return false;
		}

		if( !base.hasTagCompound() || !comparison.hasTagCompound() )
		{
			return false;
		}

		String baseUID = base.getTagCompound().getString( KEY_UID );
		String comparisonUID = comparison.getTagCompound().getString( KEY_UID );
		return baseUID != null && comparisonUID != null && baseUID.equals( comparisonUID );
	}

	public void readFromNBT( NBTTagCompound nbt )
	{

		if( nbt == null )
		{
			return;
		}

		if( nbt.hasKey( KEY_SLOTS ) )
		{
			NBTTagCompound nbtSlots = nbt.getCompoundTag( KEY_SLOTS );
			for( int i = 0; i < inventoryStacks.length; i++ )
			{
				String slotKey = getSlotNBTKey( i );
				if( nbtSlots.hasKey( slotKey ) )
				{
					NBTTagCompound itemNbt = nbtSlots.getCompoundTag( slotKey );
					ItemStack itemStack = ItemStack.loadItemStackFromNBT( itemNbt );
					inventoryStacks[i] = itemStack;
				}
				else
				{
					inventoryStacks[i] = null;
				}
			}
		}
	}

	private void writeToParentNBT()
	{
		ItemStack parent = getParent();
		if( parent == null )
		{
			return;
		}

		NBTTagCompound nbt = parent.getTagCompound();
		NBTTagCompound slotsNbt = new NBTTagCompound();
		for( int i = 0; i < getSizeInventory(); i++ )
		{
			ItemStack itemStack = getStackInSlot( i );
			if( itemStack != null )
			{
				String slotKey = getSlotNBTKey( i );
				NBTTagCompound itemNbt = new NBTTagCompound();
				itemStack.writeToNBT( itemNbt );
				slotsNbt.setTag( slotKey, itemNbt );
			}
		}

		nbt.setTag( KEY_SLOTS, slotsNbt );
	}

	private static String getSlotNBTKey( int i )
	{
		return Integer.toString( i, Character.MAX_RADIX );
	}

	@Override
	public ItemStack decrStackSize( int i, int j )
	{
		ItemStack stack = getStackInSlot( i );
		if( stack == null )
		{
			return null;
		}

		if( stack.stackSize <= j )
		{
			setInventorySlotContents( i, null );
			return stack;
		}
		else
		{
			ItemStack product = stack.splitStack( j );
			setInventorySlotContents( i, stack );
			return product;
		}
	}

	@Override
	public void setInventorySlotContents( int i, ItemStack itemstack )
	{
		if( itemstack != null && itemstack.stackSize == 0 )
		{
			itemstack = null;
		}

		inventoryStacks[i] = itemstack;

		ItemStack parent = getParent();

		NBTTagCompound nbt = parent.getTagCompound();
		if( nbt == null )
		{
			nbt = new NBTTagCompound();
			parent.setTagCompound( nbt );
		}

		NBTTagCompound slotNbt;
		if( !nbt.hasKey( KEY_SLOTS ) )
		{
			slotNbt = new NBTTagCompound();
			nbt.setTag( KEY_SLOTS, slotNbt );
		}
		else
		{
			slotNbt = nbt.getCompoundTag( KEY_SLOTS );
		}

		String slotKey = getSlotNBTKey( i );

		if( itemstack == null )
		{
			slotNbt.removeTag( slotKey );
		}
		else
		{
			NBTTagCompound itemNbt = new NBTTagCompound();
			itemstack.writeToNBT( itemNbt );

			slotNbt.setTag( slotKey, itemNbt );
		}
	}

	@Override
	public ItemStack getStackInSlot( int i )
	{
		return inventoryStacks[i];
	}

	@Override
	public int getSizeInventory()
	{
		return inventoryStacks.length;
	}

	@Override
	public String getInventoryName()
	{
		return "BeeBag";
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public final void markDirty()
	{
		writeToParentNBT();
	}

	@Override
	public boolean isUseableByPlayer( EntityPlayer entityplayer )
	{
		return true;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
	}

	@Override
	public boolean isItemValidForSlot( int slotIndex, ItemStack itemStack )
	{
		return canSlotAccept( slotIndex, itemStack );
	}

	@Override
	public void openInventory()
	{
	}

	@Override
	public void closeInventory()
	{
	}

	@Override
	public ItemStack getStackInSlotOnClosing( int slot )
	{
		ItemStack toReturn = getStackInSlot( slot );

		if( toReturn != null )
		{
			setInventorySlotContents( slot, null );
		}

		return toReturn;
	}

	/* Filter Slot Delegate */
	@Override
	public boolean canSlotAccept( int slotIndex, ItemStack itemStack )
	{
		return true;
	}

	@Override
	public boolean isLocked( int slotIndex )
	{
		return false;
	}
}
