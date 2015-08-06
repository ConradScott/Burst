package uk.me.conradscott.burst;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.me.conradscott.maths.AxisAlignedBoundingBox3D;
import uk.me.conradscott.maths.AxisAlignedBoundingBox3DIfc;
import uk.me.conradscott.maths.AxisAlignedBoundingBoxIfc;
import uk.me.conradscott.maths.EuclideanDistance;
import uk.me.conradscott.maths.MetricIfc;
import uk.me.conradscott.maths.Point3D;
import uk.me.conradscott.maths.Point3DIfc;
import uk.me.conradscott.maths.PointIfc;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class World implements WorldIfc {
    private static final Random RANDOM = new Random( System.currentTimeMillis() );
    private static final MetricIfc METRIC = new EuclideanDistance();

    @NotNull private final TileIfc[][][] m_tiles;

    @NotNull private final AxisAlignedBoundingBox3DIfc m_boundingBox3D;

    @NotNull private final BiMap<CreatureIfc, Point3DIfc> m_creatures = HashBiMap.create();
    @NotNull private final BiMap<ItemIfc, Point3DIfc> m_items = HashBiMap.create();

    public World( @NotNull final TileIfc[][][] tiles ) {
        m_tiles = tiles;
        m_boundingBox3D = new AxisAlignedBoundingBox3D( tiles.length, tiles[ 0 ].length, tiles[ 0 ][ 0 ].length );
    }

    @Override
    @NotNull
    public TileIfc tile( @NotNull final Point3DIfc location ) {
        return m_boundingBox3D.contains( location ) ? m_tiles[ location.x() ][ location.y() ][ location.z() ]
                                                    : Tile.BOUNDS;
    }

    @Override
    public char glyph( @NotNull final Point3DIfc location ) {
        return getCreatureOrItemOrTile( location ).glyph();
    }

    @Override
    @NotNull
    public Color color( @NotNull final Point3DIfc location ) {
        return getCreatureOrItemOrTile( location ).color();
    }

    @Override
    @NotNull
    public AxisAlignedBoundingBoxIfc boundingBox() {
        return m_boundingBox3D.boundingBox();
    }

    @Override
    @NotNull
    public AxisAlignedBoundingBox3DIfc boundingBox3D() {
        return m_boundingBox3D;
    }

    @Override
    public boolean isEmpty( @NotNull final Point3DIfc location ) {
        final TileIfc creatureOrItemOrTile = getCreatureOrItemOrTile( location );

        return !creatureOrItemOrTile.isCreature() && creatureOrItemOrTile.isGround();
    }

    @Override
    @NotNull
    public Point3DIfc getEmptyLocation() {
        Point3DIfc location;

        do {
            final int x = RANDOM.nextInt( m_boundingBox3D.width() );
            final int y = RANDOM.nextInt( m_boundingBox3D.height() );
            final int z = RANDOM.nextInt( m_boundingBox3D.depth() );

            location = new Point3D( x, y, z );
        } while ( !isEmpty( location ) );

        return location;
    }

    @Override
    @NotNull
    public Point3DIfc getEmptyLocationAtDepth( final int z ) {
        Point3DIfc location;

        do {
            final int x = RANDOM.nextInt( m_boundingBox3D.width() );
            final int y = RANDOM.nextInt( m_boundingBox3D.height() );

            location = new Point3D( x, y, z );
        } while ( !isEmpty( location ) );

        return location;
    }

    @Override
    public void addCreatureAt( @NotNull final CreatureIfc creature, @NotNull final Point3DIfc location ) {
        assert isEmpty( location );

        m_creatures.put( creature, location );
    }

    @Override
    @Nullable
    public CreatureIfc creature( @NotNull final Point3DIfc location ) {
        return m_creatures.inverse().get( location );
    }

    @Override
    @NotNull
    public Point3DIfc locationOf( @NotNull final CreatureIfc creature ) {
        return m_creatures.get( creature );
    }

    @Override
    @Nullable
    public Point3DIfc moveCreatureTo( @NotNull final CreatureIfc creature, @NotNull final Point3DIfc location ) {
        final Point3DIfc old = locationOf( creature );

        m_creatures.put( creature, location );

        return old;
    }

    @Override
    @NotNull
    public Map<Point3DIfc, CreatureIfc> findCreaturesNear( @NotNull final Point3DIfc location, final float radius ) {
        final Map<Point3DIfc, CreatureIfc> results = new HashMap<>();

        final int rx = ( int ) radius;

        for ( int dx = -rx; dx <= rx; ++dx ) {
            final int ry = ( int ) Math.sqrt( ( radius * radius ) - ( dx * dx ) );

            for ( int dy = -ry; dy <= ry; ++dy ) {
                if ( METRIC.squaredLength( dx, dy ) <= ( radius * radius ) ) {
                    final Point3DIfc point = new Point3D( location.x() + dx, location.y() + dy, location.z() );

                    final CreatureIfc creature = creature( point );

                    if ( creature != null ) {
                        results.put( point, creature );
                    }
                }
            }
        }

        return results;
    }

    @Override
    @Nullable
    public Point3DIfc removeCreature( @NotNull final CreatureIfc creature ) {
        return m_creatures.remove( creature );
    }

    @Override
    public void addItemAt( @NotNull final ItemIfc item, @NotNull final Point3DIfc location ) {
        assert isEmpty( location );

        m_items.put( item, location );
    }

    @Override
    public boolean addAtEmptySpace( @NotNull final ItemIfc item, @NotNull final Point3DIfc location ) {
        final List<Point3DIfc> points = Lists.newArrayList();
        final Collection<Point3DIfc> checked = Sets.newHashSet();

        points.add( location );

        while ( !points.isEmpty() ) {
            final Point3DIfc point = points.remove( 0 );

            checked.add( point );

            if ( !tile( point ).isGround() ) {
                continue;
            }

            if ( item( point ) == null ) {
                addItemAt( item, point );

                final CreatureIfc creature = creature( point );

                if ( creature != null ) {
                    creature.notify( "A %s lands between your feet.", item.name() );
                }

                // Dropped the thing somewhere.
                return true;
            }

            final List<Point3DIfc> neighbors = point.neighbors8();
            neighbors.removeAll( checked );
            points.addAll( neighbors );
        }

        // Nowhere to drop it.
        return false;
    }

    @Nullable
    @Override
    public Point3DIfc removeItem( @NotNull final ItemIfc item ) {
        return m_items.remove( item );
    }

    @Override
    @Nullable
    public ItemIfc item( @NotNull final Point3DIfc location ) {
        return m_items.inverse().get( location );
    }

    @Override
    public void update() {
        final Collection<CreatureIfc> values = ImmutableList.copyOf( m_creatures.keySet() );

        for ( final CreatureIfc creature : values ) {
            creature.update();
        }
    }

    @Override
    public void dig( @NotNull final Point3DIfc location ) {
        if ( tile( location ).isDiggable() ) {
            m_tiles[ location.x() ][ location.y() ][ location.z() ] = Tile.FLOOR;
        }
    }

    @Override
    @NotNull
    public Point3DIfc origin() {
        return m_boundingBox3D.origin();
    }

    @Override
    @NotNull
    public Point3DIfc corner() {
        return m_boundingBox3D.corner();
    }

    @Override
    @NotNull
    public Point3DIfc size() {
        return m_boundingBox3D.size();
    }

    @Override
    public int width() {
        return m_boundingBox3D.width();
    }

    @Override
    public int height() {
        return m_boundingBox3D.height();
    }

    @Override
    public int depth() {
        return m_boundingBox3D.depth();
    }

    @Override
    public boolean contains( @NotNull final PointIfc point ) {
        return m_boundingBox3D.contains( point );
    }

    @Override
    public boolean contains( final int x, final int y ) {
        return m_boundingBox3D.contains( x, y );
    }

    @Override
    public boolean contains( @NotNull final Point3DIfc point ) {
        return m_boundingBox3D.contains( point );
    }

    @Override
    public boolean contains( final int x, final int y, final int z ) {
        return m_boundingBox3D.contains( x, y, z );
    }

    private TileIfc getCreatureOrItemOrTile( final Point3DIfc location ) {
        final CreatureIfc creature = creature( location );

        if ( creature != null ) {
            return creature;
        }

        final ItemIfc item = item( location );

        if ( item != null ) {
            return item;
        }

        return tile( location );
    }

    @Override
    public String toString() {
        return "World{" +
               "m_envelope=" + m_boundingBox3D +
               '}';
    }
}
