import image.Image;
import image.ImagePadding;

public class Main {
    public static void main(String[] args) {
        try {
            Image image = new Image("cat.jpeg");
                Image paddedImage = ImagePadding.padImage(image);
            paddedImage.saveImage("newImg");
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }
}