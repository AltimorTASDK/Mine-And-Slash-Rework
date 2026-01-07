package com.robertx22.mine_and_slash.database.data.spells.components.conditions;

import com.robertx22.mine_and_slash.database.data.spells.components.MapHolder;
import com.robertx22.mine_and_slash.database.data.spells.spell_classes.SpellCtx;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;

import net.minecraft.world.entity.player.Player;

import java.util.Arrays;

public class IsSpellKeyHeldCondition extends EffectCondition {

    public IsSpellKeyHeldCondition() {
        super(Arrays.asList());
    }

    @Override
    public boolean canActivate(SpellCtx ctx, MapHolder data) {
        return ctx.caster instanceof Player p && Load.player(p).spellCastingData.isSpellKeyHeld();
    }

    public MapHolder create() {
        MapHolder d = new MapHolder();
        d.type = GUID();
        return d;
    }

    @Override
    public String GUID() {
        return "is_spell_key_held";
    }
}

