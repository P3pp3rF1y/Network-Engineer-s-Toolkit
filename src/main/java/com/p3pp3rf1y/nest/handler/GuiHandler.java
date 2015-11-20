package com.p3pp3rf1y.nest.handler;


import com.p3pp3rf1y.nest.client.gui.inventory.GuiBackpack;
import com.p3pp3rf1y.nest.client.gui.inventory.GuiBackpackT2;
import com.p3pp3rf1y.nest.inventory.ContainerBackpack;
import com.p3pp3rf1y.nest.item.ItemNEBackpack;
import com.p3pp3rf1y.nest.reference.GUIs;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement( int ID, EntityPlayer player, World world, int x, int y, int z )
	{
		ItemStack equipped;

		if( ID == GUIs.BACKPACK.ordinal() )
		{
			equipped = getBackpackItem( player );
			if( equipped == null )
			{
				return null;
			}

			return new ContainerBackpack( player, ContainerBackpack.Size.DEFAULT, equipped );
		}
		else if( ID == GUIs.BACKPACK_T2.ordinal() )
		{
			equipped = getBackpackItem( player );
			if( equipped == null )
			{
				return null;
			}

			return new ContainerBackpack( player, ContainerBackpack.Size.T2, equipped );
		}

		return null;
	}

	@Override
	public Object getClientGuiElement( int ID, EntityPlayer player, World world, int x, int y, int z )
	{
		ItemStack equipped;

		if( ID == GUIs.BACKPACK.ordinal() )
		{
			equipped = getBackpackItem( player );
			if( equipped == null )
			{
				return null;
			}
			return new GuiBackpack( new ContainerBackpack( player, ContainerBackpack.Size.DEFAULT, equipped ) );
		}
		else if( ID == GUIs.BACKPACK_T2.ordinal() )
		{
			equipped = getBackpackItem( player );
			if( equipped == null )
			{
				return null;
			}
			return new GuiBackpackT2( new ContainerBackpack( player, ContainerBackpack.Size.T2, equipped ) );
		}
		return null;
	}

	private static ItemStack getBackpackItem( EntityPlayer player )
	{
		ItemStack equipped = player.getCurrentEquippedItem();
		if( equipped == null )
		{
			return null;
		}
		if( equipped.getItem() instanceof ItemNEBackpack )
		{
			return equipped;
		}
		return null;
	}

}
