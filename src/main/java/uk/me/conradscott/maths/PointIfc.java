package uk.me.conradscott.maths;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PointIfc {
    int x();

    int y();

    @NotNull
    PointIfc plus( int dx, int dy );

    @NotNull
    PointIfc plus( @NotNull PointIfc point );

    @NotNull
    PointIfc minus( int x, int y );

    @NotNull
    PointIfc minus( @NotNull PointIfc point );

    @NotNull
    List<PointIfc> neighbors8();
}
