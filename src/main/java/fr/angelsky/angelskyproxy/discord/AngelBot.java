package fr.angelsky.angelskyproxy.discord;

import com.github.smuddgge.squishyconfiguration.implementation.YamlConfiguration;
import com.github.smuddgge.squishyconfiguration.interfaces.Configuration;
import fr.angelsky.angelskyproxy.AngelSkyProxy;
import fr.angelsky.angelskyproxy.discord.listeners.commands.LinkCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.io.File;
import java.nio.file.Path;

public class AngelBot {

    private final AngelSkyProxy angelSkyProxy;
    private JDA jda;
    private Configuration botConfig;

    public AngelBot(AngelSkyProxy angelSkyProxy)
    {
        this.angelSkyProxy = angelSkyProxy;
    }

    public void loadBot()
    {
        angelSkyProxy.getLogger().info("Starting AngelBot...");
        this.botConfig = getBotConfig(angelSkyProxy.getDataDirectory());
        this.botConfig.load();
        this.jda = JDABuilder.createDefault(botConfig.getString("token"))
                .addEventListeners(new LinkCommandListener(angelSkyProxy, this))
                .build();
        updateCommands(jda);
        try { jda.awaitReady(); }
        catch (InterruptedException e) { throw new RuntimeException(e); }
        new AngelBotActivity(jda, angelSkyProxy).activityTask();
        angelSkyProxy.getLogger().info("AngelBot is up!");
    }

    public void updateCommands(JDA jda)
    {
        jda.upsertCommand(
                Commands.slash("link", "Relier votre compte Discord à Minecraft")
        ).queue();
    }

    public Configuration getBotConfig(Path path)
    {
        return new YamlConfiguration(path.toFile(), "angelbot.yml");
    }

    public AngelSkyProxy getAngelSkyProxy() {
        return angelSkyProxy;
    }

    public JDA getJda() {
        return jda;
    }
}
