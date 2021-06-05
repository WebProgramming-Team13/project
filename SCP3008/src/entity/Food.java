package entity;

import space.Map;
import space.Space;

public class Food extends Entity{
	public Food(Space space, String imagename) {
		this.tag=2;
		this.id=Map.id++;
		this.x=20.0;
		this.y=20.0;
		this.angle=0.0;
		this.space=space;
		this.imagename=imagename;
		space.entitys.add(this);
	}
}
