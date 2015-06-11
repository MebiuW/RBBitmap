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
import mebiuw.rbb.fundation.rowkey.simplerowkey.SimpleDataItem;
import mebiuw.rbb.fundation.storage.FileStorage;

public class LinkedBplusTree extends BplusTree  {
	
	public void sleep(){
		try {
			Thread.sleep((long) Math.log(super.hashIndex.size())/2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public LinkedBplusTree(int order) {
		super(order);
		// TODO Auto-generated constructor stub
	}

	public void insertOrUpdate(double firstKey, SimpleDataItem item) {
		// TODO Auto-generated method stub
		long id=(long) (firstKey%500);
		List<SimpleDataItem> list=(List<SimpleDataItem>) super.get(id);
		if(list==null){
			list=new ArrayList<SimpleDataItem>();
			super.insertOrUpdate(id, list);
		}
		list.add(item);
		
		
	}

	public List<SimpleDataItem>  getLinkedList(double key ) {
		// TODO Auto-generated method stub
		long id=(long) (key%500);
		return (List<SimpleDataItem>) super.get(id);
		
	}  
	
  
}  