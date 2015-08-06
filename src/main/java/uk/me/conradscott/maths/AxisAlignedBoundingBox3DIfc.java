package uk.me.conradscott.maths;

import org.jetbrains.annotations.NotNull;

public interface AxisAlignedBoundingBox3DIfc {
    @NotNull
    Point3DIfc origin();

    @NotNull
    Point3DIfc corner();

    @NotNull
    Point3DIfc size();

    int width();

    int height();

    int depth();

    @NotNull
    AxisAlignedBoundingBoxIfc boundingBox();

    boolean contains( @NotNull PointIfc point );

    boolean contains( int x, int y );

    boolean contains( @NotNull Point3DIfc point );

    boolean contains( int x, int y, int z );
}
