package com.robertx22.mine_and_slash.vanilla_mc.packets;

import com.robertx22.mine_and_slash.database.data.spells.components.Spell;
import com.robertx22.mine_and_slash.database.registry.ExileDB;
import com.robertx22.mine_and_slash.gui.screens.stat_gui.StatScreen;
import com.robertx22.mine_and_slash.mmorpg.SlashRef;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import com.robertx22.mine_and_slash.uncommon.datasaving.UnitNbt;
import com.robertx22.library_of_exile.main.MyPacket;
import com.robertx22.library_of_exile.packets.ExilePacketContext;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class GetSpellStatsReplyPacket extends MyPacket<GetSpellStatsReplyPacket> {

    public int id;
    public String spellid;
    public CompoundTag nbt;

    public GetSpellStatsReplyPacket() {

    }

    public GetSpellStatsReplyPacket(Player player, Spell spell) {
        this.id = player.getId();
        this.spellid = spell.GUID();
        this.nbt = new CompoundTag();
        UnitNbt.Save(this.nbt, Load.player(player).getSpellUnitStats(spell));
    }

    @Override
    public ResourceLocation getIdentifier() {
        return new ResourceLocation(SlashRef.MODID, "getspellstatsreply");
    }

    @Override
    public void loadFromData(FriendlyByteBuf tag) {
        id = tag.readInt();
        spellid = tag.readUtf();
        nbt = tag.readNbt();
    }

    @Override
    public void saveToData(FriendlyByteBuf tag) {
        tag.writeInt(id);
        tag.writeUtf(spellid);
        tag.writeNbt(nbt);
    }

    @Override
    public void onReceived(ExilePacketContext ctx) {
        if (!(Minecraft.getInstance().screen instanceof StatScreen screen)) {
            return;
        }
        if (!ExileDB.Spells().isRegistered(spellid)) {
            return;
        }
        // make sure we're still looking at this spell
        if (screen.getTarget() != ctx.getPlayer().level().getEntity(id)) {
            return;
        }
        if (screen.getSpell() != ExileDB.Spells().get(spellid)) {
            return;
        }
        screen.setUnit(UnitNbt.Load(nbt));
    }

    @Override
    public MyPacket<GetSpellStatsReplyPacket> newInstance() {
        return new GetSpellStatsReplyPacket();
    }
}
