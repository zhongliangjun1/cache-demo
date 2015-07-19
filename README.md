# cache-demo

##guava cache

###1. 简介
guava cache 是 Google 提供的一套本地内存缓存的 java 实现，具备了缓存的加载、更新、并发保护、自动回收、状态监控等功能，够用且还算轻量。

![image](http://img.hb.aicdn.com/c1962738db9a5ff1360f766875add422afeb51cde5ea-f7Oo7r_fw658)

###2. demo

	// 数据加载器，key 对应数据未被缓存时，通过此加载器去获取数据
	CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            public String load(String key) {
                System.out.println("-------- load value for key:" + key);
                return key.toUpperCase();
            }
        };

    LoadingCache<String, String> cache = CacheBuilder.newBuilder().build(loader);  
    
    String key = "test1";
    String value = cache.getUnchecked(key);

###3. 获取数据 method 对比

**V get(K key) throws ExecutionException;**

获取 key 对应缓存数据，如果数据尚未被缓存，则通过加载器去加载数据，如果加载过程中抛出受检异常，则该方法同样会感知到。

**V getUnchecked(K key);**

获取 key 对应缓存数据，如果数据尚未被缓存，则通过加载器去加载数据，但不 care 加载过程中的受检异常。

**V getIfPresent(Object key);**

上述两种方法在数据尚未被缓存时，均会自主去通过加载器加载数据，而 getIfPresent 则只是单纯地去查看该 key 对应的数据有没有被缓存，如果没有的话，则返回为 null。

**V get(K key, Callable<? extends V> valueLoader) throws ExecutionException;**

除了通过初始化缓存时定义数据加载器，你也可以在获取方法中直接提供数据加载的方式。

###4. 更新数据 method 对比

**void refresh(K key);**

通知加载器重新去加载该 key 值对应的数据，在加载过程中仍会继续返回旧数据。

**void put(K key, V value);**

直接将数据更新到缓存，注意：value 不可为空，否则会 throw NPE。

###5. 并发控制

上述某一个 key 值对应的数据未被缓存，在数据加载器加载数据的过程中，所有获取该 key 值的线程均会堵塞，也就是说，数据只会去加载一次。

如果你担心这样做有性能问题，可以通过数据预加载等方式来进行优化， guava cache 也确实提供了诸如：Asynchronous Refresh、Bulk Operations 等实现来帮助你优化性能。

###6. 自动回收

**Maximum Size**

限制缓存 entry 数，通过 LRU （Least Recently Used 近期最少使用算法）进行数据更新剔除。

	LoadingCache<String, String> cache = CacheBuilder.newBuilder()
	.maximumSize(200)
	.build(loader);


**Time to Idle**

数据被访问多久之后即失效

	LoadingCache<String, String> cache = CacheBuilder.newBuilder()    .expireAfterAccess(2, TimeUnit.MINUTES)    .build(loader);

**Time to Live**

数据被写入缓存多久之后即失效

	LoadingCache<String, String> cache =CacheBuilder.newBuilder()    .expireAfterWrite(2, TimeUnit.MINUTES)    .build(loader);

**SoftReference**

WeakReference 在垃圾回收器线程扫描它所管辖的内存区域的过程中，一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存。

SoftReference 如果内存空间足够，垃圾回收器就不会回收它，如果内存空间不足了，就会回收这些对象的内存。

	LoadingCache<String, String> cache = CacheBuilder.newBuilder()    .softValues()    .build(loader);


###7. 状态监控

guava cache 还提供了简单的状态监控实现：hitCount、missCount、loadSuccessCount、loadExceptionCount、totalLoadTime、evictionCount。

	LoadingCache<String, String> cache = CacheBuilder.newBuilder().recordStats().build(loader);
	
	CacheStats stats = cache.stats();








