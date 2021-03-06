package de.marcelsauer.profiler.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * yaml bean
 *
 * @author msauer
 */
public class Config {
    public Classes classes = new Classes();
    public Recorder recorder = new Recorder();

    public static Config createDefaultFromYamlFile(String yamlFile) {
        Yaml yaml = new Yaml(new Constructor(Config.class));
        return (Config) yaml.load(FileUtils.getLocalResource(yamlFile));
    }

    public static Config createCustomFromYamlFile(String yamlFile) {
        Yaml yaml = new Yaml(new Constructor(Config.class));
        try {
            InputStream result = new FileInputStream(yamlFile);
            return (Config) yaml.load(result);
        } catch (IOException e) {
            throw new RuntimeException("could not load " + yamlFile);
        }
    }

    @Override
    public String toString() {
        return "Config{" +
            "classes=" + classes +
            ", recorder=" + recorder +
            '}';
    }

    public void merge(Config otherConfig) {
        mergeClasses(otherConfig);
        mergeRecorder(otherConfig);
    }

    private void mergeRecorder(Config otherConfig) {
        addNullSafe(otherConfig.recorder.superClasses, this.recorder.superClasses);
        addNullSafe(otherConfig.recorder.interfaces, this.recorder.interfaces);
        addNullSafe(otherConfig.recorder.methodLevelAnnotations, this.recorder.methodLevelAnnotations);
        addNullSafe(otherConfig.recorder.classLevelAnnotations, this.recorder.classLevelAnnotations);
    }

    private void mergeClasses(Config otherConfig) {
        addNullSafe(otherConfig.classes.included, this.classes.included);
        addNullSafe(otherConfig.classes.excluded, this.classes.excluded);
    }

    private void addNullSafe(Set<String> src, Set<String> target) {
        if (src != null) {
            target.addAll(src);
        }
    }

    public boolean isInclusionConfigured() {
        return !this.classes.included.isEmpty();
    }
}
