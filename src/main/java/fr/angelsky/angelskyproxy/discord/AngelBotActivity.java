package fr.angelsky.angelskyproxy.discord;

import fr.angelsky.angelskyproxy.AngelSkyProxy;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
        AtomicInteger integer = new AtomicInteger();
        angelSkyProxy.getServer().getScheduler().buildTask(angelSkyProxy, () -> {
            if (integer.getAndAdd(1) % 2 == 0)
                jda.getPresence().setActivity(Activity.watching(angelSkyProxy.getServer().getPlayerCount() + " joueur" + (angelSkyProxy.getServer().getPlayerCount() > 1 ? "s" : "")));
            else
                jda.getPresence().setActivity(Activity.playing("play.angelsky.fr"));
        }).repeat(30, TimeUnit.SECONDS).schedule();
    }

}
