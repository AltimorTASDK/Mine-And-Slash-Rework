package com.robertx22.orbs_of_crafting.register.mods.base;

import com.robertx22.mine_and_slash.uncommon.utilityclasses.PlayerUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemModificationResult {


    public List<ItemStack> extraItemsCreated = new ArrayList<>();


    public void onFinish(Player p) {

        for (ItemStack stack : extraItemsCreated) {
            PlayerUtils.giveItem(stack, p);
        }

    }

}