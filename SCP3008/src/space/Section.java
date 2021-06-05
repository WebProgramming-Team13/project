package space;

import java.util.Random;

import entity.Food;
import entity.Monster;
import terrain.Furniture;
import terrain.Wall;

public class Section {
	public Section up;
	public Section down;
	public Section left;
	public Section right;
	public Space headspace;
	
	//구역 생성 + 구역 내부 설정 : 1개의 구역은 100*100 space들로 이루어짐
	public Section(Section upsection, Section downsection, Section leftsection, Section rightsection) {
		Random random = new Random();
		Space[][] spaces=new Space[100][100];
		Space space;
		int i,j,k,n, rdnum;
		for(i=0;i<100;i++) {
			for(j=0;j<100;j++) {
				spaces[i][j]=new Space("basicfloor");
			}
		}
		for(i=0;i<3;i++) {
			for(j=0;j<3;j++) {
				new Wall(0, spaces[i][j]);
				spaces[i][j].imagename="none";
				new Wall(0, spaces[i+97][j]);
				spaces[i+97][j].imagename="none";
				new Wall(0, spaces[i][j+97]);
				spaces[i][j+97].imagename="none";
				new Wall(0, spaces[i+97][j+97]);
				spaces[i+97][j+97].imagename="none";
			}
		}
		for(i=1;i<99;i++) {
			for(j=1;j<99;j++) {
				spaces[i][j].up=spaces[i][j-1];
				spaces[i][j].down=spaces[i][j+1];
				spaces[i][j].left=spaces[i-1][j];
				spaces[i][j].right=spaces[i+1][j];
			}
		}
		for(i=1;i<99;i++) {
			spaces[99][i].up=spaces[99][i-1];
			spaces[0][i].up=spaces[0][i-1];
			spaces[i][99].up=spaces[i][98];
			spaces[99][i].down=spaces[99][i+1];
			spaces[0][i].down=spaces[0][i+1];
			spaces[i][0].down=spaces[i][1];
			spaces[i][0].left=spaces[i-1][0];
			spaces[i][99].left=spaces[i-1][99];
			spaces[99][i].left=spaces[98][i];
			spaces[i][0].right=spaces[i+1][0];
			spaces[i][99].right=spaces[i+1][99];
			spaces[0][i].right=spaces[1][i];
		}
		spaces[0][0].down=spaces[0][1];
		spaces[0][0].right=spaces[1][0];
		spaces[0][99].up=spaces[0][98];
		spaces[0][99].right=spaces[1][99];
		spaces[99][0].down=spaces[99][1];
		spaces[99][0].left=spaces[98][0];
		spaces[99][99].up=spaces[99][98];
		spaces[99][99].left=spaces[98][99];
		
		if(upsection==null) {
			this.up=Map.VoidSection;
			for(i=0;i<100;i++) spaces[i][0].up=Map.VoidSpace;
		}
		else {
			this.up=upsection;
			space=upsection.getspace(0,99);
			for(i=0;i<100;i++) {
				spaces[i][0].up=space;
				space=space.right;
			}
		}
		if(downsection==null) {
			this.down=Map.VoidSection;
			for(i=0;i<100;i++) spaces[i][99].down=Map.VoidSpace;
		}
		else {
			this.down=downsection;
			space=downsection.getspace(0,0);
			for(i=0;i<100;i++) {
				spaces[i][99].down=space;
				space=space.right;
			}
		}
		if(leftsection==null) {
			this.left=Map.VoidSection;
			for(i=0;i<100;i++) spaces[0][i].left=Map.VoidSpace;
		}
		else {
			this.left=leftsection;
			space=leftsection.getspace(99,0);
			for(i=0;i<100;i++) {
				spaces[0][i].left=space;
				space=space.down;
			}
		}
		if(rightsection==null) {
			this.right=Map.VoidSection;
			for(i=0;i<100;i++) spaces[99][i].right=Map.VoidSpace;
		}
		else {
			this.right=rightsection;
			space=rightsection.getspace(0,0);
			for(i=0;i<100;i++) {
				spaces[99][i].right=space;
				space=space.down;
			}
		}
		this.headspace=spaces[0][0];
		for(i=0;i<100;i++) {
			for(j=0;j<100;j++) {
				spaces[i][j].SendCreate();//공간이 생성되었음을 주변플레이어들에게 전송
			}
		}
		for(i=0;i<100;i+=20) {
			for(j=0;j<100;j+=20) {
				if((i!=0 && i!=80) || (j!=0 && j!=80)) {
					for(k=1;k<19;k++) {
						for(n=1;n<19;n++) {
							if((k+n)%2==0) spaces[i+k][j+n].imagename="woodfloor1";
							else spaces[i+k][j+n].imagename="woodfloor2";
							rdnum=random.nextInt(1000);
							if(rdnum<300) new Furniture(0, spaces[i+k][j+n], "box"); //30%확률로 가구 생성, 한 구역 당 평균2041.2개의 가구 생성
							else if(rdnum<302) new Monster(spaces[i+k][j+n], "monster1"); //0.2%확률로 몹 생성, 한 구역 당 평균13.608마리의 몬스터 생성
							else if(rdnum<303) new Food(spaces[i+k][j+n], "food1"); //0.1%확률로 음식 생성
						}
					}
				}
			}
		}
		spaces=null;
	}
	// 공허 구역 생성
	public Section(boolean t) {
		this.up=this;
		this.down=this;
		this.left=this;
		this.right=this;
		this.headspace=null;
	}
	// 구역 내 특정 위치의 space를 반환
	public Space getspace(int x, int y) {
		if(x<0||x>99||y<0||y>99) return Map.VoidSpace;
		Space space=headspace;
		int i;
		for(i=0;i<x;i++) {
			space=space.right;
		}
		for(i=0;i<y;i++) {
			space=space.down;
		}
		return space;
	}
}
