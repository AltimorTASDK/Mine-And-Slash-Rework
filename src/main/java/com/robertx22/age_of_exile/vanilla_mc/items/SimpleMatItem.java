package com.robertx22.age_of_exile.vanilla_mc.items;

import net.minecraft.world.item.Item;

public class SimpleMatItem extends Item {

    public SimpleMatItem() {
        super(new Item.Properties().tab(CreativeTabs.MyModTab)
                .stacksTo(64));

    }
}

