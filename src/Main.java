import image.Image;
import image.ImageDivision;
import image.ImagePadding;

public class Main {
    public static void main(String[] args) {
        try {
            Image image = new Image("board.jpeg");
                Image paddedImage = ImagePadding.padImage(image);
            paddedImage.saveImage("newImg");
            Image[][] subImages = ImageDivision.divideToImages(image, 2);
            for (int i = 0; i < subImages.length; i++) {
                for (int j = 0; j < subImages[i].length; j++) {
                    subImages[i][j].saveImage("subImg" + i + "_" + j);
                }
            }
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }
}