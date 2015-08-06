package uk.me.conradscott.burst;

import org.jetbrains.annotations.NotNull;
import uk.me.conradscott.maths.EuclideanDistance;
import uk.me.conradscott.maths.Line;
import uk.me.conradscott.maths.Point3D;
import uk.me.conradscott.maths.Point3DIfc;
import uk.me.conradscott.maths.PointIfc;

import java.util.List;

public final class PlayerBehaviour implements CreatureBehaviourIfc {
    @NotNull private final List<String> m_messages;

    private PlayerBehaviour( @NotNull final List<String> messages ) {
        m_messages = messages;
    }

    @NotNull
    public static CreatureBehaviourIfc instance( @NotNull final List<String> messages ) {
        return new PlayerBehaviour( messages );
    }

    @Override
    public void onEnter( @NotNull final CreatureIfc creature, @NotNull final Point3DIfc location ) {
        final TileIfc tile = creature.world().tile( location );

        if ( tile.isGround() ) {
            creature.world().moveCreatureTo( creature, location );
        } else if ( tile.isDiggable() ) {
            creature.dig( location );
        }
    }

    @Override
    public void onNotify( @NotNull final String message ) {
        m_messages.add( message );
    }

    @Override
    public boolean canSee( @NotNull final CreatureIfc creature, @NotNull final Point3DIfc location ) {
        final WorldIfc world = creature.world();

        final Point3DIfc here = world.locationOf( creature );

        if ( here.z() != location.z() ) {
            return false;
        }

        if ( new EuclideanDistance().squaredDistance( here, location ) > ( creature.getVisionRadius() * creature
                .getVisionRadius() ) )
        {
            return false;
        }

        for ( final PointIfc point : new Line( here.x(), here.y(), location.x(), location.y() ) ) {
            final Point3DIfc point3D = new Point3D( point, location.z() );

            assert !point3D.equals( location );

            if ( !world.tile( point3D ).isGround() ) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "PlayerAI{}";
    }
}
