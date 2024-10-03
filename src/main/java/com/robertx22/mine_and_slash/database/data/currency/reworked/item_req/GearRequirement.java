package com.robertx22.mine_and_slash.database.data.currency.reworked.item_req;

import com.robertx22.mine_and_slash.itemstack.ExileStack;

public abstract class GearRequirement extends ItemRequirement {
    public GearRequirement(String serializer, String id) {
        super(serializer, id);
    }

    public abstract boolean isGearValid(ExileStack gear);

    @Override
    public boolean isValid(ExileStack obj) {
        return obj.GEAR.has() && isGearValid(obj);
    }
}