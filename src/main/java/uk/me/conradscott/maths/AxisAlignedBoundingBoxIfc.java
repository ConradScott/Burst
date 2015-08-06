package uk.me.conradscott.maths;

import org.jetbrains.annotations.NotNull;

public interface AxisAlignedBoundingBoxIfc {
    @NotNull
    PointIfc origin();

    @NotNull
    PointIfc corner();

    @NotNull
    PointIfc size();

    int width();

    int height();

    boolean contains( @NotNull PointIfc point );

    boolean contains( int x, int y );
}
