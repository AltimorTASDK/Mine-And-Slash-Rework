package com.robertx22.age_of_exile.loot.generators.stack_changers;

import net.minecraft.world.item.ItemStack;

public interface IStackAction {
    void changeStack(ItemStack stack);
}
