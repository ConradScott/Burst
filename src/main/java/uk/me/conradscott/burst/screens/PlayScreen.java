package uk.me.conradscott.burst.screens;

import asciipanel.AsciiPanel;
import org.jetbrains.annotations.NotNull;
import uk.me.conradscott.burst.CreatureFactory;
import uk.me.conradscott.burst.CreatureIfc;
import uk.me.conradscott.burst.ItemFactory;
import uk.me.conradscott.burst.ItemIfc;
import uk.me.conradscott.burst.Tile;
import uk.me.conradscott.burst.WorldIfc;
import uk.me.conradscott.burst.worlds.WorldBuilder;
import uk.me.conradscott.maths.Point3D;
import uk.me.conradscott.maths.Point3DIfc;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public final class PlayScreen implements ScreenIfc {
    private static final int BORDER_WIDTH = 5;
    public static final int WORLD_DEPTH = 5;

    @NotNull private final WorldIfc m_world = createWorld();

    private static final int WIDTH = 80;
    private static final int HEIGHT = 21;

    @NotNull private final CreatureIfc m_player;

    @NotNull private final List<String> m_messages = new ArrayList<>();

    private ScreenIfc m_subscreen = null;

    public PlayScreen() {
        final CreatureFactory creatureFactory = CreatureFactory.instance( m_world );
        final ItemFactory itemFactory = ItemFactory.instance( m_world );

        m_player = creatureFactory.newPlayer( m_messages );
        m_world.addCreatureAt( m_player, m_world.getEmptyLocation() );

        creatureFactory.createCreatures();
        itemFactory.createItems();
    }

    @NotNull
    private static WorldIfc createWorld() {
        return new WorldBuilder( WIDTH + ( 2 * BORDER_WIDTH ), HEIGHT + ( 2 * BORDER_WIDTH ), WORLD_DEPTH ).makeCaves()
                                                                                                           .build();
    }

    @Override
    public void displayOutput( @NotNull final AsciiPanel terminal ) {
        terminal.write( 1, 1, "You are having fun." );
        terminal.writeCenter( 22, "-- press [escape] to lose or [enter] to win --" );

        final Point3DIfc screenOffset = getScreenOffset();

        displayTiles( terminal, screenOffset );

        final String stats = String.format( " %3d/%3d hp", m_player.hp(), m_player.maxHp() );
        terminal.write( 1, 23, stats );

        displayMessages( terminal );

        if ( m_subscreen != null ) {
            m_subscreen.displayOutput( terminal );
        }
    }

    private void displayMessages( @NotNull final AsciiPanel terminal ) {
        final int top = HEIGHT - m_messages.size();

        for ( int index = 0; index != m_messages.size(); ++index ) {
            final String message = m_messages.get( index );
            terminal.writeCenter( top + index, message );
        }

        m_messages.clear();
    }

    @Override
    @NotNull
    public ScreenIfc respondToUserInput( @NotNull final KeyEvent key ) {
        if ( m_subscreen != null ) {
            m_subscreen = m_subscreen.respondToUserInput( key );
        } else {
            switch ( key.getKeyCode() ) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_H:
                m_player.moveBy( -1, 0, 0 );
                break;

            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_L:
                m_player.moveBy( 1, 0, 0 );
                break;

            case KeyEvent.VK_UP:
            case KeyEvent.VK_K:
                m_player.moveBy( 0, -1, 0 );
                break;

            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_J:
                m_player.moveBy( 0, 1, 0 );
                break;

            case KeyEvent.VK_Y:
                m_player.moveBy( -1, -1, 0 );
                break;

            case KeyEvent.VK_U:
                m_player.moveBy( 1, -1, 0 );
                break;

            case KeyEvent.VK_B:
                m_player.moveBy( -1, 1, 0 );
                break;

            case KeyEvent.VK_N:
                m_player.moveBy( 1, 1, 0 );
                break;

            case KeyEvent.VK_ESCAPE:
                return new LoseScreen();

            case KeyEvent.VK_ENTER:
                return new WinScreen();

            default:
                switch ( key.getKeyChar() ) {
                case '<':
                    if ( userIsTryingToExit() ) {
                        return userExits();
                    }
                    m_player.moveBy( 0, 0, -1 );
                    break;

                case '>':
                    m_player.moveBy( 0, 0, 1 );
                    break;

                case 'g':
                case ',':
                    m_player.pickup();
                    break;

                case 'd':
                    m_subscreen = new DropScreen( m_player );
                    break;

                default:
                    break;
                }
                break;
            }
        }

        if ( m_subscreen == null ) {
            m_world.update();
        }

        if ( m_player.hp() < 1 ) {
            return new LoseScreen();
        }

        return this;
    }

    private Point3DIfc getScreenOffset() {
        final Point3DIfc location = m_world.locationOf( m_player );

        final int x = Math.max( 0, Math.min( location.x() - ( WIDTH / 2 ), m_world.width() - WIDTH ) );
        final int y = Math.max( 0, Math.min( location.y() - ( HEIGHT / 2 ), m_world.height() - HEIGHT ) );

        return new Point3D( x, y, location.z() );
    }

    private void displayTiles( @NotNull final AsciiPanel terminal, final Point3DIfc offset ) {
        for ( int screenX = 0; screenX != WIDTH; ++screenX ) {
            for ( int screenY = 0; screenY != HEIGHT; ++screenY ) {
                final int worldX = screenX + offset.x();
                final int worldY = screenY + offset.y();

                final Point3DIfc location = new Point3D( worldX, worldY, offset.z() );

                if ( m_player.canSee( location ) ) {
                    terminal.write( screenX, screenY, m_world.glyph( location ), m_world.color( location ) );
                } else {
                    terminal.write( screenX, screenY, m_world.glyph( location ), Color.darkGray );
                }
            }
        }
    }

    private boolean userIsTryingToExit() {
        final Point3DIfc location = m_world.locationOf( m_player );

        return ( location.z() == 0 ) && ( m_world.tile( location ) == Tile.STAIRS_UP );
    }

    private ScreenIfc userExits() {
        for ( final ItemIfc item : m_player.inventory().getItems() ) {
            if ( ( item != null ) && "teddy bear".equals( item.name() ) ) {
                return new WinScreen();
            }
        }

        return new LoseScreen();
    }

    @Override
    public String toString() {
        return "PlayScreen{" +
               "m_world=" + m_world +
               ", m_player=" + m_player +
               ", m_messages=" + m_messages +
               ", m_subscreen=" + m_subscreen +
               '}';
    }
}
