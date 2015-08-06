package uk.me.conradscott.burst;

import asciipanel.Colors;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class CreatureFactory {
    @NotNull private final WorldIfc m_world;

    private CreatureFactory( @NotNull final WorldIfc world ) {
        m_world = world;
    }

    @NotNull
    public static CreatureFactory instance( @NotNull final WorldIfc world ) {
        return new CreatureFactory( world );
    }

    public void createCreatures() {
        for ( int i = 0; i != 8; ++i ) {
            m_world.addCreatureAt( newFungus(), m_world.getEmptyLocation() );
        }

        for ( int i = 0; i != 20; ++i ) {
            m_world.addCreatureAt( newBat(), m_world.getEmptyLocation() );
        }
    }

    @NotNull
    public CreatureIfc newPlayer( @NotNull final List<String> messages ) {
        return new Creature( m_world, '@', Colors.BRIGHT_WHITE, "player", PlayerBehaviour.instance( messages ), 100, 20,
                             5 );
    }

    @NotNull
    public CreatureIfc newBat() {
        return new Creature( m_world, 'b', Colors.YELLOW, "bat", BatBehaviour.instance(), 15, 5, 0 );
    }

    @NotNull
    public CreatureIfc newFungus() {
        return new Creature( m_world, 'f', Colors.GREEN, "fungus", FungusBehaviour.instance( this ), 10, 0, 0 );
    }

    @Override
    public String toString() {
        return "CreatureFactory{" +
               "m_world=" + m_world +
               '}';
    }
}
