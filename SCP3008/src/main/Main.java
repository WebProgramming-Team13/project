package main;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Main implements ServletContextListener{
	public void contextInitialized(ServletContextEvent event){
		System.out.println("run");
	    final ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(3);
	    exec.scheduleAtFixedRate(MainInterval.runnable, 20, 20, TimeUnit.MICROSECONDS);
	    exec.scheduleAtFixedRate(Interval1sec.runnable, 1, 1, TimeUnit.SECONDS);
	    exec.scheduleAtFixedRate(Interval1min.runnable, 10, 10, TimeUnit.SECONDS); //원래는 60
	}
	public void contextDestroyed(ServletContextEvent event) { 
		
	}
}
