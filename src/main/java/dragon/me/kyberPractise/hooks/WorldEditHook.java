package dragon.me.kyberPractise.hooks;

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
import com.sk89q.worldedit.util.SideEffectSet;
import com.sk89q.worldedit.util.io.Closer;
import dragon.me.kyberPractise.KyberPractice;
import dragon.me.kyberPractise.storage.Arena;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

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
            FileOutputStream outputStream = new FileOutputStream(KyberPractice.instance.getDataFolder() + "/schematics/" + filename + ".shem");
            ClipboardWriter writer = closer.register(BuiltInClipboardFormat.SPONGE_V3_SCHEMATIC.getWriter(outputStream));
            writer.write(clipboard);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void pasteSchematic(Location location, String filename){

        Optional<Arena> arena = KyberPractice.arenaDataManager.getArenas().stream()
                .filter(arena1 -> arena1.getName().equalsIgnoreCase(filename))
                .findFirst();

        if (arena.isPresent()) {
            location = getMidPoint(arena.get().getPos1(), arena.get().getPos2());
        }else {
            location = location.clone().add(0.5, 0, 0.5);
            KyberPractice.instance.getLogger().warning("Failed to paste schematic, arena not found!");
            return;
        }

        EditSession session = createSelection(location.getWorld());

        ClipboardFormat format = ClipboardFormats.findByFile(new File(KyberPractice.instance.getDataFolder() + "/schematics/" + filename));
        if (format == null) {
            KyberPractice.instance.getLogger().warning("Failed to paste schematic, file not found!");
            return;
        }
        try {
            ClipboardReader reader = format.getReader(new FileInputStream(KyberPractice.instance.getDataFolder() + "/schematics/"+filename));
            Clipboard schematic = reader.read();
            Operation operation = new ClipboardHolder(schematic)
                    .createPaste(session)
                    .to(BukkitAdapter.asBlockVector(location))
                    .build();

            Operations.complete(operation);

            reader.close();
        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }


    }

    private static EditSession createSelection(World world){
        final EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world));
        session.setSideEffectApplier(SideEffectSet.defaults());
        return session;
    }

    /*
    Get the middle point between two points
    Used:
        - For reseting arenas
     */
    public static Location getMidPoint(Location location1, Location location2){
        return location1.clone().toVector().midpoint(location2.clone().toVector()).toLocation(location1.getWorld());
    }
}
