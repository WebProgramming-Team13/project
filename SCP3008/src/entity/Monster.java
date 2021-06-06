package entity;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import org.json.simple.JSONObject;

import space.Map;
import space.Space;
import terrain.Furniture;
import terrain.Terrain;

public class Monster extends Entity {
	public Player target;
	public int hp;
	public double speed;
	public int damage;
	//연산용 변수
	private int dsx;
	private int dsy;
	private Space target_space;
	private int right_bump_count;
	private int left_bump_count;
	private int down_bump_count;
	private int up_bump_count;
	private int attack_count;
	public double ds; //target 과의 거리^2
	
	public Monster(int type, Space space) {
		this.tag=1;
		this.id=Map.id++;
		this.x=20.0;
		this.y=20.0;
		this.angle=0.0;
		this.space=space;
		this.target=null;
		this.right_bump_count=0;
		this.left_bump_count=0;
		this.down_bump_count=0;
		this.up_bump_count=0;
		this.attack_count=0;
		int n;
		if(type==-1) n=(new Random()).nextInt(3);
		else n=type;
		switch(n) {
		case 0:
			this.imagename="monster1";
			this.hp=1000;
			this.speed=3.3;
			this.damage=-200;
			break;
		case 1:
			this.imagename="monster1";
			this.hp=1000;
			this.speed=3.0;
			this.damage=-300;
			break;
		case 2:
			this.imagename="monster1";
			this.hp=1000;
			this.speed=2.7;
			this.damage=-400;
			break;
		}
		Map.monsterlist.add(this);
		space.entitys.add(this);
	}
	public Monster(int type, Space space, boolean t) {//주변 플레이어에게 해당 객체가 생성됬음을 알리지 않는 생성자
		this.tag=1;
		this.id=Map.id++;
		this.x=20.0;
		this.y=20.0;
		this.angle=0.0;
		this.space=space;
		this.target=null;
		this.right_bump_count=0;
		this.left_bump_count=0;
		this.down_bump_count=0;
		this.up_bump_count=0;
		this.attack_count=0;
		int n;
		if(type==-1) n=(new Random()).nextInt(3);
		else n=type;
		switch(n) {
		case 0:
			this.imagename="monster1";
			this.hp=1000;
			this.speed=3.3;
			this.damage=-200;
			break;
		case 1:
			this.imagename="monster1";
			this.hp=1000;
			this.speed=3.0;
			this.damage=-300;
			break;
		case 2:
			this.imagename="monster1";
			this.hp=1000;
			this.speed=2.7;
			this.damage=-400;
			break;
		}
		Map.monsterlist.add(this);
		space.entitys.add(this);
	}
	public void SetTarget(Player player, int dsx, int dsy) {
		this.target=player;
		this.dsx=dsx;
		this.dsy=dsy;
		this.target_space=this.target.space;
	}
	@SuppressWarnings("unchecked")
	public void ChaseTarget() {
		if(this.target!=null) {
			//target을 향해 이동
			double dx=dsx+this.target.x-this.x;
			double dy=dsy+this.target.y-this.y;
			this.ds=Math.sqrt(dx*dx+dy*dy);
			this.x+=this.speed*dx/ds;
			this.y+=this.speed*dy/ds;
			//방향 설정
			if(dx<0) {//monster의 방향을 player를 향하도록 설정
				 if(dy<0) this.angle=Math.atan(dy/dx)+Math.PI*3/2;
				 else this.angle=Math.atan(dy/dx)-Math.PI/2;
			 }
			 else this.angle=Math.atan(dy/dx)+Math.PI/2;
			//충돌처리
			this.CollisionHandling();
			//공격
			attack_count++;
			if(ds<60.0 && attack_count>=20) {//공격 사거리 60.0 (=1.5칸), 공격 속도 = 0.6초에 한번
				this.target.ChangeHp(this.damage); //피해를 입힘
				attack_count=0;
			}
			if(this.target_space!=this.target.space) {
				if(target_space.right==this.target.space) dsx+=40;
				if(target_space.left==this.target.space) dsx-=40;
				if(target_space.down==this.target.space) dsy+=40;
				if(target_space.up==this.target.space) dsy-=40;
				this.target_space=this.target.space;
			}
			Iterator<Player> iter = this.space.PlayerNearby(40, 40).iterator(); //Iterator 선언 
			while(iter.hasNext()){//다음값이 있는지 체크
				JSONObject obj = new JSONObject();
				obj.put("type", "Mmv");
				obj.put("id", this.id);
				obj.put("x", this.x);
				obj.put("y", this.y);
				obj.put("agl", this.angle);
				try { iter.next().session.getBasicRemote().sendText(obj.toJSONString());
				} catch (IOException e) {e.printStackTrace();}
			}
		}
	}
	private void CollisionHandling() {//충돌처리 + 벽 공격
		double c_l2, c_dx, c_dy, c_ratio;
		Terrain terrain;
		//terrain 충돌처리 + 공간경계 충돌처리 : 알고리즘효율을 높이기 위해 코드를 반복함
		terrain=space.right.terrain;
		if(terrain!=null) {//terrain 충돌 검사
			if(x>22.0) x=22.0; //terrain 충돌 처리
			if(terrain.tag==1) {
				if(right_bump_count==20) {
					((Furniture) terrain).ChangeHp(damage); //몬스터가 벽을 부숨
					right_bump_count=0;
				}
				else right_bump_count++;
			}
		}
		else {
			right_bump_count=0;
			if(x>40.0) {//공간 경계 충돌 검사
				//공간 경계 충돌 처리
				x-=40.0;
				dsx-=40;
				this.space.entitys.remove(this);
				space=space.right;
				space.entitys.add(this);
			}
		}
		terrain=space.left.terrain;
		if(terrain!=null) {//terrain 충돌 검사
			if(x<18.0) x=18.0; //terrain 충돌 처리
			if(terrain.tag==1) {
				if(left_bump_count==20) {
					((Furniture) terrain).ChangeHp(damage); //몬스터가 벽을 부숨
					left_bump_count=0;
				}
				else left_bump_count++;
			}
		}
		else {
			left_bump_count=0;
			if(x<0.0) {//공간 경계 충돌 검사
				//공간 경계 충돌 처리
				x+=40.0;
				dsx+=40;
				this.space.entitys.remove(this);
				space=space.left;
				space.entitys.add(this);
			}
		}
		terrain=space.down.terrain;
		if(terrain!=null) {//terrain 충돌 검사
			if(y>22.0) y=22.0; //terrain 충돌 처리
			if(terrain.tag==1) {
				if(down_bump_count==20) {
					((Furniture) terrain).ChangeHp(damage); //몬스터가 벽을 부숨
					down_bump_count=0;
				}
				else down_bump_count++;
			}
		}
		else {
			down_bump_count=0;
			if(y>40.0) {//공간 경계 충돌 검사
				//공간 경계 충돌 처리
				y-=40.0;
				dsy-=40;
				this.space.entitys.remove(this);
				space=space.down;
				space.entitys.add(this);
			}
		}
		terrain=space.up.terrain;
		if(terrain!=null) {//terrain 충돌 검사
			if(y<18.0) y=18.0; //terrain 충돌 처리
			if(terrain.tag==1) {
				if(up_bump_count==20) {
					((Furniture) terrain).ChangeHp(damage); //몬스터가 벽을 부숨
					up_bump_count=0;
				}
				else up_bump_count++;
			}
		}
		else {
			up_bump_count=0;
			if(y<0.0) {//공간 경계 충돌 검사
				//공간 경계 충돌 처리
				y+=40.0;
				dsy+=40;
				this.space.entitys.remove(this);
				space=space.up;
				space.entitys.add(this);
			}
		}
		if(space.right.down.terrain!=null) { //terrain 충돌 검사
			//terrain 충돌 처리
			c_dx=40.0-x;
			c_dy=40.0-y;
			c_l2=c_dx*c_dx+c_dy*c_dy;
			if(c_l2<324.0) {
				c_ratio=18.0/Math.sqrt(c_l2);
				c_dx*=c_ratio;
				x=40.0-c_dx;
				c_dy*=c_ratio;
				y=40.0-c_dy;
			}
		}
		if(space.right.up.terrain!=null) { //terrain 충돌 검사
			//terrain 충돌 처리
			c_dx=40.0-x;
			c_l2=c_dx*c_dx+y*y;
			if(c_l2<324.0) {
				c_ratio=18.0/Math.sqrt(c_l2);
				c_dx*=c_ratio;
				x=40.0-c_dx;
				y*=c_ratio;
			}
		}
		if(space.left.down.terrain!=null) { //terrain 충돌 검사
			//terrain 충돌 처리
			c_dy=40.0-y;
			c_l2=x*x+c_dy*c_dy;
			if(c_l2<324.0) {
				c_ratio=18.0/Math.sqrt(c_l2);
				x*=c_ratio;
				c_dy*=c_ratio;
				y=40.0-c_dy;
			}
		}
		if(space.left.up.terrain!=null) { //terrain 충돌 검사
			//terrain 충돌 처리
			c_l2=x*x+y*y;
			if(c_l2<324.0) {
				c_ratio=18.0/Math.sqrt(c_l2);
				x*=c_ratio;
				y*=c_ratio;
			}
		}
		//entity 충돌처리
		Space tempspace, _space;
		Entity entity;
		Iterator<Entity> iter;
		
		_space=this.space.up.left;
		for (int i = -1; i <= 1; i++) {
			tempspace=_space;
		    for (int j = -1; j <= 1; j++) {
		    	iter = _space.entitys.iterator(); //Iterator 선언 
				while(iter.hasNext()){//다음값이 있는지 체크
					entity=iter.next();
					if(entity.tag!=2) {
						c_dx=entity.x+40.0*i-this.x;
						c_dy=entity.y+40.0*j-this.y;
						c_l2=c_dx*c_dx+c_dy*c_dy;
			    		if(c_l2<1296.0) {
			    			c_ratio=36.0/Math.sqrt(c_l2);// 자기자신은 제외하고 충돌처리수행
			    			c_dx*=c_ratio;
			    			x=entity.x+40.0*i-c_dx;
			    			c_dy*=c_ratio;
			    			y=entity.y+40.0*j-c_dy;
			    		}
		    		}
				}
				_space=_space.down;
			}
		    _space=tempspace.right;
		}
	}
}
