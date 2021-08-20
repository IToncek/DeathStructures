package cf.itoncek.deathstructure;

import cf.itoncek.deathstructure.items.CraftableBlocks;
import cf.itoncek.deathstructure.listener.BlockPlaceListener;
import cf.itoncek.deathstructure.listener.PlayerDeathListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.stream.Stream;

public final class DeathStructure extends JavaPlugin {

    public static DeathStructure plugin;

    @Override
    public void onEnable() {
        FileConfiguration config = this.getConfig();
        config.addDefault("userID", "");
        config.addDefault("licenceID", "");
        config.options().copyDefaults(true);
        saveConfig();

        if (config.get("userID") == "" || config.get("licenceID") == "") {
            System.out.println("You need to fill the Config for my plugin to work. If you don't have credintials, contact me on Discord/Instagram. Now I'm stopping this server.");
            Bukkit.getServer().shutdown();
        } else {
            try {
                verify(config.getString("userID"), config.getString("licenceID"));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("There was an error, try it again in a few seconds or go to \"https://l.itoncek.cf\" to check, if the licence server is responding. If not, contact me on Discord/Instagram");
                Bukkit.getServer().shutdown();
            }
        }
        plugin = this;
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        File mainPath = new File(plugin.getDataFolder().getPath());
        if (!mainPath.exists()){
            mainPath.mkdir();
        }
        File directory = new File(plugin.getDataFolder() + "/structures");
        if(!directory.exists()){
            directory.mkdir();
        }
        CraftableBlocks.initRecipes(this);
        exportAllResourceLocales();
        getCommand("giveallstructures").setExecutor(new GiveAllStructuresCommand());
    }

    public static void verify(String id, String licence) throws Exception {
        URL licenceserver = new URL("https://l.itoncek.cf/verify/" + id + "/" + licence + "/");
        URLConnection yc = licenceserver.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yc.getInputStream()));
        String inputLine = in.readLine();
        System.out.println(inputLine);

        if ((inputLine = in.readLine()) != null) {
            if (inputLine == "200 OK") {
                System.out.println("Verified, Your server is OK");
                return;
            } else {
                System.out.println("There was an error, try it again in a few seconds or go to \"https://l.itoncek.cf\" to check, if the licence server is responding. If not, contact me on Discord/Instagram");
                Bukkit.getServer().shutdown();
            }
        }
        in.close();
    }

    private void exportAllResourceLocales() {
        Iterator<Path> pathIterator = getAllPathsWithinLocales();
        if (pathIterator == null) {
            return;
        }
        while (pathIterator.hasNext()) {
            Path localePath = pathIterator.next();
            InputStream resourceStream = plugin.getResource(localePath.toString().replace("/structures", "structures"));
            if (resourceStream == null) {
                plugin.getLogger().log(Level.SEVERE, "Could not load resource " + localePath.toString());
                continue;
            }
            try {
                File outFile = new File(plugin.getDataFolder() + "/structures/" + localePath.getFileName());
                if (outFile.exists()) continue;
                Path outPath = outFile.toPath();
                Files.copy(resourceStream, outPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not copy to " + localePath.toString());
                ex.printStackTrace();
            }
        }
    }

    private Iterator<Path> getAllPathsWithinLocales() {
        URI uri;
        try {
            uri = this.plugin.getClass().getResource("/structures").toURI();
        } catch (URISyntaxException ex) {
            plugin.getLogger().log(Level.SEVERE, "Got invalid URI Syntax from /structures folder");
            return null;
        }
        Path localesPath;
        if (uri.getScheme().equals("jar")) {
            FileSystem fileSystem;
            try {
                fileSystem = FileSystems.getFileSystem(uri);
            } catch (FileSystemNotFoundException ignored) {
                try {
                    fileSystem = FileSystems.newFileSystem(uri, new HashMap<String, Object>());
                } catch (IOException ex) {
                    plugin.getLogger().log(Level.SEVERE, "Error enumerating resources");
                    ex.printStackTrace();
                    return null;
                }
            }
            localesPath = fileSystem.getPath("/structures");
        } else {
            localesPath = Paths.get(uri);
        }
        Stream<Path> walker;
        try {
            walker = Files.walk(localesPath, 1);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Error enumerating resources");
            ex.printStackTrace();
            return null;
        }
        return walker.iterator();
    }
}
