package uk.me.conradscott.burst;

import asciipanel.Colors;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public enum Tile implements TileIfc {
    FLOOR( ( char ) 250, Colors.YELLOW ) {
        @Override
        public boolean isGround() {
            return true;
        }
    },

    WALL( ( char ) 177, Colors.YELLOW ) {
        @Override
        public boolean isDiggable() {
            return true;
        }
    },

    BOUNDS( 'x', Colors.BRIGHT_BLACK ),

    STAIRS_DOWN( '>', Colors.WHITE ) {
        @Override
        public boolean isGround() {
            return true;
        }
    },

    STAIRS_UP( '<', Colors.WHITE ) {
        @Override
        public boolean isGround() {
            return true;
        }
    },

    UNKNOWN( ' ', Colors.WHITE );

    private final char m_glyph;

    @NotNull private final Color m_color;

    Tile( final char glyph, @NotNull final Color color ) {
        m_glyph = glyph;
        m_color = color;
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
    public String toString() {
        return "Tile{" +
               "m_glyph=" + m_glyph +
               ", m_color=" + m_color +
               '}';
    }
}
