package com.p3pp3rf1y.nest.inventory.slots;


import net.minecraft.item.ItemStack;


public interface IInvSlot
{
	boolean canPutStackInSlot( ItemStack stack );

	ItemStack getStackInSlot();

	void setStackInSlot( ItemStack stack );
}
