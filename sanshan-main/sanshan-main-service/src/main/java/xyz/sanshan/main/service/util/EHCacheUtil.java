package xyz.sanshan.main.service.util;


import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.util.List;
import java.util.Objects;

public class EHCacheUtil {


        private  static CacheManager cacheManager = null;

        private static Cache cache=null;

//------------------简化---------------------

        /**
         * 初始化缓存管理容器
         */
        public static CacheManager initCacheManager() {
            try {
                if (cacheManager == null)
                    cacheManager = CacheManager.getInstance();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return cacheManager;
        }

        /**
         * 初始化缓存管理容器
         *
         * @param path
         *            ehcache.xml存放的路徑
         */
        public static CacheManager initCacheManager(String path) {
            try {
                if (cacheManager == null)
                    cacheManager = CacheManager.create(path);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return cacheManager;
        }

        /**
         * 初始化cache
         * @param cacheName
         * @return
         */
        public static Cache initCache(String cacheName) {
            checkCacheManager();

            if(null==cacheManager.getCache(cacheName)){
                cacheManager.addCache(cacheName);
            }

            cache=cacheManager.getCache(cacheName);
            return cache;
        }


        /**
         * 添加缓存
         * @param key
         *            关键字
         * @param value
         *            值
         */
        public static void put(Object key,Object value) {
            checkCache();
            // 创建Element,然后放入Cache对象中
            Element element = new Element(key, value);
            cache.put(element);
        }
        /**
         * 获取cache
         *
         * @param key
         *            关键字
         * @return

         */
        public static Object get(Object key){
            checkCache();
            Element element = cache.get(key);
            if (Objects.isNull(element)) {
                return null;
            }
            return element.getObjectValue();
        }

        //------------------方便调用------------

        /**
         * 释放CacheManage
         *
         */
        public static  void shutdown() {
            cacheManager.shutdown();
        }

        /**
         * 移除cache
         * @param cacheName
         */
        public static void removeCache(String cacheName){
            checkCacheManager();
            cache=cacheManager.getCache(cacheName);
            if(null!=cache){
                cacheManager.removeCache(cacheName);
            }
        }

        /**
         * 移除所有cache
         */
        public static void removeAllCache(){
            checkCacheManager();
            cacheManager.removalAll();
        }

        /**
         * 移除所有Element
         */
        public static void removeAllKey(){
            checkCache();
            cache.removeAll();
        }

        /**
         * 获取所有的cache名称
         * @return
         */
        public static  String[]getAllCaches(){
            checkCacheManager();
            return cacheManager.getCacheNames();
        }
        /**
         * 获取Cache所有的Keys
         * @return
         */
        public  static List getKeys(){
            checkCache();
            return cache.getKeys();
        }

        /**
         * 检测cacheManager
         */
        private static void checkCacheManager(){
            if(null==cacheManager){
                throw new IllegalArgumentException("调用前请先初始化CacheManager值：EHCacheUtil.initCacheManager");
            }

        }

        private static void checkCache(){
            if(null==cache){
                throw new IllegalArgumentException("调用前请先初始化Cache值：EHCacheUtil.initCache(参数)");
            }
        }


}
