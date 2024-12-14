import image.Image;
import ascii_art.AsciiArtAlgorithm;

public class Main {
    private static final int DEFAULT_RESOLUTION = 2;

    public static void main(String[] args) {
        try {
            Image image = new Image("board.jpeg");
            char[] charset = new char[]{'m', 'o'};

            AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(image, charset, DEFAULT_RESOLUTION);
            char[][] asciiImage = asciiArtAlgorithm.run();

            for (char[] row : asciiImage) {
                for (char aChar : row) {
                    System.out.print(aChar + " ");
                }
                System.out.println();
            }
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }
}