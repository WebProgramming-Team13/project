package entity;

import java.util.Random;

import space.Map;
import space.Space;

public class Monster extends Entity {
	public Player target;
	public int hp;
	public double speed;
	public int power;
	
	public Monster(int type, Space space) {
		this.tag=1;
		this.id=Map.id++;
		this.x=20.0;
		this.y=20.0;
		this.angle=0.0;
		this.space=space;
		this.target=null;
		int n;
		if(type==-1) n=(new Random()).nextInt(3);
		else n=type;
		switch(n) {
		case 0:
			this.imagename="monster1";
			this.hp=1000;
			this.speed=3.3;
			this.power=200;
			break;
		case 1:
			this.imagename="monster1";
			this.hp=1000;
			this.speed=3.0;
			this.power=300;
			break;
		case 2:
			this.imagename="monster1";
			this.hp=1000;
			this.speed=2.7;
			this.power=400;
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
		int n;
		if(type==-1) n=(new Random()).nextInt(3);
		else n=type;
		switch(n) {
		case 0:
			this.imagename="monster1";
			this.hp=1000;
			this.speed=3.3;
			this.power=200;
			break;
		case 1:
			this.imagename="monster1";
			this.hp=1000;
			this.speed=3.0;
			this.power=300;
			break;
		case 2:
			this.imagename="monster1";
			this.hp=1000;
			this.speed=2.7;
			this.power=400;
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
