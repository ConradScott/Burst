package uk.me.conradscott.burst.screens;

import asciipanel.AsciiPanel;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;

public final class StartScreen implements ScreenIfc {
    @Override
    public void displayOutput( @NotNull final AsciiPanel terminal ) {
        terminal.write( 1, 1, "rl tutorial" );
        terminal.writeCenter( 22, "-- press [enter] to start --" );
    }

    @Override
    @NotNull
    public ScreenIfc respondToUserInput( @NotNull final KeyEvent key ) {
        return ( key.getKeyCode() == KeyEvent.VK_ENTER ) ? new PlayScreen() : this;
    }
}
