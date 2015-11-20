package com.p3pp3rf1y.nest.utility;


import com.p3pp3rf1y.nest.utility.inventory.ChestWrapper;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.common.util.ForgeDirection;


public abstract class TileHelper
{
	public static IInventory getInventoryFromTile( TileEntity tile, ForgeDirection side )
	{
		if( tile == null || !( tile instanceof IInventory ) )
		{
			return null;
		}

		if( tile instanceof TileEntityChest )
		{
			TileEntityChest chest = (TileEntityChest) tile;
			return new ChestWrapper( chest );
		}
		return InventoryHelper.getInventory( (IInventory) tile, side );
	}
}
