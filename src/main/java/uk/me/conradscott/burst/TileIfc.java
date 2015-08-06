package uk.me.conradscott.burst;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public interface TileIfc {
    char glyph();

    @NotNull
    Color color();

    default boolean isDiggable() {
        return false;
    }

    default boolean isGround() {
        return false;
    }

    default boolean isCreature() {
        return false;
    }

    default boolean isItem() {
        return false;
    }
}
