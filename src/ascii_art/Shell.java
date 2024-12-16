package ascii_art;

import image.Image;
import image_char_matching.SubImgCharMatcher;
import ascii_output.ConsoleAsciiOutput;

import exceptions.InvalidAddFormatException;
import exceptions.InvalidCharsetSizeException;
import exceptions.InvalidRemoveFormatException;

public class Shell {
    // Default parameters
    private static final int DEFAULT_RESOLUTION = 2;
    private static final char DEFAULT_FIRST_CHAR = '0';
    private static final char DEFAULT_LAST_CHAR = '9';
    private static final int MIN_CHARSET_SIZE = 2; // Min size for ascii art
    private static final int MIN_WORDS_FOR_ADD_FORMAT = 2;
    private static final int MIN_WORDS_FOR_REMOVE_FORMAT = 2;

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
        try {
            String commandType = command.split(" ")[0];
            switch (commandType) {
                case VIEW_CHARS:
                    this.charMatcher.printChars();
                    break;
                case ADD_CHAR:
                    this.addChars(command);
                    break;
                case REMOVE_CHAR:
                    this.removeChars(command);
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
                    this.runAsciiArt();
                    break;
                default:
                    // TODO: Check if exception needed
                    System.out.println(INVALID_COMMAND_MESSAGE);
            }
        } catch (InvalidCharsetSizeException | InvalidAddFormatException | InvalidRemoveFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addChars(String command) throws InvalidAddFormatException {
        if (command.split(" ").length < MIN_WORDS_FOR_ADD_FORMAT) {
            throw new InvalidAddFormatException();
        }

        String formatToAdd = command.split(" ")[1];
        this.addChar(formatToAdd.charAt(0));
    }

    private void addChar(char c) {
        if (!this.charMatcher.containsChar(c)) {
            this.charMatcher.addChar(c);
        }
    }

    private void removeChars(String command) throws InvalidRemoveFormatException {
        if (command.split(" ").length < MIN_WORDS_FOR_REMOVE_FORMAT) {
            throw new InvalidRemoveFormatException();
        }

        String formatToRemove = command.split(" ")[1];
        this.removeChar(formatToRemove.charAt(0));
    }

    private void removeChar(char c) {
        if (this.charMatcher.containsChar(c)) {
            this.charMatcher.removeChar(c);
        }
    }

    private void runAsciiArt() throws InvalidCharsetSizeException {
        if (this.charMatcher.getCharsNumber() < MIN_CHARSET_SIZE) {
            throw new InvalidCharsetSizeException();
        }

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