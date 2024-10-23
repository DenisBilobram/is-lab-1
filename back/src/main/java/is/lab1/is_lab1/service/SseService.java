package is.lab1.is_lab1.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;

@Service
public class SseService {

    private final List<SseEmitter> humanEmitters = new CopyOnWriteArrayList<>();
    private final List<SseEmitter> carEmitters = new CopyOnWriteArrayList<>();
    private final List<SseEmitter> coordEmitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe(String entityType) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        List<SseEmitter> emitters = getEmitterList(entityType);
    
        synchronized (emitters) {
            emitters.add(emitter);
            System.out.println(emitters.size());
        }
    
        System.out.println("Added emmiter for " + entityType + "now it " + emitters.size());

        emitter.onCompletion(() -> {
            synchronized (emitters) {
                emitters.remove(emitter);
            }
            System.out.println("Emitter completed for " + entityType + "now it " + + emitters.size());
        });
        emitter.onTimeout(() -> {
            synchronized (emitters) {
                emitters.remove(emitter);
            }
            System.out.println("Emitter timed out for " + entityType);
        });
        emitter.onError(e -> {
            emitter.complete();
            synchronized (emitters) {
                emitters.remove(emitter);
            }
            System.out.println("An error occurred during subscription: " + e.getMessage());
        });
    
        return emitter;
    }
    
    public void notifySubscribers(String entityType, Object object) {
        List<SseEmitter> emitters = getEmitterList(entityType);
        System.out.println("Notify " + entityType + " " + emitters.size());
    
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("data")
                        .data(object)
                        .reconnectTime(0));
                System.out.println("Event sent to emitter for " + entityType + " to " + emitters.size());
            } catch (Throwable e) {
                System.out.println("Error in emitter, removing it.");
                emitters.remove(emitter);
            }
        }
    }
    

    private List<SseEmitter> getEmitterList(String entityType) {
        switch (entityType) {
            case "human-being":
                return humanEmitters;
            case "car":
                return carEmitters;
            case "coordinates":
                return coordEmitters;
            default:
                throw new IllegalArgumentException("Unknown entity type: " + entityType);
        }
    }
}