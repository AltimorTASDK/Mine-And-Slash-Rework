package com.robertx22.mine_and_slash.database.data.stats.layers;

import com.robertx22.mine_and_slash.uncommon.STATICS;

public class StatLayers {

    public static class Offensive {
        public static StatLayer DAMAGE_CONVERSION = new StatLayer("damage_conversion", "Damage Conversion", StatLayer.LayerAction.ADD, -999, 0, STATICS.MAX_FLOAT);

        public static StatLayer FLAT_DAMAGE = new StatLayer("flat_damage", "Flat Damage", StatLayer.LayerAction.ADD, 0, -STATICS.MAX_FLOAT, STATICS.MAX_FLOAT);
        public static StatLayer ADDITIVE_DMG = new StatLayer("additive_damage", "Additive Damage", StatLayer.LayerAction.MULTIPLY, 1, -1, STATICS.MAX_FLOAT);
        public static StatLayer DOT_DMG_MULTI = new StatLayer("dot_dmg_multi", "DOT Damage Multi", StatLayer.LayerAction.MULTIPLY, 1, -1, STATICS.MAX_FLOAT);
        public static StatLayer CRIT_DAMAGE = new StatLayer("crit_damage", "Crit Damage", StatLayer.LayerAction.MULTIPLY, 2, -1, STATICS.MAX_FLOAT);
        public static StatLayer DOUBLE_DAMAGE = new StatLayer("double_damage", "Double Damage", StatLayer.LayerAction.MULTIPLY, 3, 2, 2);

        public static void init() {


        }
    }

    public static class Defensive {

        // todo does this work its still confusing and probably buggy!!!

        public static StatLayer PHYS_MITIGATION = new StatLayer("physical_mitigation", "Physical Mitigation", StatLayer.LayerAction.MULTIPLY, 100, 0.1F, STATICS.MAX_FLOAT);
        public static StatLayer ELEMENTAL_MITIGATION = new StatLayer("elemental_mitigation", "Elemental Mitigation", StatLayer.LayerAction.MULTIPLY, 101, 0.1F, STATICS.MAX_FLOAT);
        public static StatLayer DAMAGE_REDUCTION = new StatLayer("damage_reduction", "Damage Reduction", StatLayer.LayerAction.MULTIPLY, 102, 0.5F, STATICS.MAX_FLOAT);
        public static StatLayer DAMAGE_SUPPRESSION = new StatLayer("damage_suppression", "Damage Suppression", StatLayer.LayerAction.MULTIPLY, 103, 0.5F, 1);

        public static StatLayer FLAT_DAMAGE_REDUCTION = new StatLayer("flat_damage_reduction", "Flat Damage Reduction", StatLayer.LayerAction.ADD, 200, -1000, STATICS.MAX_FLOAT);

        public static void init() {


        }

    }


    public static class Misc {

        public static void init() {


        }
    }

    public static void init() {

        Defensive.init();
        Offensive.init();
        Misc.init();

    }

    public static void register() {

        for (StatLayer a : StatLayer.ALL) {
            a.addToSerializables();
        }

    }
}
