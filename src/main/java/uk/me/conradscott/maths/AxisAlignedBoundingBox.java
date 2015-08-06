package uk.me.conradscott.maths;

import org.jetbrains.annotations.NotNull;

public final class AxisAlignedBoundingBox implements AxisAlignedBoundingBoxIfc {
    @NotNull private final PointIfc m_origin;

    @NotNull private final PointIfc m_corner;

    public AxisAlignedBoundingBox( final int x0, final int y0, final int x1, final int y1 ) {
        m_origin = new Point( Math.min( x0, x1 ), Math.min( y0, y1 ) );
        m_corner = new Point( Math.max( x0, x1 ), Math.max( y0, y1 ) );
    }

    public AxisAlignedBoundingBox( @NotNull final PointIfc u, @NotNull final PointIfc v ) {
        this( u.x(), u.y(), v.x(), v.y() );
    }

    public AxisAlignedBoundingBox( final int x, final int y ) {
        this( 0, 0, x, y );
    }

    public AxisAlignedBoundingBox( @NotNull final PointIfc point ) {
        this( point.x(), point.y() );
    }

    @Override
    @NotNull
    public PointIfc origin() {
        return m_origin;
    }

    @Override
    @NotNull
    public PointIfc corner() {
        return m_corner;
    }

    @Override
    @NotNull
    public PointIfc size() {
        return m_corner.minus( m_origin );
    }

    @Override
    public int width() {
        return m_corner.x() - m_origin.x();
    }

    @Override
    public int height() {
        return m_corner.y() - m_origin.y();
    }

    @Override
    public boolean contains( @NotNull final PointIfc point ) {
        return contains( point.x(), point.y() );
    }

    @Override
    public boolean contains( final int x, final int y ) {
        return ( m_origin.x() <= x ) && ( x < m_corner.x() ) && ( m_origin.y() <= y ) && ( y < m_corner.y() );
    }

    @Override
    public boolean equals( final Object obj ) {
        if ( this == obj ) {
            return true;
        }

        if ( ( obj == null ) || ( getClass() != obj.getClass() ) ) {
            return false;
        }

        final AxisAlignedBoundingBoxIfc that = ( AxisAlignedBoundingBoxIfc ) obj;

        return m_origin.equals( that.origin() ) && m_corner.equals( that.corner() );
    }

    @Override
    public int hashCode() {
        int result = m_origin.hashCode();
        result = ( 31 * result ) + m_corner.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AxisAlignedBoundingBox{" +
               "m_origin=" + m_origin +
               ", m_corner=" + m_corner +
               '}';
    }
}
