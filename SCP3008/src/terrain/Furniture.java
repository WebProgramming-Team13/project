package terrain;

import space.Map;
import space.Space;

public class Furniture extends Terrain {// Furniture = 부수거나 옮길 수 있는 지형지물 (가구)
	public int hp;
	public Furniture(int type, Space space, String imagename) {
		this.tag=1;
		this.id=Map.id++;
		switch(type) {
		case 0:
			this.imagename=imagename;
			this.hp=3000;
			break;
		}
		this.space=space;
		space.terrain=this;
	}
}
