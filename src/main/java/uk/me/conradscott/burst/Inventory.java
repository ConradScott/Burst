package uk.me.conradscott.burst;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Inventory implements InventoryIfc {
    @NotNull private final ItemIfc[] m_items;

    public Inventory( final int max ) {
        m_items = new ItemIfc[ max ];
    }

    @Override
    @NotNull
    public ItemIfc[] getItems() {
        return m_items;
    }

    @Override
    @Nullable
    public ItemIfc get( final int index ) {
        return m_items[ index ];
    }

    @Override
    public void add( @NotNull final ItemIfc item ) {
        for ( int i = 0; i != m_items.length; ++i ) {
            if ( m_items[ i ] == null ) {
                m_items[ i ] = item;
                break;
            }
        }
    }

    @Override
    public void remove( @NotNull final ItemIfc item ) {
        for ( int i = 0; i < m_items.length; i++ ) {
            if ( m_items[ i ] == item ) {
                m_items[ i ] = null;
                return;
            }
        }
    }

    @Override
    public boolean isFull() {
        int size = 0;

        for ( final ItemIfc item : m_items ) {
            if ( item != null ) {
                size++;
            }
        }

        return size == m_items.length;
    }
}
