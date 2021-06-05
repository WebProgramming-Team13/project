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
		
		//주기적으로 몬스터가 target을 설정함
		double distance;
		Monster monster;
		Iterator<Monster> miter = Map.monsterlist.iterator(); //Iterator 선언 
		while(miter.hasNext()){//다음값이 있는지 체크
			monster=miter.next();
			if(monster.target==null) {
				distance=1600.0;//몬스터의 최대 인식 거리 = 40칸
				Space[][] spaces=monster.space.SpaceNearby(40, 40);
				Iterator<Entity> iter;
				Entity entity;
				for(int i = 0; i <= 80; i++) {
					for(int j = 0; j<= 80; j++) {
						if(spaces[i][j].entitys!=null) {
							iter = spaces[i][j].entitys.iterator(); //Iterator 선언 
							while(iter.hasNext()){//다음값이 있는지 체크
								entity=iter.next();
								if(entity.tag==0) {
									player=(Player) entity;
								}
							}
						}
					}
				}
			}
			else {
				
			}
		}
    };
}
