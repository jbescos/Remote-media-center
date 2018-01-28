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

@Named
@Singleton
public class ScreenService {

	public byte[] screenShoot(int widthScale, int heightScale) throws AWTException, IOException {
		Robot robot = new Robot();
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
		BufferedImage escaledImage = escaleImage(screenFullImage, widthScale, heightScale);
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
