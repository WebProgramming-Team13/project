package main;

import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONObject;

import entity.Entity;
import entity.Monster;
import entity.Player;
import space.Map;
import space.Space;

public class Interval3sec { //3초에 한번씩 실행
	@SuppressWarnings("unchecked")
	public final static Runnable runnable = () -> {
		Player player;
		//주기적으로 몬스터가 target을 설정함
		double distance2, dx, dy, ds;
		int selected_dsx=0, selected_dsy=0;
		Iterator<Entity> iter;
		Entity entity;
		Monster monster;
		Player selected_player=null;
		Iterator<Monster> miter = Map.monsterlist.iterator(); //Iterator 선언 
		while(miter.hasNext()){//다음값이 있는지 체크
			monster=miter.next();
			
			if(monster.target==null) {
				distance2=2560000.0;//몬스터의 최대 인식 거리 = 40칸
				Space[][] spaces=monster.space.SpaceNearby(40, 40); //인식 범위와 겹치는 공간
				for(int i = 0; i <= 80; i++) {
					for(int j = 0; j<= 80; j++) {
						if(spaces[i][j].entitys!=null) {
							iter = spaces[i][j].entitys.iterator(); //Iterator 선언 
							while(iter.hasNext()){//다음값이 있는지 체크
								entity=iter.next();
								if(entity.tag==0) {
									player=(Player) entity;
									dx=(i-40)*40+player.x-monster.x;
									dy=(j-40)*40+player.y-monster.y;
									ds=dx*dx+dy*dy;
									if(ds<distance2) {
										selected_player=player;
										selected_dsx=(i-40)*40;
										selected_dsy=(j-40)*40;
										distance2=ds;
									}
								}
							}
						}
					}
				}
				if(selected_player!=null) {
					monster.SetTarget(selected_player, selected_dsx, selected_dsy);
				}
			}
			else {
				distance2=monster.ds*monster.ds;//몬스터의 최대 인식 거리 = 현재 타겟과의 거리
				int range=(int) Math.round(monster.ds);
				Space[][] spaces=monster.space.SpaceNearby(range, range);//인식 범위와 겹치는 공간
				for(int i = 0; i <= range*2; i++) {
					for(int j = 0; j<= range*2; j++) {
						if(spaces[i][j].entitys!=null) {
							iter = spaces[i][j].entitys.iterator(); //Iterator 선언 
							while(iter.hasNext()){//다음값이 있는지 체크
								entity=iter.next();
								if(entity.tag==0) {
									player=(Player) entity;
									dx=(i-range)*40+player.x-monster.x;
									dy=(j-range)*40+player.y-monster.y;
									ds=dx*dx+dy*dy;
									if(ds<distance2) {
										selected_player=player;
										selected_dsx=(i-40)*40;
										selected_dsy=(j-40)*40;
										distance2=ds;
									}
								}
							}
						}
					}
				}
				if(selected_player!=null) {
					monster.SetTarget(selected_player, selected_dsx, selected_dsy);
				}
			}
		}
    };
}
