package utils;

import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static pages.BasePage.getProperty;

public class ImageInfo {
    private int height;
    private int width;

    private void readImageDimension(String fileUrl) {
        BufferedImage image = null;
        try {
            URL url = new URL("http://localhost:4502" + fileUrl);

            // This is where you'd define the proxy's host name and port.
            //SocketAddress address = new InetSocketAddress(getProperty("PROXY_HOST"), Integer.parseInt(getProperty("PROXY_PORT")));

            // Create an HTTP Proxy using the above SocketAddress.
            //Proxy proxy = new Proxy(Proxy.Type.HTTP, address);

            // Open a connection to the URL using the proxy information.
            URLConnection conn = url.openConnection();

            String login = getProperty("AEMUsername") + ":" + getProperty("AEMUsernamePassword");
            String encodedLogin = Base64.encodeBase64String( login.getBytes() );
            conn.setRequestProperty("Authorization", "Basic " + encodedLogin);

            InputStream inStream = conn.getInputStream();

            // Use the InputStream flavor of ImageIO.read() instead.
            image = ImageIO.read(inStream);

            height = image.getHeight();
            width = image.getWidth();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getImageDimension(String fileUrl) {
        readImageDimension(fileUrl);
        return this.width + "X" + this.height;
    }
    public static void main(String[] args) {
        ImageInfo imageInfo = new ImageInfo();
        Log.info("Image Dimension: " + imageInfo.getImageDimension("/content/dam/securecontent/16_9_ratio_retina_master.png"));
    }
}
