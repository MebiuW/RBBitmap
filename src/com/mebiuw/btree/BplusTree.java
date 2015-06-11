package com.mebiuw.btree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;  
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import mebiuw.rbb.fundation.rowkey.IRowkeyable;
import mebiuw.rbb.fundation.storage.FileStorage;

public class BplusTree implements IBtree {  
	 private  ReadWriteLock lock = new ReentrantReadWriteLock(); 
	 /**
	  * 用来保存路由表用的
	  */
	 public HashMap<Long,Object> hashIndex;
      
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
    /**
     * 保存这棵树
     * @return
     * @throws IOException
     */
    public boolean saveRegions(String bposition,String rposition,int dimension) throws IOException{
    	Iterator<Object> it = this.hashIndex.values().iterator();
    	while(it.hasNext()){
    		((IRowkeyable) it.next()).save();
    	}
    	FileStorage fs=new FileStorage(bposition);
    	/**
		 * b+的几个叶
		 * rowkey位置
		 * 有几个rowkey
		 * 具体数据的维度
		 * 有的序列
		 */
    	List<String> rs=new ArrayList<String>();
    	rs.add(this.order+"");
    	rs.add(rposition);
    	rs.add(this.hashIndex.keySet().size()+"");
    	rs.add(dimension+"");
    	Iterator<Long> kit = this.hashIndex.keySet().iterator();
    	while(kit.hasNext())
    		rs.add(kit.next()+"");
    	fs.writeToFile(rs);
    	
    	
    	
    	return true;
    }
  
    @Override  
    public Object get(long key) {  
    	//加一个读写锁
		
    	try{
    		this.lock.readLock().lock();
    		Object result = root.get(key);  
        return result;  
    	}
    	finally{
    		this.lock.readLock().unlock();
    	}
    }  
  
    @Override  
    public void remove(long key) {  
        root.remove(key, this);  
  
    }  
  
    @Override  
    public void insertOrUpdate(long key, Object obj) {  
    	//加一个读写锁
    	
    	try{
    		this.lock.writeLock().lock();
        root.insertOrUpdate(key, obj, this);
        if(!this.hashIndex.containsKey(key)){
        	this.hashIndex.put(key, obj);
        }
    	}
    	finally{
    		this.lock.writeLock().unlock();
    	}
  
    }  
    public void insertOrUpdate2(long key, Object obj) {  
    	//加一个读写锁
    	
    	try{
    		this.lock.writeLock().lock();
        root.insertOrUpdate(key, obj, this);
        if(!this.hashIndex.containsKey(key)){
        	this.hashIndex.put(key, obj);
        }
    	}
    	finally{
    		this.lock.writeLock().unlock();
    	}
  
    }  
      
    public BplusTree(int order){  
        if (order < 3) {  
            System.out.print("order must be greater than 2");  
            System.exit(0);  
        }  
        this.hashIndex=new HashMap<Long,Object>();
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