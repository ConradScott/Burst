package uk.me.conradscott.burst;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public final class BatBehaviour implements CreatureBehaviourIfc {
    private static final Random RANDOM = new Random( System.currentTimeMillis() );

    private BatBehaviour() {
    }

    @NotNull
    public static CreatureBehaviourIfc instance() {
        return new BatBehaviour();
    }

    @Override
    public void onUpdate( @NotNull final CreatureIfc creature ) {
        wander( creature );
    }

    private static void wander( @NotNull final CreatureIfc creature ) {
        final int dx = RANDOM.nextInt( 3 ) - 1;
        final int dy = RANDOM.nextInt( 3 ) - 1;

        creature.moveBy( dx, dy, 0 );
    }
}
