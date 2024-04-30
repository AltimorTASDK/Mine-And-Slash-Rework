package com.robertx22.age_of_exile.database.data.stats.layers;

import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.age_of_exile.uncommon.MathHelper;


// example: you have 200% fire resist, enemy has 10% fire pene, you end up with 190% fire res which means enemy doesnt do any extra dmg to you
public class StatLayerData {

    private String layer = "";
    public String numberID = "";
    private float number = 0;

    public StatLayerData(String layer, String numberID) {
        this.layer = layer;
        this.numberID = numberID;
    }

    public StatLayer getLayer() {
        return ExileDB.StatLayers().get(layer);
    }

    public void add(float num) {
        this.number += num;
    }

    public void multiply(float multi) {
        this.number *= multi;
    }

    public void reduce(float num) {
        this.number -= num;
    }

    public float getNumber() {
        var lay = getLayer();
        float num = MathHelper.clamp(number, lay.min_multi, lay.max_multi);
        return num;
    }

    public float getMultiplier() {
        var lay = getLayer();

        float num = number;
        float multi = 1 + (num / 100F);
        multi = MathHelper.clamp(multi, lay.min_multi, lay.max_multi);
        return multi;

    }
}