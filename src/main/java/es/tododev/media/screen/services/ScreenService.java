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
	
}
