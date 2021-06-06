// 기본 셋팅 
let ws;
let mainintervalId;
let canvas;	
let context;
let default_image;
let day;
let night;
let voidspace; //빈 공간
let leftward;
let rightward;
let forward;
let backward;
let spaces; 
let temp_spaces;
let map_road_ready;
let map_roading;
let start_map_loading;
let saved_x;
let saved_y;
let list_all;

// 변수이름에서 "p_"는 본인의 player를 의미함
//플레이어 START
//변수 START
let p_x;
let p_y;
let p_space_x;
let p_space_y;
let p_angle;
let p_image;
let p_name;
let p_hp;
let p_ep;
let p_speed;
//변수 END
//함수 START
function setPlayer() { //player를 설정
	p_x=20.0;
	p_y=20.0;
	p_space_x=40;
	p_space_y=40;
	p_angle=0.0;
	p_image = new Image(); p_image.src = "images/player.png";
	p_name="Player";//원래는 입력된 이름이 들어가야함
	p_hp=1000;
	p_ep=1000;
	p_speed=3.0;
}
function drawPlayer() {//player 그리기 : player는 무조건 캔버스 정 중앙에 그려짐
	context.save();
	context.translate(800,450);
	//player이름text 그리기
	context.rotate(p_angle);
	context.drawImage(p_image, -20, -20, 40, 40);
	context.restore();
	
}
//함수 END
//플레이어 END

function Space(imagename, sx, sy) { //space 객체
	this.sx=sx;
	this.sy=sy;
	this.entitylist=new Array();
	this.terrain=null;
	this.image = new Image(); this.image.src = "images/"+imagename+".png";
	
	this.draw = function(x, y) { //space 내의 물체들 그리기
		context.drawImage(this.image, x, y, 40, 40); //바닥 그리기
		if(this.terrain!=null) this.terrain.draw(x,y);//지형물 그리기
		for (let i = 0; i < this.entitylist.length; i++) {//개체 그리기
			this.entitylist[i].draw(x,y);
		}
	}
}

function Monster(space, id, x, y, angle, imagename, hp) {//monster 객체
	this.tag=1;//객체 식별 : Entity 중에서 Monster
	this.id=id;
	this.x=x;
	this.y=y;
	this.angle=Math.PI*2*Math.random();
	this.space=space;
	this.image = new Image(); this.image.src = "images/"+imagename+".png";
	this.hp=hp;
	this.speed=3.3; //딱히 쓰일 일이 없는 변수
	list_all.push(this);
	
	this.draw = function(x, y) {//monster 그리기
		context.save();
		context.translate(x+((this.x+10.5)|0)-10,y+((this.y+10.5)|0)-10); //정수값으로 그리므로 x,y를 반올림하여 사용
		context.rotate(this.angle);
		context.drawImage(this.image, -20, -20, 40, 40);
		context.restore();
	}
}

function Player(space, id, x, y, angle, hp, name) {//player 객체
	this.tag=0;//객체 식별 : Entity 중에서 Player
	this.id=id;
	this.x=x;
	this.y=y;
	this.angle=0.0;
	this.space=angle;
	this.image = new Image(); this.image.src = "images/player.png";
	this.hp=hp;
	this.speed=3.0; //딱히 쓰일 일이 없는 변수
	this.name=name;
	list_all.push(this);
	
	this.draw = function(x, y) {//player 그리기
		context.save();
		context.translate(x+((this.x+10.5)|0)-10,y+((this.y+10.5)|0)-10); //정수값으로 그리므로 x,y를 반올림하여 사용
		//player이름text 그리기
		context.rotate(this.angle);
		context.drawImage(this.image, -20, -20, 40, 40);
		context.restore();
	}
}

function Food(space, id, x, y, angle, imagename) {//Food 객체
	this.tag=2;//객체 식별 : Entity 중에서 Food
	this.id=id;
	this.x=x;
	this.y=y;
	this.angle=angle;
	this.space=space;
	this.image = new Image(); this.image.src = "images/"+imagename+".png";
	list_all.push(this);
	
	this.draw = function(x, y) {//Food 그리기
		context.save();
		context.translate(x+((this.x+10.5)|0)-10,y+((this.y+10.5)|0)-10); //정수값으로 그리므로 x,y를 반올림하여 사용
		context.rotate(this.angle);
		context.drawImage(this.image, -20, -20, 40, 40);
		context.restore();
	}
}

