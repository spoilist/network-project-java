package src;

import java.util.ArrayList;
import java.util.List;

public class tree<T>{
    private T data = null;
    private List<tree<T>> children = new ArrayList<>();
    private tree<T> parent = null;

    public tree(T data) {
        this.data = data;
    }

    public void addChild(tree<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(T data) {
        tree<T> newChild = new tree<>(data);
        this.addChild(newChild);
    }

    public void addChildren(List<tree<T>> children) {
        for(tree<T> t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public List<tree<T>> getChildren() {
        return children;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private void setParent(tree<T> parent) {
        this.parent = parent;
    }

    public tree<T> getParent() {
        return parent;
    }
    
    public void move(tree<T> node) {
    	this.setParent(node);
    	this.setData(node.getData());
    }
}