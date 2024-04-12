package fr.angelsky.angelskyproxy.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fr.angelsky.angelskyproxy.AngelSkyProxy;
import fr.angelsky.angelskyproxy.discord.AngelBot;
import fr.angelsky.angelskyproxy.discord.DiscordLinkManager;
import fr.angelsky.angelskyproxy.discord.listeners.commands.LinkCommandListener;
import fr.angelsky.angelskyproxy.discord.listeners.commands.LinkingPlayer;
import net.dv8tion.jda.api.entities.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.units.qual.C;


public class LinkCommand {

    private final AngelSkyProxy angelSkyProxy;
    private final AngelBot bot;

    public LinkCommand(AngelSkyProxy angelSkyProxy, AngelBot bot)
    {
        this.angelSkyProxy = angelSkyProxy;
        this.bot = bot;
    }

    public BrigadierCommand createBrigadierCommand(final ProxyServer proxyServer)
    {
        LiteralCommandNode<CommandSource> linkCommand = BrigadierCommand.literalArgumentBuilder("link")
                .requires(source -> source instanceof Player)
                .executes(context -> {
                    CommandSource source = context.getSource();
                    Component message = Component.text("Un code est requis. Entrez /link sur le serveur discord.", NamedTextColor.RED);
                    source.sendMessage(message);
                    return Command.SINGLE_SUCCESS;
                })
                .then(BrigadierCommand.requiredArgumentBuilder("code", StringArgumentType.word())
                .executes(context -> {
                    String code = context.getArgument("code", String.class);
                    DiscordLinkManager discordLinkManager = angelSkyProxy.getProxyManager().getDiscordLinkManager();

                    CommandSource source = context.getSource();
                    Player player = (Player) source;

                    if (!discordLinkManager.codeExists(code))
                    {
                        Component message = Component.text("Le code est invalide ou a expiré.", NamedTextColor.RED);
                        source.sendMessage(message);
                        return Command.SINGLE_SUCCESS;
                    }

                    User user = discordLinkManager.getUserByCode(code);
                    user.openPrivateChannel().complete().sendMessage("Votre compte a été lié au joueur " + player.getUsername()).queue();
                    Component message = Component.text("Votre compte discord "+user.getName()+" a bien été lié.", NamedTextColor.GREEN);
                    source.sendMessage(message);
                    discordLinkManager.completeLinking(user);
                    discordLinkManager.getLinkingPlayers().remove(discordLinkManager.getLinkingPlayer(user));
                    return Command.SINGLE_SUCCESS;
                }))
                .build();
        return new BrigadierCommand(linkCommand);
    }

}
