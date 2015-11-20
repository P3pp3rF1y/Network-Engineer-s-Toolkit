package com.p3pp3rf1y.nest.client.gui.inventory;


import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;


public abstract class GuiNEsT<C extends Container, I extends IInventory> extends GuiContainer
{
	protected final I inventory;
	protected final C container;
	public final ResourceLocation textureFile;

	protected GuiNEsT( ResourceLocation texture, C container, I inventory )
	{
		super( container );
		this.textureFile = texture;

		this.inventory = inventory;
		this.container = container;
	}

	@Override
	protected void drawGuiContainerForegroundLayer( int mouseX, int mouseY )
	{
		GL11.glPushAttrib( GL11.GL_ENABLE_BIT );
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable( GL11.GL_LIGHTING );
		GL11.glDisable( GL11.GL_DEPTH_TEST );
		GL11.glPushMatrix();
		GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );

		GL11.glEnable( GL12.GL_RESCALE_NORMAL );
		OpenGlHelper.setLightmapTextureCoords( OpenGlHelper.lightmapTexUnit, 240 / 1.0F, 240 / 1.0F );
		GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );

		InventoryPlayer playerInv = mc.thePlayer.inventory;

		GL11.glPopMatrix();
		GL11.glPopAttrib();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer( float f, int mouseX, int mouseY )
	{
		GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
		bindTexture( textureFile );
		int x = ( width - xSize ) / 2;
		int y = ( height - ySize ) / 2;
		drawTexturedModalRect( x, y, 0, 0, xSize, ySize );

		int left = this.guiLeft;
		int top = this.guiTop;

		GL11.glPushAttrib( GL11.GL_ENABLE_BIT );
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable( GL11.GL_LIGHTING );
		GL11.glDisable( GL11.GL_DEPTH_TEST );
		GL11.glPushMatrix();
		GL11.glTranslatef( left, top, 0.0F );
		GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );

		GL11.glEnable( GL12.GL_RESCALE_NORMAL );
		OpenGlHelper.setLightmapTextureCoords( OpenGlHelper.lightmapTexUnit, 240 / 1.0F, 240 / 1.0F );
		GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );

		GL11.glPopMatrix();
		GL11.glPopAttrib();

		bindTexture( textureFile );
	}

	protected void bindTexture( ResourceLocation texturePath )
	{
		GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
		this.mc.getTextureManager().bindTexture( texturePath );
	}

	@Override
	public void drawGradientRect( int par1, int par2, int par3, int par4, int par5, int par6 )
	{
		super.drawGradientRect( par1, par2, par3, par4, par5, par6 );
	}

}
