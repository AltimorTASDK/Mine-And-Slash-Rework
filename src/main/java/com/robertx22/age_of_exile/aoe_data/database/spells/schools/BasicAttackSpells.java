package com.robertx22.age_of_exile.aoe_data.database.spells.schools;

import com.robertx22.age_of_exile.aoe_data.database.spells.PartBuilder;
import com.robertx22.age_of_exile.aoe_data.database.spells.SpellBuilder;
import com.robertx22.age_of_exile.aoe_data.database.spells.SpellCalcs;
import com.robertx22.age_of_exile.database.data.spells.SpellTag;
import com.robertx22.age_of_exile.database.data.spells.components.SpellConfiguration;
import com.robertx22.age_of_exile.database.data.spells.components.actions.SpellAction;
import com.robertx22.age_of_exile.database.data.spells.spell_classes.CastingWeapon;
import com.robertx22.age_of_exile.mmorpg.registers.common.SlashEntities;
import com.robertx22.age_of_exile.mmorpg.registers.common.items.SlashItems;
import com.robertx22.age_of_exile.uncommon.enumclasses.Elements;
import com.robertx22.age_of_exile.uncommon.enumclasses.PlayStyle;
import com.robertx22.library_of_exile.registry.ExileRegistryInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

import java.util.Arrays;

public class BasicAttackSpells implements ExileRegistryInit {
    public static String POISONBALL_ID = "poison_ball";
    public static String FIREBALL_ID = "fireball";
    public static String FROSTBALL_ID = "frostball";


    @Override
    public void registerAll() {
        int cd = 10;
        int mana = 0;
        SpellBuilder.of(FROSTBALL_ID, PlayStyle.INT, SpellConfiguration.Builder.instant(mana, cd)
                                .setSwingArm()
                                .applyCastSpeedToCooldown(), "Ice Ball",
                        Arrays.asList(SpellTag.projectile, SpellTag.damage, SpellTag.staff_spell))
                .manualDesc(
                        "Throw out a ball of ice, dealing " + SpellCalcs.ICEBALL.getLocDmgTooltip()
                                + " " + Elements.Cold.getIconNameDmg())

                .weaponReq(CastingWeapon.MAGE_WEAPON)
                .onCast(PartBuilder.playSound(SoundEvents.SNOWBALL_THROW, 1D, 1D))
                .onCast(PartBuilder.justAction(SpellAction.SUMMON_PROJECTILE.create(SlashItems.SNOWBALL.get(), 1D, 2.5D, SlashEntities.SIMPLE_PROJECTILE.get(), 8D, false)
                ))
                .onTick(PartBuilder.particleOnTick(1D, ParticleTypes.ITEM_SNOWBALL, 2D, 0.15D))
                .onTick(PartBuilder.particleOnTick(1D, ParticleTypes.SNOWFLAKE, 7D, 0.3D))
                .onExpire(PartBuilder.damageInAoe(SpellCalcs.ICEBALL, Elements.Cold, 2D))
                .onExpire(PartBuilder.aoeParticles(ParticleTypes.ITEM_SNOWBALL, 5D, 1D))
                .onExpire(PartBuilder.aoeParticles(ParticleTypes.SNOWFLAKE, 15D, 0.5D))
                .build();

        SpellBuilder.of(FIREBALL_ID, PlayStyle.INT, SpellConfiguration.Builder.instant(mana, cd)
                                .setSwingArm()
                                .applyCastSpeedToCooldown(), "Fire Ball",
                        Arrays.asList(SpellTag.projectile, SpellTag.damage, SpellTag.staff_spell))
                .manualDesc(
                        "Throw out a ball of fire, dealing " + SpellCalcs.FIREBALL.getLocDmgTooltip()
                                + " " + Elements.Fire.getIconNameDmg())
                .weaponReq(CastingWeapon.MAGE_WEAPON)

                .onCast(PartBuilder.playSound(SoundEvents.BLAZE_SHOOT, 1D, 0.6D))
                .onCast(PartBuilder.justAction(SpellAction.SUMMON_PROJECTILE.create(SlashItems.FIREBALL.get(), 1D, 2.5D, SlashEntities.SIMPLE_PROJECTILE.get(), 8D, false)
                ))
                .onTick(PartBuilder.particleOnTick(1D, ParticleTypes.FLAME, 1D, 0.1D))
                .onTick(PartBuilder.particleOnTick(1D, ParticleTypes.FALLING_LAVA, 1D, 0.5D))
                .onTick(PartBuilder.particleOnTick(1D, ParticleTypes.SMOKE, 1D, 0.01D))
                .onHit(PartBuilder.damageInAoe(SpellCalcs.FIREBALL, Elements.Fire, 2D))
                .onHit(PartBuilder.playSound(SoundEvents.GENERIC_BURN, 1D, 2D))
                .onHit(PartBuilder.aoeParticles(ParticleTypes.SMOKE, 3D, 1D))
                .build();


        SpellBuilder.of(POISONBALL_ID, PlayStyle.INT, SpellConfiguration.Builder.instant(mana, cd)
                                .setSwingArm()
                                .applyCastSpeedToCooldown(), "Poison Ball",
                        Arrays.asList(SpellTag.projectile, SpellTag.damage, SpellTag.staff_spell))
                .manualDesc(
                        "Throw out a ball of poison, dealing " + SpellCalcs.POISON_BALL.getLocDmgTooltip()
                                + " " + Elements.Chaos.getIconNameDmg())
                .weaponReq(CastingWeapon.MAGE_WEAPON)
                .onCast(PartBuilder.playSound(SoundEvents.SNOWBALL_THROW, 1D, 1D))
                .onCast(PartBuilder.justAction(SpellAction.SUMMON_PROJECTILE.create(SlashItems.SLIMEBALL.get(), 1D, 2.5D, SlashEntities.SIMPLE_PROJECTILE.get(), 8D, false)
                ))
                .onTick(PartBuilder.particleOnTick(1D, ParticleTypes.SNEEZE, 1D, 0.15D))
                .onTick(PartBuilder.particleOnTick(1D, ParticleTypes.ITEM_SLIME, 10D, 0.15D))
                .onExpire(PartBuilder.damageInAoe(SpellCalcs.POISON_BALL, Elements.Chaos, 2D))
                .onExpire(PartBuilder.aoeParticles(ParticleTypes.ITEM_SLIME, 100D, 1D))
                .onExpire(PartBuilder.aoeParticles(ParticleTypes.SNEEZE, 25D, 1D))

                .build();
    }
}