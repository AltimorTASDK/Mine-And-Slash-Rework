package com.robertx22.mine_and_slash.tags;

public enum TagType {
    Effect("effect"),
    Spell("spell"),
    Element("element"),
    Mob("mob"),
    Dungeon("dungeon"),
    GearSlot("gear_slot");

    public String id;

    TagType(String id) {
        this.id = id;
    }
}