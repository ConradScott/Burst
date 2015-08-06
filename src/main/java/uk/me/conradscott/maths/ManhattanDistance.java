package uk.me.conradscott.maths;

public final class ManhattanDistance implements MetricIfc {
    @Override
    public double length( final int x, final int y ) {
        return Math.abs( x ) + Math.abs( y );
    }

    @Override
    public int squaredLength( final int x, final int y ) {
        final int length = Math.abs( x ) + Math.abs( y );

        return length * length;
    }
}
