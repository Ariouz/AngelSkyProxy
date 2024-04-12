package fr.angelsky.angelskyproxy.managers;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.ProxyServer;
import fr.angelsky.angelskyproxy.AngelSkyProxy;
import fr.angelsky.angelskyproxy.commands.LinkCommand;
import fr.angelsky.angelskyproxy.discord.AngelBot;
import fr.angelsky.angelskyproxy.discord.DiscordLinkManager;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.nio.file.Path;

public class ProxyManager {

    private final AngelSkyProxy angelSkyProxy;
    private final AngelBot angelBot;

    private DiscordLinkManager discordLinkManager;

    public ProxyManager(AngelSkyProxy angelSkyProxy)
    {
        this.angelSkyProxy = angelSkyProxy;
        this.angelBot = new AngelBot(angelSkyProxy);
    }

    public void load()
    {
        setupDataDirectory(angelSkyProxy.getDataDirectory());
        this.angelBot.loadBot();
        this.discordLinkManager = new DiscordLinkManager(angelSkyProxy, angelBot);
        registerCommands();
    }

    public void registerCommands()
    {
        CommandManager commandManager = angelSkyProxy.getServer().getCommandManager();
        CommandMeta meta = commandManager.metaBuilder("link").plugin(angelSkyProxy).build();

        commandManager.register(meta, new LinkCommand(angelSkyProxy, angelBot).createBrigadierCommand(angelSkyProxy.getServer()));
    }

    public void setupDataDirectory(Path path)
    {
        File folder = path.toFile();
        if (!folder.exists())
            if (folder.mkdir())
                angelSkyProxy.getLogger().info("DatDirectory created");
    }

    public DiscordLinkManager getDiscordLinkManager() {
        return discordLinkManager;
    }

    public AngelBot getAngelBot() {
        return angelBot;
    }
}
