package uk.me.conradscott.burst;

import org.jetbrains.annotations.NotNull;
import uk.me.conradscott.maths.Point3DIfc;

public interface CreatureBehaviourIfc {
    default void onEnter( @NotNull final CreatureIfc creature, @NotNull final Point3DIfc location ) {
        final TileIfc tile = creature.world().tile( location );

        if ( tile.isGround() ) {
            creature.world().moveCreatureTo( creature, location );
        } else {
            creature.doAction( "bump into a wall" );
        }
    }

    default void onUpdate( @NotNull final CreatureIfc creature ) { }

    default void onNotify( @NotNull final String message ) { }

    default boolean canSee( @NotNull final CreatureIfc creature, @NotNull final Point3DIfc location ) {
        return false;
    }
}
