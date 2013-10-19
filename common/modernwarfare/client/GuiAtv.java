package modernwarfare.client;

import modernwarfare.common.ContainerAtv;
import modernwarfare.common.EntityAtv;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiAtv extends GuiContainer
{
    private EntityAtv atv;

    public GuiAtv(InventoryPlayer inventoryplayer, EntityAtv entityatv)
    {
        super(new ContainerAtv(inventoryplayer, entityatv));
        atv = entityatv;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
        fontRenderer.drawString(atv.getInvName(), xSize / 2 - fontRenderer.getStringWidth(atv.getInvName()) / 2, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 132) + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        mc.renderEngine.bindTexture(new ResourceLocation("modernwarfare:gui/Atv.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
    }
}
