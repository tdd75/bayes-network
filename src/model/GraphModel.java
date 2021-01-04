package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphModel {
    private Set<String> listNode = new HashSet<String>();
    private Set<String> listEdge = new HashSet<String>();

    public void addNode(String node) {
        System.out.println("Add");
        // int prevsize = this.listNode.size();
        this.listNode.add(node);
        // if (prevsize == this.listNode.size())
        // return false;
        // return true;
    }

    public void removeNode(String node) {
        // int prevsize = this.listNode.size();
        this.listNode.remove(node);
        // if (prevsize == this.listNode.size())
        // return false;
        // return true;
    }

    public List<String> getListNode() {
        List<String> list = new ArrayList<String>();
        System.out.println(listNode.size());
        list.addAll(this.listNode);
        return list;
    }

    public void addEdge(String edge) {
        // int prevsize = this.listEdge.size();
        this.listEdge.add(edge);
        // if (prevsize == this.listEdge.size())
        // return false;
        // return true;
    }

    public void removeEdge(String edge) {
        // int prevsize = this.listEdge.size();
        this.listEdge.remove(edge);
        // if (prevsize == this.listEdge.size())
        // return false;
        // return true;
    }

    public List<String> getListEdge() {
        List<String> list = new ArrayList<String>();
        list.addAll(this.listEdge);
        return list;
    }
}
