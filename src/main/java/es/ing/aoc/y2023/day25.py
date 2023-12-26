import networkx as nx
g=nx.Graph()
for l in open("full.txt").read().split("\n"):
    p=l.split()
    for t in p[1:]:
        g.add_edge(p[0][:-1],t)
cv, p= nx.stoer_wagner(g)
print(len(p[0])*len(p[1]))