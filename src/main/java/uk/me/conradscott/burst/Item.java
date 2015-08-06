package uk.me.conradscott.burst;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public class Item implements ItemIfc {
    private final char m_glyph;

    @NotNull private final Color m_color;

    @NotNull private final String m_name;
    private int m_foodValue = 0;

    public Item( final char glyph, @NotNull final Color color, @NotNull final String name ) {
        m_glyph = glyph;
        m_color = color;
        m_name = name;
    }

    @Override
    public char glyph() {
        return m_glyph;
    }

    @Override
    @NotNull
    public Color color() {
        return m_color;
    }

    @Override
    @NotNull
    public String name() {
        return m_name;
    }

    @Override
    public int foodValue() {
        return m_foodValue;
    }

    @Override
    public void modifyFoodValue( final int amount ) {
        m_foodValue += amount;
    }
}
