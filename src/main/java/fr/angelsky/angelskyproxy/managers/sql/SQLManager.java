package fr.angelsky.angelskyproxy.managers.sql;

import com.github.smuddgge.squishyconfiguration.implementation.YamlConfiguration;
import fr.angelsky.angelskyproxy.AngelSkyProxy;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;

public class SQLManager {

    private final AngelSkyProxy angelSkyProxy;

    private MySQL mySQL;
    private SQLDiscordLink sqlDiscordLink;

    private final HashMap<String, String> db_log = new HashMap<>();

    public SQLManager(AngelSkyProxy angelSkyProxy){
        this.angelSkyProxy = angelSkyProxy;
        this.loadDatabaseLogin();
        this.initConnection();
        this.sqlDiscordLink = new SQLDiscordLink(angelSkyProxy);
    }

    public void loadDatabaseLogin(){
        YamlConfiguration config = angelSkyProxy.getProxyManager().getConfig();
        db_log.put("db_name", config.getString("database_login.db_name"));
        db_log.put("db_user", config.getString("database_login.db_user"));
        db_log.put("db_password", config.getString("database_login.db_password"));
        db_log.put("host", config.getString("database_login.host"));
    }

    private void initConnection(){
        mySQL = new MySQL(db_log.get("db_user"), db_log.get("db_password"), "jdbc:mysql://"+db_log.get("host")+":3306/"+db_log.get("db_name")+"?autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false&allowPublicKeyRetrieval=true");
    }

    public SQLDiscordLink getSqlDiscordLink() {
        return sqlDiscordLink;
    }

    public MySQL getMySQL(){
        return mySQL;
    }

}
