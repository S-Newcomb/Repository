package com.superglue.toweroffense.level.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class LevelGraph implements IndexedGraph<LevelNode> {
    private static final float NODE_WIDTH = 1f;
    private static final float NODE_HEIGHT = 1f;

    private int numNodesX;
    private int numNodesY;

    LevelHeuristic heuristic;
    LevelNode[][] nodes;
    Array<LevelNodeConnection> connections;

    // map of nodes to connections which start with that node
    ObjectMap<LevelNode, Array<Connection<LevelNode>>> connectionsMap;

    private int lastNodeIndex;

    public LevelGraph(float width, float height){
        heuristic = new LevelHeuristic();

        this.numNodesX = (int)(width / NODE_WIDTH);
        this.numNodesY = (int)(height / NODE_HEIGHT);
        nodes = new LevelNode[numNodesX][numNodesY];

        connections = new Array<>();

        connectionsMap = new ObjectMap<>();

        lastNodeIndex = 0;
    }

    private void addNode(LevelNode node){
        node.index = lastNodeIndex;
        lastNodeIndex++;

        nodes[node.x][node.y] = node;
    }

    private void connectNodes(LevelNode fromNode, LevelNode toNode){
        // create connection
        LevelNodeConnection connection = new LevelNodeConnection(fromNode, toNode);
        // add this node to the hashmap as a key if it isn't one already
        if(!connectionsMap.containsKey(fromNode)){
            connectionsMap.put(fromNode, new Array<>());
        }
        // add this connection to the array of conections associated with the source node
        connectionsMap.get(fromNode).add(connection);
        // add the connection to the list of connections.
        connections.add(connection);
    }

    public void createGraph() {
        for(int i = 0; i < numNodesX; i++){
            for(int j = 0; j < numNodesY; j++){
                LevelNode node = new LevelNode(i, j);
                addNode(node);
                nodes[i][j] = node;

                if(i > 0){
                    connectNodes(nodes[i-1][j], node);
                }
                if(j > 0){
                    connectNodes(nodes[i][j-1], node);
                }
            }
        }
    }

    public GraphPath<LevelNode> findPath(LevelNode startNode, LevelNode goalCity){
        GraphPath<LevelNode> nodePath = new DefaultGraphPath<>();
        new IndexedAStarPathFinder<>(this).searchNodePath(startNode, goalCity, heuristic, nodePath);
        return nodePath;
    }

    public LinePath<Vector2> getLinePath(GraphPath<LevelNode> graphPath){
        Array<Vector2> waypoints = new Array<>();

        for(LevelNode node : graphPath){
            waypoints.add(new Vector2(node.x, node.y));
        }

        LinePath<Vector2> linePath = new LinePath<>(waypoints);
        return linePath;
    }

    public LevelNode getNode(int x, int y) {
        return nodes[x][y];
    }

    public LevelNode getNodeAt(float meterX, float meterY){
        // 1 meter == 1 tile
        return nodes[(int)meterX][(int)meterY];
    }

    @Override
    public int getIndex(LevelNode node) {
        return node.index;
    }

    @Override
    public int getNodeCount() {
        return lastNodeIndex;
    }

    @Override
    public Array<Connection<LevelNode>> getConnections(LevelNode fromNode) {
        // if this node has connections associated with it, return that list of connections
        if(connectionsMap.containsKey(fromNode)) {
            return connectionsMap.get(fromNode);
        }

        // otherwise return empty list.
        return new Array<>(0);
    }
}