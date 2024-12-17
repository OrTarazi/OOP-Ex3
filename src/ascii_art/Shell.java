package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import image.Image;
import ascii_art.AsciiArtAlgorithm.RoundType;
import exceptions.*;


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


    // expected strings as input following commands
    private static final String HTML_OUTPUT = "html";
    private static final String CONSOLE_OUTPUT = "console";
    private static final String UPSCALE_BY_TWO = "up";
    private static final String DOWNSCALE_BY_TWO = "down";
    private static final int SCALE_FACTOR = 2;


    // an enum for desired output method.
    public enum OutputMethod {
        HTML, CONSOLE
    }

    // private fields
    private int resolution;
    private Image image;
    private char[] charset;
    private RoundType roundType;
    private OutputMethod outputMethod;


    public Shell() {
        this.resolution = DEFAULT_RESOLUTION;
        this.outputMethod = OutputMethod.CONSOLE;
        this.roundType = RoundType.ABS;
        // Init default chars set
        this.charset = new char[DEFAULT_LAST_CHAR - DEFAULT_FIRST_CHAR + 1];
        for (int charIndex = DEFAULT_FIRST_CHAR; charIndex <= DEFAULT_LAST_CHAR; charIndex++) {
            charset[charIndex - DEFAULT_FIRST_CHAR] = (char) charIndex;
        }
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

    /**
     * changes the resolution by multiplying the current resolution by 2 or dividing it by 2.
     * @param direction "up" for upscale by 2, "down" for downscale by 2.
     * @throws InvalidResolutionValueException if the new resolution after the change exceeds the limits
     * defined in the exercise.
     * @throws InvalidResolutionFormatException if the user inserted any string other than "up" or "down".
     */
    private void changeResolution(String direction) throws InvalidResolutionValueException, InvalidResolutionFormatException {
        int newResolution = switch (direction) {
            case UPSCALE_BY_TWO -> this.resolution * SCALE_FACTOR;
            case DOWNSCALE_BY_TWO -> this.resolution / SCALE_FACTOR;
            default -> throw new InvalidResolutionFormatException();
        };
        if (isResolutionLegal(newResolution)) {
            this.resolution = newResolution;
        }
        else {
            throw new InvalidResolutionValueException();
        }
    }



    /**
     * checks if the desired resolution stands within the resolution boundaries defined in the exercise.
     * @param inspectedResolution the new resolution being inspected if legal.
     * @return true if legal, false if not
     */
    private boolean isResolutionLegal(int inspectedResolution)  {
        int imgWidth = this.image.getWidth();
        int imgHeight = this.image.getHeight();
        int maximumRes = imgHeight*imgWidth;
        int minimumRes = Math.max(1, imgWidth/imgHeight);
        return inspectedResolution >= minimumRes && inspectedResolution <= maximumRes;
    }

    /**
     * changes the output in which the program will print the final result of the ascii-art.
     * @param outputMethod either "html" or "console" (console is default)
     * @throws InvalidOutputFormatException if the user inserted any string other than "html" or "console".
     */
    private void changeOutputMethod(String outputMethod) throws InvalidOutputFormatException {
        switch (outputMethod){
            case HTML_OUTPUT -> this.outputMethod = OutputMethod.HTML;
            case CONSOLE_OUTPUT -> this.outputMethod = OutputMethod.CONSOLE;
            default -> throw new InvalidOutputFormatException();
        }
    }

    /**
     * changes the way the SubImageCharMatcher will match the closest char to a given brightness-
     * 1) absolute "abs" - will return the char in the charset closest in absolute distance.
     * 2) up "up" - will return the char in the charset closest from the top.
     * 3) down "down" - will return the char in the charset closest from bottom.
     * @param roundType "up" for up-rounding, "down" for down-rounding, "abs" for rounding in absolute distance.
     * @throws InvalidRoundFormatException if user inserted any other string rather than "up", "down" or "abs".
     */
    private void changeRoundType(String roundType) throws InvalidRoundFormatException{
        switch (roundType) {
            // TODO: make strings constants!
            case "up":
                this.roundType = RoundType.UP;
            case "down":
                this.roundType = RoundType.DOWN;
            case "abs":
                this.roundType = RoundType.ABS;
            default:
                throw new InvalidRoundFormatException();
        }
    }


    private void runCommand(String command) {
        switch (command) {
            case VIEW_CHARS:
                // TODO: implement method
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
                if (this.charset.length < MIN_CHARSET_SIZE) {
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
        AsciiArtAlgorithm asciiArt = new AsciiArtAlgorithm(
                this.image, this.charset, this.resolution, this.roundType);
        char[][] asciiImage = asciiArt.run();

        // TODO: change to output outing
        ConsoleAsciiOutput consoleAsciiOutput = new ConsoleAsciiOutput();
        consoleAsciiOutput.out(asciiImage);
    }

    public static void main(String[] args) {
        Shell shell = new Shell();
        String imageName = "cat.jpeg"; // Assume valid input: path file
        shell.run(imageName);
    }
}
