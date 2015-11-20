package com.p3pp3rf1y.nest.reference;


import com.p3pp3rf1y.nest.utility.ResourceLocationHelper;
import net.minecraft.util.ResourceLocation;


public final class Textures
{
	public static final String RESOURCE_PREFIX = Reference.LOWERCASE_MOD_ID + ":";


	public static final class Gui
	{
		protected static final String GUI_TEXTURE_LOCATION = "textures/gui/";
		public static final ResourceLocation BACKPACK = ResourceLocationHelper.getResourceLocation( GUI_TEXTURE_LOCATION + "backpack.png" );
		public static final ResourceLocation BACKPACK_T2 = ResourceLocationHelper.getResourceLocation( GUI_TEXTURE_LOCATION + "backpackT2.png" );
	}
}
