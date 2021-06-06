package main;

import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONObject;

import entity.Entity;
import entity.Monster;
import entity.Player;
import space.Map;
import space.Space;

public class Interval1sec { //1초에 한번씩 실행
	@SuppressWarnings("unchecked")
	public final static Runnable runnable = () -> {
		Player player;
		
		//주기적으로 허기 또는 체력 감소
		Iterator<Player> piter = Map.playerlist.iterator(); //Iterator 선언 
		while(piter.hasNext()){//다음값이 있는지 체크
			player=piter.next();
			if(player.ep==0) player.ChangeHp(-10);
			else player.ChangeEp(-4);
		}
    };
}
