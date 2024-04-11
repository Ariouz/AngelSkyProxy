package fr.angelsky.angelskyproxy.managers;

import com.velocitypowered.api.proxy.ProxyServer;
import fr.angelsky.angelskyproxy.AngelSkyProxy;
import fr.angelsky.angelskyproxy.discord.AngelBot;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.nio.file.Path;

public class ProxyManager {

    private final AngelSkyProxy angelSkyProxy;
    private final AngelBot angelBot;

    public ProxyManager(AngelSkyProxy angelSkyProxy)
    {
        this.angelSkyProxy = angelSkyProxy;
        this.angelBot = new AngelBot(angelSkyProxy);
    }

    public void load()
    {
        setupDataDirectory(angelSkyProxy.getDataDirectory());
        this.angelBot.loadBot();
    }

    public void setupDataDirectory(Path path)
    {
        File folder = path.toFile();
        if (!folder.exists())
            if (folder.mkdir())
                angelSkyProxy.getLogger().info("DatDirectory created");
    }

    public AngelBot getAngelBot() {
        return angelBot;
    }
}
