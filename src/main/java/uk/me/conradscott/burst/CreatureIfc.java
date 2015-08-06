package uk.me.conradscott.burst;

import org.jetbrains.annotations.NotNull;
import uk.me.conradscott.maths.Point3DIfc;

public interface CreatureIfc extends TileIfc {
    @NotNull
    WorldIfc world();

    @NotNull
    String name();

    int hp();

    int maxHp();

    int attackValue();

    int defenceValue();

    int getVisionRadius();

    boolean canSee( @NotNull Point3DIfc location );

    void notify( @NotNull String message, Object... params );

    void moveBy( int dx, int dy, int dz );

    void dig( Point3DIfc location );

    void update();

    void attack( @NotNull CreatureIfc other );

    void modifyHp( int amount );

    void doAction( @NotNull CharSequence message, Object... params );

    @NotNull
    InventoryIfc inventory();

    @Override
    default boolean isCreature() {
        return true;
    }

    void pickup();

    void drop( @NotNull ItemIfc item );
}
