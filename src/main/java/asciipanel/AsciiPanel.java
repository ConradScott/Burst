package asciipanel;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.swing.JPanel;

/**
 * This simulates a code page 437 ASCII terminal display.
 *
 * @author Trystan Spangler
 */
public final class AsciiPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger( AsciiPanel.class );
    private static final long serialVersionUID = 6986836973487101884L;

    public static final int DEFAULT_WIDTH = 80;
    public static final int DEFAULT_HEIGHT = 24;

    public static final Color DEFAULT_FOREGROUND_COLOR = Colors.WHITE;
    public static final Color DEFAULT_BACKGROUND_COLOR = Colors.BLACK;

    private static final String SHEET_NAME = "/cp437.png";
    private static final char CLEAR_CHARACTER = ' ';

    private final int m_widthInCharacters;
    private final int m_heightInCharacters;

    @NotNull private final Glyphs m_glyphs;

    private final Color m_defaultBackgroundColor;
    private final Color m_defaultForegroundColor;

    @NotNull private final CharacterMatrix m_characterMatrix;

    @NotNull private final CharacterMatrix m_oldCharacterMatrix;

    private Image m_offscreenBuffer = null;
    private Graphics m_offscreenGraphics = null;

    private int m_cursorX = 0;
    private int m_cursorY = 0;

    /**
     * Class constructor. Default size is 80x24.
     */
    public AsciiPanel() throws IOException {
        this( DEFAULT_WIDTH, DEFAULT_HEIGHT );
    }

    /**
     * Class constructor specifying the width and height in characters.
     *
     * @param width
     * @param height
     */
    public AsciiPanel( final int width, final int height ) throws IOException {
        checkWidthIsValid( width );
        checkHeightIsValid( height );

        m_widthInCharacters = width;
        m_heightInCharacters = height;

        m_glyphs = new Glyphs( SHEET_NAME );

        setPreferredSize( Glyphs.dimensionFromChars( m_widthInCharacters, m_heightInCharacters ) );

        m_defaultForegroundColor = DEFAULT_FOREGROUND_COLOR;
        m_defaultBackgroundColor = DEFAULT_BACKGROUND_COLOR;

        m_characterMatrix = new CharacterMatrix( m_widthInCharacters, m_heightInCharacters,
                                                 new CharacterData( CLEAR_CHARACTER, m_defaultForegroundColor,
                                                                    m_defaultBackgroundColor ) );

        m_oldCharacterMatrix = new CharacterMatrix( m_widthInCharacters, m_heightInCharacters,
                                                    new CharacterData( 'x', m_defaultForegroundColor,
                                                                       m_defaultBackgroundColor ) );
    }

    /**
     * Clear the entire screen to whatever the default background color is.
     *
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel clear() {
        return clear( 0, 0, m_widthInCharacters, m_heightInCharacters, CLEAR_CHARACTER, m_defaultForegroundColor,
                      m_defaultBackgroundColor );
    }

    /**
     * Clear the section of the screen with the specified character and whatever the specified foreground and background
     * colors are.
     *
     * @param left       the distance from the left to begin writing from
     * @param top        the distance from the top to begin writing from
     * @param width      the height of the section to clear
     * @param height     the width of the section to clear
     * @param character  the character to write
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel clear( final int left, final int top, final int width, final int height, final char character,
                         @Nullable final Color foreground, @Nullable final Color background )
    {
        checkLocationIsValid( left, top );
        checkWidthIsValid( width );
        checkHeightIsValid( height );
        checkWidthIsValid( left, width );
        checkHeightIsValid( top, height );
        m_glyphs.checkCharacterIsValid( character );

        return clear( left, top, width, height, new CharacterData( character, getDefaultedForeground( foreground ),
                                                                   getDefaultedBackground( background ) ) );
    }

    /**
     * Clear the section of the screen with the specified character and whatever the specified foreground and background
     * colors are.
     *
     * @param left   the distance from the left to begin writing from
     * @param top    the distance from the top to begin writing from
     * @param width  the height of the section to clear
     * @param height the width of the section to clear
     * @param data   the character data to write
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel clear( final int left, final int top, final int width, final int height,
                         @NotNull final CharacterData data )
    {
        checkLocationIsValid( left, top );
        checkWidthIsValid( width );
        checkHeightIsValid( height );
        checkWidthIsValid( left, width );
        checkHeightIsValid( top, height );
        m_glyphs.checkCharacterIsValid( data.getCharacter() );

        for ( int x = left; x != ( left + width ); ++x ) {
            for ( int y = top; y != ( top + height ); ++y ) {
                m_characterMatrix.write( x, y, data );
            }
        }

        m_cursorX = left + width + 1;
        m_cursorY = top + height;

        return this;
    }

    /**
     * Write a character to the specified position with the specified foreground and background colors. This updates the
     * cursor's position but not the default foreground or background colors.
     *
     * @param x          the distance from the left to begin writing from
     * @param y          the distance from the top to begin writing from
     * @param character  the character to write
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel write( final int x, final int y, final char character, @Nullable final Color foreground,
                         @Nullable final Color background )
    {
        checkLocationIsValid( x, y );
        m_glyphs.checkCharacterIsValid( character );

        return write( x, y, new CharacterData( character, getDefaultedForeground( foreground ),
                                               getDefaultedBackground( background ) ) );
    }

    /**
     * Write a character to the specified position with the specified foreground and background colors. This updates the
     * cursor's position but not the default foreground or background colors.
     *
     * @param x             the distance from the left to begin writing from
     * @param y             the distance from the top to begin writing from
     * @param characterData the character data to write
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel write( final int x, final int y, @NotNull final CharacterData characterData ) {
        checkLocationIsValid( x, y );
        m_glyphs.checkCharacterIsValid( characterData.getCharacter() );

        m_characterMatrix.write( x, y, characterData );

        m_cursorX = x + 1;
        m_cursorY = y;

        return this;
    }

    /**
     * Sets the distance from the left new text will be written to. This should be equal to or greater than 0 and less
     * than the the width in characters.
     *
     * @param x the distance from the left new text should be written to
     */
    public void setCursorX( final int x ) {
        checkLocationIsValid( x, 0 );

        m_cursorX = x;
    }

    /**
     * Sets the distance from the top new text will be written to. This should be equal to or greater than 0 and less
     * than the the height in characters.
     *
     * @param y the distance from the top new text should be written to
     */
    public void setCursorY( final int y ) {
        checkLocationIsValid( 0, y );

        m_cursorY = y;
    }

    /**
     * Sets the x and y position of where new text will be written to. The origin (0,0) is the upper left corner. The x
     * should be equal to or greater than 0 and less than the the width in characters. The y should be equal to or
     * greater than 0 and less than the the height in characters.
     *
     * @param x the distance from the left new text should be written to
     * @param y the distance from the top new text should be written to
     */
    public void setCursorPosition( final int x, final int y ) {
        checkLocationIsValid( x, y );

        m_cursorX = x;
        m_cursorY = y;
    }

    @Override
    protected void paintComponent( final Graphics g ) {
        super.paintComponent( g );

        if ( m_offscreenBuffer == null ) {
            m_offscreenBuffer = createImage( getWidth(), getHeight() );
            m_offscreenGraphics = m_offscreenBuffer.getGraphics();
        }

        for ( int x = 0; x != m_widthInCharacters; ++x ) {
            for ( int y = 0; y != m_heightInCharacters; ++y ) {
                final CharacterData characterData = m_characterMatrix.read( x, y );
                final CharacterData oldCharacterData = m_oldCharacterMatrix.read( x, y );

                if ( !characterData.equals( oldCharacterData ) ) {
                    m_glyphs.drawAt( m_offscreenGraphics, x, y, characterData );
                }
            }
        }

        m_oldCharacterMatrix.copyFrom( m_characterMatrix );

        g.drawImage( m_offscreenBuffer, 0, 0, this );
    }

    /**
     * Clear the entire screen with the specified character and whatever the default foreground and background colors
     * are.
     *
     * @param character the character to write
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel clear( final char character ) {
        return clear( 0, 0, m_widthInCharacters, m_heightInCharacters, character, m_defaultForegroundColor,
                      m_defaultBackgroundColor );
    }

    /**
     * Clear the entire screen with the specified character and whatever the specified foreground and background colors
     * are.
     *
     * @param character  the character to write
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel clear( final char character, @Nullable final Color foreground, @Nullable final Color background ) {
        return clear( 0, 0, m_widthInCharacters, m_heightInCharacters, character, foreground, background );
    }

    /**
     * Clear the section of the screen with the specified character and whatever the default foreground and background
     * colors are.
     *
     * @param x         the distance from the left to begin writing from
     * @param y         the distance from the top to begin writing from
     * @param width     the height of the section to clear
     * @param height    the width of the section to clear
     * @param character the character to write
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel clear( final int x, final int y, final int width, final int height, final char character ) {
        return clear( x, y, width, height, character, m_defaultForegroundColor, m_defaultBackgroundColor );
    }

    /**
     * Write a character to the cursor's position. This updates the cursor's position.
     *
     * @param character the character to write
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel write( final char character ) {
        return write( m_cursorX, m_cursorY, character, m_defaultForegroundColor, m_defaultBackgroundColor );
    }

    /**
     * Write a character to the cursor's position with the specified foreground color. This updates the cursor's
     * position but not the default foreground color.
     *
     * @param character  the character to write
     * @param foreground the foreground color or null to use the default
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel write( final char character, @Nullable final Color foreground ) {
        return write( m_cursorX, m_cursorY, character, foreground, m_defaultBackgroundColor );
    }

    /**
     * Write a character to the cursor's position with the specified foreground and background colors. This updates the
     * cursor's position but not the default foreground or background colors.
     *
     * @param character  the character to write
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel write( final char character, @Nullable final Color foreground, @Nullable final Color background ) {
        return write( m_cursorX, m_cursorY, character, foreground, background );
    }

    /**
     * Write a character to the specified position. This updates the cursor's position.
     *
     * @param x         the distance from the left to begin writing from
     * @param y         the distance from the top to begin writing from
     * @param character the character to write
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel write( final int x, final int y, final char character ) {
        return write( x, y, character, m_defaultForegroundColor, m_defaultBackgroundColor );
    }

    /**
     * Write a character to the specified position with the specified foreground color. This updates the cursor's
     * position but not the default foreground color.
     *
     * @param x          the distance from the left to begin writing from
     * @param y          the distance from the top to begin writing from
     * @param character  the character to write
     * @param foreground the foreground color or null to use the default
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel write( final int x, final int y, final char character, @Nullable final Color foreground ) {
        return write( x, y, character, foreground, m_defaultBackgroundColor );
    }

    /**
     * Write a string to the cursor's position. This updates the cursor's position.
     *
     * @param charSequence the string to write
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel write( @NotNull final CharSequence charSequence ) {
        checkStringWidthIsValid( m_cursorX, charSequence );

        return write( m_cursorX, m_cursorY, charSequence, m_defaultForegroundColor, m_defaultBackgroundColor );
    }

    /**
     * Write a string to the specified position with the specified foreground and background colors. This updates the
     * cursor's position but not the default foreground or background colors.
     *
     * @param x            the distance from the left to begin writing from
     * @param y            the distance from the top to begin writing from
     * @param charSequence the string to write
     * @param foreground   the foreground color or null to use the default
     * @param background   the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel write( final int x, final int y, @NotNull final CharSequence charSequence,
                         @Nullable final Color foreground, @Nullable final Color background )
    {
        checkLocationIsValid( x, y );
        checkStringWidthIsValid( x, charSequence );

        for ( int i = 0; i != charSequence.length(); ++i ) {
            write( x + i, y, charSequence.charAt( i ), foreground, background );
        }

        return this;
    }

    /**
     * Write a string to the cursor's position with the specified foreground color. This updates the cursor's position
     * but not the default foreground color.
     *
     * @param charSequence the string to write
     * @param foreground   the foreground color or null to use the default
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel write( @NotNull final CharSequence charSequence, @Nullable final Color foreground ) {
        return write( m_cursorX, m_cursorY, charSequence, foreground, m_defaultBackgroundColor );
    }

    /**
     * Write a string to the cursor's position with the specified foreground and background colors. This updates the
     * cursor's position but not the default foreground or background colors.
     *
     * @param charSequence the string to write
     * @param foreground   the foreground color or null to use the default
     * @param background   the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel write( @NotNull final CharSequence charSequence, @Nullable final Color foreground,
                         @Nullable final Color background )
    {
        return write( m_cursorX, m_cursorY, charSequence, foreground, background );
    }

    /**
     * Write a string to the specified position. This updates the cursor's position.
     *
     * @param x            the distance from the left to begin writing from
     * @param y            the distance from the top to begin writing from
     * @param charSequence the string to write
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel write( final int x, final int y, @NotNull final CharSequence charSequence ) {
        return write( x, y, charSequence, m_defaultForegroundColor, m_defaultBackgroundColor );
    }

    /**
     * Write a string to the specified position with the specified foreground color. This updates the cursor's position
     * but not the default foreground color.
     *
     * @param x            the distance from the left to begin writing from
     * @param y            the distance from the top to begin writing from
     * @param charSequence the string to write
     * @param foreground   the foreground color or null to use the default
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel write( final int x, final int y, @NotNull final CharSequence charSequence,
                         @Nullable final Color foreground )
    {
        return write( x, y, charSequence, foreground, m_defaultBackgroundColor );
    }

    /**
     * Write a string to the center of the panel at the specified y position. This updates the cursor's position.
     *
     * @param y            the distance from the top to begin writing from
     * @param charSequence the string to write
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel writeCenter( final int y, @NotNull final CharSequence charSequence ) {
        final int x = ( m_widthInCharacters - charSequence.length() ) / 2;

        return write( x, y, charSequence, m_defaultForegroundColor, m_defaultBackgroundColor );
    }

    /**
     * Write a string to the center of the panel at the specified y position with the specified foreground color. This
     * updates the cursor's position but not the default foreground color.
     *
     * @param y            the distance from the top to begin writing from
     * @param charSequence the string to write
     * @param foreground   the foreground color or null to use the default
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel writeCenter( final int y, @NotNull final CharSequence charSequence, @Nullable final Color foreground )
    {
        final int x = ( m_widthInCharacters - charSequence.length() ) / 2;

        return write( x, y, charSequence, foreground, m_defaultBackgroundColor );
    }

    /**
     * Write a string to the center of the panel at the specified y position with the specified foreground and
     * background colors. This updates the cursor's position but not the default foreground or background colors.
     *
     * @param y            the distance from the top to begin writing from
     * @param charSequence the string to write
     * @param foreground   the foreground color or null to use the default
     * @param background   the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    @NotNull
    public JPanel writeCenter( final int y, @NotNull final CharSequence charSequence, @Nullable final Color foreground,
                               @Nullable final Color background )
    {
        final int x = ( m_widthInCharacters - charSequence.length() ) / 2;

        return write( x, y, charSequence, foreground, background );
    }

    public void withEachTile( @NotNull final TileTransformerIfc transformer ) {
        withEachTile( 0, 0, m_widthInCharacters, m_heightInCharacters, transformer );
    }

    public void withEachTile( final int left, final int top, final int width, final int height,
                              @NotNull final TileTransformerIfc transformer )
    {
        checkLocationIsValid( left, top );
        checkWidthIsValid( width );
        checkHeightIsValid( height );
        checkWidthIsValid( left, width );
        checkHeightIsValid( top, height );

        for ( int x = left; x != ( left + width ); ++x ) {
            for ( int y = top; y != ( top + height ); ++y ) {
                m_characterMatrix.write( x, y, transformer.transformTile( x, y, m_characterMatrix.read( x, y ) ) );
            }
        }
    }

    @Override
    public String toString() {
        return "AsciiPanel{" +
               "m_widthInCharacters=" + m_widthInCharacters +
               ", m_heightInCharacters=" + m_heightInCharacters +
               '}';
    }

    @NotNull
    private Color getDefaultedForeground( @Nullable final Color foreground ) {
        return ( foreground == null ) ? m_defaultForegroundColor : foreground;
    }

    @NotNull
    private Color getDefaultedBackground( @Nullable final Color background ) {
        return ( background == null ) ? m_defaultBackgroundColor : background;
    }

    private void checkLocationIsValid( final int x, final int y ) {
        if ( ( x < 0 ) || ( x >= m_widthInCharacters ) ) {
            error( "The x coordinate " + x + " must be in the range [0," + m_widthInCharacters + ')' );
        }

        if ( ( y < 0 ) || ( y >= m_heightInCharacters ) ) {
            error( "The y coordinate " + y + " must be in the range [0," + m_heightInCharacters + ')' );
        }
    }

    private void checkWidthIsValid( final int width ) {
        if ( width <= 0 ) {
            error( "The width " + width + " must be greater than zero." );
        }
    }

    private void checkWidthIsValid( final int x, final int width ) {
        if ( ( x + width ) > m_widthInCharacters ) {
            error( "The x coordinate " + x + " + the width " + width + " must be less than the total width " +
                   m_widthInCharacters + '.' );
        }
    }

    private void checkHeightIsValid( final int height ) {
        if ( height <= 0 ) {
            error( "The height " + height + " must be greater than zero." );
        }
    }

    private void checkHeightIsValid( final int y, final int height ) {
        if ( ( y + height ) > m_heightInCharacters ) {
            error( "The y coordinate " + y + " + the height " + height + " must be less than the total height " +
                   m_heightInCharacters + '.' );
        }
    }

    private void checkStringWidthIsValid( final int x, @NotNull final CharSequence charSequence ) {
        if ( ( x + charSequence.length() ) >= m_widthInCharacters ) {
            throw new IllegalArgumentException(
                    "x + string.length() " + ( x + charSequence.length() ) + " must be less than " +
                    m_widthInCharacters +
                    '.' );
        }
    }

    private static void error( final String msg ) {
        LOGGER.error( msg );
        throw new IllegalArgumentException( msg );
    }
}
