package uk.me.conradscott.burst;

import org.jetbrains.annotations.NotNull;

public interface ItemIfc extends TileIfc {
    @NotNull
    String name();

    @Override
    default boolean isItem() {
        return true;
    }

    int foodValue();

    void modifyFoodValue( int amount );
}
