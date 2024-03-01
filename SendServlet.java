package project.simplechat;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import java.util.ArrayList;
import java.util.List;

import java.util.Base64;
import javax.imageio.ImageIO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import jakarta.servlet.ServletContext;
import project.simplechat.Chat;
import project.simplechat.Statement;
import project.simplechat.User;

@WebServlet("/project/simplechat/send")
@MultipartConfig
public class SendServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        ServletContext context = this.getServletContext();
        Chat chat = (Chat) context.getAttribute("chat");
        if (chat == null) {
            chat = new Chat();
            context.setAttribute("chat", chat);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            String message = request.getParameter("message");

            ServletContext context = this.getServletContext();
            Chat chat = (Chat) context.getAttribute("chat");
            Statement statement = new Statement();
            statement.setUser(user);
            statement.setMessage(message);

            // アップロードされたファイルを取得
            Part filePart = request.getPart("file_upload");

            if (filePart.getContentType() != null) {
                // アップロードされたファイルをサーバー上の一時ディレクトリに保存
                String fileName = getFileName(filePart);
                Path filePath = Files.createTempFile("uploaded-file-", "-" + fileName);

                try (InputStream input = filePart.getInputStream(); OutputStream output = Files.newOutputStream(filePath)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                }

                // BufferedImage を Base64 に変換
        		String base64Image = getBase64Image(filePath.toFile());

                // 画像処理が終わったら一時ファイルを削除する
                Files.delete(filePath);

                statement.setBase64Image(base64Image);
            }

            chat.addStatement(statement);
        }
    }

    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

	private String getBase64Image(File file) {
		try {
			BufferedImage bufferedImage = ImageIO.read(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", baos);
			byte[] imageBytes = baos.toByteArray();
			return Base64.getEncoder().encodeToString(imageBytes);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}

