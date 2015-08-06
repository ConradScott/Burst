package asciipanel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;

public final class CharacterData {
    private final char m_character;

    @NotNull private final Color m_foregroundColor;

    @NotNull private final Color m_backgroundColor;

    public CharacterData( final char character, @NotNull final Color foregroundColor,
                          @NotNull final Color backgroundColor )
    {
        m_character = character;
        m_foregroundColor = foregroundColor;
        m_backgroundColor = backgroundColor;
    }

    public char getCharacter() {
        return m_character;
    }

    @NotNull
    public Color getForegroundColor() {
        return m_foregroundColor;
    }

    @NotNull
    public Color getBackgroundColor() {
        return m_backgroundColor;
    }

    @Override
    public int hashCode() {
        int result = m_character;
        result = ( 31 * result ) + m_foregroundColor.hashCode();
        result = ( 31 * result ) + m_backgroundColor.hashCode();
        return result;
    }

    @Override
    public boolean equals( @Nullable final Object obj ) {
        if ( this == obj ) {
            return true;
        }

        if ( ( obj == null ) || ( getClass() != obj.getClass() ) ) {
            return false;
        }

        final CharacterData that = ( CharacterData ) obj;

        return ( m_character == that.m_character ) && m_backgroundColor.equals( that.m_backgroundColor ) &&
               m_foregroundColor.equals( that.m_foregroundColor );
    }

    @Override
    public String toString() {
        return "AsciiCharacterData{" +
               "m_character=" + m_character +
               ", m_foregroundColor=" + m_foregroundColor +
               ", m_backgroundColor=" + m_backgroundColor +
               '}';
    }
}
