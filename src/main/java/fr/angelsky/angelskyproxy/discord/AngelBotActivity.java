package fr.angelsky.angelskyproxy.discord;

import fr.angelsky.angelskyproxy.AngelSkyProxy;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.util.concurrent.TimeUnit;

public class AngelBotActivity {

    private final JDA jda;
    private final AngelSkyProxy angelSkyProxy;

    public AngelBotActivity(JDA jda, AngelSkyProxy angelSkyProxy)
    {
        this.jda = jda;
        this.angelSkyProxy = angelSkyProxy;
    }

    public void activityTask()
    {
        angelSkyProxy.getServer().getScheduler().buildTask(angelSkyProxy, () -> {
            jda.getPresence().setActivity(Activity.watching(angelSkyProxy.getServer().getPlayerCount() + " joueur" + (angelSkyProxy.getServer().getPlayerCount() > 1 ? "s" : "")));
        }).repeat(30, TimeUnit.SECONDS).schedule();
    }

}
