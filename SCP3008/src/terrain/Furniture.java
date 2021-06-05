package terrain;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import org.json.simple.JSONObject;

import entity.Entity;
import entity.Player;
import space.Map;
import space.Space;

public class Furniture extends Terrain {// Furniture = 부수거나 옮길 수 있는 지형지물 (가구)
	public int hp;
	@SuppressWarnings("unchecked")
	public Furniture(int type, Space space) {
		this.tag=1;
		this.id=Map.id++;
		int n;
		if(type==-1) n=(new Random()).nextInt(3);
		else n=type;
		switch(n) {
		case 0:
			this.imagename="box";
			this.hp=2000;
			break;
		case 1:
			this.imagename="box";
			this.hp=2000;
			break;
		case 2:
			this.imagename="box";
			this.hp=2000;
			break;
		}
		this.space=space;
		space.terrain=this;
		
		// 새로운 가구가 생성되었으므로 주변 플레이어들에게 해당 정보 전송
		Space[][] spaces=this.space.SpaceNearby(40, 40);
		Iterator<Entity> iter;
		Entity entity;
		Player player;
		for(int i = 0; i <= 80; i++) {
			for(int j = 0; j<= 80; j++) {
				if(spaces[i][j].entitys!=null) {
					iter = spaces[i][j].entitys.iterator(); //Iterator 선언 
					while(iter.hasNext()){//다음값이 있는지 체크
						entity=iter.next();
						if(entity.tag==0) {
							player=(Player) entity;
							JSONObject obj = new JSONObject();
							obj.put("type", "NewFnt");
							obj.put("dsx", 40-i);
							obj.put("dsy", 40-j);
							obj.put("id", this.id);
							obj.put("img", this.imagename);
							obj.put("hp", this.hp);
							try {player.session.getBasicRemote().sendText(obj.toJSONString());
							} catch (IOException e) {e.printStackTrace();} 
						}
					}
				}
			}
		}
	}
	public Furniture(int type, Space space, boolean t) {//주변 플레이어에게 해당 객체가 생성됬음을 알리지 않는 생성자
		this.tag=1;
		this.id=Map.id++;
		int n;
		if(type==-1) n=(new Random()).nextInt(3);
		else n=type;
		switch(n) {
		case 0:
			this.imagename="box";
			this.hp=2000;
			break;
		case 1:
			this.imagename="box";
			this.hp=2000;
			break;
		case 2:
			this.imagename="box";
			this.hp=2000;
			break;
		}
		this.space=space;
		space.terrain=this;
	}
	@SuppressWarnings("unchecked")
	public void Delete() {
		// 가구가 제거되었으므로 주변 플레이어들에게 해당 정보 전송
		Iterator<Player> iter = this.space.PlayerNearby(40, 40).iterator(); //Iterator 선언 
		while(iter.hasNext()){//다음값이 있는지 체크
			JSONObject obj = new JSONObject();
			obj.put("type", "DelTrn");
			obj.put("id", this.id);
			try { iter.next().session.getBasicRemote().sendText(obj.toJSONString());
			} catch (IOException e) {e.printStackTrace();}
		}
		this.space.terrain=null;
	}
}
