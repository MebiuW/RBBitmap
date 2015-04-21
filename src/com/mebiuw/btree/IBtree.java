package com.mebiuw.btree;

public interface IBtree {  
    public Object get(long key);   //查询  
    public void remove(long key);    //移除  
    public void insertOrUpdate(long key, Object obj); //插入或者更新，如果已经存在，就更新，否则插入  
} 