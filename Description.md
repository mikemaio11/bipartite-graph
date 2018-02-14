Description​:
My program utilizes an adjacency list to store the graph as to prevent memory overflow that an
adjacency matrix may cause. However, the graph is determined to be bipartite or not as it’s
built. By assigning a color to each value as it’s added to the list, I can simply check if any
neighbors it may have contain the same color. If neighbors do have the same color, the
construction halts. This saves time if the graph fails early on. After it fails, a depth first search is
called to find an odd length cycle in the adjacency list. Since the graph was determined to not
be bipartite, there must be an odd length cycle, even though we may not have constructed the
entire graph yet. Once an odd cycle greater than length one is discovered, it is written to the
output file. If the graph is fully constructed, then it must be bipartite because no neighboring
colors ever clashed. In this scenario, we simply write the colors array to the file to show a valid
coloring of the graph and no further work is needed.

Time​ ​Analysis:
Construction of the graph takes linear time. If there’s an odd cycle it may take less, but we
assume runtime for that is O(V). If the graph is bipartite we write it to the file in Θ(V) time.
Otherwise a depth first search is called on the graph, which runs in O(V+E) time. An unlikely,
but worst case scenario involves the construction of the graph which fails at the very end,
leading to a depth first search, which fails to find a cycle until the end of the graph. This gives us
a total runtime of O(V+(V+E))​ or O(V+E). In the case of a bipartite graph, the runtime is simply Θ(2V) or
Θ(V).