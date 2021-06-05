package entity;

import space.Map;
import space.Space;

public class Monster extends Entity {
	public Player target;
	public int hp;
	public double speed;
	
	public Monster(int type, Space space) {
		this.tag=1;
		this.id=Map.id++;
		this.x=20.0;
		this.y=20.0;
		this.angle=0.0;
		this.space=space;
		this.target=null;
		switch(type) {
		case 0:
			this.imagename="monster1";
			this.hp=1000;
			this.speed=3.3;
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
		switch(type) {
		case 0:
			this.imagename="monster1";
			this.hp=1000;
			this.speed=3.3;
			break;
		}
		Map.monsterlist.add(this);
		space.entitys.add(this);
	}
	public void Setspace(Space space) {
		this.space.entitys.remove(this);
		space.entitys.add(this);
		this.space=space;
	}
}
