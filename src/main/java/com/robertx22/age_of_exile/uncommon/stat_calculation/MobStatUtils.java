package com.robertx22.age_of_exile.uncommon.stat_calculation;

import com.robertx22.age_of_exile.aoe_data.database.stats.Stats;
import com.robertx22.age_of_exile.capability.entity.EntityData;
import com.robertx22.age_of_exile.config.forge.ServerContainer;
import com.robertx22.age_of_exile.database.data.EntityConfig;
import com.robertx22.age_of_exile.database.data.rarities.MobRarity;
import com.robertx22.age_of_exile.database.data.stats.types.defense.Armor;
import com.robertx22.age_of_exile.database.data.stats.types.defense.DodgeRating;
import com.robertx22.age_of_exile.database.data.stats.types.generated.ElementalResist;
import com.robertx22.age_of_exile.database.data.stats.types.offense.SpellDamage;
import com.robertx22.age_of_exile.database.data.stats.types.resources.health.Health;
import com.robertx22.age_of_exile.database.data.stats.types.resources.health.HealthRegen;
import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.age_of_exile.saveclasses.ExactStatData;
import com.robertx22.age_of_exile.saveclasses.unit.Unit;
import com.robertx22.age_of_exile.saveclasses.unit.stat_ctx.MiscStatCtx;
import com.robertx22.age_of_exile.saveclasses.unit.stat_ctx.StatContext;
import com.robertx22.age_of_exile.uncommon.datasaving.Load;
import com.robertx22.age_of_exile.uncommon.enumclasses.Elements;
import com.robertx22.age_of_exile.uncommon.enumclasses.ModType;
import com.robertx22.age_of_exile.uncommon.utilityclasses.WorldUtils;
import com.robertx22.library_of_exile.utils.EntityUtils;
import net.minecraft.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class MobStatUtils {

    public static void addMapStats(LivingEntity en, EntityData mobdata, Unit unit) {


        if (WorldUtils.isMapWorldClass(en.level)) {


        }

    }

    public static List<StatContext> getAffixStats(LivingEntity en) {
        List<StatContext> list = new ArrayList<>();
        Load.Unit(en)
                .getAffixData()
                .getAffixes()
                .forEach(x -> list.addAll(x.getStatAndContext(en)));
        return list;

    }

    public static List<StatContext> getWorldMultiplierStats(LivingEntity en) {

        List<StatContext> list = new ArrayList<>();

        List<ExactStatData> stats = new ArrayList<>();

        float val = (-1F + ExileDB.getDimensionConfig(en.level).mob_strength_multi) * 100F;

        stats.add(ExactStatData.noScaling(val, ModType.MORE, Health.getInstance()
                .GUID()));
        stats.add(ExactStatData.noScaling(val, ModType.MORE, Stats.TOTAL_DAMAGE.get()
                .GUID()));

        list.add(new MiscStatCtx(stats));

        return list;

    }

    public static List<StatContext> getMobConfigStats(LivingEntity entity, EntityData unitdata) {
        List<StatContext> list = new ArrayList<>();
        List<ExactStatData> stats = new ArrayList<>();

        EntityConfig config = ExileDB.getEntityConfig(entity, unitdata);

        config.stats.stats.forEach(x -> stats.add(x.toExactStat(unitdata.getLevel())));

        float hp = (float) ((-1F + config.hp_multi) * 100F);
        float dmg = (float) ((-1F + config.dmg_multi) * 100F);
        float stat = (float) ((-1F + config.stat_multi) * 100F);

        stats.add(ExactStatData.noScaling(hp, ModType.MORE, Health.getInstance()
                .GUID()));
        stats.add(ExactStatData.noScaling(dmg, ModType.FLAT, Stats.TOTAL_DAMAGE.get()
                .GUID()));

        stats.add(ExactStatData.noScaling(stat, ModType.MORE, DodgeRating.getInstance()
                .GUID()));
        stats.add(ExactStatData.noScaling(stat, ModType.MORE, Armor.getInstance()
                .GUID()));
        stats.add(ExactStatData.noScaling(stat, ModType.MORE, new ElementalResist(Elements.Elemental)
                .GUID()));
        stats.add(ExactStatData.noScaling(stat, ModType.MORE, Health.getInstance()
                .GUID()));

        list.add(new MiscStatCtx(stats));

        return list;
    }

    public static List<StatContext> getMobBaseStats(EntityData unitdata, LivingEntity en) {
        List<StatContext> list = new ArrayList<>();
        List<ExactStatData> stats = new ArrayList<>();

        MobRarity rar = unitdata.getMobRarity();

        int lvl = unitdata.getLevel();

        float hpToAdd = EntityUtils.getVanillaMaxHealth(en) * rar.ExtraHealthMulti();

        hpToAdd += (ServerContainer.get().EXTRA_MOB_STATS_PER_LEVEL.get() * lvl) * hpToAdd;

        if (hpToAdd < 0) {
            hpToAdd = 0;
        }


        stats.add(ExactStatData.scaleTo(hpToAdd, ModType.FLAT, Health.getInstance()
                .GUID(), lvl));

        stats.add(ExactStatData.scaleTo(0.5F, ModType.FLAT, HealthRegen.getInstance()
                .GUID(), lvl));

        stats.add(ExactStatData.scaleTo(1, ModType.FLAT, Stats.ACCURACY.get()
                .GUID(), lvl));
        stats.add(ExactStatData.scaleTo(10 * rar.StatMultiplier(), ModType.FLAT, Armor.getInstance()
                .GUID(), lvl));
        stats.add(ExactStatData.scaleTo(10 * rar.StatMultiplier(), ModType.FLAT, new ElementalResist(Elements.Elemental)
                .GUID(), lvl));

        stats.add(ExactStatData.scaleTo(5 * rar.DamageMultiplier(), ModType.FLAT, Stats.CRIT_CHANCE.get()
                .GUID(), lvl));

        stats.add(ExactStatData.scaleTo(-25, ModType.FLAT, SpellDamage.getInstance()
                .GUID(), lvl));

        list.add(new MiscStatCtx(stats));

        return list;

    }

}
