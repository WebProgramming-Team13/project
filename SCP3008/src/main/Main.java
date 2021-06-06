package main;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Main implements ServletContextListener{
	public void contextInitialized(ServletContextEvent event){
		System.out.println("run");
	    final ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(4);
	    exec.scheduleAtFixedRate(MainInterval.runnable, 30, 30, TimeUnit.MICROSECONDS);//33fps
	    exec.scheduleAtFixedRate(Interval1sec.runnable, 1, 1, TimeUnit.SECONDS);//1초에 한번씩 실행
	    exec.scheduleAtFixedRate(Interval3sec.runnable, 3, 3, TimeUnit.SECONDS);//3초에 한번씩 실행
	    exec.scheduleAtFixedRate(Interval1min.runnable, 10, 10, TimeUnit.SECONDS); //원래는 60
	}
	public void contextDestroyed(ServletContextEvent event) { 
		
	}
}
