package uk.me.conradscott.maths;

import org.jetbrains.annotations.NotNull;

public final class AxisAlignedBoundingBox3D implements AxisAlignedBoundingBox3DIfc {
    @NotNull private final Point3DIfc m_origin;

    @NotNull private final Point3DIfc m_corner;

    public AxisAlignedBoundingBox3D( final int x0, final int y0, final int z0, final int x1, final int y1,
                                     final int z1 )
    {
        m_origin = new Point3D( Math.min( x0, x1 ), Math.min( y0, y1 ), Math.min( z0, z1 ) );
        m_corner = new Point3D( Math.max( x0, x1 ), Math.max( y0, y1 ), Math.max( z0, z1 ) );
    }

    public AxisAlignedBoundingBox3D( @NotNull final Point3DIfc u, @NotNull final Point3DIfc v ) {
        this( u.x(), u.y(), u.z(), v.x(), v.y(), v.z() );
    }

    public AxisAlignedBoundingBox3D( final int x, final int y, final int z ) {
        this( 0, 0, 0, x, y, z );
    }

    public AxisAlignedBoundingBox3D( @NotNull final Point3DIfc point ) {
        this( point.x(), point.y(), point.z() );
    }

    @Override
    @NotNull
    public Point3DIfc origin() {
        return m_origin;
    }

    @Override
    @NotNull
    public Point3DIfc corner() {
        return m_corner;
    }

    @Override
    @NotNull
    public Point3DIfc size() {
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
    public int depth() {
        return m_corner.z() - m_origin.z();
    }

    @Override
    @NotNull
    public AxisAlignedBoundingBoxIfc boundingBox() {
        return new AxisAlignedBoundingBox( m_origin.x(), m_origin.y(), m_corner.x(), m_corner.y() );
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
    public boolean contains( @NotNull final Point3DIfc point ) {
        return contains( point.x(), point.y(), point.z() );
    }

    @Override
    public boolean contains( final int x, final int y, final int z ) {
        return contains( x, y ) && ( m_origin.z() <= z ) && ( z < m_corner.z() );
    }

    @Override
    public boolean equals( final Object obj ) {
        if ( this == obj ) {
            return true;
        }

        if ( ( obj == null ) || ( getClass() != obj.getClass() ) ) {
            return false;
        }

        final AxisAlignedBoundingBox3DIfc that = ( AxisAlignedBoundingBox3DIfc ) obj;

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
        return "AxisAlignedBoundingBox3D{" +
               "m_origin=" + m_origin +
               ", m_corner=" + m_corner +
               '}';
    }
}
