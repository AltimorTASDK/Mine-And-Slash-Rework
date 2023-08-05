package com.robertx22.age_of_exile.database.data.gems;

import com.robertx22.age_of_exile.database.data.BaseRuneGem;
import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.age_of_exile.database.registry.ExileRegistryTypes;
import com.robertx22.library_of_exile.registry.ExileRegistryType;
import com.robertx22.library_of_exile.registry.IAutoGson;
import com.robertx22.library_of_exile.registry.JsonExileRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.ChatFormatting;

import java.util.List;

public class Gem extends BaseRuneGem implements IAutoGson<Gem>, JsonExileRegistry<Gem> {

    public static Gem SERIALIZER = new Gem();

    public String gem_type = "";
    public String text_format = "";

    public int perc_upgrade_chance = 0;

    public ChatFormatting getFormat() {
        try {
            return ChatFormatting.valueOf(text_format);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return ChatFormatting.GRAY;
    }

    public Gem getHigherTierGem() {
        List<Gem> list = ExileDB.Gems()
            .getFiltered(x -> x.tier == tier + 1 && x.gem_type.equals(gem_type));

        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public boolean hasHigherTierGem() {
        return getHigherTierGem() != null;
    }

    public Item getItem() {
        return Registry.ITEM.get(new ResourceLocation(item_id));
    }

    @Override
    public Class<Gem> getClassForSerialization() {
        return Gem.class;
    }

    @Override
    public ExileRegistryType getExileRegistryType() {
        return ExileRegistryTypes.GEM;
    }

}
