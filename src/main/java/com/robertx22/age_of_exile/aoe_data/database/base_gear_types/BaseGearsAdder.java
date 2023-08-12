package com.robertx22.age_of_exile.aoe_data.database.base_gear_types;

import com.robertx22.age_of_exile.aoe_data.database.GearDataHelper;
import com.robertx22.age_of_exile.aoe_data.database.gear_slots.GearSlots;
import com.robertx22.age_of_exile.aoe_data.database.stats.Stats;
import com.robertx22.age_of_exile.database.data.StatMod;
import com.robertx22.age_of_exile.database.data.gear_types.bases.BaseGearType.SlotTag;
import com.robertx22.age_of_exile.database.data.gear_types.bases.TagList;
import com.robertx22.age_of_exile.database.data.stats.types.defense.Armor;
import com.robertx22.age_of_exile.saveclasses.gearitem.gear_bases.StatRequirement;
import com.robertx22.age_of_exile.uncommon.enumclasses.ModType;
import com.robertx22.age_of_exile.uncommon.enumclasses.PlayStyle;
import com.robertx22.age_of_exile.uncommon.enumclasses.WeaponTypes;
import com.robertx22.library_of_exile.registry.ExileRegistryInit;

public class BaseGearsAdder implements ExileRegistryInit, GearDataHelper {

