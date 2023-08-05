package com.robertx22.age_of_exile.gui.screens.character_screen;

import com.robertx22.age_of_exile.a_libraries.curios.MyCurioUtils;
import com.robertx22.age_of_exile.a_libraries.curios.RefCurio;
import com.robertx22.age_of_exile.gui.bases.BaseScreen;
import com.robertx22.age_of_exile.mmorpg.SlashRef;
import com.robertx22.age_of_exile.uncommon.datasaving.Load;
import com.robertx22.library_of_exile.gui.ItemSlotButton;
import com.robertx22.library_of_exile.utils.GuiUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

public class PlayerGearButton extends ImageButton {

    public static int xSize = 99;
    public static int ySize = 80;

    static ResourceLocation TEX = new ResourceLocation(SlashRef.MODID, "textures/gui/player_gear.png");
    BaseScreen screen;
    Player player;

    public PlayerGearButton(Player player, BaseScreen screen, int xPos, int yPos) {
        super(xPos, yPos, xSize, ySize, 0, 0, ySize + 1, TEX, (button) -> {
        });
        this.player = player;
        this.screen = screen;

        addItemButton(MyCurioUtils.get(RefCurio.NECKLACE, player, 0), 0, 4);
        addItemButton(MyCurioUtils.get(RefCurio.RING, player, 0), 0, 22);
        addItemButton(MyCurioUtils.get(RefCurio.RING, player, 1), 0, 40);

        addItemButton(player.getItemBySlot(EquipmentSlot.HEAD), 81, 4);
        addItemButton(player.getItemBySlot(EquipmentSlot.CHEST), 81, 22);
        addItemButton(player.getItemBySlot(EquipmentSlot.LEGS), 81, 40);
        addItemButton(player.getItemBySlot(EquipmentSlot.FEET), 81, 58);

        // addItemButton(player.getEquippedStack(EquipmentSlot.MAINHAND), 58, 69);
        //addItemButton(player.getEquippedStack(EquipmentSlot.OFFHAND), 179, 69);

    }

    @Override
    public void render(GuiGraphics gui, int x, int y, float ticks) {
        super.render(gui, x, y, ticks);

        String str = "Level: " + Load.Unit(player)
                .getLevel();

        Minecraft mc = Minecraft.getInstance();

        Quaternionf ARMOR_STAND_ANGLE = (new Quaternionf()).rotationXYZ(0.43633232F, 0.0F, (float) Math.PI);

        // player 3d view
        InventoryScreen.renderEntityInInventory(gui, this.getX() + 50, this.getY() + 77, 30, ARMOR_STAND_ANGLE, null, player);
        gui.drawString(mc.font, str, this.getX() + xSize / 2 - mc.font.width(str) / 2, this.getY() + 3, ChatFormatting.YELLOW.getColor());

        str = "Avg ILVL: " + Load.Unit(mc.player)
                .getAverageILVL();

        GuiUtils.renderScaledText(gui, this.getX() + xSize / 2, this.getY() + 17, 0.8F, str, ChatFormatting.GREEN);

        // mc.font.draw(matrix, str, this.x + xSize / 2 - mc.font.width(str) / 2, this.y + 5, TextFormatting.GREEN.getColor());

        // player 3d view

    }

    private void addItemButton(ItemStack stack, int x, int y) {
        screen.publicAddButton(new ItemSlotButton(stack, this.getX() + x, this.getY() + y));
    }

}
