# Arabesque: Distributed graph mining made simple

Distributed data processing platforms such as MapReduce and Pregel (Giraph, GraphLab, Graph-X) have substantially simplified the design and deployment of certain classes of distributed graph analytics algorithms such as computing Pagerank, SSP, CC etc. However, these platforms do not represent a good match for graph mining problems, as for example finding frequent subgraphs (FSM) in a labeled graph or finding Cliques. Given an input graph, these problems require exploring a very large number of subgraphs(embeddings) and finding patterns that match some “interestingness” criteria desired by the user. The number of the embeddings that the system needs to explore grows exponential to the size of the embeddings.

Arabesque: Think like an Embedding paradigm Our system Arabesque is the first distributed data processing platform focused on solving these graph mining problems. Arabesque automates the process of exploring a very large number of embeddings, and defines a high-level computational model (filter – process) that simplifies the development of scalable graph mining algorithms. In a nutshell, Arabesque explores embeddings and passes them to the user defined code, which must simply decide whether an embedding is of interest and should be further extended (filter function) and whether we should compute outputs for the embeddings that passed the filter function (process function). This allows the end-user to think of the problem in a natural way, Think like an Embedding (TLE), where the embedding is a first class citizen, and not to try to force the problem into other paradigms. Even so, Arabesque runs under the familiar and well supported Apache Giraph system in a Hadoop cluster. Arabesque simply uses Giraph as a generic data processing platform that provides a BSP execution flow.

Elegant but above all Efficient Arabesque is quite unique in that despite the simple and elegant API and the distributed implementation it is extremely efficient. Indeed, in our SOSP 2015 paper, we show that a single threaded Arabesque provides a comparable performance to specialized centralized implementations. This is possible because the user-defined functions are not the computational expensive parts of the Graph Mining Algorithms. The computational expensive functions are handled by Arabesque and deal mostly with the graph exploration, the isomorphism computations and the canonicality checks all inherent to Graph mining algorithms, and thus can be handled as efficient as a centralized implementation.

*Current Version:* 1.0.1-BETA

Arabesque is a distributed graph mining system that enables quick and easy
development of graph mining algorithms, while providing a scalable and efficient
execution engine running on top of Hadoop.

Benefits of Arabesque:
* Simple and intuitive API, specially tailored for Graph Mining algorithms.
* Transparently handling of all complexities associated with these algorithms.
* Scalable to hundreds of workers.
* Efficient implementation: negligible overhead compared to equivalent centralized solutions.

Arabesque is open-source with the Apache 2.0 license.

## Requirements for running

* Linux/Mac with 64-bit JVM
* [A functioning installation of Hadoop2 with MapReduce (local or in a cluster)](http://www.alexjf.net/blog/distributed-systems/hadoop-yarn-installation-definitive-guide/)

## Preparing your input
Arabesque currently takes as input graphs with the following formats:

* **Graphs label on vertex(default)**
```
# <num vertices> <num edges>
<vertex id> <vertex label> [<neighbour id1> <neighbour id2> ... <neighbour id n>]
<vertex id> <vertex label> [<neighbour id1> <neighbour id2> ... <neighbour id n>]
...
```

* **Graphs label on edges**
To enable processing label on edges, in the yaml file, add the following lines
```
arabesque.graph.edge_labelled: true
arabesque.graph.multigraph: true   # Set this to true if multiple edges
                                     # exist between two vertices.
```
Input format
```
# <num vertices> <num edges>
<vertex id> <vertex label> [<neighbour id1> <edge label> <neighbour id2> <edge label>... ]
<vertex id> <vertex label> [<neighbour id1> <edge label> <neighbour id2> <edge label>... ]
...
```

Vertex ids are expected to be sequential integers between 0 and (total number of vertices - 1).

## Test/Execute the included algorithms

You can find an execution-helper script and several configuration files for the different algorithms under the [scripts
folder in the repository](https://github.com/Qatar-Computing-Research-Institute/Arabesque/tree/master/scripts):

* `run_arabesque.sh` - Launcher for arabesque executions. Takes as parameters one or more yaml files describing the configuration of the execution to be run. Configurations are applied in sequence with configurations in subsequent yaml files overriding entries of previous ones.
* `cluster.yaml` - File with configurations related to the cluster and, so, common to all algorithms: number of workers, number of threads per worker, number of partitions, etc.
* `<algorithm>.yaml` - Files with configurations related to particular algorithm executions using as input the [provided citeseer graph](https://github.com/Qatar-Computing-Research-Institute/Arabesque/tree/master/data):
  * `fsm.yaml` - Run frequent subgraph mining over the citeseer graph.
  * `cliques.yaml` - Run clique finding over the citeseer graph.
  * `motifs.yaml` - Run motif counting over the citeseer graph.
  * `triangles.yaml` - Run triangle counting over the citeseer graph.

**Steps:**

1. Compile Arabesque using 
  ```
  mvn package
  ```
  You will find the jar file under `target/`
  
2. Copy the newly generated jar file, the `run_arabesque.sh` script and the desired yaml files onto a folder on a computer with access to an Hadoop cluster. 

3. Upload the input graph to HDFS. Sample graphs are under the `data` directory. Make sure you have initialized HDFS first.

  ```
  hdfs dfs -put <input graph file> <destination graph file in HDFS>
  ```

4. Configure the `cluster.yaml` file with the desired number of containers, threads per container and other cluster-wide configurations.

5. Configure the algorithm-specific yamls to reflect the HDFS location of your input graph as well as the parameters you want to use (max size for motifs and cliques or support for FSM).

6. Run your desired algorithm by executing:

  ```
  ./run_arabesque.sh cluster.yaml <algorithm>.yaml
  ```

7. Follow execution progress by checking the logs of the Hadoop containers.

8. Check any output (generated with calls to the `output` function) in the HDFS path indicated by the `output_path` configuration entry.


## Implementing your own algorithms
The easiest way to get to code your own implementations on top of Arabesque is by forking our [Arabesque Skeleton Project](https://github.com/Qatar-Computing-Research-Institute/Arabesque-Skeleton). You can do this via
[Github](https://help.github.com/articles/fork-a-repo/) or manually by executing the following:

```
git clone https://github.com/Qatar-Computing-Research-Institute/Arabesque-Skeleton.git $PROJECT_PATH
cd $PROJECT_PATH
git remote rename origin upstream
git remote add origin $YOUR_REPO_URL
```
