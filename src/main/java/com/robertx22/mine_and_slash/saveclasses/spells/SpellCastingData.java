package com.robertx22.mine_and_slash.saveclasses.spells;

import com.robertx22.library_of_exile.main.ExileLog;
import com.robertx22.library_of_exile.main.Packets;
import com.robertx22.library_of_exile.util.ExplainedResult;
import com.robertx22.mine_and_slash.a_libraries.player_animations.PlayerAnimations;
import com.robertx22.mine_and_slash.capability.entity.EntityData;
import com.robertx22.mine_and_slash.capability.player.data.PlayerConfigData;
import com.robertx22.mine_and_slash.config.forge.compat.CompatConfig;
import com.robertx22.mine_and_slash.database.data.exile_effects.ExileEffect;
import com.robertx22.mine_and_slash.database.data.exile_effects.ExileEffectInstanceData;
import com.robertx22.mine_and_slash.database.data.game_balance_config.GameBalanceConfig;
import com.robertx22.mine_and_slash.database.data.spells.components.Spell;
import com.robertx22.mine_and_slash.database.data.spells.entities.CalculatedSpellData;
import com.robertx22.mine_and_slash.database.data.spells.spell_classes.CastingWeapon;
import com.robertx22.mine_and_slash.database.data.spells.spell_classes.bases.SpellCastContext;
import com.robertx22.mine_and_slash.database.data.spells.spell_classes.bases.SpellPredicates;
import com.robertx22.mine_and_slash.database.data.stats.types.LearnSpellStat;
import com.robertx22.mine_and_slash.database.data.stats.types.MaxAllSpellLevels;
import com.robertx22.mine_and_slash.database.data.stats.types.MaxSpellLevel;
import com.robertx22.mine_and_slash.database.registry.ExileDB;
import com.robertx22.mine_and_slash.mmorpg.MMORPG;
import com.robertx22.mine_and_slash.saveclasses.item_classes.GearItemData;
import com.robertx22.mine_and_slash.saveclasses.skill_gem.SkillGemData;
import com.robertx22.mine_and_slash.saveclasses.unit.Unit;
import com.robertx22.mine_and_slash.uncommon.MathHelper;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import com.robertx22.mine_and_slash.uncommon.effectdatas.SpendResourceEvent;
import com.robertx22.mine_and_slash.uncommon.localization.Chats;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.RepairUtils;
import com.robertx22.mine_and_slash.vanilla_mc.packets.NoManaPacket;
import com.robertx22.mine_and_slash.vanilla_mc.packets.spells.TellClientEntityCastingSpell;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class SpellCastingData {

    public static final int SPELL_KEY_NOT_EXIST = -1;
    public HashMap<Integer, String> hotbar = new HashMap<>();


    public static class HotbarSpellData {
        public Spell spell;
        public int hotbarkey;

        public HotbarSpellData(Spell spell, int hotbarkey) {
            this.spell = spell;
            this.hotbarkey = hotbarkey;
        }
    }


    public boolean learnedSpellButHotbarIsEmpty() {
        return getAllHotbarSpells().isEmpty() && !spells.isEmpty();

    }

    public int keyOfSpell(String spell) {

        for (Map.Entry<Integer, String> en : hotbar.entrySet()) {
            if (en.getValue().equals(spell)) {
                return en.getKey();
            }
        }
        return SPELL_KEY_NOT_EXIST;
    }

    public List<HotbarSpellData> getAllHotbarSpellsInfo() {
        List<HotbarSpellData> list = new ArrayList<>();
        for (Integer i : hotbar.keySet()) {
            String spell = hotbar.getOrDefault(i, "");
            if (ExileDB.Spells().isRegistered(spell)) {
                list.add(new HotbarSpellData(ExileDB.Spells().get(spell), i));
            }
        }
        return list;
    }

    public List<InsertedSpell> getAllHotbarSpells() {
        List<InsertedSpell> list = new ArrayList<>();
        for (Integer i : hotbar.keySet()) {
            list.add(getSpellData(i));
        }
        list.removeIf(x -> x == null || x.getData() == null);
        return list;
    }

    public List<InsertedSpell> spells = new ArrayList<>();


    public void setHotbar(int slot, String spell) {

        for (Map.Entry<Integer, String> en : hotbar.entrySet()) {
            if (en.getValue().equals(spell)) {
                hotbar.put(en.getKey(), "");
            }
        }

        hotbar.put(slot, spell);
    }

    public void resetSpells() {
        spells.clear();
    }

    public void calcSpellLevels(Unit unit) {
        resetSpells();


        unit.getStats().stats.values()
                .forEach(x -> {
                    if (x.GetStat() instanceof LearnSpellStat learn) {
                        addSpell(new SpellCastingData.InsertedSpell(learn.spell.GUID(), (int) x.getValue()));
                    }
                });

        // todo this might be a bit perf heavy?
        unit.getStats().stats.values().forEach(x -> {
            if (x.GetStat() instanceof MaxSpellLevel max) {
                for (InsertedSpell spell : this.spells) {
                    if (spell.getSpell().config.tags.contains(max.tag)) {
                        spell.bonus_ranks += x.getValue();
                    }
                }
            } else if (x.GetStat() instanceof MaxAllSpellLevels) {
                for (InsertedSpell spell : this.spells) {
                    spell.bonus_ranks += x.getValue();
                }
            }
        });
        // caps the bonus ranks to config value max
        for (InsertedSpell spell : this.spells) {
            spell.bonus_ranks = MathHelper.clamp(spell.bonus_ranks, 0, GameBalanceConfig.get().MAX_BONUS_SPELL_LEVELS);
            spell.rank += spell.bonus_ranks;
        }

    }

    public void addSpell(InsertedSpell spell) {
        spells.add(spell);
    }

    public InsertedSpell getSpellData(int slot) {
        String id = getSpellId(slot);
        return spells.stream().filter(x -> x.id.equals(id)).findAny().orElse(new InsertedSpell("", 0));
    }

    public InsertedSpell getSpellData(String id) {
        return spells.stream().filter(x -> x.id.equals(id)).findAny().orElse(new InsertedSpell("", 0));
    }


    public String getSpellId(int slot) {
        return hotbar.getOrDefault(slot, "");
    }


    public static class InsertedSpell {

        public String id;
        public int rankBeforePlusSkills = 0;
        public int rank;
        public int bonus_ranks = 0;

        public Spell getSpell() {
            return ExileDB.Spells().get(id);
        }

        public InsertedSpell(String id, int rank) {
            this.id = id;
            this.rank = rank;
            this.rankBeforePlusSkills = rank;
        }

        public SkillGemData getData() {

            if (id.isEmpty()) {
                return null;
            }

            SkillGemData data = new SkillGemData();
            data.id = id;
            data.type = SkillGemData.SkillGemType.SKILL;

            data.setLinks(0);

            data.perc = (int) ((rankBeforePlusSkills / (float) data.getSpell().max_lvl) * 100);

            if (rankBeforePlusSkills > 1) {

                int total = rankBeforePlusSkills - 1;

                while (total > 2) {
                    total -= 3;

                    data.setLinks(data.getFlatLinks() + 1);

                }

            }

            return data;
        }
    }

    private static class SpellInputBufferEntry {
        public int number;
        public int ticksLeft;

        public SpellInputBufferEntry(int number) {
            this.number = number;
            this.ticksLeft = 5;
        }
    }

    public int castTickLeft = 0;
    public int castTicksDone = 0;
    public int spellTotalCastTicks = 0;
    public CalculatedSpellData calcSpell = null;
    public Boolean casting = false;
    public ChargeData charges = new ChargeData();

    // Spell inputs to continuously attempt
    transient List<SpellInputBufferEntry> spellInputBuffer = new LinkedList<>();
    // The hotbar index of the spell the client is casting
    transient int castSpellNumber = -1;
    // The hotbar index of the spell key the client is holding
    transient int spellInputNumber = -1;
    // How many ticks left without another packet before we stop casting
    transient int spellInputTimeoutTicks = 0;
    // Has no effect for non-channeled spells; init to true so channeled spells cancel on login
    transient boolean cancelChanneledSpell = true;
    // Whether this is the initial cast or a repeat of a channeled spell
    transient SpellCastContext.CastType castType = SpellCastContext.CastType.INITIAL_CAST;

    public void onSpellInputPressed(int number) {
        if (number != -1 && number != spellInputNumber) {
            // Cap size to prevent DoS
            if (spellInputBuffer.size() < 10) {
                spellInputBuffer.add(new SpellInputBufferEntry(number));
            }
        }
        if (number != castSpellNumber) {
            // no need to check if channeled
            cancelChanneledSpell = true;
        }
        spellInputNumber = number;
        spellInputTimeoutTicks = 8;
    }

    public Spell getSpellByNumber(Player player, int number) {
        return Load.player(player).getSkillGemInventory().getHotbarGem(number).getSpell();
    }

    private void setToCastAndSpendResources(SpellCastContext ctx) {

        ItemStack wep = ctx.caster.getMainHandItem();

        if (!wep.isEmpty() && !RepairUtils.isItemBroken(wep) && ctx.caster instanceof ServerPlayer p) {
            wep.hurt(1, ctx.caster.getRandom(), p);
        }

        setToCast(ctx);
        ctx.spell.spendResources(ctx);
    }

    public boolean tryStartSpellCast(Player player, int number) {

        var spell = getSpellByNumber(player, number);
        var data = Load.player(player);
        var cds = Load.Unit(player).getCooldowns();

        if (player.isBlocking() || player.swinging) {
            return false;
        }

        if (cds.isOnCooldown("global_cooldown")) {
            return false;
        }

        if (spell != null) {

            SpellCastContext c = new SpellCastContext(player, 0, spell);

            var can = canCast(c);

            if (can.can) {

                setToCastAndSpendResources(c);
                castSpellNumber = number;

                // Limit global cooldown to spell cooldown to allow rapid fire spells
                int gcd = Math.min(GameBalanceConfig.get().GLOBAL_COOLDOWN_TICKS, spell.getCooldownTicks(c));
                cds.setOnCooldown("global_cooldown", gcd);

                return true;
            } else if (!cds.isOnCooldown("spell_fail")) {
                cds.setOnCooldown("spell_fail", 40);
                if (can.answer != null) {
                    if (Load.Unit(player).getLevel() < 15 || Load.player(player).config.isConfigEnabled(PlayerConfigData.Config.CAST_FAIL)) {
                        player.sendSystemMessage(Chats.CAST_FAILED.locName().append(can.answer));
                    }
                }
            }

        }
        return false;
    }

    public void cancelCast(LivingEntity entity) {

        if (!isCasting()) {
            return;
        }

        Spell spell = getSpellBeingCast();
        SpellCastContext ctx = new SpellCastContext(entity, 0, spell, castType);

        onSpellCastFinished(ctx);

        for (Map.Entry<String, ExileEffectInstanceData> en : ctx.data.statusEffects.exileMap.entrySet()) {
            ExileEffect eff = ExileDB.ExileEffects().get(en.getKey());
            if (eff.remove_on_spell_cast != null) {
                if (spell.config.tags.contains(eff.remove_on_spell_cast)) {
                    en.getValue().stacks--;
                }
            }
        }

        this.calcSpell = null;
        castTickLeft = 0;
        spellTotalCastTicks = 0;
        castTicksDone = 0;
        castSpellNumber = -1;

        if (entity instanceof ServerPlayer p) {
            Load.player(p).playerDataSync.setDirty();
            TellClientEntityCastingSpell.sendUpdates(PlayerAnimations.CastEnum.CAST_FINISH, p, spell);
        }
    }

    public boolean isCasting() {
        return calcSpell != null && casting && ExileDB.Spells()
                .isRegistered(calcSpell.spell_id);
    }

    private void processSpellInputs(Player player) {

        if (spellInputTimeoutTicks > 0) {
            spellInputTimeoutTicks--;
        } else {
            // client stopped responding, don't cast forever
            spellInputNumber = -1;
        }

        // Prune input buffer
        for (Iterator<SpellInputBufferEntry> iterator = spellInputBuffer.iterator(); iterator.hasNext(); ) {
            if (iterator.next().ticksLeft-- == 0) {
                iterator.remove();
            }
        }

        // See if any buffered inputs succeed
        for (Iterator<SpellInputBufferEntry> iterator = spellInputBuffer.iterator(); iterator.hasNext(); ) {
            if (tryStartSpellCast(player, iterator.next().number)) {
                iterator.remove();
                return;
            }
        }

        // If not, try held input
        if (spellInputNumber != -1) {
            tryStartSpellCast(player, spellInputNumber);
        }
    }

    private boolean tryLoopChanneledSpell(LivingEntity entity, Spell spell) {

        if (spell.config.channeled && entity instanceof Player player) {
            SpellCastContext ctx = new SpellCastContext(player, 0, spell, SpellCastContext.CastType.CHANNEL_LOOP);

            if (canCast(ctx).can) {
                setToCastAndSpendResources(ctx);
                this.castType = SpellCastContext.CastType.CHANNEL_LOOP;
                return true;
            }
        }

        return false;
    }

    private void onCastingTick(LivingEntity entity) {

        castTickLeft--;
        castTicksDone++;

        if (entity.level().isClientSide) {
            return;
        }

        Spell spell = this.calcSpell.getSpell();

        if (spell.config.channeled) {
            if (castSpellNumber != spellInputNumber) {
                cancelChanneledSpell = true;
            }

            if (cancelChanneledSpell) {
                cancelCast(entity);
                return;
            }
        }

        SpellCastContext ctx = new SpellCastContext(entity, castTicksDone, spell, castType);

        spell.runTickActions(ctx);

        int timesToCast = (int) ctx.spell.getConfig().times_to_cast;

        if (timesToCast > 1) {
            // check how many times we should've cast by now to see if it increased
            int castCountLastTick = (castTicksDone - 1) * timesToCast / spellTotalCastTicks;
            int castCountThisTick = castTicksDone * timesToCast / spellTotalCastTicks;

            if (castCountThisTick != castCountLastTick) {
                spell.cast(ctx);
            }
        } else if (timesToCast == 1) {
            if (castTickLeft <= 0) {
                spell.cast(ctx);
            }
        } else {
            ExileLog.get().warn("Times to cast spell is: " + timesToCast + " . this seems like a bug.");
        }

        if (castTickLeft <= 0) {
            if (!tryLoopChanneledSpell(entity, spell)) {
                cancelCast(entity);
            }
        }
    }

    public void onTimePass(LivingEntity entity) {

        if (entity instanceof ServerPlayer player) {
            processSpellInputs(player);
        }

        if (isCasting()) {
            try {
                onCastingTick(entity);
            } catch (Exception e) {
                e.printStackTrace();
                this.cancelCast(entity);
                // cancel when error, cus this is called on tick, so it doesn't crash servers when 1 spell fails
            }
        }
    }

    public List<String> getSpellsOnCooldown(LivingEntity en) {
        return Load.Unit(en)
                .getCooldowns()
                .getAllSpellsOnCooldown();
    }

    public void setToCast(SpellCastContext ctx) {

        this.calcSpell = ctx.calcData;
        this.castTickLeft = ctx.spell.getCastTimeTicks(ctx);
        this.spellTotalCastTicks = this.castTickLeft;
        this.castTicksDone = 0;
        this.casting = true;
        this.cancelChanneledSpell = false;
        this.castType = SpellCastContext.CastType.INITIAL_CAST;

        if (ctx.caster instanceof ServerPlayer p) {
            Load.player(p).playerDataSync.setDirty();
            TellClientEntityCastingSpell.sendUpdates(PlayerAnimations.CastEnum.CAST_START, p, ctx.spell);
        }
    }

    public Spell getSpellBeingCast() {

        if (calcSpell != null) {
            return calcSpell.getSpell();
        }


        return null;
    }

    public ExplainedResult canCast(SpellCastContext ctx) {

        Player player = (Player) ctx.caster;
        Spell spell = ctx.spell;

        if (player.level().isClientSide) {
            return ExplainedResult.failure(Component.literal("Client side"));
        }
        if (ctx.type != SpellCastContext.CastType.CHANNEL_LOOP && isCasting()) {
            return ExplainedResult.failure(Chats.ALREADY_CASTING.locName());
        }


        if (spell == null) {
            return ExplainedResult.failure(Component.literal("Trying to cast NULL Spell, this shouldn't happen"));
        }

        if (spell.getLevelOf(player) < 1) {
            return ExplainedResult.failure(Component.literal("You did not learn this spell"));
        }

        if (Load.Unit(player).getCooldowns().isOnCooldown(spell.GUID())) {
            // dont spam chat with no cd msgs for stuff like fireball
            if (Load.Unit(player).getCooldowns().getCooldownTicks(spell.GUID()) > 40) {
                return ExplainedResult.failure(Chats.SPELL_IS_ON_CD.locName());
            }
            return ExplainedResult.silentlyFail();
        }


        if (player.isCreative()) {
            return ExplainedResult.success();
        }

        if (spell.GUID().contains("test")) {
            if (!MMORPG.RUN_DEV_TOOLS) {
                return ExplainedResult.failure(Chats.USING_TEST_SPELL.locName());
            }
        }

        if (spell.config.charges > 0) {
            if (!charges.hasCharge(spell.config.charge_name)) {
                return ExplainedResult.failure(Chats.NO_CHARGES.locName());
            }
        }

        EntityData data = Load.Unit(player);

        if (data != null) {

            if (!spell.isAllowedInDimension(player.level())) {
                return ExplainedResult.failure(Chats.NOT_IN_THIS_DIMENSION.locName());
            }

            SpendResourceEvent mana = spell.getManaCostCtx(ctx);
            SpendResourceEvent energy = spell.getEnergyCostCtx(ctx);


            if (data.getResources().hasEnough(mana) && data.getResources().hasEnough(energy)) {

                var opt = Load.Unit(player).equipmentCache.getWeaponOpt();

                if (RepairUtils.isItemBroken(player.getMainHandItem())) {
                    return ExplainedResult.failure(Chats.CANT_CAST_WITH_BROKEN_WEAPON.locName());
                }


                if (!CompatConfig.get().ignoreWeaponReqForSpells()) {

                    GearItemData wep = opt.map(x -> x.gear).orElse(null);

                    if (wep == null) {
                        return ExplainedResult.failure(Chats.NOT_MNS_WEAPON.locName());
                    }

                    if (!spell.getConfig().castingWeapon.predicate.predicate.test(player)) {
                        // If the spell requires a mage weapon and the player is a battlemage, allow casting
                        if (spell.getConfig().castingWeapon == CastingWeapon.MAGE_WEAPON && data.getUnit().isBattlemage()) {
                            // Do nothing, allow casting
                        } else {
                            return ExplainedResult.failure(Chats.WRONG_CASTING_WEAPON.locName());
                        }
                    }

                    if (!wep.canPlayerWear(ctx.data)) {
                        return ExplainedResult.failure(Chats.WEAPON_REQ_NOT_MET.locName());
                    }
                }

                return ExplainedResult.success();
            } else {
                if (player instanceof ServerPlayer) {
                    Packets.sendToClient((Player) player, new NoManaPacket());
                    return ExplainedResult.failure(Chats.NO_MANA.locName());
                }
            }
        }
        return ExplainedResult.silentlyFail();

    }

    public void setCooldownOnCasted(SpellCastContext ctx) {

        int cd = ctx.spell.getCooldownTicks(ctx);

        ctx.data.getCooldowns().setOnCooldown(ctx.spell.GUID(), cd);

        if (ctx.spell.config.charges > 0) {
            if (ctx.caster instanceof Player) {
                int chargecd = ctx.spell.getChargeCooldownTicks(ctx);
                this.charges.spendCharge((Player) ctx.caster, ctx.spell, chargecd);
            }
        }

        if (ctx.caster instanceof Player) {
            Player p = (Player) ctx.caster;
            if (p.isCreative()) {
                if (cd > 20) {
                    ctx.data.getCooldowns().setOnCooldown(ctx.spell.GUID(), 20);
                }
            }
        }

        ctx.data.sync.setDirty();

    }

    public void onSpellCastFinished(SpellCastContext ctx) {

        setCooldownOnCasted(ctx);
        this.casting = false;

        /*
        if (ctx.caster instanceof ServerPlayer p) {
            Load.Unit(ctx.caster).sync.setDirty();
            Packets.sendToClient(p, new TellClientEntityCastingSpell(PlayerAnimations.CastEnum.CAST_FINISH, p, ctx.spell));
        }

         */
    }

}
