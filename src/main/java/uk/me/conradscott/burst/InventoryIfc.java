package uk.me.conradscott.burst;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface InventoryIfc {
    @NotNull
    ItemIfc[] getItems();

    @Nullable
    ItemIfc get( int index );

    void add( @NotNull ItemIfc item );

    void remove( @NotNull ItemIfc item );

    boolean isFull();
}
