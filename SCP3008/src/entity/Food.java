package entity;

import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONObject;

import space.Map;
import space.Space;

public class Food extends Entity{
	public int hp_get;
	public int ep_get;
	@SuppressWarnings("unchecked")
	public Food(int type, Space space) {
		this.tag=2;
		this.id=Map.id++;
		this.x=20.0;
		this.y=20.0;
		this.angle=0.0;
		this.space=space;
		switch(type) {
		case 0:
			this.imagename="food1";
			this.hp_get=200;
			this.ep_get=100;
			break;
		}
		space.entitys.add(this);
		
		// 새로운 음식이 생성되었으므로 주변 플레이어들에게 해당 정보 전송
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
							obj.put("type", "newFood");
							obj.put("dsx", 40-i);
							obj.put("dsy", 40-j);
							obj.put("id", this.id);
							obj.put("x", this.x);
							obj.put("y", this.y);
							obj.put("agl", this.angle);
							obj.put("img", this.imagename);
							try {player.session.getBasicRemote().sendText(obj.toJSONString());
							} catch (IOException e) {e.printStackTrace();} 
						}
					}
				}
			}
		}
	}
	public Food(int type, Space space, boolean t) {//주변 플레이어에게 해당 객체가 생성됬음을 알리지 않는 생성자
		this.tag=2;
		this.id=Map.id++;
		this.x=20.0;
		this.y=20.0;
		this.angle=0.0;
		this.space=space;
		switch(type) {
		case 0:
			this.imagename="food1";
			this.hp_get=200;
			this.ep_get=100;
			break;
		}
		space.entitys.add(this);
	}
}
