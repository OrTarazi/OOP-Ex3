package image_char_matching;


public class Main {

    /**
     * Draws a 2D boolean array as ASCII art.
     * @param arr The 2D boolean array to draw.
     */
    static void draw(boolean[][] arr) {
        for (boolean[] row : arr) {
            for (boolean pixel : row) {
                System.out.print(pixel ? ' ' : '*'); // '*' for true (black), ' ' for false (white)
            }
            System.out.println();
        }
    }
    public static void main(String[] args){
        CharBrightnessMap map = new CharBrightnessMap();
        for (int i = 65; i < 69; i++) {
            map.addChar((char)i);
        }
        map.removeChar((char)66);
        map.removeChar((char)67);
        System.out.println("test");
    }
}