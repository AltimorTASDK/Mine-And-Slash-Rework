package com.robertx22.mine_and_slash.gui.screens.stat_gui;

import com.robertx22.library_of_exile.utils.TextUTIL;
import com.robertx22.mine_and_slash.database.data.spells.components.Spell;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.ModRange;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.StatRangeInfo;
import com.robertx22.mine_and_slash.saveclasses.unit.Unit;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.util.List;

public class SpellStatButton extends ImageButton {

    public static int xSize = 16;
    public static int ySize = 16;

    private StatScreen screen;
    private Spell spell;

    public SpellStatButton(StatScreen screen, Spell spell, int xPos, int yPos) {
        super(xPos, yPos, xSize, ySize, 0, 0, 0, spell.getIconLoc(), xSize, ySize, (button) -> {
            if (screen.getSpell() != spell) {
                screen.setSpell(spell);
            } else {
                screen.setSpell(null);
            }
        });

        this.screen = screen;
        this.spell = spell;
    }

    @Override
    public void render(GuiGraphics gui, int x, int y, float ticks) {
        super.render(gui, x, y, ticks);

        if (this.isHoveredOrFocused()) {
            Unit unit = screen.getUnitForSpell(spell);
            if (unit != null) {
                List<Component> tooltip = spell.GetTooltipString(new StatRangeInfo(ModRange.hide()), unit);
                this.setTooltip(Tooltip.create(TextUTIL.mergeList(tooltip)));
            }
        }

    }

}
