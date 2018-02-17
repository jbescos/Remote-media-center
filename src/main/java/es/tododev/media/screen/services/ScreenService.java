package es.tododev.media.screen.services;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tododev.media.screen.dto.EventData;


@Named
@Singleton
public class ScreenService {

	private final Logger logger = LoggerFactory.getLogger(ScreenService.class);
	private final Robot robot;
	private final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	private final static int BUTTON_LEFT = 1024;
	private final static int BUTTON_MIDDLE = 2048;
	private final static int BUTTON_RIGHT = 4096;
	private final static int DOWN = 0;
	private final static int UP = 1;
	
	public ScreenService() throws AWTException{
		robot = new Robot();
	}
	
	public byte[] screenShoot() throws IOException {
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
//		BufferedImage escaledImage = escaleImage(screenFullImage, widthScale, heightScale);
		return imageToBytes(screenFullImage);
	}
	
	public void action(List<EventData> events) {
		for(EventData event : events) {
			if(event.getAction() == EventData.MOUSE_MOVE) {
				mouseMove(event.getX(), event.getY(), event.getWidth(), event.getHeight());
			}else if(event.getAction() == EventData.MOUSE_CLICK) {
				mouseClick(event.getButton(), event.getEvent());
			}else if(event.getAction() == EventData.KEY_PRESS) {
				keyboardPress(event.getButton(), event.getEvent());
			}else {
				logger.warn("Unknown action {}", event.getAction());
			}
		}
	}
	
	public String screenShootBase64() throws IOException {
		byte[] screenshoot = screenShoot();
		String encoded = Base64.getEncoder().encodeToString(screenshoot);
		return encoded;
	}
	
	public void mouseMove(int x, int y, int width, int height) {
		int escalatedX = dimension.width * x / width;
		int escalatedY = dimension.height * y / height;
		robot.mouseMove(escalatedX, escalatedY);
	}
	
	public void mouseClick(int button, int event) {
		// 1024 button 0 - left
		// 4096 button 2 - right
		// 2048 button 1 - middle
		logger.debug("Mouse button: "+button+", Event: "+event);
		int mouseButton = mapToJavaMouse(button);
		if(event == DOWN) {
			robot.mousePress(mouseButton);
		}else if(event == UP) {
			robot.mouseRelease(mouseButton);
		}else {
			throw new UnsupportedOperationException("Event "+event+" is not supported");
		}
	}
	
	private int mapToJavaMouse(int mouseButton) {
		if(mouseButton == 0) {
			return BUTTON_LEFT;
		}else if(mouseButton == 1) {
			return BUTTON_MIDDLE;
		}else if(mouseButton == 2) {
			return BUTTON_RIGHT;
		}else {
			throw new UnsupportedOperationException("Mouse button "+mouseButton+" is not supported");
		}
	}
	
	private BufferedImage escaleImage(BufferedImage screenFullImage, int widthScale, int heightScale) {
		Image img = screenFullImage.getScaledInstance(widthScale, heightScale, Image.SCALE_SMOOTH);
		BufferedImage escaledImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_BYTE_INDEXED);
		escaledImage.getGraphics().drawImage(img, 0, 0 , null);
		return escaledImage;
	}
	
	private byte[] imageToBytes(BufferedImage image) throws IOException {
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
			ImageIO.write(image, "jpg", baos);
			baos.flush();
			return baos.toByteArray();
		}
	}

	public void keyboardPress(int key, int event) {
		int mappedKey = key;
		if(key == 13) {
			mappedKey = '\n';
		}
		logger.debug("Key: {}, Event: {}", key, event);
		if(event == DOWN) {
			robot.keyPress(mappedKey);
		}else if(event == UP) {
			robot.keyRelease(mappedKey);
		}else {
			throw new UnsupportedOperationException("Event "+event+" is not supported");
		}
	}
	
}
