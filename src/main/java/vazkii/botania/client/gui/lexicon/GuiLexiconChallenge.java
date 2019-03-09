/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 29, 2015, 5:25:06 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;
import vazkii.botania.client.challenge.Challenge;
import vazkii.botania.client.challenge.ModChallenges;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.PersistentVariableHelper;
import vazkii.botania.client.gui.lexicon.button.GuiButtonBack;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.material.ItemRune;
import vazkii.botania.common.lexicon.page.PageText;

import java.io.IOException;

public class GuiLexiconChallenge extends GuiLexicon implements IParented {

	private static final String TAG_CHALLENGE = "challenge";

	private Challenge challenge;
	private GuiLexicon parent;
	private GuiButton backButton, completeButton;

	public GuiLexiconChallenge(GuiLexicon parent, Challenge challenge) {
		this.parent = parent;
		this.challenge = challenge;
		setTitle();
	}

	private void setTitle() {
		title = challenge == null ? "(null)" : I18n.format(challenge.unlocalizedName);
	}

	@Override
	public void onInitGui() {
		super.onInitGui();
		setTitle();

		buttons.add(backButton = new GuiButtonBack(12, left + guiWidth / 2 - 8, top + guiHeight + 2) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				super.onClick(mouseX, mouseY);
				mc.displayGuiScreen(parent);
				ClientTickHandler.notifyPageChange();
			}
		});
		buttons.add(completeButton = new GuiButton(13, left + 20, top + guiHeight - 35, guiWidth - 40, 20, "") {
			@Override
			public void onClick(double mouseX, double mouseY) {
				super.onClick(mouseX, mouseY);
				challenge.complete = !challenge.complete;
				setCompleteButtonTitle();
				PersistentVariableHelper.saveSafe();
			}
		});
		setCompleteButtonTitle();
	}

	@Override
	public void drawScreenAfterScale(int xCoord, int yCoord, float newPartialTicks) {
		super.drawScreenAfterScale(xCoord, yCoord, newPartialTicks);

		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.enableRescaleNormal();
		mc.getItemRenderer().renderItemIntoGUI(challenge.icon, left + 18, top + 15);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableBlend();

		fontRenderer.drawString(TextFormatting.BOLD + I18n.format(challenge.unlocalizedName), left + 38, top + 13, 0);
		fontRenderer.drawString(I18n.format(challenge.level.getName()) + ((challenge.icon.getItem() instanceof ItemRune) ? "+" : "") + " / " + (challenge.complete ? TextFormatting.DARK_GREEN : TextFormatting.DARK_RED) + I18n.format(challenge.complete ? "botaniamisc.completed" : "botaniamisc.notCompleted"), left + 38, top + 23, 0);

		int width = guiWidth - 30;
		int x = left + 16;
		int y = top + 28;

		PageText.renderText(x, y, width, guiHeight, challenge.unlocalizedName + ".desc");
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
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(button == 1) {
			back();
			return false;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	private void setCompleteButtonTitle() {
		completeButton.displayString = I18n.format(challenge.complete ? "botaniamisc.markNotCompleted" : "botaniamisc.markCompleted");
	}

	private void back() {
		if(backButton.enabled) {
			actionPerformed(backButton);
			backButton.playPressSound(mc.getSoundHandler());
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
		return new GuiLexiconChallenge(parent, challenge);
	}

	@Override
	public void serialize(NBTTagCompound cmp) {
		super.serialize(cmp);
		cmp.putString(TAG_CHALLENGE, challenge.unlocalizedName);
	}

	@Override
	public void load(NBTTagCompound cmp) {
		super.load(cmp);
		String challengeName = cmp.getString(TAG_CHALLENGE);
		challenge = ModChallenges.challengeLookup.get(challengeName);
		setTitle();
	}

	@Override
	public String getNotesKey() {
		return "challenge_" + challenge.unlocalizedName;
	}

}
