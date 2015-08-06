package uk.me.conradscott.burst;

import org.jetbrains.annotations.NotNull;
import uk.me.conradscott.maths.Point3DIfc;

import java.awt.Color;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public final class Creature implements CreatureIfc {
    private static final Random RANDOM = new Random( System.currentTimeMillis() );
    private static final Pattern SPACE_PATTERN = Pattern.compile( " " );

    @NotNull private final WorldIfc m_world;

    private final char m_glyph;

    @NotNull private final Color m_color;

    @NotNull private final String m_name;

    @NotNull private final CreatureBehaviourIfc m_behaviour;

    private final int m_maxHp;
    private final int m_attackValue;
    private final int m_defenceValue;

    private int m_hp;

    private final int m_visionRadius;

    private final InventoryIfc m_inventory = new Inventory( 20 );

    public Creature( @NotNull final WorldIfc world, final char glyph, @NotNull final Color color,
                     @NotNull final String name, @NotNull final CreatureBehaviourIfc behaviour, final int maxHp,
                     final int attack, final int defence )
    {
        m_world = world;
        m_glyph = glyph;
        m_color = color;
        m_name = name;
        m_behaviour = behaviour;

        m_maxHp = maxHp;
        m_attackValue = attack;
        m_defenceValue = defence;

        m_hp = maxHp;

        m_visionRadius = 9;
    }

    @Override
    @NotNull
    public WorldIfc world() {
        return m_world;
    }

    @Override
    public char glyph() {
        return m_glyph;
    }

    @Override
    @NotNull
    public Color color() {
        return m_color;
    }

    @Override
    @NotNull
    public String name() {
        return m_name;
    }

    @Override
    public int hp() {
        return m_hp;
    }

    @Override
    public int maxHp() {
        return m_maxHp;
    }

    @Override
    public int attackValue() {
        return m_attackValue;
    }

    @Override
    public int defenceValue() {
        return m_defenceValue;
    }

    @Override
    public int getVisionRadius() {
        return m_visionRadius;
    }

    @Override
    public boolean canSee( @NotNull final Point3DIfc location ) {
        return m_behaviour.canSee( this, location );
    }

    @Override
    public void notify( @NotNull final String message, final Object... params ) {
        m_behaviour.onNotify( String.format( message, params ) );
    }

    @Override
    public void moveBy( final int dx, final int dy, final int dz ) {
        final Point3DIfc location = m_world.locationOf( this );

        final Point3DIfc newLocation = location.plus( dx, dy, dz );

        final TileIfc tile = m_world.tile( newLocation );

        if ( dz == -1 ) {
            if ( tile == Tile.STAIRS_DOWN ) {
                doAction( "walk up the stairs to level %d", newLocation.z() + 1 );
            } else {
                doAction( "try to go up but are stopped by the cave ceiling" );
                return;
            }
        } else if ( dz == 1 ) {
            if ( tile == Tile.STAIRS_UP ) {
                doAction( "walk down the stairs to level %d", newLocation.z() + 1 );
            } else {
                doAction( "try to go down but are stopped by the cave floor" );
                return;
            }
        }

        final CreatureIfc other = m_world.creature( newLocation );

        if ( other == null ) {
            m_behaviour.onEnter( this, newLocation );
        } else {
            attack( other );
        }
    }

    @Override
    public void dig( final Point3DIfc location ) {
        m_world.dig( location );
    }

    @Override
    public void update() {
        m_behaviour.onUpdate( this );
    }

    @Override
    public void attack( @NotNull final CreatureIfc other ) {
        final int max = Math.max( 1, attackValue() - other.defenceValue() );

        final int amount = RANDOM.nextInt( max ) + 1;

        doAction( "attack the %s for %d damage", other.name(), amount );
        other.modifyHp( -amount );
    }

    @Override
    public void modifyHp( final int amount ) {
        m_hp += amount;

        if ( m_hp < 1 ) {
            doAction( "die" );
            leaveCorpse();
            m_world.removeCreature( this );
        }
    }

    @Override
    public void doAction( @NotNull final CharSequence message, final Object... params ) {
        final int r = 9;
        final Point3DIfc location = m_world.locationOf( this );
        final Map<Point3DIfc, CreatureIfc> creaturesNear = m_world.findCreaturesNear( location, r );

        for ( final CreatureIfc other : creaturesNear.values() ) {
            if ( other == this ) {
                other.notify( "You " + message + '.', params );
            } else {
                if ( other.canSee( location ) ) {
                    other.notify( String.format( "The %s %s.", m_name, makeSecondPerson( message ) ), params );
                }
            }
        }
    }

    @NotNull
    @Override
    public InventoryIfc inventory() {
        return m_inventory;
    }

    @Override
    public void pickup() {
        final Point3DIfc location = m_world.locationOf( this );

        final ItemIfc item = m_world.item( location );

        if ( item == null ) {
            doAction( "grab at the ground" );
        } else if ( m_inventory.isFull() ) {
            doAction( "cannot fit that in your inventory" );
        } else {
            doAction( "pickup a %s", item.name() );
            m_world.removeItem( item );
            m_inventory.add( item );
        }
    }

    @Override
    public void drop( @NotNull final ItemIfc item ) {
        final Point3DIfc location = m_world.locationOf( this );

        if ( m_world.addAtEmptySpace( item, location ) ) {
            doAction( "drop a " + item.name() );
            m_inventory.remove( item );
        } else {
            notify( "There's nowhere to drop the %s.", item.name() );
        }
    }

    private static String makeSecondPerson( final CharSequence text ) {
        final String[] words = SPACE_PATTERN.split( text );
        words[ 0 ] += 's';

        final StringBuilder builder = new StringBuilder( text.length() + 1 );

        for ( final String word : words ) {
            builder.append( ' ' );
            builder.append( word );
        }

        return builder.toString().trim();
    }

    private void leaveCorpse() {
        final ItemIfc corpse = new Item( '%', m_color, m_name + " corpse" );
        corpse.modifyFoodValue( m_maxHp * 3 );
        m_world.addAtEmptySpace( corpse, m_world.locationOf( this ) );
    }

    @Override
    public String toString() {
        return "Creature{" +
               "m_world=" + m_world +
               ", m_glyph=" + m_glyph +
               ", m_color=" + m_color +
               ", m_maxHp=" + m_maxHp +
               ", m_hp=" + m_hp +
               ", m_attackValue=" + m_attackValue +
               ", m_defenceValue=" + m_defenceValue +
               ", m_behaviour=" + m_behaviour +
               '}';
    }
}
