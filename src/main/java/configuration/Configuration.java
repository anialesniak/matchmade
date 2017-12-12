package configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.yaml.snakeyaml.Yaml;

public class Configuration {

    private static Configuration instance = null;

    private ConfigurationParameters configurationParameters;

    private Configuration() {
        loadConfiguration();
    }

    public static ConfigurationParameters getParameters() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance.configurationParameters;
    }

    private void loadConfiguration() {
        Yaml yaml = new Yaml();
        try (InputStream in = Files.newInputStream(Paths.get("configuration.yaml"))) {
            this.configurationParameters = yaml.loadAs(in, ConfigurationParameters.class);
        } catch (IOException e) {
            System.out.println("Provide file configuration.parameters");
        }
    }

}