function Wall(space, id, imagename) { //지형 객체
	this.id=id;
	this.space=space;
	this.image = new Image(); this.image.src = "images/"+imagename+".png";
	list_all.push(this);
	
	this.draw = function(x, y) {
		context.drawImage(this.image, x, y, 40, 40);
	}
}

function Furniture(space, id, imagename, hp) { //가구 객체
	this.id=id;
	this.space=space;
	this.hp=hp;
	this.image = new Image(); this.image.src = "images/"+imagename+".png";
	list_all.push(this);
	
	this.draw = function(x, y) {
		context.drawImage(this.image, x, y, 40, 40);
	}
}

function draw() {      //캔버스에 그리기 : player기준으로 상대적인 위치를 고려하여 좌표값 설정
	context.drawImage(default_image, 0, 0, 1600, 900); //빈 공간 = 검은색
	let p_s_left=800-(((p_x+10.5)|0)-10); //정수값으로 그리므로 p_x를 반올림하여 사용
	let p_s_top=450-(((p_y+10.5)|0)-10); //정수값으로 그리므로 p_y를 반올림하여 사용
	for (let i = -21; i <= 21; i++) {
	    for (let j = -12; j <= 12; j++) {
	    	spaces[i+p_space_x][j+p_space_y].draw(p_s_left+i*40, p_s_top+j*40); //player의 화면범위와 충돌하는 space를 draw
		}
	}
	drawPlayer(); //player 그리기
	if(night) {//밤 효과(어두워짐)
		context.save();
		context.globalAlpha = 0.5;//투명도 0.5
		context.drawImage(night_image, 0, 0, 1600, 900); 
		context.restore();
	}
}

function setNum() {   // 날짜, hp보여주기
	$("#DAY").html("D - " + day);
    $("#HP").html("HP " + p_hp);
    $("#EP").html("EP " + p_ep);
}

//키누르기 : wasd 이동(그냥 w만 이용하는게 컨트롤이 쉬움)
function onkey_press() {      //키눌렀을때 
    if (event.keyCode == 87) {    //w
  	  forward = true;
    }
    else if (event.keyCode == 83) {    //s
  	  backward = true;
  	  
    }
    else if (event.keyCode == 65) {    //a
  	  leftward = true;
    }
    else if (event.keyCode == 68) {    //d
  	  rightward = true;
    }
}
function onkey_up() {      //키땠을때
	  if (event.keyCode == 87) {    //w
		  forward = false;
    }
    else if (event.keyCode == 83) {    //s
  	  backward = false;
    }
    else if (event.keyCode == 65) {    //a
  	  leftward = false;
    }
    else if (event.keyCode == 68) {    //d
  	  rightward = false;
    }
}

//아래에서 쓰일 변수들
let mx;
let my;
let dx;
let dy;
let ds;

function getMP() {//canvas기준 마우스의 좌표값 설정
	mx=event.offsetX;
	my=event.offsetY;
}

function playermove() {//player 이동
	 dx=mx-800.5;
	 dy=my-450.5;
	 if(mx<800.5) {//마우스가 가리키는 곳으로 player의 방향이 설정됨
		 if(my<450.5) p_angle=Math.atan(dy/dx)+Math.PI*3/2;
		 else p_angle=Math.atan(dy/dx)-Math.PI/2;
	 }
	 else p_angle=Math.atan(dy/dx)+Math.PI/2;
	 ds=Math.sqrt(dx*dx+dy*dy);
	 if(forward) {
		 p_x+=p_speed*dx/ds;
		 p_y+=p_speed*dy/ds;
	 }
	 else if(backward) {
		 p_x-=p_speed*dx/ds;
		 p_y-=p_speed*dy/ds;
	 }
	 else if(leftward) {
		 p_x+=p_speed*dy/ds;
		 p_y-=p_speed*dx/ds;
	 }
	 else if(rightward) {
		 p_x-=p_speed*dy/ds;
		 p_y+=p_speed*dx/ds;
	 }
}


