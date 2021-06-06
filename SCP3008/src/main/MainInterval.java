package main;

import java.util.Iterator;

import entity.Monster;
import entity.Player;
import space.Map;

public class MainInterval {
	public final static Runnable runnable = () -> {
		//모든 몬스터의 움직임
		Iterator<Monster> iter = Map.monsterlist.iterator(); //Iterator 선언 
		while(iter.hasNext()){//다음값이 있는지 체크
			iter.next().ChaseTarget();
		}
    };
}
