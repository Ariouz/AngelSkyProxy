package fr.angelsky.angelskyproxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import fr.angelsky.angelskyproxy.managers.ProxyManager;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "angelskyproxy",
        name = "AngelSkyProxy",
        version = "1.0.0-SNAPSHOT",
        authors = {"Ariouz"}
)
public class AngelSkyProxy {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private ProxyManager proxyManager;

    @Inject
    public AngelSkyProxy(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory)
    {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Proxy loading...");
        this.proxyManager = new ProxyManager(this);
        proxyManager.load();
        logger.info("Proxy loaded!");
    }

    public ProxyManager getProxyManager() {
        return proxyManager;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public Logger getLogger() {
        return logger;
    }

    public ProxyServer getServer() {
        return server;
    }
}
