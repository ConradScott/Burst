package uk.me.conradscott.maths;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Point3D implements Point3DIfc {
    private final int m_x;
    private final int m_y;
    private final int m_z;

    public Point3D( final int x, final int y, final int z ) {
        m_x = x;
        m_y = y;
        m_z = z;
    }

    public Point3D( @NotNull final PointIfc point, final int z ) {
        m_x = point.x();
        m_y = point.y();
        m_z = z;
    }

    @Override
    public int x() {
        return m_x;
    }

    @Override
    public int y() {
        return m_y;
    }

    @Override
    public int z() {
        return m_z;
    }

    @Override
    @NotNull
    public Point3DIfc plus( final int dx, final int dy, final int dz ) {
        return new Point3D( m_x + dx, m_y + dy, m_z + dz );
    }

    @Override
    @NotNull
    public Point3DIfc plus( @NotNull final Point3DIfc point ) {
        return plus( point.x(), point.y(), point.z() );
    }

    @Override
    @NotNull
    public Point3DIfc plus( final int dx, final int dy ) {
        return plus( dx, dy, 0 );
    }

    @Override
    @NotNull
    public Point3DIfc plus( @NotNull final PointIfc point ) {
        return plus( point.x(), point.y() );
    }

    @Override
    @NotNull
    public Point3DIfc minus( final int dx, final int dy, final int dz ) {
        return new Point3D( m_x - dx, m_y - dy, m_z - dz );
    }

    @Override
    @NotNull
    public Point3DIfc minus( @NotNull final Point3DIfc point ) {
        return minus( point.x(), point.y(), point.z() );
    }

    @Override
    @NotNull
    public Point3DIfc minus( final int dx, final int dy ) {
        return minus( dx, dy, 0 );
    }

    @Override
    @NotNull
    public Point3DIfc minus( @NotNull final PointIfc point ) {
        return minus( point.x(), point.y() );
    }

    /**
     * "We shuffle the list before returning it so we don't introduce bias. Otherwise the upper left neighbor would
     * always be checked first and the lower right would be last which may lead to some odd things." (http://trystans
     * .blogspot.co.uk/2011/09/roguelike-tutorial-07-z-levels-and.html)
     *
     * @return
     */
    @Override
    @NotNull
    public List<Point3DIfc> neighbors8() {
        final List<Point3DIfc> points = new ArrayList<>( 8 );

        for ( int dx = -1; dx <= 1; dx++ ) {
            for ( int dy = -1; dy <= 1; dy++ ) {
                if ( ( dx == 0 ) && ( dy == 0 ) ) {
                    continue;
                }

                points.add( plus( dx, dy, 0 ) );
            }
        }

        Collections.shuffle( points );

        return points;
    }

    @Override
    public boolean equals( final Object obj ) {
        if ( this == obj ) {
            return true;
        }

        if ( ( obj == null ) || ( getClass() != obj.getClass() ) ) {
            return false;
        }

        final Point3DIfc point = ( Point3DIfc ) obj;

        return ( m_x == point.x() ) && ( m_y == point.y() ) && ( m_z == point.z() );
    }

    @Override
    public int hashCode() {
        int result = m_x;
        result = ( 31 * result ) + m_y;
        result = ( 31 * result ) + m_z;
        return result;
    }

    @Override
    public String toString() {
        return "Point{" +
               "m_x=" + m_x +
               ", m_y=" + m_y +
               ", m_z=" + m_z +
               '}';
    }
}
