package configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.inject.Singleton;
import org.yaml.snakeyaml.Yaml;

@Singleton
public class Configuration {

    private ConfigurationParameters configurationParameters;

    public Configuration() {
        Yaml yaml = new Yaml();
        try (InputStream in = Files.newInputStream(Paths.get("configuration.yaml"))) {
            this.configurationParameters = yaml.loadAs(in, ConfigurationParameters.class);
        } catch (IOException e) {
            System.out.println("Provide file configuration.parameters");
        }
    }

    public ConfigurationParameters getConfigurationParameters(){
        return this.configurationParameters;
    }

}
