package uk.me.conradscott.maths;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Point3DIfc {
    int x();

    int y();

    int z();

    @NotNull
    Point3DIfc plus( int dx, int dy, int dz );

    @NotNull
    Point3DIfc plus( @NotNull Point3DIfc point );

    @NotNull
    Point3DIfc plus( int dx, int dy );

    @NotNull
    Point3DIfc plus( @NotNull PointIfc point );

    @NotNull
    Point3DIfc minus( int dx, int dy, int dz );

    @NotNull
    Point3DIfc minus( @NotNull Point3DIfc point );

    @NotNull
    Point3DIfc minus( int dx, int dy );

    @NotNull
    Point3DIfc minus( @NotNull PointIfc point );

    @NotNull
    List<Point3DIfc> neighbors8();
}
