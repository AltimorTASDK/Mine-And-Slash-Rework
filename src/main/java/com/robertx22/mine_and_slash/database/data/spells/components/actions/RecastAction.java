package com.robertx22.mine_and_slash.database.data.spells.components.actions;

import com.robertx22.mine_and_slash.database.data.spells.components.MapHolder;
import com.robertx22.mine_and_slash.database.data.spells.map_fields.MapField;
import com.robertx22.mine_and_slash.database.data.spells.spell_classes.SpellCtx;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import static com.robertx22.mine_and_slash.database.data.spells.map_fields.MapField.CAST_TIME_TICKS;

import java.util.Arrays;
import java.util.Collection;

public class RecastAction extends SpellAction {

    public RecastAction() {
        super(Arrays.asList());
    }

    @Override
    public void tryActivate(Collection<LivingEntity> targets, SpellCtx ctx, MapHolder data) {

        if (ctx.caster instanceof Player p) {
            Load.player(p).spellCastingData.castTimeOverride = data.getOrDefault(CAST_TIME_TICKS, -1.0).intValue();
            Load.player(p).spellCastingData.recast(p);
        }

    }

    public MapHolder create(int castTimeOverride) {
        MapHolder c = new MapHolder();
        c.type = GUID();
        c.put(CAST_TIME_TICKS, (double) castTimeOverride);
        this.validate(c);
        return c;
    }

    public MapHolder create() {
        return create(-1);
    }

    @Override
    public String GUID() {
        return "recast";
    }

}
