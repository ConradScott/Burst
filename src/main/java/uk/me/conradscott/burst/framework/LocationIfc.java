package uk.me.conradscott.burst.framework;

import uk.me.conradscott.maths.Point3DIfc;

public interface LocationIfc extends AttributeIfc {
    @Override
    default Class<? extends ComponentIfc> type() {
        return LocationIfc.class;
    }

    Point3DIfc location();
}
