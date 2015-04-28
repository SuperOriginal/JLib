package io.ibj.JLib.cmd2;

import java.util.HashMap;
import java.util.Map;

/**
 * @author joe 2/17/2015
 * Represents a tree node that can contain a object of underlying objects. Can have one parent and multiple children,
 * all organized by an object as a key. 
 */
public class TreeNode<K extends Object, O extends Object> {
    
    protected TreeNode(TreeNode<K, O> parent, K identifier, O object){
        this.parent = parent;
        this.identifier = identifier;
        this.object = object;
    }
    
    private TreeNode<K, O> parent;
    
    private K identifier;
    
    protected Map<K,TreeNode<K, O>> childMap = new HashMap<>();
    
    protected O object;
    
    
    public O get(){
        return object;
    }
    
    public K getIdentifier(){
        return identifier;
    }
    
    public TreeNode<K, O> getParent(){
        return parent;
    }
    
    public Map<K,TreeNode<K, O>> getChildMap(){
        return childMap;
    }
    
    public TreeNode<K, O> getChildByKey(K key)
    {
        return childMap.get(key);
    }
    
    private void setParent(TreeNode<K, O> parent){
        this.parent = parent;
    }
    
    public TreeNode<K, O> createChild(K key, O childObject){
        if(childMap.containsKey(key)){
            throw new RuntimeException("Tree already contains key: Adding duplicates are not permitted!");
        }
        TreeNode<K, O> child = new TreeNode<>(this,key,childObject);
        this.childMap.put(key,child);
        return child;
    }
    
    public static <K extends Object, O extends Object> TreeNode<K,O> createRootNode(O childrenObject){
        return new TreeNode<>(null,null,childrenObject);
    }
}
