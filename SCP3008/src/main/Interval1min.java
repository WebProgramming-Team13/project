package main;

import java.util.Iterator;
import java.util.Random;

import entity.Player;
import space.Map;

public class Interval1min {
	public final static Runnable runnable = () -> {
		Player player;
		Random random = new Random();
		int rdnum=random.nextInt(1000);
		Iterator<Player> iter = Map.playerlist.iterator(); //Iterator 선언 
        if(Map.night) {
        	Map.night=false;
    		while(iter.hasNext()){//다음값이 있는지 체크
    			player=iter.next();
    			player.SetNight(false);
    		}
    		
        }
        else {
        	Map.night=true;
        	while(iter.hasNext()){//다음값이 있는지 체크
    			player=iter.next();
    			player.SetNight(true);
    		}
        	
        }
    };
}
