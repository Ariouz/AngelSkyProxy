package fr.angelsky.angelskyproxy.discord.listeners.commands.mod;

import fr.angelsky.angelskyproxy.discord.AngelBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ClearCommandListener extends ListenerAdapter {

	private final AngelBot angelBot;

	public ClearCommandListener(AngelBot angelBot)
	{
		this.angelBot = angelBot;
	}

	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event)
	{
		if (event.getName().equalsIgnoreCase("clear"))
		{
			event.deferReply().queue();

			if (!event.getInteraction().isFromGuild())
			{
				event.getHook().sendMessage("Cette commande ne peut être utilisé que dans un serveur.").setEphemeral(true).queue();
				return ;
			}
			Member member = event.getMember();

			assert member != null;
			if (!member.hasPermission(Permission.MESSAGE_MANAGE))
			{
				event.getHook().sendMessage("Vous n'avez pas la permission d'executer cette commande.").setEphemeral(true).queue();
				return ;
			}

			int amount = Objects.requireNonNull(event.getOption("amount")).getAsInt();
			TextChannel channel = event.getGuildChannel().asTextChannel();

			channel.getHistory()
					.retrievePast(amount)
					.complete().forEach(message -> {
						if (message != null)
							message.delete().queue();
					});
			event.getHook().sendMessage(amount + " message supprimé(s).").queue(msg -> {
				if (msg != null)
					msg.delete().queueAfter(5, TimeUnit.SECONDS);
			});
		}
	}

}
