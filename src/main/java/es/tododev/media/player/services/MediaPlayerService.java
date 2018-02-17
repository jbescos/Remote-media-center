package es.tododev.media.player.services;

public interface MediaPlayerService {

	void open(String path) throws Exception;
    void close() throws Exception;
    void pause();
    void play();
    void position(int value, int top);
    void stop() throws Exception;
    boolean isStarted();
	
}
