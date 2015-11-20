package com.p3pp3rf1y.nest.inventory;


import com.p3pp3rf1y.nest.item.BackpackDefinition;
import com.p3pp3rf1y.nest.item.ItemNEBackpack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class ItemInventoryBackpack extends ItemInventory
{

	private final BackpackDefinition backpackDefinition;

	public ItemInventoryBackpack( EntityPlayer player, int size, ItemStack parent )
	{
		super( player, size, parent );

		if( parent == null )
		{
			throw new IllegalArgumentException( "Parent cannot be null." );
		}

		Item item = parent.getItem();
		if( !( item instanceof ItemNEBackpack ) )
		{
			throw new IllegalArgumentException( "Parent must be a backpack." );
		}

		this.backpackDefinition = ( (ItemNEBackpack) item ).getDefinition();

		if( this.backpackDefinition == null )
		{
			throw new IllegalArgumentException( "Backpack must have a backpack definition." );
		}
	}

	@Override
	public boolean canSlotAccept( int slotIndex, ItemStack itemStack )
	{
		return backpackDefinition.isValidItem( itemStack );
	}
}
