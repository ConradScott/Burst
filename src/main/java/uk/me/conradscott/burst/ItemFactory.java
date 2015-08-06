package uk.me.conradscott.burst;

import asciipanel.Colors;
import org.jetbrains.annotations.NotNull;

public final class ItemFactory {
    @NotNull private final WorldIfc m_world;

    private ItemFactory( @NotNull final WorldIfc world ) {
        m_world = world;
    }

    @NotNull
    public static ItemFactory instance( @NotNull final WorldIfc world ) {
        return new ItemFactory( world );
    }

    public void createItems() {
        for ( int z = 0; z < m_world.depth(); z++ ) {
            for ( int i = 0; i < ( ( m_world.width() * m_world.height() ) / 20 ); i++ ) {
                m_world.addItemAt( newRock(), m_world.getEmptyLocationAtDepth( z ) );
            }
        }

        m_world.addItemAt( newVictoryItem(), m_world.getEmptyLocationAtDepth( m_world.depth() - 1 ) );
    }

    public static ItemIfc newRock() {
        return new Item( ',', Colors.YELLOW, "rock" );
    }

    public static ItemIfc newVictoryItem() {
        return new Item( '*', Colors.BRIGHT_WHITE, "teddy bear" );
    }

    @Override
    public String toString() {
        return "CreatureFactory{" +
               "m_world=" + m_world +
               '}';
    }
}
