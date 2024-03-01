package project.simplechat;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;

public class Statement {

	private User user;
	
	private String message;

	private String imgBase64;
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setMessage(String message) {
		//System.out.println(message);
		this.message = message;
	}

	public void setBase64Image(String base64Image){
		this.imgBase64 = base64Image;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public String getMessage() {
		return this.message;
	}

	public String getImgBase64() {
		return this.imgBase64;
	}
	
}
