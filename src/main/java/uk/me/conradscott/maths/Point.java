package uk.me.conradscott.maths;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Point implements PointIfc {
    private final int m_x;
    private final int m_y;

    public Point( final int x, final int y ) {
        m_x = x;
        m_y = y;
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
    @NotNull
    public PointIfc plus( final int dx, final int dy ) {
        return new Point( m_x + dx, m_y + dy );
    }

    @Override
    @NotNull
    public PointIfc plus( @NotNull final PointIfc point ) {
        return plus( point.x(), point.y() );
    }

    @Override
    @NotNull
    public PointIfc minus( final int x, final int y ) {
        return new Point( m_x - x, m_y - y );
    }

    @Override
    @NotNull
    public PointIfc minus( @NotNull final PointIfc point ) {
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
    public List<PointIfc> neighbors8() {
        final List<PointIfc> points = new ArrayList<>( 8 );

        for ( int dx = -1; dx <= 1; dx++ ) {
            for ( int dy = -1; dy <= 1; dy++ ) {
                if ( ( dx == 0 ) && ( dy == 0 ) ) {
                    continue;
                }

                points.add( plus( dx, dy ) );
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

        final PointIfc point = ( PointIfc ) obj;

        return ( m_x == point.x() ) && ( m_y == point.y() );
    }

    @Override
    public int hashCode() {
        int result = m_x;
        result = ( 31 * result ) + m_y;
        return result;
    }

    @Override
    public String toString() {
        return "Point{" +
               "m_x=" + m_x +
               ", m_y=" + m_y +
               '}';
    }
}
