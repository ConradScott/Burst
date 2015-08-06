package uk.me.conradscott.burst;

import org.jetbrains.annotations.NotNull;
import uk.me.conradscott.maths.Point3D;
import uk.me.conradscott.maths.Point3DIfc;

import java.util.Random;

public final class FungusBehaviour implements CreatureBehaviourIfc {
    private static final Random RANDOM = new Random( System.currentTimeMillis() );

    private static final double SPREAD_PROBABILITY = 0.02;
    private static final int SPREAD_RANGE = 5;
    private static final int SPREAD_LIMIT = 5;

    @NotNull private final CreatureFactory m_factory;

    private int m_spreadCount = 0;

    private FungusBehaviour( @NotNull final CreatureFactory factory ) {
        m_factory = factory;
    }

    @NotNull
    public static CreatureBehaviourIfc instance( @NotNull final CreatureFactory factory ) {
        return new FungusBehaviour( factory );
    }

    @Override
    public void onUpdate( @NotNull final CreatureIfc creature ) {
        assert ( m_spreadCount >= 0 ) && ( m_spreadCount <= SPREAD_LIMIT );

        if ( ( m_spreadCount != SPREAD_LIMIT ) && ( RANDOM.nextFloat() < SPREAD_PROBABILITY ) ) {
            spread( creature );
        }
    }

    private void spread( @NotNull final CreatureIfc fungus ) {
        assert ( m_spreadCount >= 0 ) && ( m_spreadCount <= SPREAD_LIMIT );

        final Point3DIfc location = fungus.world().locationOf( fungus );

        final int x = location.x() + getRandomSpread();
        final int y = location.y() + getRandomSpread();

        final Point3DIfc newLocation = new Point3D( x, y, location.z() );

        if ( !fungus.world().isEmpty( newLocation ) ) {
            return;
        }

        fungus.world().addCreatureAt( m_factory.newFungus(), newLocation );
        fungus.doAction( "spawn a child" );

        m_spreadCount += 1;
    }

    private static int getRandomSpread() {
        return RANDOM.nextInt( ( 2 * SPREAD_RANGE ) + 1 ) - SPREAD_RANGE;
    }

    @Override
    public String toString() {
        return "FungusAI{" +
               "m_factory=" + m_factory +
               ", m_spreadCount=" + m_spreadCount +
               '}';
    }
}
