package uk.me.conradscott.burst;

/**
 * Save file notes: <ul> <li>Save the random seed as it stood when the game was saved; i.e., we need to be able to
 * re-create the programme state, not just the state of the game itself.</li> <li>What should happen if a save file is
 * read into an earlier or later version of the game than was used to create it? In particular, what about monsters and
 * items that are added or removed from the game? One idea is to have 'pre-' and 'post-' images of all monsters, and to
 * embed that information in the save file. So if a Goblin archer is added in v2, where v1 only had Goblins, a v2 save
 * file should include a pre-image for Goblin archer that maps it to a Goblin for earlier versions. For deleted
 * monsters, if Goblin archers are later deleted in v3, then v3 must have a post-image table that replaces Goblin
 * archers in save files with, say, plain Goblins again. Alternatively, never delete anything: v3 may no longer create
 * Goblin archers under any circumstances, but it still knows what they are and will restore them from a save file with
 * no problem.</li> </ul>
 */
public interface GameStateIfc {}
