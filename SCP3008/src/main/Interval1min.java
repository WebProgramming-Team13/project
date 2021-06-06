package main;

import java.util.Iterator;
import java.util.Random;

import entity.Player;
import space.Map;
import space.Section;

public class Interval1min {
	public final static Runnable runnable = () -> {
		Player player;
		Section section;
		Iterator<Player> piter = Map.playerlist.iterator(); //Iterator 선언 
		Iterator<Section> iter = Map.sectionlist.iterator(); //Iterator 선언 
        if(Map.night) {//밤에서 낮으로
        	Map.night=false;
    		while(piter.hasNext()){//다음값이 있는지 체크
    			player=piter.next();
    			player.SetNight(false);//낮으로 설정
    		}
    		while(iter.hasNext()){//다음값이 있는지 체크
        		section=iter.next();
        		section.RefillFood(6);//음식 리필
    		}
        }
        else {//낮에서 밤으로
        	Map.night=true;
        	while(piter.hasNext()){//다음값이 있는지 체크
    			player=piter.next();
    			player.SetNight(true);//밤으로 설정
    		}
        	while(iter.hasNext()){//다음값이 있는지 체크
        		section=iter.next();
        		section.RefillFurniture(0.3);//가구 리필
    		}
        }
    };
}
