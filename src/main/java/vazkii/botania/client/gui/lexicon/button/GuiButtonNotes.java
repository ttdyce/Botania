/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 24, 2015, 2:49:36 AM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiButtonNotes extends GuiButtonLexicon {

	private final GuiLexicon parent;

	public GuiButtonNotes(GuiLexicon parent, int id, int x, int y) {
		super(id, x, y, 11, 11, "");
		this.parent = parent;
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		super.onClick(mouseX, mouseY);
		GuiLexicon.notesEnabled = !GuiLexicon.notesEnabled;
	}

	@Override
	public void render(int x, int y, float partialTicks) {
		hovered = x >= this.x && y >= this.y && x < this.x + width && y < this.y + height;
		int k = getHoverState(hovered);

		Minecraft.getInstance().textureManager.bindTexture(GuiLexicon.texture);
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		drawTexturedModalRect(x, y, k == 2 ? 130 : 120, 191, 10, 11);

		List<String> tooltip = new ArrayList<>();
		if(GuiLexicon.notesEnabled)
			tooltip.add(TextFormatting.GREEN + I18n.format("botaniamisc.hideNotes"));
		else {
			tooltip.add(TextFormatting.GREEN + I18n.format("botaniamisc.showNotes"));
			if(parent.note != null && !parent.note.isEmpty())
				Minecraft.getInstance().fontRenderer.drawStringWithShadow("!", x + 10, y, 0xFF0000);
		}

		int tooltipY = (tooltip.size() - 1) * 10;
		if(k == 2)
			RenderHelper.renderTooltip(x, y + tooltipY, tooltip);
	}

}
