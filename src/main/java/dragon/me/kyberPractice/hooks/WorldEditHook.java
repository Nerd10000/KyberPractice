package dragon.me.kyberPractice.hooks;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.util.SideEffectSet;
import com.sk89q.worldedit.util.io.Closer;
import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.storage.Arena;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class WorldEditHook {

    public WorldEditHook() {
        if (!KyberPractice.isWorldEditAvailable) {
            KyberPractice.instance.getLogger().warning("WorldEdit/FAWE is not installed, some features (like arena restoration) is NOT GOING TO WORK!");
        } else {
            KyberPractice.instance.getLogger().info("WorldEdit/FAWE is installed, enjoy the features!");
        }
    }

    public static void saveSchematic(World world, Location location1, Location location2, String filename){
        Region region = new CuboidRegion(BukkitAdapter.asBlockVector(location1), BukkitAdapter.asBlockVector(location2));
        EditSession session = createSelection(world);

        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
        ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(session, region, clipboard, region.getMinimumPoint());
        try {
            Operations.complete(forwardExtentCopy);
        } catch (WorldEditException e) {
            e.printStackTrace();
        }

        try (Closer closer = Closer.create()){
            FileOutputStream outputStream = new FileOutputStream(KyberPractice.instance.getDataFolder() + "/schematics/" + filename + ".schem");
            ClipboardWriter writer = closer.register(BuiltInClipboardFormat.SPONGE_V3_SCHEMATIC.getWriter(outputStream));
            writer.write(clipboard);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void pasteSchematic(Location location, String filename, Runnable callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Arena arena = KyberPractice.arenaDataManager.getArena(filename);

                if (arena == null) {
                    KyberPractice.instance.getLogger().warning("Failed to paste schematic, arena not found!");
                    return;
                }


                if (arena.getPos1() == null || arena.getPos2() == null) {
                    KyberPractice.instance.getLogger().warning("Cannot paste schematic, arena region not defined for '" + filename + "'.");
                    return;
                }

                // Create a region to find the actual minimum point, which is our paste location
                Region region = new CuboidRegion(BukkitAdapter.asBlockVector(arena.getPos1()), BukkitAdapter.asBlockVector(arena.getPos2()));
                Location pasteLocation = BukkitAdapter.adapt(arena.getPos1().getWorld(), region.getMinimumPoint());


                File schematicFile = new File(KyberPractice.instance.getDataFolder(), "schematics/" + filename + ".schem");
                KyberPractice.instance.getLogger().info("Attempting to paste schematic from: " + schematicFile.getAbsolutePath());

                if (!schematicFile.exists()) {
                    KyberPractice.instance.getLogger().warning("Schematic file not found at the specified path!");
                    return;
                }

                ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
                if (format == null) {
                    KyberPractice.instance.getLogger().warning("Failed to determine clipboard format for the schematic. Is it a valid schematic file?");
                    return;
                }
                KyberPractice.instance.getLogger().info("Successfully found schematic format: " + format.toString());

                try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(pasteLocation.getWorld()))) {
                    try (ClipboardReader reader = format.getReader(new FileInputStream(schematicFile))) {
                        Clipboard schematic = reader.read();
                        ClipboardHolder clipboardHolder = new ClipboardHolder(schematic);
                        Transform transform = new AffineTransform().translate(schematic.getOrigin().toVector3().multiply(-1));
                        clipboardHolder.setTransform(transform);
                        Operation operation = clipboardHolder
                                .createPaste(editSession)
                                .to(BukkitAdapter.asBlockVector(pasteLocation))
                                .ignoreAirBlocks(false)
                                .build();
                        Operations.complete(operation);
                        KyberPractice.instance.getLogger().info("Successfully pasted schematic '" + filename + "' at " + pasteLocation.toVector());
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                callback.run();
                            }
                        }.runTask(KyberPractice.instance);
                    } catch (IOException | WorldEditException e) {
                        KyberPractice.instance.getLogger().severe("An error occurred while reading or pasting the schematic: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskAsynchronously(KyberPractice.instance);
    }

    private static EditSession createSelection(World world){
        final EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world));
        session.setSideEffectApplier(SideEffectSet.defaults());
        return session;
    }

    /*
    Get the middle point between two points
    Used:
        - For restoring arenas
     */
    public static Location getMidPoint(Location location1, Location location2){
        return location1.clone().toVector().midpoint(location2.clone().toVector()).toLocation(location1.getWorld());
    }
}
