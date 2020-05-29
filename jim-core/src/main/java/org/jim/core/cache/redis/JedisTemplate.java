package org.jim.core.cache.redis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;
/**
 * @author wchao
 * @modify 2016-08-29 增加了set(final String key, final Object value)和<T> T get(final String key,final Class<T> clazz)
 */
@SuppressWarnings({"deprecation"})
public  class JedisTemplate implements  Serializable{  
	
	private static final long serialVersionUID = 9135301078135982677L;
	private static final Logger logger = LoggerFactory.getLogger(JedisTemplate.class);
	private static volatile JedisTemplate instance = null;
	private static JedisPool jedisPool = null;
	private static volatile Jedis jedis = null;
	private static RedisConfiguration redisConfig = null;
	private JedisTemplate (){}
	
	public static JedisTemplate me() throws Exception{
		 if (instance == null) { 
	        	synchronized (JedisTemplate.class) {
					if(instance == null){
						redisConfig = RedisConfigurationFactory.parseConfiguration();
						init();
						instance = new JedisTemplate();
					}
				}
	     }
		 return instance;
	}
	
	private static final void init() throws Exception {
		
		if(redisConfig.getHost() == null) {
			logger.error("the server ip of redis  must be not null!");
			throw new Exception("the server ip of redis  must be not null!");
		}	
		
		JedisPoolConfig poolConfig = new JedisPoolConfig();

		poolConfig.setMaxTotal(redisConfig.getMaxActive());
		poolConfig.setMaxIdle(redisConfig.getMaxIdle());		
		poolConfig.setMaxWaitMillis(redisConfig.getMaxWait());
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		try {
		    if (StringUtils.isEmpty(redisConfig.getAuth())) {
                jedisPool = new JedisPool(poolConfig, redisConfig.getHost(),redisConfig.getPort(), redisConfig.getTimeout());
            } else {
                jedisPool = new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(),  redisConfig.getTimeout(),  redisConfig.getAuth());
            }
		} catch (Exception e) {
			logger.error("cann't create JedisPool for server"+redisConfig.getHost());
			throw new Exception("cann't create JedisPool for server"+redisConfig.getHost());
		}
		
}
	
	public  JedisPool getDefaultPool() {
		return jedisPool;
	}
	
	public synchronized Jedis getSingletonJedis(){
		 if (jedis == null) { 
	        	synchronized (JedisTemplate.class) {
					if(jedis == null){
						jedis = getJedis();
					}
				}
	     }
		 return jedis;
	}
	public  Jedis getJedis() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(jedis!=null){
				jedisPool.returnBrokenResource(jedis);
			}
		}finally {
		}
		if(jedis!=null){
			return jedis;
		}
		
	    int count = 0;
		do {
			jedis = jedisPool.getResource();
			count++;
		} while (jedis == null && count < redisConfig.getRetryNum());
		return jedis;
	}

	
	public  void close(Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}
	}
   
   
   abstract class Executor<T> { 
	   Jedis jedis;  
       JedisPool jedisPool; 
 
       public Executor(JedisPool jedisPool) {  
           this.jedisPool = jedisPool;  
           jedis = getJedis();  
       }  
 
       /** 
        * 回调 
        * @return 执行结果 
        */  
       abstract T execute();  
 
       /** 
        * 调用{@link #execute()}并返回执行结果 
        * 它保证在执行{@link #execute()}之后释放数据源returnResource(jedis) 
        * @return 执行结果 
        */  
       public T getResult() {  
           T result = null;  
           try {  
               result = execute();  
           } catch (Throwable e) {  
               throw new RuntimeException("Redis execute exception", e);  
           } finally {  
               if (jedis != null) {  
                   jedisPool.returnResource(jedis);  
               }  
           }  
           return result;  
       }  
   }  
   /**
    * 模糊获取所有的key
    * @return
    */
   public Set<String> keys(String likeKey){
	   return new Executor<Set<String>>(jedisPool) {  
           @Override  
           Set<String> execute() {  
        	   Set<String> keys = jedis.keys(likeKey + "*");  
               return keys; 
           }  
       }.getResult();  
   }
   /** 
    * 删除模糊匹配的key 
    * @param likeKey 模糊匹配的key 
    * @return 删除成功的条数 
    */  
   public long delKeysLike(final String likeKey) {  
       return new Executor<Long>(jedisPool) {  
 
           @Override  
           Long execute() {  
        	   Set<String> keys = jedis.keys(likeKey + "*");  
               return jedis.del(keys.toArray(new String[keys.size()]));  
           }  
       }.getResult();  
   }  
 
   /** 
    * 删除 
    * @param key 匹配的key 
    * @return 删除成功的条数 
    */  
   public Long delKey(final String key) {  
       return new Executor<Long>(jedisPool) { 
 
           @Override  
           Long execute() {  
               return jedis.del(key);  
           }  
       }.getResult();  
   }  
 
   /** 
    * 删除 
    * @param keys 匹配的key的集合 
    * @return 删除成功的条数 
    */  
   public Long delKeys(final String[] keys) {  
       return new Executor<Long>(jedisPool) {  
 
           @Override  
           Long execute() {  
        	   return jedis.del(keys);  
           }  
       }.getResult();  
   }  
 
   /** 
    * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。 
    * 在 Redis 中，带有生存时间的 key 被称为『可挥发』(volatile)的。 
    * @param key key 
    * @param expire 生命周期，单位为秒 
    * @return 1: 设置成功 0: 已经超时或key不存在 
    */  
   public Long expire(final String key, final int expire) {  
       return new Executor<Long>(jedisPool) {  
 
           @Override  
           Long execute() {  
               return jedis.expire(key, expire);  
           }  
       }.getResult();  
   }  
 
   /** 
    * 一个跨jvm的id生成器，利用了redis原子性操作的特点 
    * @param key id的key 
    * @return 返回生成的Id 
    */  
   public long makeId(final String key) {  
       return new Executor<Long>(jedisPool) {  
 
           @Override  
           Long execute() {  
               long id = jedis.incr(key);  
               if ((id + 75807) >= Long.MAX_VALUE) {  
                   // 避免溢出，重置，getSet命令之前允许incr插队，75807就是预留的插队空间  
                   jedis.getSet(key, "0");  
               }  
               return id;  
           }  
       }.getResult();  
   }
   
   
   
   
   public long decr(final String key,final long increment) {  
       return new Executor<Long>(jedisPool) {  
 
           @Override  
           Long execute() {  
        	  return jedis.decrBy(key, increment); 
           }  
       }.getResult();  
   }
 
   public long incr(final String key,final long increment) {  
       return new Executor<Long>(jedisPool) {  
 
           @Override  
           Long execute() {  
        	  return jedis.incrBy(key, increment); 
           }  
       }.getResult();  
   }  
 
   /* ======================================Strings====================================== */  
 
   /** 
    * 将字符串值 value 关联到 key 。 
    * 如果 key 已经持有其他值， setString 就覆写旧值，无视类型。 
    * 对于某个原本带有生存时间（TTL）的键来说， 当 setString 成功在这个键上执行时， 这个键原有的 TTL 将被清除。 
    * 时间复杂度：O(1) 
    * @param key key 
    * @param value string value 
    * @return 在设置操作成功完成时，才返回 OK 。 
    */  
   public String setString(final String key, final String value) {  
       return new Executor<String>(jedisPool) {  
 
           @Override  
           String execute() {  
               return jedis.set(key, value);  
           }  
       }.getResult();  
   }
   
   public boolean setString2Boolean(final String key, final String value,final int expire) {  
       return new Executor<Boolean>(jedisPool) {  
 
           @Override  
           Boolean execute() {  
                jedis.setex(key, expire, value); 
                return true;
           }  
       }.getResult();  
   }  
 
   /** 
    * 将值 value 关联到 key ，并将 key 的生存时间设为 expire (以秒为单位)。 
    * 如果 key 已经存在， 将覆写旧值。 
    * 类似于以下两个命令: 
    * SET key value 
    * EXPIRE key expire # 设置生存时间 
    * 不同之处是这个方法是一个原子性(atomic)操作，关联值和设置生存时间两个动作会在同一时间内完成，在 Redis 用作缓存时，非常实用。 
    * 时间复杂度：O(1) 
    * @param key key 
    * @param value string value 
    * @param expire 生命周期 
    * @return 设置成功时返回 OK 。当 expire 参数不合法时，返回一个错误。 
    */  
   public String setString(final String key, final String value, final int expire) {  
       return new Executor<String>(jedisPool) {  
 
           @Override  
           String execute() {  
               return jedis.setex(key, expire, value);  
           }  
       }.getResult();  
   }  
 
   /** 
    * 将 key 的值设为 value ，当且仅当 key 不存在。若给定的 key 已经存在，则 setStringIfNotExists 不做任何动作。 
    * 时间复杂度：O(1) 
    * @param key key 
    * @param value string value 
    * @return 设置成功，返回 1 。设置失败，返回 0 。 
    */  
   public Long setStringIfNotExists(final String key, final String value) {  
       return new Executor<Long>(jedisPool) {  
 
           @Override  
           Long execute() {  
               return jedis.setnx(key, value);  
           }  
       }.getResult();  
   }  
   
   public boolean setStringIfNotExists(final String key, final String value,final int timeout) {  
       return new Executor<Boolean>(jedisPool) {  
 
           @Override  
           Boolean execute() {  
        	   Boolean result = jedis.setnx(key, value)==1; 
               if(result){
            	   jedis.expire(key, timeout);
               }
              return result; 
           }  
       }.getResult();  
   }  
 
   /** 
    * 返回 key 所关联的字符串值。如果 key 不存在那么返回特殊值 nil 。 
    * 假如 key 储存的值不是字符串类型，返回一个错误，因为 getString 只能用于处理字符串值。 
    * 时间复杂度: O(1) 
    * @param key key 
    * @return 当 key 不存在时，返回 nil ，否则，返回 key 的值。如果 key 不是字符串类型，那么返回一个错误。 
    */  
   public String getString(final String key) {  
       return new Executor<String>(jedisPool) {  
 
           @Override  
           String execute() {  
               return jedis.get(key);  
           }  
       }.getResult();  
   }  
 
   /** 
    * 批量的 {@link #setString(String, String)} 
    * @param pairs 键值对数组{数组第一个元素为key，第二个元素为value} 
    * @return 操作状态的集合 
    */  
   public List<Object> batchSetString(final List<Pair<String, String>> pairs) {  
       return new Executor<List<Object>>(jedisPool) {  
 
           @Override  
           List<Object> execute() {  
               Pipeline pipeline = jedis.pipelined();  
               for (Pair<String, String> pair : pairs) {  
                   pipeline.set(pair.getKey(), pair.getValue());
               }  
               return pipeline.syncAndReturnAll();  
           }  
       }.getResult();  
   }  
   /** 
    * 批量的 {@link #setString(String, String)} 
    * @param pairs 键值对数组{数组第一个元素为key，第二个元素为value} 
    * @return 操作状态的集合 
    */  
   public List<Object> batchSetStringEx(final List<PairEx<String,String,Integer>> pairs) {  
       return new Executor<List<Object>>(jedisPool) {  
           @Override  
           List<Object> execute() {  
               Pipeline pipeline = jedis.pipelined();  
               for (PairEx<String, String,Integer> pair : pairs) {  
                   pipeline.setex(pair.getKey(),pair.getExpire(), pair.getValue());
               }  
               return pipeline.syncAndReturnAll();  
           }  
       }.getResult();  
   }  
   /** 
    * 批量的 {@link #getString(String)} 
    * @param keys key数组 
    * @return value的集合 
    */  
   public List<String> batchGetString(final String[] keys) {  
       return new Executor<List<String>>(jedisPool) {  
 
           @Override  
           List<String> execute() {  
        	   Pipeline pipeline = jedis.pipelined();  
               List<String> result = new ArrayList<String>(keys.length);  
               List<Response<String>> responses = new ArrayList<Response<String>>(keys.length);  
               for (String key : keys) {  
                   responses.add(pipeline.get(key));  
               }  
               pipeline.sync();  
               for (Response<String> resp : responses) {  
                   result.add(resp.get());  
               }  
               return result;  
           }  
       }.getResult();  
   }  
 
   /* ======================================Hashes====================================== */  
 
   /** 
    * 将哈希表 key 中的域 field 的值设为 value 。 
    * 如果 key 不存在，一个新的哈希表被创建并进行 hashSet 操作。 
    * 如果域 field 已经存在于哈希表中，旧值将被覆盖。 
    * 时间复杂度: O(1) 
    * @param key key 
    * @param field 域 
    * @param value string value 
    * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 0 。 
    */  
   public Long hashSet(final String key, final String field, final String value) {  
       return new Executor<Long>(jedisPool) {  
 
           @Override  
           Long execute() {  
               return jedis.hset(key, field, value);  
           }  
       }.getResult();  
   }  
 
   /** 
    * 将哈希表 key 中的域 field 的值设为 value 。 
    * 如果 key 不存在，一个新的哈希表被创建并进行 hashSet 操作。 
    * 如果域 field 已经存在于哈希表中，旧值将被覆盖。 
    * @param key key 
    * @param field 域 
    * @param value string value 
    * @param expire 生命周期，单位为秒 
    * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 0 。 
    */  
   public Long hashSet(final String key, final String field, final String value, final int expire) {  
       return new Executor<Long>(jedisPool) {  
 
           @Override  
           Long execute() {  
               Pipeline pipeline = jedis.pipelined();  
               Response<Long> result = pipeline.hset(key, field, value);  
               pipeline.expire(key, expire);  
               pipeline.sync();  
               return result.get();  
           }  
       }.getResult();  
   }  
 
   /** 
    * 返回哈希表 key 中给定域 field 的值。 
    * 时间复杂度:O(1) 
    * @param key key 
    * @param field 域 
    * @return 给定域的值。当给定域不存在或是给定 key 不存在时，返回 nil 。 
    */  
   public String hashGet(final String key, final String field) {  
       return new Executor<String>(jedisPool) {  
 
           @Override  
           String execute() {  
               return jedis.hget(key, field);  
           }  
       }.getResult();  
   }  
 
   /** 
    * 返回哈希表 key 中给定域 field 的值。 如果哈希表 key 存在，同时设置这个 key 的生存时间 
    * @param key key 
    * @param field 域 
    * @param expire 生命周期，单位为秒 
    * @return 给定域的值。当给定域不存在或是给定 key 不存在时，返回 nil 。 
    */  
   public String hashGet(final String key, final String field, final int expire) {  
       return new Executor<String>(jedisPool) {  
 
           @Override  
           String execute() {  
               Pipeline pipeline = jedis.pipelined();  
               Response<String> result = pipeline.hget(key, field);  
               pipeline.expire(key, expire);  
               pipeline.sync();  
               return result.get();  
           }  
       }.getResult();  
   }  
 
   /** 
    * 同时将多个 field-value (域-值)对设置到哈希表 key 中。 
    * 时间复杂度: O(N) (N为fields的数量) 
    * @param key key 
    * @param hash field-value的map 
    * @return 如果命令执行成功，返回 OK 。当 key 不是哈希表(hash)类型时，返回一个错误。 
    */  
   public String hashMultipleSet(final String key, final Map<String, String> hash) {  
       return new Executor<String>(jedisPool) {  
 
           @Override  
           String execute() {  
               return jedis.hmset(key, hash);  
           }  
       }.getResult();  
   }  
 
   /** 
    * 同时将多个 field-value (域-值)对设置到哈希表 key 中。同时设置这个 key 的生存时间 
    * @param key key 
    * @param hash field-value的map 
    * @param expire 生命周期，单位为秒 
    * @return 如果命令执行成功，返回 OK 。当 key 不是哈希表(hash)类型时，返回一个错误。 
    */  
   public String hashMultipleSet(final String key, final Map<String, String> hash, final int expire) {  
       return new Executor<String>(jedisPool) {  
 
           @Override  
           String execute() {  
               Pipeline pipeline = jedis.pipelined();  
               Response<String> result = pipeline.hmset(key, hash);  
               pipeline.expire(key, expire);  
               pipeline.sync();  
               return result.get();  
           }  
       }.getResult();  
   }  
 
   /** 
    * 返回哈希表 key 中，一个或多个给定域的值。如果给定的域不存在于哈希表，那么返回一个 nil 值。 
    * 时间复杂度: O(N) (N为fields的数量) 
    * @param key key 
    * @param fields field的数组 
    * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。 
    */  
   public List<String> hashMultipleGet(final String key, final String... fields) {  
       return new Executor<List<String>>(jedisPool) {  
 
           @Override  
           List<String> execute() {  
               return jedis.hmget(key, fields);  
           }  
       }.getResult();  
   }  
 
   /** 
    * 返回哈希表 key 中，一个或多个给定域的值。如果给定的域不存在于哈希表，那么返回一个 nil 值。 
    * 同时设置这个 key 的生存时间 
    * @param key key 
    * @param fields field的数组 
    * @param expire 生命周期，单位为秒 
    * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。 
    */  
   public List<String> hashMultipleGet(final String key, final int expire, final String... fields) {  
       return new Executor<List<String>>(jedisPool) {  
 
           @Override  
           List<String> execute() {  
               Pipeline pipeline = jedis.pipelined();  
               Response<List<String>> result = pipeline.hmget(key, fields);  
               pipeline.expire(key, expire);  
               pipeline.sync();  
               return result.get();  
           }  
       }.getResult();  
   }  
 
   /** 
    * 批量的{@link #hashMultipleSet(String, Map)}，在管道中执行 
    * @param pairs 多个hash的多个field 
    * @return 操作状态的集合 
    */  
   public List<Object> batchHashMultipleSet(final List<Pair<String, Map<String, String>>> pairs) {  
       return new Executor<List<Object>>(jedisPool) {  
 
           @Override  
           List<Object> execute() {  
               Pipeline pipeline = jedis.pipelined();  
               for (Pair<String, Map<String, String>> pair : pairs) {  
                   pipeline.hmset(pair.getKey(), pair.getValue());  
               }  
               return pipeline.syncAndReturnAll();  
           }  
       }.getResult();  
   }  
 
   /** 
    * 批量的{@link #hashMultipleSet(String, Map)}，在管道中执行 
    * @param data Map<String, Map<String, String>>格式的数据 
    * @return 操作状态的集合 
    */  
   public List<Object> batchHashMultipleSet(final Map<String, Map<String, String>> data) {  
       return new Executor<List<Object>>(jedisPool) {  
 
           @Override  
           List<Object> execute() {  
               Pipeline pipeline = jedis.pipelined();  
               for (Map.Entry<String, Map<String, String>> iter : data.entrySet()) {  
                   pipeline.hmset(iter.getKey(), iter.getValue());  
               }  
               return pipeline.syncAndReturnAll();  
           }  
       }.getResult();  
   }  
 
   /** 
    * 批量的{@link #hashMultipleGet(String, String...)}，在管道中执行 
    * @param pairs 多个hash的多个field 
    * @return 执行结果的集合 
    */  
   public List<List<String>> batchHashMultipleGet(final List<Pair<String, String[]>> pairs) {  
       return new Executor<List<List<String>>>(jedisPool) {  
 
           @Override  
           List<List<String>> execute() {  
               Pipeline pipeline = jedis.pipelined();  
               List<List<String>> result = new ArrayList<List<String>>(pairs.size());  
               List<Response<List<String>>> responses = new ArrayList<Response<List<String>>>(pairs.size());  
               for (Pair<String, String[]> pair : pairs) {  
                   responses.add(pipeline.hmget(pair.getKey(), pair.getValue()));  
               }  
               pipeline.sync();  
               for (Response<List<String>> resp : responses) {  
                   result.add(resp.get());  
               }  
               return result;  
           }  
       }.getResult();  
 
   }  
 
   /** 
    * 返回哈希表 key 中，所有的域和值。在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。 
    * 时间复杂度: O(N) 
    * @param key key 
    * @return 以列表形式返回哈希表的域和域的值。若 key 不存在，返回空列表。 
    */  
   public Map<String, String> hashGetAll(final String key) {  
       return new Executor<Map<String, String>>(jedisPool) {  
 
           @Override  
           Map<String, String> execute() {  
               return jedis.hgetAll(key);  
           }  
       }.getResult();  
   } 
   /**
    * 批量更新key的过期时间
    * @param pairDatas
    */
   public void batchSetExpire(List<PairEx<String,Void,Integer>> pairDatas){
	   if(pairDatas == null || pairDatas.size() == 0) {
           return;
       }
       new Executor<Void>(jedisPool) {  
           @Override
           Void execute() {
        	  Pipeline pipeline =  jedis.pipelined();
        	  for(PairEx<String,Void,Integer> pairEx : pairDatas){
        		  pipeline.expire(pairEx.getKey(),pairEx.getExpire());
        	  }
        	  pipeline.sync();
              return null;  
           }  
	  }.getResult();  
   }
   /** 
    * 返回哈希表 key 中，所有的域和值。在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。 
    * 同时设置这个 key 的生存时间 
    * @param key key 
    * @param expire 生命周期，单位为秒 
    * @return 以列表形式返回哈希表的域和域的值。若 key 不存在，返回空列表。 
    */  
   public Map<String, String> hashGetAll(final String key, final int expire) {  
       return new Executor<Map<String, String>>(jedisPool) {  
 
           @Override  
           Map<String, String> execute() {  
               Pipeline pipeline = jedis.pipelined();
               Response<Map<String, String>> result = pipeline.hgetAll(key);  
               pipeline.expire(key, expire);  
               pipeline.sync();  
               return result.get();  
           }  
       }.getResult();  
   }  
   /** 
    * 批量的{@link #hashGetAll(String)} 
    * @param keys key的数组 
    * @return 执行结果的集合 
    */  
   public List<Map<String, String>> batchHashGetAll(final String... keys) {  
       return new Executor<List<Map<String, String>>>(jedisPool) {  
 
           @Override  
           List<Map<String, String>> execute() {  
              Pipeline pipeline = jedis.pipelined();  
               List<Map<String, String>> result = new ArrayList<Map<String, String>>(keys.length);  
               List<Response<Map<String, String>>> responses = new ArrayList<Response<Map<String, String>>>(keys.length);  
               for (String key : keys) {  
                   responses.add(pipeline.hgetAll(key));  
               }  
               pipeline.sync();  
               for (Response<Map<String, String>> resp : responses) {  
                   result.add(resp.get());  
               }  
               return result;  
           }  
       }.getResult();  
   }  
 
   /** 
    * 批量的{@link #hashMultipleGet(String, String...)}，与{@link #batchHashGetAll(String...)}不同的是，返回值为Map类型 
    * @param keys key的数组 
    * @return 多个hash的所有filed和value 
    */  
   public Map<String, Map<String, String>> batchHashGetAllForMap(final String... keys) {  
       return new Executor<Map<String, Map<String, String>>>(jedisPool) {  
 
           @Override  
           Map<String, Map<String, String>> execute() {  
               Pipeline pipeline = jedis.pipelined();  
 
               // 设置map容量防止rehash  
               int capacity = 1;  
               while ((int) (capacity * 0.75) <= keys.length) {  
                   capacity <<= 1;  
               }  
               Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>(capacity);  
               List<Response<Map<String, String>>> responses = new ArrayList<Response<Map<String, String>>>(keys.length);  
               for (String key : keys) {  
                   responses.add(pipeline.hgetAll(key));  
               }  
               pipeline.sync();  
               for (int i = 0; i < keys.length; ++i) {  
                   result.put(keys[i], responses.get(i).get());  
               }  
               return result;  
           }  
       }.getResult();  
   } 
   
   /** 
    * 删除哈希表 key 中给定域 fields 的值。 
    * 时间复杂度:O(fields) 
    * @param key 哈希表 key 
    * @param fields 哈希表的field
    * @return 1-fields存在并成功删除，0-不存在不做任何操作 。 
    */  
   public Long hashDel(final String key, final String[] fields) {  
       return new Executor<Long>(jedisPool) { 
           @Override  
           Long execute() {  
               return jedis.hdel(key, fields);  
           }  
       }.getResult();  
   }  
 
   /* ======================================List====================================== */  
 
   /** 
    * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。 
    * @param key key 
    * @param values value的数组 
    * @return 执行 listPushTail 操作后，表的长度 
    */  
   public Long listPushTail(final String key, final String... values) {  
       return new Executor<Long>(jedisPool) {  
 
           @Override  
           Long execute() { 
        	   return jedis.rpush(key, values);  
           }  
       }.getResult();  
   }  
 
   /** 
    * 将一个或多个值 value 插入到列表 key 的表头 
    * @param key key 
    * @param value string value 
    * @return 执行 listPushHead 命令后，列表的长度。 
    */  
   public Long listPushHead(final String key, final String value) {  
       return new Executor<Long>(jedisPool) {  
 
           @Override  
           Long execute() {  
               return jedis.lpush(key, value);  
           }  
       }.getResult();  
   }
   /**
    * 从集合中删除值为value的指定元素;
    * @param key
    * @param count
    * @param value
    * @return
    */
   public Long listRemove(final String key , int count , final String value){
	   return new Executor<Long>(jedisPool) {  
		   
           @Override  
           Long execute() {  
               return jedis.lrem(key, count, value);
           }  
       }.getResult();  
   }
   /** 
    * 将一个或多个值 value 插入到列表 key 的表头, 当列表大于指定长度是就对列表进行修剪(trim) 
    * @param key key 
    * @param value string value 
    * @param size 链表超过这个长度就修剪元素 
    * @return 执行 listPushHeadAndTrim 命令后，列表的长度。 
    */  
   public Long listPushHeadAndTrim(final String key, final String value, final long size) {  
       return new Executor<Long>(jedisPool) {  
 
           @Override  
           Long execute() {  
               Pipeline pipeline = jedis.pipelined();  
               Response<Long> result = pipeline.lpush(key, value);  
               // 修剪列表元素, 如果 size - 1 比 end 下标还要大，Redis将 size 的值设置为 end 。  
               pipeline.ltrim(key, 0, size - 1);  
               pipeline.sync();  
               return result.get();  
           }  
       }.getResult();  
   }  
 
   /** 
    * 批量的{@link #listPushTail(String, String...)}，以锁的方式实现 
    * @param key key 
    * @param values value的数组 
    * @param delOld 如果key存在，是否删除它。true 删除；false: 不删除，只是在行尾追加 
    */  
   public void batchListPushTail(final String key, final String[] values, final boolean delOld) {  
       new Executor<Object>(jedisPool) {  
 
           @Override  
           Object execute() {  
               if (delOld) {  
                   RedisLock lock = new RedisLock(key, jedisPool);  
                   lock.lock();  
                   try {  
                       Pipeline pipeline = jedis.pipelined();  
                       pipeline.del(key);  
                       for (String value : values) {  
                           pipeline.rpush(key, value);  
                       }  
                       pipeline.sync();  
                   } finally {  
                       lock.unlock();  
                   }  
               } else {  
            	   Pipeline pipeline = jedis.pipelined(); 
                   for (String value : values) {  
                       pipeline.rpush(key, value);  
                   }  
                   pipeline.sync();  
               }  
               return null;  
           }  
       }.getResult();  
   }  
 
   /** 
    * 同{@link #batchListPushTail(String, String[], boolean)},不同的是利用redis的事务特性来实现 
    * @param key key 
    * @param values value的数组 
    * @return null 
    */  
   public Object updateListInTransaction(final String key, final List<String> values) {  
       return new Executor<Object>(jedisPool) {  
 
           @Override  
           Object execute() {  
               Transaction transaction = jedis.multi();  
               transaction.del(key);  
               for (String value : values) {  
                   transaction.rpush(key, value);  
               }  
               transaction.exec();  
               return null;  
           }  
       }.getResult();  
   }  
 
   /** 
    * 在key对应list的尾部部添加字符串元素,如果key存在，什么也不做 
    * @param key key 
    * @param values value的数组 
    * @return 执行insertListIfNotExists后，表的长度 
    */  
   public Long insertListIfNotExists(final String key, final String[] values) {  
       return new Executor<Long>(jedisPool) {  
 
           @Override  
           Long execute() {  
               RedisLock lock = new RedisLock(key, jedisPool);  
               lock.lock();  
               try {  
                   if (!jedis.exists(key)) {  
                       return jedis.rpush(key, values);  
                   }  
               } finally {  
                   lock.unlock();  
               }  
               return 0L;  
           }  
       }.getResult();  
   }  
 
   /** 
    * 返回list所有元素，下标从0开始，负值表示从后面计算，-1表示倒数第一个元素，key不存在返回空列表 
    * @param key key 
    * @return list所有元素 
    */  
   public List<String> listGetAll(final String key) {  
       return new Executor<List<String>>(jedisPool) {  
 
           @Override  
           List<String> execute() {  
               return jedis.lrange(key, 0, -1);  
           }  
       }.getResult();  
   }  
 
   /** 
    * 返回指定区间内的元素，下标从0开始，负值表示从后面计算，-1表示倒数第一个元素，key不存在返回空列表 
    * @param key key 
    * @param beginIndex 下标开始索引（包含） 
    * @param endIndex 下标结束索引（不包含） 
    * @return 指定区间内的元素 
    */  
   public List<String> listRange(final String key, final long beginIndex, final long endIndex) {  
       return new Executor<List<String>>(jedisPool) {  
 
           @Override  
           List<String> execute() {  
               return jedis.lrange(key, beginIndex, endIndex - 1);  
           }  
       }.getResult();  
   }  
 
   /** 
    * 一次获得多个链表的数据 
    * @param keys key的数组 
    * @return 执行结果 
    */  
   public Map<String, List<String>> batchGetAllList(final List<String> keys) {  
       return new Executor<Map<String, List<String>>>(jedisPool) {  
 
           @Override  
           Map<String, List<String>> execute() {  
               Pipeline pipeline = jedis.pipelined();  
               Map<String, List<String>> result = new HashMap<String, List<String>>();  
               List<Response<List<String>>> responses = new ArrayList<Response<List<String>>>(keys.size());  
               for (String key : keys) {  
                   responses.add(pipeline.lrange(key, 0, -1));  
               }  
               pipeline.sync();  
               for (int i = 0; i < keys.size(); ++i) {  
                   result.put(keys.get(i), responses.get(i).get());  
               }  
               return result;  
           }  
       }.getResult();  
   }
   /**
    * 往有序集合sortSet中添加数据;
    * @param key
    * @param score要排序的值
    * @param value
    * @return
    */
   public Long sortSetPush(final String key ,final double score, final String value){
	   return new Executor<Long>(jedisPool) {  
		   
           @Override  
           Long execute() {  
               return jedis.zadd(key, score, value);  
           }  
       }.getResult(); 
   }
   /**
    * 根据Score获取集合区间数据;
    * @param key
    * @param min score区间最小值
    * @param max scroe区间最大值
    * @return
    */
   public Set<String> sorSetRangeByScore(final String key, final double min, final double max) {  
       return new Executor<Set<String>>(jedisPool) {  
 
           @Override  
           Set<String> execute() {
               return jedis.zrangeByScore(key, min, max);  
           }  
       }.getResult();  
   }
   /**
    * 根据Score获取集合区间数据;
    * @param key
    * @param min score区间最小值
    * @param max scroe区间最大值
    * @param offet 偏移量（类似LIMIT 0,10）
    * @param count 数量
    * @return
    */
   public Set<String> sorSetRangeByScore(final String key, final double min, final double max , final int offset , final int count) {  
       return new Executor<Set<String>>(jedisPool) {  
 
           @Override  
           Set<String> execute() {
               return jedis.zrangeByScore(key, min, max,offset,count);  
           }  
       }.getResult();  
   }
   /* ======================================Pub/Sub====================================== */  
 
   /** 
    * 将信息 message 发送到指定的频道 channel。 
    * 时间复杂度：O(N+M)，其中 N 是频道 channel 的订阅者数量，而 M 则是使用模式订阅(subscribed patterns)的客户端的数量。 
    * @param channel 频道 
    * @param message 信息 
    * @return 接收到信息 message 的订阅者数量。 
    */  
   public Long publish(final String channel, final String message) {  
       return new Executor<Long>(jedisPool) {  
 
           @Override  
           Long execute() { 
              return jedis.publish(channel, message);  
           }  
             
       }.getResult();  
   }
   
   /** 
    * 将信息 message 发送到指定的频道 channel。 
    * 时间复杂度：O(N+M)，其中 N 是频道 channel 的订阅者数量，而 M 则是使用模式订阅(subscribed patterns)的客户端的数量。 
    * @param channel 频道 
    * @param List<message> 要发布的信息 
    */  
   public void publishAll(final String channel, final List<String> messages) {
	   if(messages == null || messages.size() == 0) {
           return;
       }
       new Executor<Void>(jedisPool) {  
           @Override  
           Void execute() {
        	  Pipeline pipeline =  jedis.pipelined();
        	  for(String message : messages){
        		  pipeline.publish(channel, message);
        	  }
        	  pipeline.sync();
              return null;  
           }  
       }.getResult();  
   } 
   /** 
    * 订阅给定的一个频道的信息。 
    * @param jedisPubSub 监听器 
    * @param channel 频道 
    */  
   public void subscribe(final JedisPubSub jedisPubSub, final String channel) {  
       new Executor<Object>(jedisPool) {  
 
           @Override  
           Object execute() {  
               jedis.subscribe(jedisPubSub, channel);  
               return null;  
           }  
       }.getResult();  
   }  
 
   /** 
    * 取消订阅 
    * @param jedisPubSub 监听器 
    */  
   public void unSubscribe(final JedisPubSub jedisPubSub) {  
       jedisPubSub.unsubscribe();  
   }  
 
   /* ======================================Sorted set================================= */  
 
   /** 
    * 将一个 member 元素及其 score 值加入到有序集 key 当中。 
    * @param key key 
    * @param score score 值可以是整数值或双精度浮点数。 
    * @param member 有序集的成员 
    * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。 
    */  
   public Long addWithSortedSet(final String key, final double score, final String member) {  
       return new Executor<Long>(jedisPool) {  
 
           @Override  
           Long execute() {  
               return jedis.zadd(key, score, member);  
           }  
       }.getResult();  
   }  
 
   /** 
    * 将多个 member 元素及其 score 值加入到有序集 key 当中。 
    * @param key key 
    * @param scoreMembers score、member的pair 
    * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。 
    */  
   public Long addWithSortedSet(final String key, final Map<String,Double> scoreMembers) {  
       return new Executor<Long>(jedisPool) { 
           @Override  
           Long execute() {  
               return jedis.zadd(key, scoreMembers);  
           }  
       }.getResult();  
   }  
 
   /** 
    * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。 
    * 有序集成员按 score 值递减(从大到小)的次序排列。 
    * @param key key 
    * @param max score最大值 
    * @param min score最小值 
    * @return 指定区间内，带有 score 值(可选)的有序集成员的列表 
    */  
   public Set<String> revrangeByScoreWithSortedSet(final String key, final double max, final double min) {  
       return new Executor<Set<String>>(jedisPool) {  
 
           @Override  
           Set<String> execute() {  
               return jedis.zrevrangeByScore(key, max, min);  
           }  
       }.getResult();  
   }  
 
   
 
   /** 
    * 构造Pair键值对 
    * @param key key 
    * @param value value 
    * @return 键值对 
    */  
   public <K, V> Pair<K, V> makePair(K key, V value) {  
       return new Pair<K, V>(key, value);  
   }  
   /** 
    * 构造Pair键值对 
    * @param key key 
    * @param value value
    * @param expire expire
    * @return 键值对 
    */  
   public <K, V , E> PairEx<K, V , E> makePairEx(K key, V value , E expire) {  
       return new PairEx<K, V , E>(key, value , expire);  
   }  
   /** 
    * 键值对 
    * @version V1.0 
    * @author fengjc 
    * @param <K> key 
    * @param <V> value 
    */  
   public class Pair<K,V> {  
 
       private K key;  
       private V value;  
 
       public Pair(K key, V value) {  
           this.key = key;  
           this.value = value;  
       }  
 
       public K getKey() {  
           return key;  
       }  
 
       public void setKey(K key) {  
           this.key = key;  
       }  
 
       public V getValue() {  
           return value;  
       }  
 
       public void setValue(V value) {  
           this.value = value;  
       }  
   }
   public class PairEx<K,V,E> extends Pair<K, V>{
	   
		private E expire;
		
		public PairEx(K key, V value) {
			super(key, value);
		}
		public PairEx(K key, V value,E expire) {
			super(key, value);
			this.expire = expire;
		}
		public E getExpire() {
			return expire;
		}
		public void setExpire(E expire) {
			this.expire = expire;
		} 
	
   }

	/**
	 * @author dave
	 * 向缓存中设置对象
	 * @param key 
	 * @param value
	 * @return
	 */
	public Object set(final String key, final Object value) {
		return new Executor<Object>(jedisPool) {
			@Override
			Object execute() {
				String objectJson = JSON.toJSONString(value);
				return jedis.set(key, objectJson);
			}
		}.getResult();
	}
	
	/**
	 * @author dave
	 * 向缓存中设置对象有有效期
	 * @param key 
	 * @param value
	 * @param expire
	 * @return
	 */
	public Object set(final String key, final Object value, final int expire) {
		return new Executor<Object>(jedisPool) {
			@Override
			Object execute() {
				String objectJson = JSON.toJSONString(value);
				return jedis.setex(key, expire, objectJson);
			}
		}.getResult();
	}

	/**
	 * @author dave
	 * 根据key 获取对象
	 * @param key
	 * @return
	 */
	public <T> T get(final String key,final Class<T> clazz) {
		return new Executor<T>(jedisPool) {
			@Override
			T execute() {
				String json = jedis.get(key);
				return JSON.parseObject(json, clazz);
			}
		}.getResult();
	}
}