//"개체->개체의 주변공간->그 공간 내의 개체,지형들->충돌처리"와 같은 방식으로 충돌처리를 수행
//개체의 주변공간에 존재하는 개체,지형의 개수는 유한함(서로 겹치지 못하게 충돌처리를 수행함)
//따라서 한 개체의 충돌처리의 연산량은 O(1)

//충돌처리에 사용되는 변수, 변수이름에서 "c_"는 collision을 의미함
let c_terrain;
let c_entitylist;
let c_l2;
let c_dx;
let c_dy;
let c_ratio;

function p_collision() {//player충돌처리
	//terrain 충돌처리 + 공간경계 충돌처리 : 알고리즘효율을 높이기 위해 코드를 반복함
	c_terrain = spaces[p_space_x+1][p_space_y].terrain;
	if(c_terrain!=null) { //terrain 충돌 검사
		if(p_x>22.0) p_x=22.0; //terrain 충돌 처리
	}
	else {
		if(p_x>40.0) { //공간 경계 충돌 검사
			if(p_space_x==59) { //로딩된 범위를 벗어나지 못하게 막음
				p_x=40.0;
			}
			else { //공간 경계 충돌 처리
				p_x-=40.0;
				p_space_x+=1;
				if(p_space_x==55) start_map_loading=true;
			}
		}
	}
	c_terrain = spaces[p_space_x-1][p_space_y].terrain;
	if(c_terrain!=null) { //terrain 충돌 검사
		if(p_x<18.0) p_x=18.0; //terrain 충돌 처리
	}
	else {
		if(p_x<0.0) { //공간 경계 충돌 검사
			if(p_space_x==21) { //로딩된 범위를 벗어나지 못하게 막음
				p_x=0.0;
			}
			else { //공간 경계 충돌 처리
				p_x+=40.0;
				p_space_x-=1;
				if(p_space_x==25) start_map_loading=true;
			}
		}
	}
	c_terrain = spaces[p_space_x][p_space_y+1].terrain;
	if(c_terrain!=null) { //terrain 충돌 검사
		if(p_y>22.0) p_y=22.0; //terrain 충돌 처리
	}
	else {
		if(p_y>40.0) { //공간 경계 충돌 검사
			if(p_space_y==68) { //로딩된 범위를 벗어나지 못하게 막음
				p_y=40.0;
			}
			else { //공간 경계 충돌 처리
				p_y-=40.0;
				p_space_y+=1;
				if(p_space_y==64) start_map_loading=true;
			}
		}
	}
	c_terrain = spaces[p_space_x][p_space_y-1].terrain;
	if(c_terrain!=null) { //terrain 충돌 검사
		if(p_y<18.0) p_y=18.0; //terrain 충돌 처리
	}
	else {
		if(p_y<0.0) { //공간 경계 충돌 검사
			if(p_space_y==12) { //로딩된 범위를 벗어나지 못하게 막음
				p_y=0.0;
			}
			else { //공간 경계 충돌 처리
				p_y+=40.0;
				p_space_y-=1;
				if(p_space_y==16) start_map_loading=true;
			}
		}
	}
	c_terrain = spaces[p_space_x+1][p_space_y+1].terrain;
	if(c_terrain!=null) { //terrain 충돌 검사
		//terrain 충돌 처리
		c_dx=40.0-p_x;
		c_dy=40.0-p_y;
		c_l2=c_dx*c_dx+c_dy*c_dy;
		if(c_l2<324.0) {
			c_ratio=18.0/Math.sqrt(c_l2);
			c_dx*=c_ratio;
			p_x=40.0-c_dx;
			c_dy*=c_ratio;
			p_y=40.0-c_dy;
		}
	}
	c_terrain = spaces[p_space_x+1][p_space_y-1].terrain;
	if(c_terrain!=null) { //terrain 충돌 검사
		//terrain 충돌 처리
		c_dx=40.0-p_x;
		c_l2=c_dx*c_dx+p_y*p_y;
		if(c_l2<324.0) {
			c_ratio=18.0/Math.sqrt(c_l2);
			c_dx*=c_ratio;
			p_x=40.0-c_dx;
			p_y*=c_ratio;
		}
	}
	c_terrain = spaces[p_space_x-1][p_space_y+1].terrain;
	if(c_terrain!=null) { //terrain 충돌 검사
		//terrain 충돌 처리
		c_dy=40.0-p_y;
		c_l2=p_x*p_x+c_dy*c_dy;
		if(c_l2<324.0) {
			c_ratio=18.0/Math.sqrt(c_l2);
			p_x*=c_ratio;
			c_dy*=c_ratio;
			p_y=40.0-c_dy;
		}
	}
	c_terrain = spaces[p_space_x-1][p_space_y-1].terrain;
	if(c_terrain!=null) { //terrain 충돌 검사
		//terrain 충돌 처리
		c_l2=p_x*p_x+p_y*p_y;
		if(c_l2<324.0) {
			c_ratio=18.0/Math.sqrt(c_l2);
			p_x*=c_ratio;
			p_y*=c_ratio;
		}
	}
	
	//entity 충돌처리
	for (let i = -1; i <= 1; i++) {
	    for (let j = -1; j <= 1; j++) {
	    	c_entitylist= spaces[p_space_x+i][p_space_y+j].entitylist;
	    	for (let k = 0; k < c_entitylist.length; k++) {
	    		if(c_entitylist[k].tag!=2) {
	    			c_dx=c_entitylist[k].x+40.0*i-p_x;
		    		c_dy=c_entitylist[k].y+40.0*j-p_y;
		    		c_l2=c_dx*c_dx+c_dy*c_dy;
		    		if(c_l2<1296.0) {
		    			c_ratio=36.0/Math.sqrt(c_l2);
		    			c_dx*=c_ratio;
		    			p_x=c_entitylist[k].x+40.0*i-c_dx;
		    			c_dy*=c_ratio;
		    			p_y=c_entitylist[k].y+40.0*j-c_dy;
		    		}
	    		}
	    	}
		}
	}
	ws.send(JSON.stringify({//JSON객체를 직렬화하고 보내기
        type: "mov",
        x: p_x,
        y: p_y,
        sx: p_space_x,
        sy: p_space_y,
        agl:p_angle
      }));
	
	if(start_map_loading && !map_roading) { //새로 맵 로딩 시작할지 여부
		ws.send(JSON.stringify({//JSON객체를 직렬화하고 보내기
	        type: "MapLoad"
	      }));
		map_roading=true;
		start_map_loading=false;
		//loading 시작 당시의 space좌표 저장
		saved_x=p_space_x;
		saved_y=p_space_y;
	}
}

