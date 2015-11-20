package com.p3pp3rf1y.nest.item;


import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BackpackDefinition
{

	private final String name;

	private final int primaryColor;
	private final int secondaryColor;

	private final Set<String> validItemStacks = new HashSet<String>();
	private final Set<Class> validItemClasses = new HashSet<Class>();
	private final Set<Class> validBlockClasses = new HashSet<Class>();

	public BackpackDefinition( String name, int primaryColor )
	{
		this( name, primaryColor, 0xffffff );
	}

	public BackpackDefinition( String name, int primaryColor, int secondaryColor )
	{
		this.name = name;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
	}

	public String getName( ItemStack backpack )
	{
		Item item = backpack.getItem();
		String display = ( "" + StatCollector.translateToLocal( item.getUnlocalizedNameInefficiently( backpack ) + ".name" ) ).trim();

		if( backpack.stackTagCompound != null && backpack.stackTagCompound.hasKey( "display", 10 ) )
		{
			NBTTagCompound nbt = backpack.stackTagCompound.getCompoundTag( "display" );

			if( nbt.hasKey( "Name", 8 ) )
			{
				display = nbt.getString( "Name" );
			}
		}

		return display;
	}

	public int getPrimaryColour()
	{
		return primaryColor;
	}

	public int getSecondaryColour()
	{
		return secondaryColor;
	}

	public void addValidItem( ItemStack validItem )
	{
		if( validItem == null )
		{
			return;
		}

		Item item = validItem.getItem();
		if( item == null )
		{
			return;
		}

		String itemStackString = GameData.getItemRegistry().getNameForObject( item );

		int meta = validItem.getItemDamage();
		if( meta != OreDictionary.WILDCARD_VALUE )
		{
			itemStackString = itemStackString + ':' + meta;
		}

		this.validItemStacks.add( itemStackString );
	}

	public void addValidItems( List<ItemStack> validItems )
	{
		for( ItemStack validItem : validItems )
		{
			addValidItem( validItem );
		}
	}

	public void addValidItemClass( Class itemClass )
	{
		if( itemClass != null )
		{
			validItemClasses.add( itemClass );
		}
	}

	public void addValidItemClasses( List<Class> itemClasses )
	{
		for( Class itemClass : itemClasses )
		{
			addValidItemClass( itemClass );
		}
	}

	public void addValidBlockClass( Class blockClass )
	{
		if( blockClass != null )
		{
			validBlockClasses.add( blockClass );
		}
	}

	public void addValidBlockClasses( List<Class> blockClasses )
	{
		for( Class blockClass : blockClasses )
		{
			addValidBlockClass( blockClass );
		}
	}

	public boolean isValidItem( ItemStack itemStack )
	{
		if( itemStack == null )
		{
			return false;
		}

		Item item = itemStack.getItem();
		if( item == null )
		{
			return false;
		}

		String itemStackStringWild = GameData.getItemRegistry().getNameForObject( item );
		if( validItemStacks.contains( itemStackStringWild ) )
		{
			return true;
		}

		int meta = itemStack.getItemDamage();
		if( meta != OreDictionary.WILDCARD_VALUE )
		{
			String itemStackString = itemStackStringWild + ':' + meta;
			if( validItemStacks.contains( itemStackString ) )
			{
				return true;
			}
		}

		for( Class itemClass : validItemClasses )
		{
			if( itemClass.isInstance( item ) )
			{
				validItemStacks.add( itemStackStringWild );
				return true;
			}
		}

		Block block = Block.getBlockFromItem( item );
		if( block != null )
		{
			for( Class blockClass : validBlockClasses )
			{
				if( blockClass.isInstance( block ) )
				{
					validItemStacks.add( itemStackStringWild );
					return true;
				}
			}
		}

		return false;
	}

}
