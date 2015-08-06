package asciipanel;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class CharacterMatrix {
    private final int m_width;
    private final int m_height;

    @NotNull private final CharacterData[][] m_characterDatas;

    public CharacterMatrix( final int width, final int height, @NotNull final CharacterData characterData ) {
        assert width > 0;
        assert height > 0;

        m_width = width;
        m_height = height;

        m_characterDatas = new CharacterData[ m_width ][ m_height ];

        for ( int i = 0; i != m_width; ++i ) {
            Arrays.fill( m_characterDatas[ i ], characterData );
        }
    }

    @NotNull
    public CharacterData read( final int x, final int y ) {
        assert ( x > 0 ) && ( x <= m_width );
        assert ( y > 0 ) && ( y <= m_height );

        return m_characterDatas[ x ][ y ];
    }

    @NotNull
    public CharacterData write( final int x, final int y, @NotNull final CharacterData characterData ) {
        assert ( x > 0 ) && ( x <= m_width );
        assert ( y > 0 ) && ( y <= m_height );

        final CharacterData old = m_characterDatas[ x ][ y ];

        m_characterDatas[ x ][ y ] = characterData;

        return old;
    }

    public void copyFrom( @NotNull final CharacterMatrix src ) {
        assert src.m_width == m_width;
        assert src.m_height == m_height;

        for ( int i = 0; i != m_width; ++i ) {
            System.arraycopy( src.m_characterDatas[ i ], 0, m_characterDatas[ i ], 0, m_height );
        }
    }

    @Override
    public String toString() {
        return "CharacterMatrix{" +
               "m_width=" + m_width +
               ", m_height=" + m_height +
               '}';
    }
}
