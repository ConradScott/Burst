package asciipanel;

import java.awt.Color;

public final class Colors {
    private static final int LOW = 128;
    private static final int MID = 192;
    private static final int HIGH = 255;

    public static final Color BLACK = new Color( 0, 0, 0 );
    public static final Color BLUE = new Color( 0, 0, LOW );
    public static final Color GREEN = new Color( 0, LOW, 0 );
    public static final Color CYAN = new Color( 0, LOW, LOW );
    public static final Color RED = new Color( LOW, 0, 0 );
    public static final Color MAGENTA = new Color( LOW, 0, LOW );
    public static final Color YELLOW = new Color( LOW, LOW, 0 );
    public static final Color WHITE = new Color( MID, MID, MID );

    public static final Color BRIGHT_BLACK = new Color( LOW, LOW, LOW );
    public static final Color BRIGHT_BLUE = new Color( 0, 0, HIGH );
    public static final Color BRIGHT_GREEN = new Color( 0, HIGH, 0 );
    public static final Color BRIGHT_CYAN = new Color( 0, HIGH, HIGH );
    public static final Color BRIGHT_RED = new Color( HIGH, 0, 0 );
    public static final Color BRIGHT_MAGENTA = new Color( HIGH, 0, HIGH );
    public static final Color BRIGHT_YELLOW = new Color( HIGH, HIGH, 0 );
    public static final Color BRIGHT_WHITE = new Color( HIGH, HIGH, HIGH );

    private Colors() {
    }
}
