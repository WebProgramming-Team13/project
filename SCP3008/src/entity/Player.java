package entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import space.Map;
import space.Space;
import terrain.Furniture;
import terrain.Terrain;

public class Player extends Entity {
	public String name;
	public Session session;
	public int ep;
	public boolean aggro;
	public int hp;
	public double speed;
	//연산용 변수
	private int sx;
	private int sy;
	
	public Player(Session session, String name, Space space) {
		this.tag=0;
		this.id=Map.id++;
		this.name=name;
		this.hp=1000;
		this.ep=1000;
		this.x=20.0;
		this.y=20.0;
		this.angle=0.0;
		this.speed=3.0;//딱히 쓰일 일이 없는 변수
		this.aggro=false;
		this.space=space;
		this.session=session;
		this.sx=40;
		this.sy=40;
		Map.playerlist.add(this);
		space.entitys.add(this);
	}
	@SuppressWarnings("unchecked")
	public void Delete() {
		// 플레이어가 제거되었으므로 주변 플레이어들에게 해당 정보 전송
		Iterator<Player> iter = this.space.PlayerNearby(40, 40).iterator(); //Iterator 선언 
		while(iter.hasNext()){//다음값이 있는지 체크
			JSONObject obj = new JSONObject();
			obj.put("type", "DelEtt");
			obj.put("id", this.id);
			try { iter.next().session.getBasicRemote().sendText(obj.toJSONString());
			} catch (IOException e) {e.printStackTrace();}
		}
		this.space.entitys.remove(this);
		Map.playerlist.remove(this);
	}
	@SuppressWarnings("unchecked")
	public void MapLoad(int x, int y) {//Map을 로딩한다.
		Space[][] spaces=this.space.SpaceNearby(x, y);
		int i,j, entity_count;
		Space space;
		Iterator<Entity> iter;
		Entity entity;
		Player player;
		Monster monster;
		Food food;
		Terrain terrain;
		Furniture furniture;
		
		JSONObject obj = new JSONObject();
		JSONArray space_list1 = new JSONArray();
		obj.put("type", "Mapload");
		for(i = 0; i <= 2*x; i++) {
			JSONArray space_list2 = new JSONArray();
			space_list1.add(space_list2);
			for(j = 0; j<= 2*y; j++) {
				space=spaces[i][j];
				JSONObject obj_space = new JSONObject();
				if(space.entitys==null) obj_space=null;//void space일 경우
				else {
					obj_space.put("img", space.imagename);
					
					JSONArray entity_list = new JSONArray();
					iter = space.entitys.iterator(); //Iterator 선언 
					entity_count=0;
					while(iter.hasNext()){//다음값이 있는지 체크
						entity_count++;
						entity=iter.next();
						JSONObject obj_entity = new JSONObject();
						obj_entity.put("id", entity.id);
						obj_entity.put("x", entity.x);
						obj_entity.put("y", entity.y);
						obj_entity.put("agl", entity.angle);
						if(entity.tag==0) {
							obj_entity.put("tag", 0);
							player=(Player) entity;
							if(player==this) {
								entity_count--;
								continue;
							}
							obj_entity.put("hp", player.hp);
							obj_entity.put("nam", player.name);
						}
						else if(entity.tag==1) {
							obj_entity.put("tag", 1);
							monster=(Monster) entity;
							obj_entity.put("img", monster.imagename);
							obj_entity.put("hp", monster.hp);
						}
						else if(entity.tag==2) {
							obj_entity.put("tag", 2);
							food=(Food) entity;
							obj_entity.put("img", food.imagename);
						}
						entity_list.add(obj_entity);
					}
					obj_space.put("e_c", entity_count);
					obj_space.put("ett", entity_list);
					
					JSONObject obj_terrain = new JSONObject();
					terrain=space.terrain;
					if(terrain!=null) {
						obj_terrain.put("id", terrain.id);
						obj_terrain.put("img", terrain.imagename);
						if(terrain.tag==0) {
							obj_terrain.put("tag", 0);
						}
						else if(terrain.tag==1) {
							obj_terrain.put("tag", 1);
							furniture=(Furniture) terrain;
							obj_terrain.put("hp", furniture.hp);
						}
					}
					else obj_terrain=null;
					obj_space.put("trn", obj_terrain);
				}
				space_list2.add(obj_space);
			}
		}
		obj.put("slist", space_list1);
		try { this.session.getBasicRemote().sendText(obj.toJSONString());
		} catch (IOException e) {e.printStackTrace();}
	}
	@SuppressWarnings("unchecked")
	public void SetNight(boolean night) { //밤낮을 설정한다.
		JSONObject obj = new JSONObject();
		obj.put("type", "SetNight");
		obj.put("night", night);
		try { this.session.getBasicRemote().sendText(obj.toJSONString());
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void Move(int sx, int sy, double x, double y) { //이동
		this.x=x;
		this.x=y;
		Space tempspace, _space;
		switch(sx-this.sx) {
		case 0:
			break;
		case 1:
			tempspace=this.space.right;
			if(tempspace.entitys!=null) {
				this.space.entitys.remove(this);
				this.space=tempspace;
				this.space.entitys.add(this);
			}
			this.sx=sx;
			break;
		case -1:
			tempspace=this.space.left;
			if(tempspace.entitys!=null) {
				this.space.entitys.remove(this);
				this.space=tempspace;
				this.space.entitys.add(this);
			}
			this.sx=sx;
			break;
		default:
			this.sx=sx;
		}
		switch(sy-this.sy) {
		case 0:
			break;
		case 1:
			tempspace=this.space.down;
			if(tempspace.entitys!=null) {
				this.space.entitys.remove(this);
				this.space=tempspace;
				this.space.entitys.add(this);
			}
			this.sy=sy;
			break;
		case -1:
			tempspace=this.space.up;
			if(tempspace.entitys!=null) {
				this.space.entitys.remove(this);
				this.space=tempspace;
				this.space.entitys.add(this);
			}
			this.sy=sy;
			break;
		default:
			this.sy=sy;
		}
		//Food 충돌처리
		Entity entity;
		Iterator<Entity> iter;
		Food food;
		double dx, dy;
		
		_space=this.space.up.left;
		for (int i = -1; i <= 1; i++) {
			tempspace=_space;
		    for (int j = -1; j <= 1; j++) {
		    	iter = _space.entitys.iterator(); //Iterator 선언 
				while(iter.hasNext()){//다음값이 있는지 체크
					entity=iter.next();
					if(entity.tag==2) {
						dx=entity.x+40.0*i-this.x;
			    		dy=entity.y+40.0*j-this.y;
			    		if(dx*dx+dy*dy<1296.0) {
			    			food=(Food) entity;
			    			this.ChangeEp(food.ep_get);
			    			this.ChangeHp(food.hp_get);
			    		}
		    		}
				}
				_space=_space.down;
			}
		    _space=tempspace.right;
		}
	}
	@SuppressWarnings("unchecked")
	public void ChangeEp(int value) {
		if(value!=0) {
			this.ep+=value;
			if(this.ep>1000) this.ep=1000;
			if(this.ep<0) this.ep=0;
			JSONObject obj = new JSONObject();
			obj.put("type", "ep");
			obj.put("val", this.ep);
			try { this.session.getBasicRemote().sendText(obj.toJSONString());
			} catch (IOException e) {e.printStackTrace();}
		}
	}
	@SuppressWarnings("unchecked")
	public void ChangeHp(int value) {
		if(value!=0) {
			this.hp+=value;
			if(this.hp>1000) this.hp=1000;
			if(this.hp<0) {
				this.hp=0;
				this.Delete();
			}
			JSONObject obj = new JSONObject();
			obj.put("type", "hp");
			obj.put("val", this.hp);
			try { this.session.getBasicRemote().sendText(obj.toJSONString());
			} catch (IOException e) {e.printStackTrace();}
		}
	}
}
