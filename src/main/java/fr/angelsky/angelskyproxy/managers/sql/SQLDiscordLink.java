package fr.angelsky.angelskyproxy.managers.sql;

import fr.angelsky.angelskyproxy.AngelSkyProxy;
import net.dv8tion.jda.api.entities.User;

import java.sql.SQLException;

public class SQLDiscordLink {

	private final AngelSkyProxy angelSkyProxy;

	private final String PLAYERS_ACCOUNT = "players_account";

	public SQLDiscordLink(AngelSkyProxy angelSkyProxy)
	{
		this.angelSkyProxy = angelSkyProxy;
	}

	public void linkUser(User user, String playerName)
	{
		String query = "UPDATE %s SET discord_id = '%s' WHERE player_name = '%s'";
		angelSkyProxy.getProxyManager().getSqlManager().getMySQL().update(String.format(query, PLAYERS_ACCOUNT, user.getId(), playerName));
	}

	public boolean isLinked(String playerName)
	{
		String query = "SELECT discord_id FROM %s WHERE player_name = '%s'";
		return (boolean) angelSkyProxy.getProxyManager().getSqlManager().getMySQL().query(String.format(query, PLAYERS_ACCOUNT, playerName), resultSet -> {
			try {
				if (resultSet.next()){
					if (resultSet.getString("discord_id").equals("null"))
						return false;
					else
						return true;
				}
			} catch (SQLException e) {
				return false;
			}
			return false;
		});
	}

}
