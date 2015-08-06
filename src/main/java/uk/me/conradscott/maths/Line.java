package uk.me.conradscott.maths;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class Line implements LineIfc {
    private final Collection<PointIfc> m_points;

    public Line( final int x0, final int y0, final int x1, final int y1 ) {
        final Collection<PointIfc> points = new ArrayList<>();

        final int dx = Math.abs( x1 - x0 );
        final int dy = Math.abs( y1 - y0 );

        final int sx = ( x0 < x1 ) ? 1 : -1;
        final int sy = ( y0 < y1 ) ? 1 : -1;

        int x = x0;
        int y = y0;
        int err = dx - dy;

        while ( ( x != x1 ) || ( y != y1 ) ) {
            assert ( sx * x ) < ( sx * x1 );
            assert ( sy * y ) < ( sy * y1 );

            points.add( new Point( x, y ) );

            final int e2 = err * 2;

            if ( e2 > -dx ) {
                err -= dy;
                x += sx;
            }

            if ( e2 < dx ) {
                err += dx;
                y += sy;
            }
        }

        m_points = points;
    }

    @Override
    public Iterator<PointIfc> iterator() {
        return m_points.iterator();
    }
}
