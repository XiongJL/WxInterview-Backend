package com.liwinon.interview.utils;

/**   
 * <p>Description: 缓存DTO</p>  
 * @author zhanglp
 */  
public class Cache {   
        private String key;//缓存ID   
        private Object value;//缓存数据   
        private boolean isForever = false;// 是否持久 默认不是
		private long timeOut;//更新时间   
        private boolean expired; //是否终止   
        public Cache() {   
                super();   
        }   
  
        public Cache(String key, Object value, long timeOut, boolean expired) {   
                this.key = key;   
                this.value = value;   
                this.timeOut = timeOut;   
                this.expired = expired;   
        }   
        public Cache(String key, Object value, long timeOut, boolean expired,boolean isForever) {   
            this.key = key;   
            this.value = value;   
            this.timeOut = timeOut;   
            this.expired = expired;   
            this.isForever = isForever;
    } 

        public boolean isForever() {
			return isForever;
		}

		public void setForever(boolean isForever) {
			this.isForever = isForever;
		}
        public String getKey() {   
                return key;   
        }   
  
        public long getTimeOut() {   
                return timeOut;   
        }   
  
        public Object getValue() {   
                return value;   
        }   
  
        public void setKey(String string) {   
                key = string;   
        }   
  
        public void setTimeOut(long l) {   
                timeOut = l;   
        }   
  
        public void setValue(Object object) {   
                value = object;   
        }   
  
        public boolean isExpired() {   
                return expired;   
        }   
  
        public void setExpired(boolean b) {   
                expired = b;   
        }   
}