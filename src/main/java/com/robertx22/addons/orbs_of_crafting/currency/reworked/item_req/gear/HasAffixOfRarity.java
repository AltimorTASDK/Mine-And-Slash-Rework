package com.robertx22.addons.orbs_of_crafting.currency.reworked.item_req.gear;

import com.robertx22.addons.orbs_of_crafting.currency.reworked.item_req.GearRequirement;
import com.robertx22.addons.orbs_of_crafting.currency.reworked.item_req.ItemReqSers;
import com.robertx22.mine_and_slash.database.data.rarities.GearRarity;
import com.robertx22.mine_and_slash.database.registry.ExileDB;
import com.robertx22.mine_and_slash.itemstack.ExileStack;
import com.robertx22.mine_and_slash.itemstack.StackKeys;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class HasAffixOfRarity extends GearRequirement {


    public Data data;

    public static record Data(String rar) {
        GearRarity getRarity() {
            return ExileDB.GearRarities().get(rar);
        }
    }

    public HasAffixOfRarity(String id, Data data) {
        super(ItemReqSers.HAS_RARITY_AFFIX, id);
        this.data = data;
    }

    @Override
    public Class<?> getClassForSerialization() {
        return HasAffixOfRarity.class;
    }

    @Override
    public MutableComponent getDescWithParams() {
        return this.getDescParams(data.getRarity().coloredName());
    }

    @Override
    public String locDescForLangFile() {
        return "Must have %1$s Affix";
    }

    @Override
    public boolean isGearValid(ItemStack stack) {
        ExileStack ex = ExileStack.of(stack);

        var gear = ex.get(StackKeys.GEAR).get();
        return gear.affixes.getPrefixesAndSuffixes().stream().anyMatch(x -> x.rar.equals(data.rar));
    }
}
