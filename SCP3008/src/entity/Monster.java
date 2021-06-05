package entity;

import space.Map;
import space.Space;

public class Monster extends Entity {
	public Player target;
	public int hp;
	public double speed;
	
	public Monster(Space space, String imagename) {
		this.tag=1;
		this.id=Map.id++;
		this.hp=1000;
		this.x=20.0;
		this.y=20.0;
		this.angle=0.0;
		this.speed=3.3;
		this.space=space;
		this.target=null;
		this.imagename=imagename;
		Map.monsterlist.add(this);
		space.entitys.add(this);
	}
	public void Setspace(Space space) {
		this.space.entitys.remove(this);
		space.entitys.add(this);
		this.space=space;
	}
}
