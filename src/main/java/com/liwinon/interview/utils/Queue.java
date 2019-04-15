package com.liwinon.interview.utils;


/**
 * 	循环队列 , 维护面试排队秩序. 
 * @author XiongJL
 *	笔记---数据结构(一)
 */
public class Queue {
	private static int MAXLen = 150;  //队列长度   实际是149个,牺牲一个位置做判断
	
	private int front ; //指向队头
	
	private int rear;  //指向队尾的下一个位置。

	private  int num; //当前元素数量
	private Object queueList[];
	
	public Object[] getQueueList() {
		return queueList;
	}
	public Queue() {
		this(MAXLen);
	}
	public Queue(int MaxLen) {
		queueList = new Object[MaxLen];
	}
	/**判空 */
	public boolean isEmpty() {   
		if(front == rear) {
			return true;
		}
		return false;
	}
	/**判满 */
	public boolean isFull() {  
		if(front==(rear+1)%queueList.length) { // 0 == (2+1) % 3==0			
			System.out.println("队列已满");
			return true;
		}
		return false;
	}
	/**添加元素*/
	public boolean enqueue(Object obj) {
		if(isFull())
			return false;
		queueList[rear]=obj;
		rear = (rear+1)%queueList.length;
		num ++;
		return true;
	}
	/**取出头部元素,并出队   注意非空判断*/
	public Object dequeue() {
		if(isEmpty()){
			return null;
		}	
		Object o = queueList[front];
		//queueList[front] =null;
        front=(front+1)%queueList.length;
        num--;
        return o;
	}
	/**取出第一个元素,不出队*/
	public Object getFirst() {
		if(isEmpty()){
			return null;
		}	
		return queueList[front];
	}
	/**获得队列已有元素总数*/
	public int getNum() {
		return num;
	}
	/**销毁队列*/
	public Queue DestroyQueue() {
		queueList = null;
        rear = front = 0;
        return this;
    }
	/**获取指定元素的前面元素总数*/
	public int getYourFront(Object obj) {
		int index = getYourIndex(obj);
		if(index==-1) {
			return 0;
		}
		int numbers = (index-this.front+Queue.MAXLen)% Queue.MAXLen;
		return numbers;	
	}
	/**获取指点元素的下标**/
	public int getYourIndex(Object obj) {
		int index=getIndex(queueList,obj);
		return index;	
	}
	private static int getIndex(Object[] arr, Object value) {
       if(value==null) {
    	   return -1;
       }
       System.out.println(value.toString());
		for (int i = 0; i < arr.length; i++) {
			//对象要重写equals方法.
            if (arr[i].equals(value)) { 
                return i;
            }
        }
        return -1;//如果未找到返回-1
    }
}
