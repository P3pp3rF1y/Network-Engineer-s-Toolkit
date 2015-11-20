package com.p3pp3rf1y.nest.item;


import com.p3pp3rf1y.nest.inventory.ContainerItemInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;


public abstract class ItemInventoried extends ItemNEsT
{

	@Override
	public boolean onDroppedByPlayer( ItemStack itemstack, EntityPlayer player )
	{
		if( itemstack != null &&
				player instanceof EntityPlayerMP &&
				player.openContainer instanceof ContainerItemInventory )
		{
			player.closeScreen();
		}

		return super.onDroppedByPlayer( itemstack, player );
	}
}
