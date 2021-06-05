package space;
import java.util.LinkedList;

import entity.Entity;
import terrain.Terrain;

public class Space { //space는 격자형으로 배치되며 상하좌우의 space와 연결되어 있음
	public Space up;
	public Space down;
	public Space left;
	public Space right;
	public LinkedList<Entity> entitys;
	public Terrain terrain;
	public String imagename;
	
	public Space(String imagename) {
		this.entitys=new LinkedList<Entity>();
		this.terrain=null;
		this.imagename=imagename;
	}
	public Space(boolean t) {
		this.up=this;
		this.down=this;
		this.left=this;
		this.right=this;
		this.entitys=null;
		this.terrain=null;
		this.imagename=null;
	}
	public Space[][] SpaceNearby(int x, int y) { //주변의 Space들(좌우 거리 x칸 이내 and 상하 거리 y칸 이내의 Space들)을 배열의 형태로 가져옴
		Space[][] spaces=new Space[1+2*x][1+2*y];
		spaces[x][y]=this;
		Space space=this;
		Space tempspace;
		int i,j;
		for(i=1; i<=x; i++) {
			space=space.right;
			spaces[x+i][y]=space;
			tempspace=space;
			for(j=1; j<=y; j++) {
				space=space.down;
				spaces[x+i][y+j]=space;
			}
			space=tempspace;
			for(j=1; j<=y; j++) {
				space=space.up;
				spaces[x+i][y-j]=space;
			}
			space=tempspace;
		}
		space=this;
		for(i=1; i<=x; i++) {
			space=space.left;
			spaces[x-i][y]=space;
			tempspace=space;
			for(j=1; j<=y; j++) {
				space=space.down;
				spaces[x-i][y+j]=space;
			}
			space=tempspace;
			for(j=1; j<=y; j++) {
				space=space.up;
				spaces[x-i][y-j]=space;
			}
			space=tempspace;
		}
		space=this;
		for(i=1; i<=y; i++) {
			space=space.down;
			spaces[x][y+i]=space;
			tempspace=space;
			for(j=1; j<=y; j++) {
				space=space.right;
				spaces[x+j][y+i]=space;
			}
			space=tempspace;
			for(j=1; j<=y; j++) {
				space=space.left;
				spaces[x-j][y+i]=space;
			}
			space=tempspace;
		}
		space=this;
		for(i=1; i<=y; i++) {
			space=space.up;
			spaces[x][y-i]=space;
			tempspace=space;
			for(j=1; j<=y; j++) {
				space=space.right;
				spaces[x+j][y-i]=space;
			}
			space=tempspace;
			for(j=1; j<=y; j++) {
				space=space.left;
				spaces[x-j][y-i]=space;
			}
			space=tempspace;
		}
		return spaces;
	}
	public void SendCreate() {
		
	}
}
