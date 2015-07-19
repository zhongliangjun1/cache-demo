/**
 * liangjun.zhong
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.vettel.demo.guavacache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author zhongliangjun1@gmail.com
 * @version $Id: LoadingCacheDemo.java, v 0.1 7/19/15 1:15 AM liangjun.zhong Exp $$
 */
public class LoadingCacheDemo {

    public static void test() {

        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            public String load(String key) {
                System.out.println("-------- load value for key:" + key);
                return key.toUpperCase();
            }
        };

        // LoadingCache<String, String> cache = CacheBuilder.newBuilder().build(loader);  // basic
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().recordStats().build(loader); // record status

        String key = "test1";
        String value;

        value = cache.getIfPresent(key);
        System.out.println("-------- getIfPresent value=" + value);
        System.out.println("---------------- status=" + cache.stats());

        value = cache.getUnchecked(key);
        System.out.println("-------- first time getUnchecked value=" + value);
        System.out.println("---------------- status=" + cache.stats());

        value = cache.getUnchecked(key);
        System.out.println("-------- second time getUnchecked value=" + value);
        System.out.println("---------------- status=" + cache.stats());


        System.out.println("---------------- asMap=" + cache.asMap());
    }

    public static void main(String[] args) {
        test();
    }

}
