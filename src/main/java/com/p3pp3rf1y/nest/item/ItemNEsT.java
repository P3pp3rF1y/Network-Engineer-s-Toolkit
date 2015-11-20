package com.p3pp3rf1y.nest.item;


import com.p3pp3rf1y.nest.creativetab.CreativeTab;
import com.p3pp3rf1y.nest.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class ItemNEsT extends Item
{
	public ItemNEsT()
	{
		super();
		this.setCreativeTab( CreativeTab.NEST_TAB );
	}

	@Override
	public String getUnlocalizedName()
	{
		return String.format( "item.%s:%s", Reference.MOD_ID.toLowerCase(), getUnwrappedUnlocalizedName( super.getUnlocalizedName() ) );
	}

	@Override
	public String getUnlocalizedName( ItemStack itemStack )
	{
		return String.format( "item.%s:%s", Reference.MOD_ID.toLowerCase(), getUnwrappedUnlocalizedName( super.getUnlocalizedName() ) );
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void registerIcons( IIconRegister iconRegister )
	{
		itemIcon = iconRegister.registerIcon( this.getUnlocalizedName().substring( this.getUnlocalizedName().indexOf( "." ) + 1 ) );
	}

	protected String getUnwrappedUnlocalizedName( String unlocalizedName )
	{
		return unlocalizedName.substring( unlocalizedName.indexOf( "." ) + 1 );
	}
}
