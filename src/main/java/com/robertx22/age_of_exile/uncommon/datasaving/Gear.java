package com.robertx22.age_of_exile.uncommon.datasaving;

import com.robertx22.age_of_exile.saveclasses.item_classes.GearItemData;
import com.robertx22.library_of_exile.utils.LoadSave;
import com.robertx22.library_of_exile.vanilla_util.main.VanillaUTIL;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class Gear {

    private static final String LOC = "GEAR_ITEM_DATA";

    public static boolean has(ItemStack stack) {
        return stack != null && stack.hasTag() && stack.getTag()
                .contains(LOC);
    }

    public static GearItemData Load(ItemStack stack) {

        if (stack == null) {
            return null;
        }
        if (!stack.hasTag()) {
            return null;
        }

        GearItemData gear = LoadSave.Load(GearItemData.class, new GearItemData(), stack.getTag(), LOC);

        if (gear != null) {
            

            String id = VanillaUTIL.REGISTRY.items().getKey(stack.getItem())
                    .toString();

            if (gear.item_id.isEmpty()) {
                gear.item_id = id;
            } else {
                if (id.equals(gear.item_id) == false) {
                    // reroll by clearing gear tag because it was changed/upgraded
                    stack.getTag()
                            .remove(LOC);
                    return new GearItemData();
                }
            }
            gear.stack = stack;
        }

        return gear;

    }

    public static void Save(ItemStack stack, GearItemData gear) {

        if (stack == null) {
            return;
        }
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }
        if (gear != null) {

            LoadSave.Save(gear, stack.getTag(), LOC);
        }

    }

}
