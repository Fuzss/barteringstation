package fuzs.barteringstation.client.gui.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.world.item.ItemStack;

public class ItemStackDisplayButton extends Button {
    private final Font font;
    private final ItemStack itemStack;

    public ItemStackDisplayButton(int x, int y, Font font, ItemStack itemStack, OnPress onPress) {
        super(x, y, 16, 16, CommonComponents.EMPTY, onPress, Button.DEFAULT_NARRATION);
        this.font = font;
        this.itemStack = itemStack;
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.isHoveredOrFocused()) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                    AbstractContainerScreen.SLOT_HIGHLIGHT_BACK_SPRITE,
                    this.getX() - 4,
                    this.getY() - 4,
                    24,
                    24);
        }

        guiGraphics.renderFakeItem(this.itemStack, this.getX(), this.getY());
        int posX = this.getX() + 19 - 2 - this.font.width(this.getMessage());
        int posY = this.getY() + 6 + 3;
        guiGraphics.drawString(this.font, this.getMessage(), posX, posY, -1);
        if (this.isHoveredOrFocused()) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                    AbstractContainerScreen.SLOT_HIGHLIGHT_FRONT_SPRITE,
                    this.getX() - 4,
                    this.getY() - 4,
                    24,
                    24);
        }
    }
}
