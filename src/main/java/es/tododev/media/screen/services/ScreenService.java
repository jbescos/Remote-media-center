package es.tododev.media.screen.services;

import java.awt.AWTException;
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
import org.springframework.cache.annotation.Cacheable;

import es.tododev.media.Main;

@Named
@Singleton
public class ScreenService {

	private final Logger logger = LoggerFactory.getLogger(ScreenService.class);
	
	@Cacheable(Main.SCREEN_SHOOT_CACHE)
	public byte[] screenShoot() throws IOException, AWTException {
		logger.debug("Creating screenshoot");
		Robot robot = new Robot();
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
//		BufferedImage escaledImage = escaleImage(screenFullImage, widthScale, heightScale);
		return imageToByes(screenFullImage);
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
