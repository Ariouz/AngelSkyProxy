package fr.angelsky.angelskyproxy.managers;

import com.github.smuddgge.squishyconfiguration.implementation.YamlConfiguration;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import fr.angelsky.angelskyproxy.AngelSkyProxy;
import fr.angelsky.angelskyproxy.commands.LinkCommand;
import fr.angelsky.angelskyproxy.discord.AngelBot;
import fr.angelsky.angelskyproxy.discord.managers.DiscordLinkManager;
import fr.angelsky.angelskyproxy.managers.sql.SQLManager;

import java.io.File;
import java.nio.file.Path;

public class ProxyManager {

    private final AngelSkyProxy angelSkyProxy;
    private final AngelBot angelBot;

    private DiscordLinkManager discordLinkManager;
    private SQLManager sqlManager;
    private YamlConfiguration config;

    public ProxyManager(AngelSkyProxy angelSkyProxy)
    {
        this.angelSkyProxy = angelSkyProxy;
        this.angelBot = new AngelBot(angelSkyProxy);
    }

    public void load()
    {
        setupDataDirectory(angelSkyProxy.getDataDirectory());
        this.loadConfig(angelSkyProxy.getDataDirectory());
        this.sqlManager = new SQLManager(angelSkyProxy);
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

    private void loadConfig(Path path)
    {
        this.config = new YamlConfiguration(path.toFile(), "config.yml");
        this.config.load();
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public DiscordLinkManager getDiscordLinkManager() {
        return discordLinkManager;
    }

    public AngelBot getAngelBot() {
        return angelBot;
    }

    public SQLManager getSqlManager() {
        return sqlManager;
    }
}
