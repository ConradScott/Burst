package uk.me.conradscott.burst;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.me.conradscott.maths.AxisAlignedBoundingBox3DIfc;
import uk.me.conradscott.maths.Point3DIfc;

import java.awt.Color;
import java.util.Map;

public interface WorldIfc extends AxisAlignedBoundingBox3DIfc {
    @NotNull
    TileIfc tile( @NotNull Point3DIfc location );

    char glyph( @NotNull Point3DIfc location );

    @NotNull
    Color color( @NotNull Point3DIfc location );

    @NotNull
    AxisAlignedBoundingBox3DIfc boundingBox3D();

    boolean isEmpty( @NotNull Point3DIfc location );

    @NotNull
    Point3DIfc getEmptyLocation();

    @NotNull
    Point3DIfc getEmptyLocationAtDepth( int z );

    void addCreatureAt( @NotNull CreatureIfc creature, @NotNull Point3DIfc location );

    @Nullable
    CreatureIfc creature( @NotNull Point3DIfc location );

    @NotNull
    Point3DIfc locationOf( @NotNull CreatureIfc creature );

    @Nullable
    Point3DIfc moveCreatureTo( @NotNull CreatureIfc creature, @NotNull Point3DIfc location );

    @NotNull
    Map<Point3DIfc, CreatureIfc> findCreaturesNear( @NotNull Point3DIfc location, float radius );

    @Nullable
    Point3DIfc removeCreature( @NotNull CreatureIfc creature );

    void addItemAt( @NotNull ItemIfc item, @NotNull Point3DIfc location );

    boolean addAtEmptySpace( @NotNull ItemIfc item, @NotNull Point3DIfc location );

    @Nullable
    Point3DIfc removeItem( @NotNull ItemIfc item );

    @Nullable
    ItemIfc item( @NotNull Point3DIfc location );

    void update();

    void dig( @NotNull Point3DIfc location );
}
