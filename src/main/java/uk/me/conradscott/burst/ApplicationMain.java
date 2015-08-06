package uk.me.conradscott.burst;

import asciipanel.AsciiPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.me.conradscott.burst.screens.ScreenIfc;
import uk.me.conradscott.burst.screens.StartScreen;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public final class ApplicationMain extends JFrame implements KeyListener {
    private static final long serialVersionUID = 1060623638149583738L;

    @NotNull private final AsciiPanel m_asciiPanel;

    @NotNull private ScreenIfc m_screen;

    private ApplicationMain() throws IOException {
        m_asciiPanel = new AsciiPanel();
        add( m_asciiPanel );
        pack();
        m_screen = new StartScreen();
        addKeyListener( this );
        repaint();
    }

    @Override
    public void repaint() {
        m_asciiPanel.clear();
        m_screen.displayOutput( m_asciiPanel );
        super.repaint();
    }

    @Override
    public void keyPressed( @NotNull final KeyEvent e ) {
        @Nullable final ScreenIfc screen = m_screen.respondToUserInput( e );
        assert screen != null;
        m_screen = screen;
        repaint();
    }

    @Override
    public void keyReleased( @NotNull final KeyEvent e ) {
    }

    @Override
    public void keyTyped( @NotNull final KeyEvent e ) {
    }

    public static void main( final String... args ) throws IOException {
        final ApplicationMain application = new ApplicationMain();
        application.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        application.setVisible( true );
    }

    @Override
    public String toString() {
        return "ApplicationMain{" +
               "m_asciiPanel=" + m_asciiPanel +
               ", m_screen=" + m_screen +
               '}';
    }
}
