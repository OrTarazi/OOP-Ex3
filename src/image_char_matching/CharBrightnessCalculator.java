package image_char_matching;

/**
 * simply gets a boolean table and calculates the brightness level describing the character.
 * @author ortar
 */
public class CharBrightnessCalculator {


    public static double calculateCharBrightness(char c){
        boolean[][] charImg = CharConverter.convertToBoolArray(c);
        int whitePixels = 0;
        int totalPixels = (charImg[0].length * charImg.length);
        for (int i = 0; i < charImg.length; i++) {
            for (int j = 0; j < charImg[i].length; j++) {
                if (charImg[i][j]) {
                    whitePixels++;
                }
            }
        }
        return (double) whitePixels / (double) totalPixels;
    }
}
