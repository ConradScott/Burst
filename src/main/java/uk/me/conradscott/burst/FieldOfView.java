package uk.me.conradscott.burst;

import uk.me.conradscott.maths.EuclideanDistance;
import uk.me.conradscott.maths.Line;
import uk.me.conradscott.maths.MetricIfc;
import uk.me.conradscott.maths.Point3D;
import uk.me.conradscott.maths.Point3DIfc;
import uk.me.conradscott.maths.PointIfc;

public final class FieldOfView {
    private final WorldIfc m_world;
    private int m_depth;

    private boolean[][] m_visible;

    private final TileIfc[][][] m_tiles;

    private final MetricIfc m_metric = new EuclideanDistance();

    public FieldOfView( final WorldIfc world ) {
        m_world = world;
        m_visible = new boolean[ world.width() ][ world.height() ];
        m_tiles = new TileIfc[ world.width() ][ world.height() ][ world.depth() ];

        for ( int x = 0; x != world.width(); x++ ) {
            for ( int y = 0; y != world.height(); y++ ) {
                for ( int z = 0; z != world.depth(); z++ ) {
                    m_tiles[ x ][ y ][ z ] = Tile.UNKNOWN;
                }
            }
        }
    }

    public boolean isVisible( final int x, final int y, final int z ) {
        return ( z == m_depth ) && ( x >= 0 ) && ( y >= 0 ) && ( x < m_visible.length ) &&
               ( y < m_visible[ 0 ].length ) && m_visible[ x ][ y ];
    }

    public TileIfc tile( final int x, final int y, final int z ) {
        return m_tiles[ x ][ y ][ z ];
    }

    public void update( final int wx, final int wy, final int wz, final int r ) {
        m_depth = wz;
        m_visible = new boolean[ m_world.width() ][ m_world.height() ];

        for ( int x = -r; x < r; x++ ) {
            for ( int y = -r; y < r; y++ ) {
                if ( m_metric.squaredLength( x, y ) > ( r * r ) ) {
                    continue;
                }

                if ( ( ( wx + x ) < 0 ) || ( ( wx + x ) >= m_world.width() ) || ( ( wy + y ) < 0 ) ||
                     ( ( wy + y ) >= m_world.height() ) )
                {
                    continue;
                }

                for ( final PointIfc point : new Line( wx, wy, wx + x, wy + y ) ) {
                    final Point3DIfc point3D = new Point3D( point, wz );

                    final TileIfc tile = m_world.tile( point3D );
                    m_visible[ point.x() ][ point.y() ] = true;
                    m_tiles[ point.x() ][ point.y() ][ wz ] = tile;

                    if ( !tile.isGround() ) {
                        break;
                    }
                }
            }
        }
    }
}
