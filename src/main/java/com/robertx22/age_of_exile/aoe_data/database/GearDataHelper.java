package com.robertx22.age_of_exile.aoe_data.database;

import com.robertx22.age_of_exile.database.data.StatModifier;
import com.robertx22.age_of_exile.database.data.stats.Stat;
import com.robertx22.age_of_exile.database.data.stats.types.defense.Armor;
import com.robertx22.age_of_exile.database.data.stats.types.defense.DodgeRating;
import com.robertx22.age_of_exile.database.data.stats.types.generated.AttackDamage;
import com.robertx22.age_of_exile.database.data.stats.types.resources.magic_shield.MagicShield;
import com.robertx22.age_of_exile.uncommon.enumclasses.Elements;
import com.robertx22.age_of_exile.uncommon.enumclasses.ModType;
import com.robertx22.age_of_exile.uncommon.enumclasses.WeaponTypes;

public interface GearDataHelper {


    public enum ArmorSlot {
        HELMET(0.5F),
        CHEST(1F),
        PANTS(0.8F),
        BOOTS(0.5F);

        public float multi;

        ArmorSlot(float multi) {
            this.multi = multi;
        }
    }

    public enum ArmorStat {
        ARMOR(6, 15, Armor.getInstance()),
        MAGIC_SHIELD(6, 15, MagicShield.getInstance()),
        DODGE(6, 15, DodgeRating.getInstance());

        public float min;
        public float max;
        public Stat stat;

        ArmorStat(float min, float max, Stat stat) {
            this.min = min;
            this.max = max;
            this.stat = stat;
        }
    }

    public default StatModifier getStat(ArmorStat stat, ArmorSlot slot) {

        float v1min = stat.min * slot.multi;
        float v1max = stat.max * slot.multi;

        return new StatModifier(v1min, v1max, stat.stat, ModType.ITEM_FLAT);
    }

    public default StatModifier getAttackDamageStat(WeaponTypes weapon, Elements element) {

        float v1min = 2 * weapon.statMulti;
        float v1max = 4 * weapon.statMulti;
// todo
        return new StatModifier(v1min, v1max, new AttackDamage(element), ModType.ITEM_FLAT);
    }

}
