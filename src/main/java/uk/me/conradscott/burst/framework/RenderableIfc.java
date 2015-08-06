package uk.me.conradscott.burst.framework;

public interface RenderableIfc extends AttributeIfc {
    @Override
    default Class<? extends ComponentIfc> type() {
        return RenderableIfc.class;
    }
}
