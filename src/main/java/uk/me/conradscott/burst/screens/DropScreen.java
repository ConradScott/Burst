package uk.me.conradscott.burst.screens;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.me.conradscott.burst.CreatureIfc;
import uk.me.conradscott.burst.ItemIfc;

public class DropScreen extends InventoryBasedScreen {
    public DropScreen( final CreatureIfc player ) {
        super( player );
    }

    @Override
    @NotNull
    protected String getVerb() {
        return "drop";
    }

    @Override
    protected boolean isAcceptable( @NotNull final ItemIfc item ) {
        return true;
    }

    @Override
    @Nullable
    protected ScreenIfc use( @NotNull final ItemIfc item ) {
        player().drop( item );
        return null;
    }
}
