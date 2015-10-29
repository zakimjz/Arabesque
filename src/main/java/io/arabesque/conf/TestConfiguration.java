package io.arabesque.conf;

import io.arabesque.embedding.Embedding;
import io.arabesque.graph.MainGraph;
import io.arabesque.graph.NullDataMainGraph;
import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;

import java.io.IOException;
import java.nio.file.Paths;

public class TestConfiguration<O extends Embedding> extends Configuration<O> {
    public TestConfiguration() {
        super(new ImmutableClassesGiraphConfiguration(new org.apache.hadoop.conf.Configuration()));
    }

    public TestConfiguration(String graphPath) {
        this();

        getUnderlyingConfiguration().set(CONF_MAINGRAPH_PATH, graphPath);
    }

    @Override
    public MainGraph createGraph() {
        try {
            return new NullDataMainGraph(Paths.get(getMainGraphPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}