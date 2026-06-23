package com.home4paws.home4paws.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ChatSseManager {

    private static final Logger log = LoggerFactory.getLogger(ChatSseManager.class);
    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long requestId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.computeIfAbsent(requestId, k -> new CopyOnWriteArrayList<>()).add(emitter);
        emitter.onCompletion(() -> remove(requestId, emitter));
        emitter.onTimeout(()   -> remove(requestId, emitter));
        emitter.onError(e     -> remove(requestId, emitter));
        return emitter;
    }

    public void broadcast(Long requestId, Map<String, Object> message) {
        List<SseEmitter> list = emitters.getOrDefault(requestId, List.of());
        String json = toJson(message);
        for (SseEmitter emitter : list) {
            try {
                emitter.send(SseEmitter.event().name("message").data(json));
            } catch (IOException e) {
                remove(requestId, emitter);
            }
        }
    }

    private void remove(Long requestId, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(requestId);
        if (list != null) list.remove(emitter);
    }

    private String toJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> e : map.entrySet()) {
            if (!first) sb.append(',');
            first = false;
            sb.append('"').append(e.getKey()).append("\":\"")
              .append(String.valueOf(e.getValue()).replace("\"", "\\\""))
              .append('"');
        }
        sb.append('}');
        return sb.toString();
    }
}
