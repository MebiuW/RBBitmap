package com.mebiuw.btree;

import java.util.Random;  

public class BplusTree implements IBtree {  
      
    /** 根节点 */  
    protected Node root;  
      
    /** 阶数，M值 */  
    protected int order;  
      
    /** 叶子节点的链表头*/  
    protected Node head;  
      
    public Node getHead() {  
        return head;  
    }  
  
    public void setHead(Node head) {  
        this.head = head;  
    }  
  
    public Node getRoot() {  
        return root;  
    }  
  
    public void setRoot(Node root) {  
        this.root = root;  
    }  
  
    public int getOrder() {  
        return order;  
    }  
  
    public void setOrder(int order) {  
        this.order = order;  
    }  
  
    @Override  
    public Object get(long key) {  
        return root.get(key);  
    }  
  
    @Override  
    public void remove(long key) {  
        root.remove(key, this);  
  
    }  
  
    @Override  
    public void insertOrUpdate(long key, Object obj) {  
        root.insertOrUpdate(key, obj, this);  
  
    }  
      
    public BplusTree(int order){  
        if (order < 3) {  
            System.out.print("order must be greater than 2");  
            System.exit(0);  
        }  
        this.order = order;  
        root = new Node(true, true);  
        head = root;  
    }  
      
    //测试  
    public static void main(String[] args) {  
        BplusTree tree = new BplusTree(6);  
        Random random = new Random();  
        long current = System.currentTimeMillis();  
        for (int j = 0; j < 1000; j++) {  
            for (int i = 0; i < 100; i++) {  
                int randomNumber = random.nextInt(1000);  
                tree.insertOrUpdate(randomNumber, randomNumber);  
                if(randomNumber!=(Integer)tree.get(randomNumber))
                	System.out.println("Error");
            }  
  
            for (int i = 0; i < 100; i++) {  
                int randomNumber = random.nextInt(1000);  
                tree.remove(randomNumber);  
            }  
        }  
  
        long duration = System.currentTimeMillis() - current;  
        System.out.println("time elpsed for duration: " + duration);  
        int search = 10080;  
        System.out.print(tree.get(search));  
    }  
  
}  