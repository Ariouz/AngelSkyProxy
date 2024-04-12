package fr.angelsky.angelskyproxy.discord.listeners.commands;

import fr.angelsky.angelskyproxy.AngelSkyProxy;
import fr.angelsky.angelskyproxy.discord.AngelBot;
import fr.angelsky.angelskyproxy.discord.DiscordLinkManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionType;

import java.util.concurrent.TimeUnit;

public class LinkCommandListener extends ListenerAdapter {


    private final AngelSkyProxy angelSkyProxy;
    private final AngelBot bot;

    public LinkCommandListener(AngelSkyProxy angelSkyProxy, AngelBot bot)
    {
        this.angelSkyProxy = angelSkyProxy;
        this.bot = bot;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        if (event.getName().equalsIgnoreCase("link"))
        {
            DiscordLinkManager discordLinkManager = angelSkyProxy.getProxyManager().getDiscordLinkManager();
            User user = event.getUser();

            event.deferReply().queue();

            if (event.getGuild() == null)
            {
                event.getHook().sendMessage("Cette commande doit être éxecutée dans un salon de commandes.").setEphemeral(true).queue();
                return ;
            }

            if (discordLinkManager.isLinked(user, event.getGuild()))
            {
                event.getHook().sendMessage("Votre compte est déja lié.").queue();
                return ;
            }

            if (discordLinkManager.getLinkingPlayers().containsKey(user))
            {
                event.getHook().sendMessage("Un code toujours valide vous a déja été envoyé.").queue();
                return ;
            }
            event.getHook().sendMessage("Un code vous a été envoyé par message privé.").queue();
            discordLinkManager.startLinking(user, event.getGuild());
            angelSkyProxy.getServer().getScheduler().buildTask(angelSkyProxy, () -> {
                discordLinkManager.getLinkingPlayers().remove(user);
            }).delay(10, TimeUnit.MINUTES).schedule();
        }
    }

}
