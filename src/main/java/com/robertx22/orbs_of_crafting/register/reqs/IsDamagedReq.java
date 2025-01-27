package com.robertx22.orbs_of_crafting.register.reqs;

import com.robertx22.addons.orbs_of_crafting.currency.reworked.item_req.ItemReqSers;
import com.robertx22.orbs_of_crafting.main.StackHolder;
import com.robertx22.orbs_of_crafting.register.reqs.base.ItemRequirement;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

public class IsDamagedReq extends ItemRequirement {

    public IsDamagedReq(String id) {
        super(ItemReqSers.IS_DAMAGED, id);
    }

    @Override
    public Class<?> getClassForSerialization() {
        return IsDamagedReq.class;
    }

    @Override
    public MutableComponent getDescWithParams() {
        return this.getDescParams();
    }

    @Override
    public boolean isValid(Player p, StackHolder s) {
        return s.stack.isDamaged();
    }

    @Override
    public String locDescForLangFile() {
        return "Must be a Damaged Item";
    }


}