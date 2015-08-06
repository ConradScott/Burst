package uk.me.conradscott.burst.worlds;

import org.apache.log4j.Logger;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.me.conradscott.burst.Tile;
import uk.me.conradscott.burst.TileIfc;
import uk.me.conradscott.burst.World;
import uk.me.conradscott.burst.WorldIfc;
import uk.me.conradscott.maths.AxisAlignedBoundingBox3D;
import uk.me.conradscott.maths.AxisAlignedBoundingBox3DIfc;
import uk.me.conradscott.maths.Point3D;
import uk.me.conradscott.maths.Point3DIfc;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public final class WorldBuilder {
    private static final Logger LOGGER = Logger.getLogger( WorldBuilder.class );
    private static final Random RANDOM = new Random( System.currentTimeMillis() );

    @Nullable private final TileIfc[][][] m_tiles;

    @NotNull private final AxisAlignedBoundingBox3DIfc m_boundingBox3D;

    @Nullable private int[][][] m_regions;

    private int m_nextRegion = 1;

    public WorldBuilder( final int width, final int height, final int depth ) {
        m_tiles = null;
        m_boundingBox3D = new AxisAlignedBoundingBox3D( width, height, depth );
        m_regions = null;
    }

    public WorldBuilder( @NotNull final TileIfc[][][] tiles ) {
        m_tiles = tiles;
        m_boundingBox3D = new AxisAlignedBoundingBox3D( tiles.length, tiles[ 0 ].length, tiles[ 0 ][ 0 ].length );
        m_regions = null;
    }

    private static void error( @NotNull final String msg ) {
        LOGGER.error( msg );
        throw new IllegalArgumentException( msg );
    }

    @NotNull
    public WorldBuilder makeCaves() {
        return randomizeTiles().smooth( 8 ).createRegions().connectRegions().addExitStairs();
    }

    @NotNull
    public WorldIfc build() {
        checkTilesIsNotNull();
        assert m_tiles != null;

        return new World( m_tiles );
    }

    @NotNull
    private WorldBuilder smooth( final int times ) {
        checkTilesIsNotNull();
        assert m_tiles != null;

        TileIfc[][][] tiles = m_tiles;

        for ( int time = 0; time != times; ++time ) {
            tiles = smooth( tiles );
        }

        return new WorldBuilder( tiles );
    }

    @NotNull
    private TileIfc[][][] smooth( @NotNull final TileIfc[][][] tiles ) {
        final TileIfc[][][] result = new TileIfc[ m_boundingBox3D.width() ][ m_boundingBox3D.height() ][ m_boundingBox3D
                .depth() ];

        for ( int x = 0; x != m_boundingBox3D.width(); ++x ) {
            for ( int y = 0; y != m_boundingBox3D.height(); ++y ) {
                for ( int z = 0; z != m_boundingBox3D.depth(); ++z ) {
                    int floors = 0;
                    int rocks = 0;

                    for ( int dx = -1; dx <= 1; ++dx ) {
                        for ( int dy = -1; dy <= 1; ++dy ) {
                            if ( !m_boundingBox3D.contains( x + dx, y + dy, z ) ) {
                                continue;
                            }

                            if ( tiles[ x + dx ][ y + dy ][ z ] == Tile.FLOOR ) {
                                floors++;
                            } else {
                                rocks++;
                            }
                        }
                    }

                    result[ x ][ y ][ z ] = ( floors >= rocks ) ? Tile.FLOOR : Tile.WALL;
                }
            }
        }

        return result;
    }

    @NotNull
    private WorldBuilder randomizeTiles() {
        final TileIfc[][][] tiles = new TileIfc[ m_boundingBox3D.width() ][ m_boundingBox3D.height() ][ m_boundingBox3D
                .depth() ];

        for ( int x = 0; x != m_boundingBox3D.width(); ++x ) {
            for ( int y = 0; y != m_boundingBox3D.height(); ++y ) {
                for ( int z = 0; z != m_boundingBox3D.depth(); ++z ) {
                    tiles[ x ][ y ][ z ] = RANDOM.nextBoolean() ? Tile.FLOOR : Tile.WALL;
                }
            }
        }

        return new WorldBuilder( tiles );
    }

    @NotNull
    private WorldBuilder createRegions() {
        assert m_tiles != null;

        m_regions = new int[ m_boundingBox3D.width() ][ m_boundingBox3D.height() ][ m_boundingBox3D.depth() ];

        for ( int x = 0; x != m_boundingBox3D.width(); ++x ) {
            for ( int y = 0; y != m_boundingBox3D.height(); ++y ) {
                for ( int z = 0; z != m_boundingBox3D.depth(); ++z ) {
                    if ( ( m_tiles[ x ][ y ][ z ] != Tile.WALL ) && ( m_regions[ x ][ y ][ z ] == 0 ) ) {
                        final int size = fillRegion( m_nextRegion, x, y, z );

                        if ( size < 25 ) {
                            removeRegion( m_nextRegion, z );
                        } else {
                            m_nextRegion += 1;
                        }
                    }
                }
            }
        }

        return this;
    }

    private int fillRegion( final int region, final int x, final int y, final int z ) {
        assert m_regions != null;
        assert m_tiles != null;

        final Deque<Point3DIfc> queue = new ArrayDeque<>();

        queue.addFirst( new Point3D( x, y, z ) );

        m_regions[ x ][ y ][ z ] = region;

        int size = 1;

        while ( !queue.isEmpty() ) {
            final Point3DIfc p = queue.remove();

            for ( final Point3DIfc neighbor : p.neighbors8() ) {
                if ( !m_boundingBox3D.contains( neighbor.x(), neighbor.y(), neighbor.z() ) ||
                     ( m_regions[ neighbor.x() ][ neighbor.y() ][ neighbor.z() ] != 0 ) ||
                     ( m_tiles[ neighbor.x() ][ neighbor.y() ][ neighbor.z() ] == Tile.WALL ) )
                {
                    continue;
                }

                size++;
                m_regions[ neighbor.x() ][ neighbor.y() ][ neighbor.z() ] = region;
                queue.add( neighbor );
            }
        }

        return size;
    }

    private void removeRegion( final int region, final int z ) {
        assert m_regions != null;
        assert m_tiles != null;

        for ( int x = 0; x != m_boundingBox3D.width(); ++x ) {
            for ( int y = 0; y != m_boundingBox3D.height(); ++y ) {
                if ( m_regions[ x ][ y ][ z ] == region ) {
                    m_regions[ x ][ y ][ z ] = 0;
                    m_tiles[ x ][ y ][ z ] = Tile.WALL;
                }
            }
        }
    }

    @NotNull
    private WorldBuilder connectRegions() {
        for ( int z = 0; z != ( m_boundingBox3D.depth() - 1 ); ++z ) {
            connectRegionsDown( z );
        }

        return this;
    }

    private void connectRegionsDown( final int z ) {
        assert m_regions != null;
        assert m_tiles != null;

        final Collection<Pair<Integer, Integer>> connected = new ArrayList<>();

        for ( int x = 0; x != m_boundingBox3D.width(); ++x ) {
            for ( int y = 0; y != m_boundingBox3D.height(); ++y ) {
                final Pair<Integer, Integer> connection = Pair
                        .with( m_regions[ x ][ y ][ z ], m_regions[ x ][ y ][ z + 1 ] );

                if ( ( m_tiles[ x ][ y ][ z ] == Tile.FLOOR ) && ( m_tiles[ x ][ y ][ z + 1 ] == Tile.FLOOR ) &&
                     !connected.contains( connection ) )
                {
                    connected.add( connection );
                    connectRegionsDown( z, m_regions[ x ][ y ][ z ], m_regions[ x ][ y ][ z + 1 ] );
                }
            }
        }
    }

    private void connectRegionsDown( final int z, final int r1, final int r2 ) {
        assert m_regions != null;
        assert m_tiles != null;

        final List<Point3DIfc> candidates = findRegionOverlaps( z, r1, r2 );

        int stairs = 0;

        do {
            final Point3DIfc p = candidates.remove( 0 );
            m_tiles[ p.x() ][ p.y() ][ z ] = Tile.STAIRS_DOWN;
            m_tiles[ p.x() ][ p.y() ][ z + 1 ] = Tile.STAIRS_UP;
            stairs++;
        } while ( ( candidates.size() / stairs ) > 250 );
    }

    private List<Point3DIfc> findRegionOverlaps( final int z, final int r1, final int r2 ) {
        assert m_regions != null;
        assert m_tiles != null;

        final List<Point3DIfc> candidates = new ArrayList<>();

        for ( int x = 0; x != m_boundingBox3D.width(); ++x ) {
            for ( int y = 0; y != m_boundingBox3D.height(); ++y ) {
                if ( ( m_tiles[ x ][ y ][ z ] == Tile.FLOOR ) && ( m_tiles[ x ][ y ][ z + 1 ] == Tile.FLOOR ) &&
                     ( m_regions[ x ][ y ][ z ] == r1 ) && ( m_regions[ x ][ y ][ z + 1 ] == r2 ) )
                {
                    candidates.add( new Point3D( x, y, z ) );
                }
            }
        }

        Collections.shuffle( candidates );

        return candidates;
    }

    @NotNull
    private WorldBuilder addExitStairs() {
        assert m_tiles != null;

        int x;
        int y;

        do {
            x = RANDOM.nextInt( m_boundingBox3D.width() );
            y = RANDOM.nextInt( m_boundingBox3D.height() );
        } while ( m_tiles[ x ][ y ][ 0 ] != Tile.FLOOR );

        m_tiles[ x ][ y ][ 0 ] = Tile.STAIRS_UP;

        return this;
    }

    private void checkTilesIsNotNull() {
        if ( m_tiles == null ) {
            error( "A world builder with tiles must be created first." );
        }
    }

    @Override
    public String toString() {
        return "WorldBuilder{" +
               ", m_boundingBox3D=" + m_boundingBox3D +
               ", m_nextRegion=" + m_nextRegion +
               '}';
    }
}
