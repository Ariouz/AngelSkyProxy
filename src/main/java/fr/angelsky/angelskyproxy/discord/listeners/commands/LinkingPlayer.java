package fr.angelsky.angelskyproxy.discord.listeners.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class LinkingPlayer {

    private final User user;
    private final Guild guild;

    public LinkingPlayer(User user, Guild guild)
    {
        this.user = user;
        this.guild = guild;
    }

    public User getUser() {
        return user;
    }

    public Guild getGuild() {
        return guild;
    }
}
