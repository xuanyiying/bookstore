package com.xuanyiying.bookstore.jobshedule;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
public class ThreadPool {
	private ThreadPoolExecutor pool;
	public ThreadPool(int nThreads) {
		pool = new ThreadPoolExecutor(nThreads, 1000, 60, 
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(16));
	}
	
	public void execute(Runnable command) {
		pool.execute(command);	
	}
	public boolean isTerminated() {
		return pool.isTerminated();
	}
	public void shutdown() throws InterruptedException {
    	while(pool.getActiveCount() != 0) {
			Thread.sleep(50);
		}
    	this.shutdown();    
	}
	public int getActiveCount() {
		return pool.getActiveCount();
	}
	
}
