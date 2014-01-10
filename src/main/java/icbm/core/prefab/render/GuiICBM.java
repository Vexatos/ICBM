package icbm.core.prefab.render;

import icbm.Reference;
import icbm.sentry.platform.TileEntityTurretPlatform;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import calclavia.lib.gui.GuiBase;
import cpw.mods.fml.client.FMLClientHandler;

public abstract class GuiICBM extends GuiBase
{
	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.DOMAIN, Reference.GUI_PATH + "gui_empty.png");

	protected int containerWidth;
	protected int containerHeight;

	@Override
	protected void drawBackgroundLayer(int var2, int var3, float var1)
	{
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.containerWidth = (this.width - this.xSize) / 2;
		this.containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
	}

}