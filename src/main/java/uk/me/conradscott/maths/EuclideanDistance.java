package uk.me.conradscott.maths;

public final class EuclideanDistance implements MetricIfc {
    @Override
    public double length( final int x, final int y ) {
        return Math.hypot( x, y );
    }

    @Override
    public int squaredLength( final int x, final int y ) {
        return ( x * x ) + ( y * y );
    }
}
