package guice;

import algorithm.MatchSearchTree;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import configuration.Configuration;
import matchmaker.ClientPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This is {@code Guice} module which injects dependencies throughout whole project. Methods from this class are not
 * supposed to be called 'by hand', only by internal {@code Guice} calls.
 */
public class MatchmadeModule extends AbstractModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchmadeModule.class);

    @Override
    protected void configure()
    {
    }

    @Provides
    @Inject
    public MatchSearchTree provideSearchTree(final ClientPool clientPool, final Configuration configuration)
    {
        final MatchSearchTree matchSearchTree = new MatchSearchTree(clientPool, configuration);
        matchSearchTree.initializeSearchTree();
        return matchSearchTree;
    }

    @Provides
    @Inject
    public Configuration provideConfiguration() {
        Yaml yaml = new Yaml();
        try (InputStream in = Files.newInputStream(Paths.get("configuration.yaml"))) {
            return yaml.loadAs(in, Configuration.class);
        } catch (IOException e) {
            LOGGER.error("You must provide file configuration.parameters");
            throw new UnsupportedOperationException(e);
        }
    }
}
