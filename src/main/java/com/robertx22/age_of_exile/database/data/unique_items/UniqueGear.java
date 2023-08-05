package com.robertx22.age_of_exile.database.data.unique_items;

import com.google.gson.JsonObject;
import com.robertx22.age_of_exile.aoe_data.datapacks.JsonUtils;
import com.robertx22.age_of_exile.database.data.StatModifier;
import com.robertx22.age_of_exile.database.data.gear_slots.GearSlot;
import com.robertx22.age_of_exile.database.data.gear_types.bases.BaseGearType;
import com.robertx22.age_of_exile.database.data.rarities.GearRarity;
import com.robertx22.age_of_exile.database.data.unique_items.drop_filters.DropFiltersGroupData;
import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.age_of_exile.database.registry.ExileRegistryTypes;
import com.robertx22.age_of_exile.mmorpg.SlashRef;
import com.robertx22.age_of_exile.uncommon.interfaces.IAutoLocName;
import com.robertx22.age_of_exile.uncommon.interfaces.data_items.IRarity;
import com.robertx22.library_of_exile.registry.ExileRegistryType;
import com.robertx22.library_of_exile.registry.JsonExileRegistry;
import com.robertx22.library_of_exile.registry.serialization.ISerializable;

import java.util.ArrayList;
import java.util.List;

import com.robertx22.age_of_exile.uncommon.interfaces.IBaseAutoLoc.AutoLocGroup;

public class UniqueGear implements IAutoLocName, JsonExileRegistry<UniqueGear>, ISerializable<UniqueGear> {

    public static UniqueGear SERIALIZER = new UniqueGear();

    public List<StatModifier> uniqueStats = new ArrayList<>();
    public int weight = 1000;
    public String guid;
    public String uniqueRarity = IRarity.UNIQUE_ID;
    public boolean replaces_name = false;

    public String base_gear = "";

    public DropFiltersGroupData filters = new DropFiltersGroupData();

    public transient String langName;


    @Override
    public JsonObject toJson() {
        JsonObject json = getDefaultJson();

        JsonUtils.addStats(uniqueStats(), json, "unique_stats");

        json.addProperty("rarity", this.uniqueRarity);
        json.addProperty("replaces_name", this.replaces_name);

        json.addProperty("base_gear", base_gear);

        json.add("filters", filters.toJson());
        return json;
    }

    @Override
    public UniqueGear fromJson(JsonObject json) {

        UniqueGear uniq = new UniqueGear();

        uniq.guid = getGUIDFromJson(json);
        uniq.weight = getWeightFromJson(json);

        uniq.uniqueStats = JsonUtils.getStats(json, "unique_stats");
  
        uniq.base_gear = json.get("base_gear")
                .getAsString();
        uniq.uniqueRarity = json.get("rarity")
                .getAsString();

        if (json.has("replaces_name")) {
            uniq.replaces_name = json.get("replaces_name")
                    .getAsBoolean();
        }
        uniq.filters = DropFiltersGroupData.fromJson(json.get("filters"));

        return uniq;
    }

    @Override
    public int Weight() {
        return weight;
    }

    @Override
    public AutoLocGroup locNameGroup() {
        return AutoLocGroup.Unique_Items;
    }

    @Override
    public ExileRegistryType getExileRegistryType() {
        return ExileRegistryTypes.UNIQUE_GEAR;
    }

    public GearRarity getUniqueRarity() {
        return ExileDB.GearRarities()
                .get(uniqueRarity);
    }

    public List<StatModifier> uniqueStats() {
        return this.uniqueStats;
    }

    @Override
    public String locNameForLangFile() {
        return this.langName;
    }

    @Override
    public String locNameLangFileGUID() {
        return SlashRef.MODID + ".unique_gear." + this.GUID() + ".name";
    }

    @Override
    public String GUID() {
        return guid;
    }

    public GearSlot getSlot() {
        return ExileDB.GearSlots()
                .get(getBaseGear().gear_slot);
    }

    public BaseGearType getBaseGear() {
        return ExileDB.GearTypes()
                .get(base_gear);
    }
}