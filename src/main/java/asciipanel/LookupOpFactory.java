package asciipanel;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.util.Arrays;

public final class LookupOpFactory {
    private LookupOpFactory() {
    }

    /**
     * Create a {@code LookupOp} object (lookup table) mapping the original pixels to the background and foreground
     * colors, respectively.
     *
     * @param foregroundColor the foreground color
     * @param backgroundColor the background color
     * @return the {@code LookupOp} object (lookup table)
     */
    @NotNull
    public static BufferedImageOp getBufferedImageOp( @NotNull final Color foregroundColor,
                                                      @NotNull final Color backgroundColor )
    {
        final short[] alpha = getBand( 255, 255 );
        final short[] red = getBand( foregroundColor.getRed(), backgroundColor.getRed() );
        final short[] blue = getBand( foregroundColor.getBlue(), backgroundColor.getBlue() );
        final short[] green = getBand( foregroundColor.getGreen(), backgroundColor.getGreen() );

        final short[][] table = { red, green, blue, alpha };

        return new LookupOp( new ShortLookupTable( 0, table ), null );
    }

    @NotNull
    private static short[] getBand( final int foreground, final int background ) {
        final short[] shorts = new short[ 256 ];

        Arrays.fill( shorts, ( byte ) foreground );
        shorts[ 0 ] = ( byte ) background;

        return shorts;
    }
}
