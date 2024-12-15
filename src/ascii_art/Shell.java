package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import image.Image;
import image_char_matching.SubImgCharMatcher;

public class Shell {
    // Default parameters
    private static final int DEFAULT_RESOLUTION = 2;
    private static final char DEFAULT_FIRST_CHAR = '0';
    private static final char DEFAULT_LAST_CHAR = '9';
    private static final int MIN_CHARSET_SIZE = 2; // Min size for ascii art

    // Input Messages
    private static final String VIEW_CHARS = "chars";
    private static final String ADD_CHAR = "add";
    private static final String REMOVE_CHAR = "remove";
    private static final String CHANGE_RESOLUTION = "res";
    private static final String CHANGE_OUTPUT = "output";
    private static final String CHANGE_ROUND = "round";
    private static final String ASCII_ART = "asciiArt";
    private static final String EXIT_COMMAND_MESSAGE = "EXIT";
    private static final String ENTER_COMMAND_MESSAGE = ">>> ";
    private static final String INVALID_COMMAND_MESSAGE = "Did not execute due to incorrect command.";
    private static final String INVALID_CHARSET_SIZE_MESSAGE = "Did not execute. Charset is too small.";

    private Image image;
    private SubImgCharMatcher charMatcher;
    private int resolution;

    public Shell() {
        this.resolution = DEFAULT_RESOLUTION;

        // Init default chars set
        char[] charset = new char[DEFAULT_LAST_CHAR - DEFAULT_FIRST_CHAR + 1];
        for (int charIndex = DEFAULT_FIRST_CHAR; charIndex <= DEFAULT_LAST_CHAR; charIndex++) {
            charset[charIndex - DEFAULT_FIRST_CHAR] = (char) charIndex;
        }
        this.charMatcher = new SubImgCharMatcher(charset);
    }

    public void run(String imageName) {
        try {
            this.image = new Image(imageName);

            String command = this.getCommand();
            while (!command.equals(EXIT_COMMAND_MESSAGE)) {
                this.runCommand(command);
                command = this.getCommand();
            }
        } catch (java.io.IOException e) {
            // TODO: handle exceptions
            throw new RuntimeException(e);
        }
    }

    private String getCommand() {
        System.out.print(ENTER_COMMAND_MESSAGE);
        return KeyboardInput.readLine();
    }

    private void runCommand(String command) {
        switch (command) {
            case VIEW_CHARS:
                this.charMatcher.printChars();
                break;
            case ADD_CHAR:
                // TODO: implement method
                break;
            case REMOVE_CHAR:
                // TODO: implement method
                break;
            case CHANGE_RESOLUTION:
                // TODO: implement method
                break;
            case CHANGE_OUTPUT:
                // TODO: implement method
                break;
            case CHANGE_ROUND:
                // TODO: implement method and change relevant parts in code
                break;
            case ASCII_ART:
                if (this.charMatcher.getCharsNumber()< MIN_CHARSET_SIZE) {
                    System.out.println(INVALID_CHARSET_SIZE_MESSAGE);
                } else {
                    this.runAsciiArt();
                }
                break;
            default:
                System.out.println(INVALID_COMMAND_MESSAGE);
        }
    }

    private void runAsciiArt() {
        // TODO: Check if can we prevent repeated creation
        AsciiArtAlgorithm asciiArt = new AsciiArtAlgorithm(this.image, this.charMatcher, this.resolution);
        char[][] asciiImage = asciiArt.run();

        // TODO: change to output outing
        ConsoleAsciiOutput consoleAsciiOutput = new ConsoleAsciiOutput();
        consoleAsciiOutput.out(asciiImage);
    }

    public static void main(String[] args) {
        Shell shell = new Shell();
        String imageName = args[0]; // Assume valid input: path file
        shell.run(imageName);
    }
}