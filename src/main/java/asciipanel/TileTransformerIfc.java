package asciipanel;

import org.jetbrains.annotations.NotNull;

public interface TileTransformerIfc {
    @NotNull
    CharacterData transformTile( int x, int y, CharacterData data );
}
