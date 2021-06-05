package main;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.JSONParser; 
import org.json.simple.parser.ParseException;

import entity.Player;
import space.Map;
import space.Section;


//@ServerEndpoint는 클라이언트에서 서버로 접속 할 주소로 지정합니다.
//@OnMessage에서는 클라이언트로 부터 메시지가 도착했을때 처리입니다.
//@OnOpen은 클라이언트에서 서버로 접속할 때의 처리입니다.
//@OnClose는 접속이 끊겼을때 처리입니다.

 
@ServerEndpoint("/server")
public class SocketServer {
    final JSONParser jsonParser = new JSONParser();
    Player player;
    @OnMessage
    public void onMessage(String data, Session session) throws IOException {
    	try {
    		JSONObject obj = (JSONObject)jsonParser.parse(data);
			String type = (String) obj.get("type");
			switch(type) {
			case "On": //0 : 플레이어 입장
				Section section=Map.CreateSection();//1개의 구역 생성
				player=new Player(session, (String) obj.get("name"), section.getspace(40, 40));//생성된 구역에 플레이어 생성
				player.MapLoad(40, 40);
				player.SetNight(Map.night);
				break;
			case "MapLoad": //플레이어 맵 로딩 요청
				Map.GetPlayer(session).MapLoad(40, 40);
				break;	
			case "mov": // 플레이어 이동
				player=Map.GetPlayer(session);
				player.Move(
						Integer.parseInt(String.valueOf(obj.get("sx"))),
						Integer.parseInt(String.valueOf(obj.get("sy"))),
						Double.parseDouble(String.valueOf(obj.get("x"))), 
						Double.parseDouble(String.valueOf(obj.get("y")))
						);
				break;
			case "": // ...
				
				break;
			}
		} catch (ParseException e) {e.printStackTrace();}
    }
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println(session);
    }
    
    @OnClose
    public void onClose(Session session) {
    	player=Map.GetPlayer(session);
    	if(player!=null) player.Delete();
    }
}