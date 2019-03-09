/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 29, 2015, 4:24:07 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;
import vazkii.botania.client.challenge.Challenge;
import vazkii.botania.client.challenge.EnumChallengeLevel;
import vazkii.botania.client.challenge.ModChallenges;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.gui.lexicon.button.GuiButtonBack;
import vazkii.botania.client.gui.lexicon.button.GuiButtonChallengeIcon;

import java.io.IOException;
import java.util.List;

public class GuiLexiconChallengesList extends GuiLexicon implements IParented {

	private GuiLexicon parent;
	private GuiButton backButton;

	public GuiLexiconChallengesList() {
		parent = new GuiLexicon();
		title = I18n.format("botaniamisc.challenges");
	}

	@Override
	public void onInitGui() {
		super.onInitGui();
		title = I18n.format("botaniamisc.challenges");

		buttons.add(backButton = new GuiButtonBack(12, left + guiWidth / 2 - 8, top + guiHeight + 2) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				super.onClick(mouseX, mouseY);
				mc.displayGuiScreen(parent);
				ClientTickHandler.notifyPageChange();
			}
		});

		int perline = 6;
		int i = 13;
		int y = top + 20;
		for(EnumChallengeLevel level : EnumChallengeLevel.class.getEnumConstants()) {
			int j = 0;
			for(Challenge c : ModChallenges.challenges.get(level)) {
				buttons.add(new GuiButtonChallengeIcon(i, left + 20 + j % perline * 18, y + j / perline * 17, c, this));
				i++;
				j++;
			}
			y += 44;
		}
	}

	@Override
	public void drawScreenAfterScale(int xCoord, int yCoord, float newPartialTicks) {
		super.drawScreenAfterScale(xCoord, yCoord, newPartialTicks);

		for(EnumChallengeLevel level : EnumChallengeLevel.class.getEnumConstants()) {
			List<Challenge> list = ModChallenges.challenges.get(level);
			int complete = 0;
			for(Challenge c : list)
				if(c.complete)
					complete++;

			fontRenderer.drawString(TextFormatting.BOLD + I18n.format(level.getName()) + TextFormatting.RESET + " (" + complete + "/" + list.size() + ")", left + 20, top + 11 + level.ordinal() * 44, 0);
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int mods) {
		if(keyCode == GLFW.GLFW_KEY_BACKSPACE && !notesEnabled) {
			back();
			return true;
		} else if(keyCode == GLFW.GLFW_KEY_HOME) {
			mc.displayGuiScreen(new GuiLexicon());
			ClientTickHandler.notifyPageChange();
			return true;
		}

		return super.keyPressed(keyCode, scanCode, mods);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mods) {
		if(mods == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			back();
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, mods);
	}

	private void back() {
		if(backButton.enabled) {
			backButton.playPressSound(mc.getSoundHandler());
			backButton.onClick(backButton.x, backButton.y);
		}
	}

	@Override
	public void setParent(GuiLexicon gui) {
		parent = gui;
	}

	@Override
	boolean isMainPage() {
		return false;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	boolean isChallenge() {
		return true;
	}

	@Override
	boolean isCategoryIndex() {
		return false;
	}

	@Override
	public GuiLexicon copy() {
		return new GuiLexiconChallengesList();
	}

	@Override
	public String getNotesKey() {
		return "challengelist";
	}

}
