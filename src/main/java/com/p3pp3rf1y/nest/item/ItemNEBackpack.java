package com.p3pp3rf1y.nest.item;


import com.p3pp3rf1y.nest.NEsT;
import com.p3pp3rf1y.nest.inventory.ItemInventory;
import com.p3pp3rf1y.nest.inventory.ItemInventoryBackpack;
import com.p3pp3rf1y.nest.inventory.slots.IInvSlot;
import com.p3pp3rf1y.nest.reference.*;
import com.p3pp3rf1y.nest.utility.InventoryHelper;
import com.p3pp3rf1y.nest.utility.TileHelper;
import com.p3pp3rf1y.nest.utility.inventory.InventoryIterator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class ItemNEBackpack extends ItemInventoried
{
	private static final int SLOTS_BACKPACK_DEFAULT = 15;
	private static final int SLOTS_BACKPACK_T2 = 45;

	private final BackpackDefinition definition;
	private final BackpackType type;

	public ItemNEBackpack( BackpackType type )
	{
		super();

		this.definition = initDefinition();
		this.type = type;
		setMaxStackSize( 1 );
		initUnlocalizedName();
	}

	private void initUnlocalizedName()
	{
		if( this.type == BackpackType.T1 )
		{
			this.setUnlocalizedName( Names.Items.BACKPACK_T1 );
		}
		else if( this.type == BackpackType.T2 )
		{
			this.setUnlocalizedName( Names.Items.BACKPACK_T2 );
		}
		else
		{
			this.setUnlocalizedName( "null" );
		}
	}

	public BackpackDefinition getDefinition()
	{
		return definition;
	}

	private BackpackDefinition initDefinition()
	{
		BackpackDefinition def = new BackpackDefinition( "networkengineer", new Color( 0xD1DFF1 ).getRGB() );
		def.addValidBlockClasses(
				Arrays.<Class>asList( appeng.block.AEBaseBlock.class ) );
		def.addValidItemClasses(
				Arrays.<Class>asList( appeng.items.AEBaseItem.class ) );

		return def;
	}

	/**
	 * @return true if the item's stackTagCompound needs to be synchronized over
	 * SMP.
	 */
	@Override
	public boolean getShareTag()
	{
		return true;
	}

	@Override
	public ItemStack onItemRightClick( ItemStack itemstack, World world, EntityPlayer player )
	{

		if( world.isRemote )
		{
			return itemstack;
		}

		if( !player.isSneaking() )
		{
			openGui( player );
		}
		else
		{
			switchMode( itemstack );
		}
		return itemstack;

	}

	@Override
	public boolean onItemUse( ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ )
	{
		return getInventoryHit( world, x, y, z, side ) != null;
	}

	@Override
	public boolean onItemUseFirst( ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ )
	{

		if( world.isRemote )
		{
			return false;
		}

		// We only do this when shift is clicked
		if( !player.isSneaking() )
		{
			return false;
		}

		return evaluateTileHit( itemstack, player, world, x, y, z, side );
	}

	public static ItemStack tryStowing( EntityPlayer player, ItemStack backpackStack, ItemStack stack )
	{

		ItemNEBackpack backpack = ( (ItemNEBackpack) backpackStack.getItem() );
		ItemInventory inventory = new ItemInventoryBackpack( player, backpack.getBackpackSize(), backpackStack );
		if( backpackStack.getItemDamage() == 1 )
		{
			return stack;
		}

		if( stack.stackSize <= 0 )
		{
			return null;
		}

		ItemStack remainder = InventoryHelper.moveItemStack( stack, inventory );
		stack.stackSize = remainder == null ? 0 : remainder.stackSize;

		return null;
	}

	private static void switchMode( ItemStack itemstack )
	{
		BackpackMode mode = getMode( itemstack );
		int nextMode = mode.ordinal() + 1;

		nextMode %= BackpackMode.values().length;
		itemstack.setItemDamage( nextMode );
	}

	private static IInventory getInventoryHit( World world, int x, int y, int z, int side )
	{
		TileEntity targeted = world.getTileEntity( x, y, z );
		return TileHelper.getInventoryFromTile( targeted, ForgeDirection.getOrientation( side ) );
	}

	private boolean evaluateTileHit( ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side )
	{

		// Shift right-clicking on an inventory tile will attempt to transfer
		// items contained in the backpack
		IInventory inventory = getInventoryHit( world, x, y, z, side );
		// Process only inventories
		if( inventory != null )
		{

			// Must have inventory slots
			if( inventory.getSizeInventory() <= 0 )
			{
				return true;
			}

			// Create our own backpack inventory
			ItemInventoryBackpack backpackInventory = new ItemInventoryBackpack( player, getBackpackSize(), stack );

			BackpackMode mode = getMode( stack );
			if( mode == BackpackMode.RECEIVE )
			{
				tryChestReceive( backpackInventory, inventory );
			}
			else
			{
				tryChestTransfer( backpackInventory, inventory );
			}

			return true;
		}

		return false;
	}

	private static void tryChestTransfer( ItemInventoryBackpack backpackInventory, IInventory target )
	{

		for( IInvSlot slot : InventoryIterator.getIterable( backpackInventory ) )
		{
			ItemStack packStack = slot.getStackInSlot();
			if( packStack == null )
			{
				continue;
			}

			ItemStack remaining = InventoryHelper.moveItemStack( packStack, target );
			slot.setStackInSlot( remaining );
		}
	}

	private void tryChestReceive( ItemInventoryBackpack backpackInventory, IInventory target )
	{

		for( IInvSlot slot : InventoryIterator.getIterable( target ) )
		{
			ItemStack targetStack = slot.getStackInSlot();
			if( targetStack == null )
			{
				continue;
			}

			if( !definition.isValidItem( targetStack ) )
			{
				continue;
			}

			ItemStack remaining = InventoryHelper.moveItemStack( targetStack, backpackInventory );
			slot.setStackInSlot( remaining );
		}

	}

	protected void openGui( EntityPlayer entityplayer )
	{
		if( getBackpackSize() == SLOTS_BACKPACK_DEFAULT )
		{
			entityplayer.openGui( NEsT.instance, GUIs.BACKPACK.ordinal(), entityplayer.worldObj, (int) entityplayer.posX, (int) entityplayer.posY,
					(int) entityplayer.posZ );
		}
		else if( getBackpackSize() == SLOTS_BACKPACK_T2 )
		{
			entityplayer.openGui( NEsT.instance, GUIs.BACKPACK_T2.ordinal(), entityplayer.worldObj, (int) entityplayer.posX, (int) entityplayer.posY,
					(int) entityplayer.posZ );
		}
	}

	public int getBackpackSize()
	{
		return getSlotsForType( type );
	}

	@SuppressWarnings( { "rawtypes", "unchecked" } )
	@Override
	public void addInformation( ItemStack itemstack, EntityPlayer player, List list, boolean flag )
	{
		int occupied = ItemInventory.getOccupiedSlotCount( itemstack );

		BackpackMode mode = getMode( itemstack );
		if( mode == BackpackMode.LOCKED )
		{
			list.add( StatCollector.translateToLocal( Names.BackpackMode.LOCKED ) );
		}
		else if( mode == BackpackMode.RECEIVE )
		{
			list.add( StatCollector.translateToLocal( Names.BackpackMode.RECEIVE ) );
		}
		else if( mode == BackpackMode.RESUPPLY )
		{
			list.add( StatCollector.translateToLocal( Names.BackpackMode.RESUPPLY ) );
		}
		list.add( StatCollector.translateToLocal( Names.GUI.Slots ).replaceAll( "%USED", String.valueOf( occupied ) ).replaceAll( "%SIZE", String.valueOf( getBackpackSize() ) ) );

	}

	@Override
	public String getItemStackDisplayName( ItemStack itemstack )
	{
		return definition.getName( itemstack );
	}

	/* ICONS */
	@SideOnly( Side.CLIENT )
	private IIcon[] icons;

	@SideOnly( Side.CLIENT )
	@Override
	public void registerIcons( IIconRegister register )
	{
		//TODO: look into moving these constants to ResourceLocation constants in Reference.Textures

		icons = new IIcon[6];

		String typeTag = "backpack/" + type.toString().toLowerCase( Locale.ENGLISH );

		icons[0] = register.registerIcon( Textures.RESOURCE_PREFIX + typeTag + ".cloth" );
		icons[1] = register.registerIcon( Textures.RESOURCE_PREFIX + typeTag + ".outline" );
		icons[2] = register.registerIcon( Textures.RESOURCE_PREFIX + "backpack/neutral" );
		icons[3] = register.registerIcon( Textures.RESOURCE_PREFIX + "backpack/locked" );
		icons[4] = register.registerIcon( Textures.RESOURCE_PREFIX + "backpack/receive" );
		icons[5] = register.registerIcon( Textures.RESOURCE_PREFIX + "backpack/resupply" );
	}

	// Return true to enable color overlay - client side only
	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}

	@Override
	public int getRenderPasses( int metadata )
	{
		return 3;
	}

	@Override
	public int getColorFromItemStack( ItemStack itemstack, int j )
	{

		if( j == 0 )
		{
			return definition.getPrimaryColour();
		}
		else if( j == 1 )
		{
			return definition.getSecondaryColour();
		}
		else
		{
			return 0xffffff;
		}
	}

	@SideOnly( Side.CLIENT )
	@Override
	public IIcon getIconFromDamageForRenderPass( int i, int j )
	{
		if( j == 0 )
		{
			return icons[0];
		}
		if( j == 1 )
		{
			return icons[1];
		}

		if( i > 2 )
		{
			return icons[5];
		}
		else if( i > 1 )
		{
			return icons[4];
		}
		else if( i > 0 )
		{
			return icons[3];
		}
		else
		{
			return icons[2];
		}
	}

	private static int getSlotsForType( BackpackType type )
	{
		switch( type )
		{
			case T2:
				return SLOTS_BACKPACK_T2;
			case T1:
			default:
				return SLOTS_BACKPACK_DEFAULT;
		}
	}

	public static BackpackMode getMode( ItemStack backpack )
	{
		int meta = backpack.getItemDamage();

		if( meta >= 3 )
		{
			return BackpackMode.RESUPPLY;
		}
		else if( meta >= 2 )
		{
			return BackpackMode.RECEIVE;
		}
		else if( meta >= 1 )
		{
			return BackpackMode.LOCKED;
		}
		else
		{
			return BackpackMode.NORMAL;
		}
	}
}
