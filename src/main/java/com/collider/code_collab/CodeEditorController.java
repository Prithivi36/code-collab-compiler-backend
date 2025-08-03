package com.collider.code_collab;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class CodeEditorController {

    SimpMessagingTemplate messagingTemplate;
    private final Map<String,String > map = new HashMap<>();
    private final Map<String, List<String>> users = new HashMap<>();

    @MessageMapping("/room/{roomId}/edit")
    @SendTo("/topic/room/{roomId}")
    public CodeEditor codeSocket(@DestinationVariable String roomId, CodeEditor codeEditor) {
        map.put(roomId, codeEditor.getContent());
        return codeEditor;
    }

    @MessageMapping("/room/{roomId}/users/{userName}")
    @SendTo("/topic/room/users/{roomId}")
    public List<String> getUsers(@DestinationVariable String roomId, @DestinationVariable String userName) {
        List<String> user = users.getOrDefault(roomId,new ArrayList<>());
        if(!user.contains(userName))
            user.add(userName);
        users.put(roomId,user);
        System.out.println(users.get(roomId));
        messagingTemplate.convertAndSend("/topic/room/users/" + roomId, users.get(roomId));
        System.out.println("brodcasting...");
//        messagingTemplate.convertAndSend("/topic/room/users/" + roomId, users.get(roomId));
        return users.getOrDefault(roomId,new ArrayList<>());
    }
    @MessageMapping("room/{roomId}/del/{userId}")
    public void delete(@DestinationVariable String roomId, @DestinationVariable String userId) {
        users.get(roomId).remove(userId);
        if(users.get(roomId).isEmpty()){
            users.remove(roomId);
        }
        map.remove(userId);
        if(users.containsKey(roomId))
            messagingTemplate.convertAndSend("/topic/room/users/"+roomId, users.get(roomId));
    }
    @MessageMapping("room/{roomId}/sync")
    public void sync(@DestinationVariable String roomId) {
        String current = map.get(roomId);
        CodeEditor latest = new CodeEditor();
        latest.setContent(current);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, latest);
    }
}
