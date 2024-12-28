package com.robertx22.addons.orbs_of_crafting.currency.reworked.item_mod.gear;

import com.robertx22.addons.orbs_of_crafting.currency.reworked.item_mod.GearModification;
import com.robertx22.addons.orbs_of_crafting.currency.reworked.item_mod.ItemModificationSers;
import com.robertx22.mine_and_slash.itemstack.ExileStack;
import com.robertx22.mine_and_slash.itemstack.StackKeys;
import com.robertx22.orbs_of_crafting.register.mods.base.ItemModificationResult;
import net.minecraft.network.chat.MutableComponent;

public class AddSocketItemMod extends GearModification {


    public Data data;

    public static record Data(int add_sockets) {
    }

    public AddSocketItemMod(String id, Data data) {
        super(ItemModificationSers.ADD_SOCKET, id);
        this.data = data;
    }

    @Override
    public void modifyGear(ExileStack stack, ItemModificationResult r) {
        stack.get(StackKeys.GEAR).edit(gear -> {
            for (int i = 0; i < data.add_sockets; i++) {
                if (gear.sockets.canAddSocket(gear).can) {
                    gear.sockets.addSocket();
                }
            }
        });

    }

    @Override
    public OutcomeType getOutcomeType() {
        return OutcomeType.GOOD;
    }

    @Override
    public Class<?> getClassForSerialization() {
        return AddSocketItemMod.class;
    }


    @Override
    public MutableComponent getDescWithParams() {
        return this.getDescParams(data.add_sockets);
    }

    @Override
    public String locDescForLangFile() {
        return "Adds %1$s Socket";
    }
}
