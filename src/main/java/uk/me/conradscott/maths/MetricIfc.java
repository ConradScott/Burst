package uk.me.conradscott.maths;

public interface MetricIfc {
    double length( int x, int y );

    default double length( final PointIfc u ) {
        return length( u.x(), u.y() );
    }

    default double length( final Point3DIfc u ) {
        return length( u.x(), u.y() );
    }

    int squaredLength( int x, int y );

    default int squaredLength( final PointIfc u ) {
        return squaredLength( u.x(), u.y() );
    }

    default int squaredLength( final Point3DIfc u ) {
        return squaredLength( u.x(), u.y() );
    }

    default double distance( final int ux, final int uy, final int vx, final int vy ) {
        return length( ux - vx, uy - vy );
    }

    default double distance( final PointIfc u, final PointIfc v ) {
        return distance( u.x(), u.y(), v.x(), v.y() );
    }

    default double distance( final PointIfc u, final Point3DIfc v ) {
        return distance( u.x(), u.y(), v.x(), v.y() );
    }

    default double distance( final Point3DIfc u, final PointIfc v ) {
        return distance( u.x(), u.y(), v.x(), v.y() );
    }

    default double distance( final Point3DIfc u, final Point3DIfc v ) {
        assert u.z() == v.z();

        return distance( u.x(), u.y(), v.x(), v.y() );
    }

    default int squaredDistance( final int ux, final int uy, final int vx, final int vy ) {
        return squaredLength( ux - vx, uy - vy );
    }

    default int squaredDistance( final PointIfc u, final PointIfc v ) {
        return squaredDistance( u.x(), u.y(), v.x(), v.y() );
    }

    default int squaredDistance( final PointIfc u, final Point3DIfc v ) {
        return squaredDistance( u.x(), u.y(), v.x(), v.y() );
    }

    default int squaredDistance( final Point3DIfc u, final PointIfc v ) {
        return squaredDistance( u.x(), u.y(), v.x(), v.y() );
    }

    default int squaredDistance( final Point3DIfc u, final Point3DIfc v ) {
        assert u.z() == v.z();

        return squaredDistance( u.x(), u.y(), v.x(), v.y() );
    }
}
