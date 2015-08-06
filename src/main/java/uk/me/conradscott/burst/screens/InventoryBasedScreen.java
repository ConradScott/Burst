package uk.me.conradscott.burst.screens;

import asciipanel.AsciiPanel;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.me.conradscott.burst.CreatureIfc;
import uk.me.conradscott.burst.ItemIfc;

import java.awt.event.KeyEvent;
import java.util.List;

public abstract class InventoryBasedScreen implements ScreenIfc {
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";

    @NotNull private final CreatureIfc m_player;

    protected InventoryBasedScreen( @NotNull final CreatureIfc player ) {
        m_player = player;
    }

    @Override
    public void displayOutput( @NotNull final AsciiPanel terminal ) {
        final List<String> lines = getList();

        int y = 23 - lines.size();
        final int x = 4;

        if ( !lines.isEmpty() ) {
            terminal.clear( x, y, 20, lines.size(), ' ' );
        }

        for ( final String line : lines ) {
            terminal.write( x, y, line );
            y += 1;
        }

        terminal.clear( 0, 23, 80, 1, ' ' );
        terminal.write( 2, 23, "What would you like to " + getVerb() + '?' );

        terminal.repaint();
    }

    @Override
    @Nullable
    public ScreenIfc respondToUserInput( @NotNull final KeyEvent key ) {
        final char c = key.getKeyChar();

        final ItemIfc[] items = m_player.inventory().getItems();

        if ( ( LETTERS.indexOf( c ) > -1 ) && ( items.length > LETTERS.indexOf( c ) ) &&
             ( items[ LETTERS.indexOf( c ) ] != null ) && isAcceptable( items[ LETTERS.indexOf( c ) ] ) )
        {
            return use( items[ LETTERS.indexOf( c ) ] );
        }

        if ( key.getKeyCode() == KeyEvent.VK_ESCAPE ) {
            return null;
        }

        return this;
    }

    @NotNull
    protected CreatureIfc player() {
        return m_player;
    }

    @NotNull
    protected abstract String getVerb();

    protected abstract boolean isAcceptable( @NotNull ItemIfc item );

    @Nullable
    protected abstract ScreenIfc use( @NotNull ItemIfc item );

    @NotNull
    private List<String> getList() {
        final ItemIfc[] inventory = m_player.inventory().getItems();

        final List<String> lines = Lists.newArrayListWithCapacity( inventory.length );

        for ( int i = 0; i != inventory.length; ++i ) {
            final ItemIfc item = inventory[ i ];

            if ( ( item == null ) || !isAcceptable( item ) ) {
                continue;
            }

            final String line = LETTERS.charAt( i ) + " - " + item.glyph() + ' ' + item.name();

            lines.add( line );
        }

        return lines;
    }
}
