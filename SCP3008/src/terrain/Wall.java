package terrain;

import space.Map;
import space.Space;

public class Wall extends Terrain { // Wall = 부수거나 옮길 수 없는 지형지물 
	public Wall(int type, Space space) {
		this.tag=0;
		this.id=Map.id++;
		switch(type) {
		case 0:
			this.imagename="pillar";
			break;
		}
		this.space=space;
		space.terrain=this;
	}
}
