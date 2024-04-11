package fr.angelsky.angelskyproxy.discord;

import com.github.smuddgge.squishyconfiguration.implementation.YamlConfiguration;
import com.github.smuddgge.squishyconfiguration.interfaces.Configuration;
import fr.angelsky.angelskyproxy.AngelSkyProxy;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

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
        this.jda = JDABuilder.createDefault(botConfig.getString("token")).build();
        try { jda.awaitReady(); }
        catch (InterruptedException e) { throw new RuntimeException(e); }
        new AngelBotActivity(jda, angelSkyProxy).activityTask();
        angelSkyProxy.getLogger().info("AngelBot is up!");
    }

    public Configuration getBotConfig(Path path)
    {
        return new YamlConfiguration(path.toFile(), "angelbot.yml");
    }

    public AngelSkyProxy getAngelSkyProxy() {
        return angelSkyProxy;
    }
}
