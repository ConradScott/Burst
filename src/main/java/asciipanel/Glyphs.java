package asciipanel;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public final class Glyphs {
    private static final Logger LOGGER = Logger.getLogger( Glyphs.class );

    private static final int CHAR_WIDTH = 9;
    private static final int CHAR_HEIGHT = 16;

    private static final int GLYPH_COUNT = 256;
    private static final int ROW_LENGTH = 32;
    public static final int BORDER = 8;

    @NotNull private final String m_sheetName;
    private final BufferedImage[] m_glyphs = new BufferedImage[ GLYPH_COUNT ];

    public Glyphs( @NotNull final String sheetName ) throws IOException {
        m_sheetName = sheetName;
        final BufferedImage sheet = getSheet( sheetName );

        for ( int index = 0; index != GLYPH_COUNT; ++index ) {
            final int sx = ( ( index % ROW_LENGTH ) * CHAR_WIDTH ) + BORDER;
            final int sy = ( ( index / ROW_LENGTH ) * CHAR_HEIGHT ) + BORDER;

            m_glyphs[ index ] = new BufferedImage( CHAR_WIDTH, CHAR_HEIGHT, BufferedImage.TYPE_INT_ARGB );
            m_glyphs[ index ].getGraphics().drawImage( sheet, 0, 0, CHAR_WIDTH, CHAR_HEIGHT, sx, sy, sx + CHAR_WIDTH,
                                                       sy + CHAR_HEIGHT, null );
        }
    }

    public void checkCharacterIsValid( final char character ) {
        if ( character >= m_glyphs.length ) {
            throw new IllegalArgumentException(
                    "The character " + Character.getNumericValue( character ) + " must be in the range [0," +
                    m_glyphs.length + ")." );
        }
    }

    public void drawAt( @NotNull final Graphics graphics, final int x, final int y,
                        @NotNull final CharacterData characterData )
    {
        final BufferedImageOp op = LookupOpFactory
                .getBufferedImageOp( characterData.getForegroundColor(), characterData.getBackgroundColor() );

        final BufferedImage filteredImage = op.filter( m_glyphs[ characterData.getCharacter() ], null );
        graphics.drawImage( filteredImage, x * CHAR_WIDTH, y * CHAR_HEIGHT, null );
    }

    @NotNull
    public static Dimension dimensionFromChars( final int widthInChars, final int heightInChars ) {
        return new Dimension( CHAR_WIDTH * widthInChars, CHAR_HEIGHT * heightInChars );
    }

    private static BufferedImage getSheet( @NotNull final String sheetName ) throws IOException {
        final BufferedImage sheet;

        try ( final InputStream stream = getSpriteSheetAsStream( sheetName ) ) {
            sheet = ImageIO.read( stream );
        } catch ( @NotNull final IOException e ) {
            LOGGER.error( "Failed to load the glyph sheet \"" + sheetName + "\": " + e.getLocalizedMessage() );
            throw e;
        }

        return sheet;
    }

    private static InputStream getSpriteSheetAsStream( @NotNull final String sheetName ) {
        return Glyphs.class.getResourceAsStream( sheetName );
    }

    @Override
    public String toString() {
        return "Glyphs{" +
               "m_sheetName='" + m_sheetName + '\'' +
               '}';
    }
}
