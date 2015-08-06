package uk.me.conradscott.burst.screens;

import asciipanel.AsciiPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.KeyEvent;

public interface ScreenIfc {
    void displayOutput( @NotNull AsciiPanel terminal );

    @Nullable
    ScreenIfc respondToUserInput( @NotNull KeyEvent key );
}
