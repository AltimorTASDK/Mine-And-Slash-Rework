package com.robertx22.addons.orbs_of_crafting.currency.reworked.item_mod.all;

import com.robertx22.addons.orbs_of_crafting.currency.reworked.item_mod.ItemModificationSers;
import com.robertx22.mine_and_slash.itemstack.ExileStack;
import com.robertx22.mine_and_slash.itemstack.StackKeys;
import com.robertx22.orbs_of_crafting.main.StackHolder;
import com.robertx22.orbs_of_crafting.register.mods.base.ItemModification;
import com.robertx22.orbs_of_crafting.register.mods.base.ItemModificationResult;
import net.minecraft.network.chat.MutableComponent;

public class AddPotentialItemMod extends ItemModification {

    public Data data;

    public static record Data(int add_potential) {
    }

    public AddPotentialItemMod(String id, Data data) {
        super(ItemModificationSers.ADD_POTENTIAL, id);
        this.data = data;
    }


    @Override
    public OutcomeType getOutcomeType() {
        return OutcomeType.GOOD;
    }

    @Override
    public void applyINTERNAL(StackHolder stack, ItemModificationResult r) {
        ExileStack ex = ExileStack.of(stack.stack);

        ex.get(StackKeys.POTENTIAL).edit(pot -> {
            pot.add(data.add_potential);
        });

        stack.stack = ex.getStack();
    }

    @Override
    public MutableComponent getDescWithParams() {
        return this.getDescParams(data.add_potential);
    }


    @Override
    public Class<?> getClassForSerialization() {
        return AddPotentialItemMod.class;
    }


    @Override
    public String locDescForLangFile() {
        return "Add %1$s Potential";
    }
}
