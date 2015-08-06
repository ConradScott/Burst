package uk.me.conradscott.maths;

public final class ChebyshevDistance implements MetricIfc {
    @Override
    public double length( final int x, final int y ) {
        return Math.max( Math.abs( x ), Math.abs( y ) );
    }

    @Override
    public int squaredLength( final int x, final int y ) {
        final int max = Math.max( x, y );
        return max * max;
    }
}