function map_road() {//map 로드
	if(map_road_ready) {
		spaces=temp_spaces; //로드 준비가 다되면 새로운 map으로 교체
		//새로운 맵에 맞추어 공간 좌표 재설정
		p_space_x+=40-saved_x;
		p_space_y+=40-saved_y;
		map_roading=false;
		map_road_ready=false;
	}
}

function maininterval() {      // 주기적 실행 
	draw();//전부 그리기
	playermove();//player움직임
	p_collision();//player충돌처리
	map_road(); //map 로드
}

function maininterval_gameover() {      // game over 이후 주기적 실행 
	draw();//전부 그리기
}

function getbyid(id) {//특정 객체를 id로 얻음
	for (let i = 0; i < list_all.length; i++) {
		if(list_all[i].id==id) return list_all[i];
	}
	return null;
}

function onMessage(event) { //서버로부터 메세지가 왔을 때 실행될 함수
	let obj = JSON.parse(event.data);//data를 역직렬화하고 JSON객체 얻기
	let entity;
	let terrain;
	let tag;
	let space;
	let i;
	let j;
	let k;
	
	switch(obj.type) {
	case "Mapload" : //플레이어 맵로드 정보 수신
		let s;
		let s_ett;
		let s_trn;
		for (i = 0; i < 81; i++) {
		    for (j = 0; j < 81; j++) {
		    	s=obj.slist[i][j];
		    	if (s==null) space=voidspace;
		    	else {
		    		space=new Space(s.img,i,j);
			    	for (k = 0; k < s.e_c; k++) {
			    		s_ett=obj.slist[i][j].ett[k];
			    		tag=s_ett.tag;
			    		if(tag==0) entity=new Player(space,
			    				s_ett.id,
			    				s_ett.x,
			    				s_ett.y,
			    				s_ett.agl,
			    				s_ett.hp,
			    				s_ett.nam
		    			);
			    		else if(tag==1) entity=new Monster(space,
			    				s_ett.id,
			    				s_ett.x,
			    				s_ett.y,
			    				s_ett.agl,
			    				s_ett.img,
			    				s_ett.hp
			    		);
			    		else if(tag==2) entity=new Food(space,
			    				s_ett.id,
			    				s_ett.x,
			    				s_ett.y,
			    				s_ett.agl,
			    				s_ett.img
				    	);
			    		space.entitylist.push(entity);
					}
			    	s_trn=obj.slist[i][j].trn;
			    	if(s_trn!=null) {
			    		tag=s_trn.tag;
				    	if(tag==0) terrain=new Wall(space,
				    			s_trn.id,
				    			s_trn.img
				    		);
				    	else if(tag==1) terrain=new Furniture(space,
				    			s_trn.id,
				    			s_trn.img,
				    			s_trn.hp
				    	);
				    	space.terrain=terrain;
			    	}
		    	}
		    	temp_spaces[i][j]=space;
			}
		}
		map_road_ready=true;
		break;
	case "SetNight" : // 밤낮 설정
		if(obj.night) {
			night=true;
		}
		else {
			day+=1;//낮이 되는 순간 날짜 증가
			setNum(); //숫자 설정
			night=false;
		}
		break;
	case "StartML" : // 맵 로딩 시작 요청
		start_map_loading=true;
		break;
	case "Mmv" : // 몬스터 움직임
		entity=getbyid(obj.id);
		if(entity!=null) {
			space = entity.space;
			if(obj.x-entity.x<-30.0) {
				for(i = 0; i < space.entitylist.length; i++) {
					if(space.entitylist[i] == entity)  {
						space.entitylist.splice(i, 1);
					    break;
					}
				}
				if(space.sx!=80) {//몹이 맵을 벗어나면 제거
					space=spaces[space.sx+1][space.sy];
					space.entitylist.push(entity);
				}
			}
			else if(obj.x-entity.x>30.0) {
				for(i = 0; i < space.entitylist.length; i++) {
					if(space.entitylist[i] == entity)  {
						space.entitylist.splice(i, 1);
					    break;
					}
				}
				if(space.sx!=0) {//몹이 맵을 벗어나면 제거
					space=spaces[space.sx-1][space.sy];
					space.entitylist.push(entity);
				}
			}
			if(obj.y-entity.y<-30.0) {
				for(i = 0; i < space.entitylist.length; i++) {
					if(space.entitylist[i] == entity)  {
						space.entitylist.splice(i, 1);
					    break;
					}
				}
				if(space.sy!=80) {//몹이 맵을 벗어나면 제거
					space=spaces[space.sx][space.sy+1];
					space.entitylist.push(entity);
				}
			}
			else if(obj.y-entity.y>30.0) {
				for(i = 0; i < space.entitylist.length; i++) {
					if(space.entitylist[i] == entity)  {
						space.entitylist.splice(i, 1);
					    break;
					}
				}
				if(space.sy!=0) {//몹이 맵을 벗어나면 제거
					space=spaces[space.sx][space.sy-1];
					space.entitylist.push(entity);
				}
			}
			entity.x=obj.x;
			entity.y=obj.y;
			entity.angle=obj.agl;
		}
		else {//몹이 맵안으로 들어오면 몹 생성
			//...
		}
		break;
	case "ep" : //ep 설정
		p_ep=obj.val;
		setNum(); //숫자 설정
		break;
	case "hp" : // hp 설정
		p_hp=obj.val;
		setNum(); //숫자 설정
		if(p_hp==0) {
			clearInterval(mainintervalId);
			mainintervalId = setInterval(maininterval_gameover, 20);
			p_image.src = "images/none.png";
			$("#GameOver").html("You survived for");
			$("#GameOverDay").html(day+" day");
			$("#GameOver").show();
			$("#GameOverDay").show();
			canvas.removeEventListener("mousemove", getMP);
			ocument.removeEventListener("keydown", onkey_press);
			document.removeEventListener("keyup", onkey_up);
			p_image.src = "images/none.png"
			ws.close();
			//*
		}
		break;
	case "NewFood" : // 새로운 음식 생성
		space=spaces[p_space_x+obj.dsx][p_space_y+obj.dsy];
		space.entitylist.push(new Food(
				space,
				obj.id,
				obj.x,
				obj.y,
				obj.agl,
				obj.img
			));
		break;
	case "NewFnt" : // 새로운 가구 생성
		space=spaces[p_space_x+obj.dsx][p_space_y+obj.dsy];
		space.terrain=new Furniture(
				space,
				obj.id,
				obj.img,
				obj.hp
			);
		break;
	case "DelEtt" : // 엔티티 제거
		entity=getbyid(obj.id);
		for(i = 0; i < entity.space.entitylist.length; i++) {
			if(entity.space.entitylist[i] == entity)  {
				entity.space.entitylist.splice(i, 1);
			    break;
			}
		}
		break;
	case "DelTrn" : // 지형물 제거
		getbyid(obj.id).space.terrain=null;
		break;
	}
}

