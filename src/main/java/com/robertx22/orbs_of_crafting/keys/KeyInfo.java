package com.robertx22.orbs_of_crafting.keys;

import com.robertx22.mine_and_slash.aoe_data.database.stats.base.AutoHashClass;

public abstract class KeyInfo extends AutoHashClass {


    @Override
    public int hashCode() {
        return GUID().hashCode();
    }
}