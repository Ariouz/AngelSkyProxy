package fr.angelsky.angelskyproxy.discord;

import fr.angelsky.angelskyproxy.AngelSkyProxy;
import fr.angelsky.angelskyproxy.discord.listeners.commands.LinkingPlayer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class DiscordLinkManager {

    private final AngelBot bot;
    private final AngelSkyProxy angelSkyProxy;

    private final HashMap<LinkingPlayer, String> linkingPlayers = new HashMap<>();

    public DiscordLinkManager(AngelSkyProxy angelSkyProxy, AngelBot angelBot)
    {
        this.angelSkyProxy = angelSkyProxy;
        this.bot = angelBot;
    }

    public String startLinking(User user, Guild guild)
    {
        String code = "";
        while (codeExists(code) || Objects.equals(code, ""))
            code = generateCode();
        this.linkingPlayers.put(new LinkingPlayer(user, guild), code);
        user.openPrivateChannel().complete().sendMessage("Pour lier votre compte Minecraft à Discord, entrez la commande **/link "+code+"** en jeu.\nCe code est valable 10 minutes.").queue();
        return code;
    }

    public boolean codeExists(String code)
    {
        return linkingPlayers.values().stream().anyMatch(val -> val.equals(code));
    }

    public User getUserByCode(String code)
    {
        for (Map.Entry<LinkingPlayer, String> entries : linkingPlayers.entrySet())
        {
            if (entries.getValue().equals(code)) return entries.getKey().getUser();
        }
        return null;
    }

    public String generateCode()
    {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++)
            code.append(new Random().nextInt(0,10));
        return code.toString();
    }

    public LinkingPlayer getLinkingPlayer(User user) {
        for (Map.Entry<LinkingPlayer, String> entries : linkingPlayers.entrySet())
        {
            if (entries.getKey().getUser() == user)
                return entries.getKey();
        }
        return null;
    }

    public void completeLinking(User user) {
        LinkingPlayer lp = getLinkingPlayer(user);
        lp.getGuild().addRoleToMember(user, Objects.requireNonNull(lp.getGuild().getRoleById(IDS.ACCOUNT_LINKED_ROLE.getId()))).queue();
    }

    public boolean isLinked(User user, Guild guild)
    {
        return Objects.requireNonNull(guild.getMember(user)).getRoles().contains(guild.getRoleById(IDS.ACCOUNT_LINKED_ROLE.getId()));
    }

    public HashMap<LinkingPlayer, String> getLinkingPlayers() {
        return linkingPlayers;
    }
}
