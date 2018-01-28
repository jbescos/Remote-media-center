package es.tododev.media.screen.services;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
		logger.debug("Creating screenshoot");
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
//		BufferedImage escaledImage = escaleImage(screenFullImage, widthScale, heightScale);
		return imageToByes(screenFullImage);
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
	
	private byte[] imageToByes(BufferedImage image) throws IOException {
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
			ImageIO.write(image, "jpg", baos);
			baos.flush();
			return baos.toByteArray();
		}
	}

	public void keyboardPress(int key, int event) {
		logger.debug("Key: "+key+", Event: "+event);
		int keyCode = KeyEvent.getExtendedKeyCodeForChar(key);
		if(event == DOWN) {
			robot.keyPress(keyCode);
		}else if(event == UP) {
			robot.keyRelease(keyCode);
		}else {
			throw new UnsupportedOperationException("Event "+event+" is not supported");
		}
	}
	
}
