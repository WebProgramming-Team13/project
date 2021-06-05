package space;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import entity.Monster;
import entity.Player;
import javax.websocket.Session;

public class Map {
	public static int id=0; //entity, terrain의 id를 지정하는 용도로 사용
	public static boolean night=false; //밤낮 구분
	public static final Space VoidSpace = new Space(true);
	public static final Section VoidSection = new Section(true);
	public static Section headsection = null;
	public static LinkedList<Player> playerlist=new LinkedList<Player>();
	public static LinkedList<Monster> monsterlist=new LinkedList<Monster>();
	
	public static Section CreateSection() {// 맵생성
		if(headsection==null) {
			headsection=new Section(null,null,null,null);
			return headsection;
		}
		else {
			Section section=headsection, rt_section=headsection;
			Random random = new Random();
			int rdnum;
			rdnum=random.nextInt(4);
			switch(rdnum) {
			case 0:
				while(section.down.headspace!=null) {
					section=section.down;
				}
				rt_section=new Section(section,null,null,null);
				break;
			case 1:
				while(section.up.headspace!=null) {
					section=section.up;
				}
				rt_section=new Section(null,section,null,null);
				break;
			case 2:
				while(section.right.headspace!=null) {
					section=section.right;
				}
				rt_section=new Section(null,null,section,null);
				break;
			case 3:
				while(section.left.headspace!=null) {
					section=section.left;
				}
				rt_section=new Section(null,null,null,section);
				break;
			}
			return rt_section;
		}
	}
	public static Player GetPlayer(Session session) {//주어진 seesion을 가지는 player찾기
		Player player;
		Iterator<Player> iter = Map.playerlist.iterator(); //Iterator 선언 
		while(iter.hasNext()){//다음값이 있는지 체크
			player=iter.next();
			if(session==player.session) {
				return player;
			}
		}
		return null;
	}
}