    @Override
    public void registerAll() {
        

        // ARMOR
        BaseGearBuilder.of(BaseGearTypes.PLATE_BOOTS, GearSlots.BOOTS, "Plate Boots", StatRequirement.of(PlayStyle.STR))
                .tags(new TagList(SlotTag.armor_stat, SlotTag.boots, SlotTag.armor_family, SlotTag.strength))
                .baseStat(getStat(ArmorStat.ARMOR, ArmorSlot.BOOTS))
                .build();

        BaseGearBuilder.of(BaseGearTypes.PLATE_PANTS, GearSlots.PANTS, "Plate Pants", StatRequirement.of(PlayStyle.STR))
                .tags(new TagList(SlotTag.armor_stat, SlotTag.pants, SlotTag.armor_family, SlotTag.strength))
                .baseStat(getStat(ArmorStat.ARMOR, ArmorSlot.PANTS))
                .build();

        BaseGearBuilder.of(BaseGearTypes.PLATE_CHEST, GearSlots.CHEST, "Plate Chest", StatRequirement.of(PlayStyle.STR))
                .tags(new TagList(SlotTag.armor_stat, SlotTag.chest, SlotTag.armor_family, SlotTag.strength))
                .baseStat(getStat(ArmorStat.ARMOR, ArmorSlot.CHEST))
                .build();

        BaseGearBuilder.of(BaseGearTypes.PLATE_HELMET, GearSlots.HELMET, "Plate Helmet", StatRequirement.of(PlayStyle.STR))
                .tags(new TagList(SlotTag.armor_stat, SlotTag.helmet, SlotTag.armor_family, SlotTag.strength))
                .baseStat(getStat(ArmorStat.ARMOR, ArmorSlot.HELMET))
                .build();
        // ARMOR

        // MS
        BaseGearBuilder.of(BaseGearTypes.CLOTH_BOOTS, GearSlots.BOOTS, "Cloth Boots", StatRequirement.of(PlayStyle.INT))
                .tags(new TagList(SlotTag.magic_shield_stat, SlotTag.boots, SlotTag.armor_family, SlotTag.intelligence))
                .baseStat(getStat(ArmorStat.MAGIC_SHIELD, ArmorSlot.BOOTS))
                .build();

        BaseGearBuilder.of(BaseGearTypes.CLOTH_PANTS, GearSlots.PANTS, "Cloth Pants", StatRequirement.of(PlayStyle.INT))
                .tags(new TagList(SlotTag.magic_shield_stat, SlotTag.pants, SlotTag.armor_family, SlotTag.intelligence))
                .baseStat(getStat(ArmorStat.MAGIC_SHIELD, ArmorSlot.PANTS))
                .build();

        BaseGearBuilder.of(BaseGearTypes.CLOTH_CHEST, GearSlots.CHEST, "Cloth Chest", StatRequirement.of(PlayStyle.INT))
                .tags(new TagList(SlotTag.magic_shield_stat, SlotTag.chest, SlotTag.armor_family, SlotTag.intelligence))
                .baseStat(getStat(ArmorStat.MAGIC_SHIELD, ArmorSlot.CHEST))
                .build();

        BaseGearBuilder.of(BaseGearTypes.CLOTH_HELMET, GearSlots.HELMET, "Cloth Helmet", StatRequirement.of(PlayStyle.INT))
                .tags(new TagList(SlotTag.magic_shield_stat, SlotTag.helmet, SlotTag.armor_family, SlotTag.intelligence))
                .baseStat(getStat(ArmorStat.MAGIC_SHIELD, ArmorSlot.HELMET))
                .build();
        // MS

        // DODGE
        BaseGearBuilder.of(BaseGearTypes.LEATHER_BOOTS, GearSlots.BOOTS, "Leather Boots", StatRequirement.of(PlayStyle.DEX))
                .tags(new TagList(SlotTag.dodge_stat, SlotTag.boots, SlotTag.armor_family, SlotTag.dexterity))
                .baseStat(getStat(ArmorStat.DODGE, ArmorSlot.BOOTS))
                .build();

        BaseGearBuilder.of(BaseGearTypes.LEATHER_PANTS, GearSlots.PANTS, "Leather Pants", StatRequirement.of(PlayStyle.DEX))
                .tags(new TagList(SlotTag.dodge_stat, SlotTag.pants, SlotTag.armor_family, SlotTag.dexterity))
                .baseStat(getStat(ArmorStat.DODGE, ArmorSlot.PANTS))
                .build();

        BaseGearBuilder.of(BaseGearTypes.LEATHER_CHEST, GearSlots.CHEST, "Leather Chest", StatRequirement.of(PlayStyle.DEX))
                .tags(new TagList(SlotTag.dodge_stat, SlotTag.chest, SlotTag.armor_family, SlotTag.dexterity))
                .baseStat(getStat(ArmorStat.DODGE, ArmorSlot.CHEST))
                .build();

        BaseGearBuilder.of(BaseGearTypes.LEATHER_HELMET, GearSlots.HELMET, "Leather Helmet", StatRequirement.of(PlayStyle.DEX))
                .tags(new TagList(SlotTag.dodge_stat, SlotTag.helmet, SlotTag.armor_family, SlotTag.dexterity))
                .baseStat(getStat(ArmorStat.DODGE, ArmorSlot.HELMET))
                .build();
        // DODGE

        BaseGearBuilder.of(BaseGearTypes.RING, GearSlots.RING, "Ring", StatRequirement.of())
                .tags(new TagList(SlotTag.ring, SlotTag.jewelry_family))
                .build();

        BaseGearBuilder.of(BaseGearTypes.NECKLACE, GearSlots.NECKLACE, "Necklace", StatRequirement.of())
                .tags(new TagList(SlotTag.necklace, SlotTag.jewelry_family))
                .build();

        BaseGearBuilder.of(BaseGearTypes.SHIELD, GearSlots.SHIELD, "Tower Shield", StatRequirement.of(PlayStyle.STR))
                .tags(new TagList(SlotTag.shield, SlotTag.offhand_family, SlotTag.armor_stat, SlotTag.strength))
                .baseStat(new StatMod(6, 12, Armor.getInstance(), ModType.FLAT))
                .baseStat(new StatMod(-25, -25, Stats.STYLE_DAMAGE.get(PlayStyle.INT)))
                .build();

        BaseGearBuilder.weapon(BaseGearTypes.BOW, GearSlots.BOW, WeaponTypes.bow, StatRequirement.of(PlayStyle.DEX))
                .tags(new TagList(SlotTag.ranger_casting_weapon, SlotTag.bow, SlotTag.weapon_family, SlotTag.ranged_weapon, SlotTag.dexterity))
                .build();

        BaseGearBuilder.weapon(BaseGearTypes.CROSSBOW, GearSlots.CROSBOW, WeaponTypes.crossbow, StatRequirement.of(PlayStyle.DEX))
                .tags(new TagList(SlotTag.crossbow, SlotTag.ranger_casting_weapon, SlotTag.weapon_family, SlotTag.ranged_weapon, SlotTag.dexterity))
                .build();


        BaseGearBuilder.weapon(BaseGearTypes.SWORD, GearSlots.SWORD, WeaponTypes.sword, StatRequirement.of(PlayStyle.STR))
                .tags(new TagList(SlotTag.warrior_casting_weapon, SlotTag.sword, SlotTag.melee_weapon, SlotTag.weapon_family, SlotTag.strength, SlotTag.dexterity))
                .build();


        BaseGearBuilder.weapon(BaseGearTypes.STAFF, GearSlots.STAFF, WeaponTypes.staff, StatRequirement.of(PlayStyle.INT))
                .tags(new TagList(SlotTag.mage_weapon, SlotTag.staff, SlotTag.weapon_family, SlotTag.melee_weapon, SlotTag.intelligence))
                .build();

    }
}
