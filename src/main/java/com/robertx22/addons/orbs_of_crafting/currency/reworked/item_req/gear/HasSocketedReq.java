package com.robertx22.addons.orbs_of_crafting.currency.reworked.item_req.gear;

import com.robertx22.addons.orbs_of_crafting.currency.reworked.item_mod.gear.ExtractSocketItemMod;
import com.robertx22.addons.orbs_of_crafting.currency.reworked.item_req.GearRequirement;
import com.robertx22.addons.orbs_of_crafting.currency.reworked.item_req.ItemReqSers;
import com.robertx22.mine_and_slash.itemstack.ExileStack;
import com.robertx22.mine_and_slash.itemstack.StackKeys;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class HasSocketedReq extends GearRequirement {

    ExtractSocketItemMod.SocketedType type;

    public HasSocketedReq(String id, ExtractSocketItemMod.SocketedType type) {
        super(ItemReqSers.HAS_SOCKET_OF_TYPE, id);
        this.type = type;
    }

    @Override
    public Class<?> getClassForSerialization() {
        return HasSocketedReq.class;
    }

    @Override
    public MutableComponent getDescWithParams() {
        return this.getDescParams(type.word.locName().withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public String locDescForLangFile() {
        return "Must have a Socketed %1$s";
    }

    @Override
    public boolean isGearValid(ItemStack stack) {
        ExileStack ex = ExileStack.of(stack);

        var gear = ex.get(StackKeys.GEAR).get();
        return gear.sockets != null && gear.sockets.lastFilledSocketGemIndex(type) > -1;
    }
}