function start() {	// start 버튼 클릭시 이벤트 
	ws = new WebSocket("ws://localhost:8080/SCP3008/server"); //소켓 연결
	ws.onopen=function () { //연결될 시 작동
		ws.send(JSON.stringify({//JSON객체를 직렬화하고 보내기
	        type: "On",
	        name: p_name,
	      }));
	};
	ws.onmessage = function(event) { //서버로부터 메세지가 왔을 때 실행될 함수 설정
        onMessage(event)
    };
    
	$("#GameOver").hide(); // GameOver 메세지 제거
	$("#GameOverDay").hide(); // GameOver 메세지 제거
	mainintervalId = clearInterval();  // 설정 초기화 
	mainintervalId = setInterval(maininterval, 30);  // 옵션 0.03 초마다 실행 (33fps)
	document.getElementById("start").style.visibility = "hidden";// start 버튼 감추기
    canvas = document.getElementById("myCanvas");
    context = canvas.getContext("2d");	// 컨버스 가져와서 작업 시작 
    default_image = new Image(); default_image.src = "images/black.png";
    night_image= new Image(); night_image.src = "images/black.png";
    canvas.addEventListener("mousemove", getMP, false); //마우스 리스너 추가
    document.addEventListener("keydown", onkey_press, false); //w a s d 리스너 추가
    document.addEventListener("keyup", onkey_up, false); //w a s d 리스너 추가
    day = 0;//0일차부터 시작
    night = false; //밤낮 여부
    voidspace ={entitylist: new Array(), terrain: {}, draw: function(x,y) {}}; //나중에 쓸 빈공간 객체
    leftward = false; //d 키 셋팅
    rightward = false; //a 키 셋팅 
    forward = false; //w 셋팅
    backward = false; //s 셋팅
    map_road_ready=false; //맵을 로드하는지 여부
    map_roading=false; //맵을 로딩중인지 여부
    start_map_loading=false; //맵을 로딩을 시작하는지 여부
    saved_x=40; //로딩 시작시 p_space_x 값을 저장할 변수
    saved_y=40; //로딩 시작시 p_space_y 값을 저장할 변수
    list_all=new Array();//나중에 모든 객체들에 접근할 수있도록 배열을 만듦
    spaces= new Array(81); //맵을 나타내는 공간들
	for (let i = 0; i < 81; i++) {
		spaces[i] = new Array(81);
	    for (let j = 0; j < 81; j++) {
	    	spaces[i][j]=new Space("basicfloor",i,j);
		}
	}
	temp_spaces= new Array(81); //임시 맵
	for (let i = 0; i < 81; i++) {
		temp_spaces[i] = new Array(81);
	}
	
    setPlayer(); //player를 설정
    setNum(); //숫자 설정
}