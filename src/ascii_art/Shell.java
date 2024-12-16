package ascii_art;

import image.Image;
import image_char_matching.SubImgCharMatcher;
import ascii_output.ConsoleAsciiOutput;

import exceptions.InvalidAddFormatException;
import exceptions.InvalidCharsetSizeException;
import exceptions.InvalidRemoveFormatException;

/**
 * The Shell class serves as the main control interface for handling and processing images into ASCII art.
 * It provides commands to add, remove characters, change settings like resolution, and convert images to
 * ASCII art.
 *
 * <p>This class uses a `CharBrightnessMap` to manage the character set and brightness mappings,
 * and an `Image` object to perform image processing.</p>
 */
public class Shell {
    // Constants for ascii
    private static final char DEFAULT_FIRST_CHAR = '0';
    private static final char DEFAULT_LAST_CHAR = '9';
    private static final char FIRST_LEGAL_CHAR = 32;
    private static final char LAST_LEGAL_CHAR = 126;
    private static final char SPACE_CHARACTER = ' ';

    // Constants for algorithm
    private static final String WORDS_SEPARATOR = " ";
    private static final char RANGE_SEPARATOR = '-';
    private static final int RANGE_START_CHAR_INDEX = 0;
    private static final int RANGE_SEPARATOR_INDEX = 1;
    private static final int RANGE_END_CHAR_INDEX = 2;
    private static final int DEFAULT_RESOLUTION = 2;
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
    private static final String SPACE_OPERAND = "space"; // Space for adding or removal
    private static final String ALL_OPERAND = "all"; // Phrase for adding/removing all legal chars
    private static final String EXIT_COMMAND_MESSAGE = "EXIT";
    private static final String ENTER_COMMAND_MESSAGE = ">>> ";
    private static final String INVALID_COMMAND_MESSAGE = "Did not execute due to incorrect command.";

    private Image image;
    private SubImgCharMatcher charMatcher;
    private int resolution;

    /**
     * Constructs a new `Shell` instance.
     * Initializes the shell with default settings, including setting the resolution and the default
     * character set.
     * The default character set includes all ASCII characters from '0' to '9' and the space character.
     *
     * <p>The constructor sets up the character matcher with the initial character set required for ASCII
     * art operations.</p>
     */
    public Shell() {
        this.resolution = DEFAULT_RESOLUTION;

        // Init default chars set
        char[] charset = new char[DEFAULT_LAST_CHAR - DEFAULT_FIRST_CHAR + 1];
        for (int charIndex = DEFAULT_FIRST_CHAR; charIndex <= DEFAULT_LAST_CHAR; charIndex++) {
            charset[charIndex - DEFAULT_FIRST_CHAR] = (char) charIndex;
        }
        this.charMatcher = new SubImgCharMatcher(charset);
    }

    /**
     * Executes the command loop for the Shell.
     * Reads commands from the user, processes them, and continues until the 'EXIT' command is received.
     *
     * @param imageName the path to the image file.
     */
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
     * Processes and executes a given command string.
     * The method interprets the command, determines the action based on its type,
     * and performs the appropriate operation.
     * It supports commands to view characters, add/remove characters, change resolution,
     * change output settings, and generate ASCII art from the current image.
     *
     * <p>In case of invalid commands or exceptions, it prints an appropriate error message.</p>
     *
     * @param command the command string to process.
     */
    private void runCommand(String command) {
        try {
            String commandType = command.split(WORDS_SEPARATOR)[0];
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

    /**
     * Checks if the provided format string is in the correct range format.
     * A valid range format is a string in the form "X-Y", where X and Y are legal ASCII characters.
     * The format string must consist of exactly three characters:
     * two valid ASCII characters separated by a '-' character.
     *
     * @param format the string to check.
     * @return true if the string matches the "X-Y" format where X and Y are legal characters, false otherwise.
     */
    private boolean isRangeFormat(String format) {
        return format.length() == 3 &&
                this.isCharLegal(format.charAt(RANGE_START_CHAR_INDEX)) &&
                format.charAt(RANGE_SEPARATOR_INDEX) == RANGE_SEPARATOR &&
                this.isCharLegal(format.charAt(RANGE_END_CHAR_INDEX));
    }

    // Check if char is in legal ascii range
    private boolean isCharLegal(char c) {
        return FIRST_LEGAL_CHAR <= c && c <= LAST_LEGAL_CHAR;
    }

    /**
     * Processes a range of characters by either adding or removing them.
     *
     * <p>The method determines the start and end of the range based on the input string,
     * ensuring that characters are processed in ascending order regardless of how the range is defined.
     * If isAdding is true, the characters in the range are added; otherwise, they are removed.</p>
     *
     * @param range    a string representing the range of characters in the format "X-Y",
     *                 where X and Y are single characters.
     * @param isAdding a boolean indicating whether to add or remove the characters in the range.
     *                 true to add characters, false to remove them.
     */
    private void applyRangeOperation(String range, boolean isAdding) {
        char firstChar, lastChar;

        // Determine the start and end of the range in ascending order
        if (range.charAt(RANGE_START_CHAR_INDEX) <= range.charAt(RANGE_END_CHAR_INDEX)) {
            firstChar = range.charAt(RANGE_START_CHAR_INDEX);
            lastChar = range.charAt(RANGE_END_CHAR_INDEX);
        } else {
            firstChar = range.charAt(RANGE_END_CHAR_INDEX);
            lastChar = range.charAt(RANGE_START_CHAR_INDEX);
        }

        // Add or remove characters in the range
        for (char c = firstChar; c <= lastChar; c++) {
            if (isAdding) {
                this.addChar(c);
            } else {
                this.removeChar(c);
            }
        }
    }

    /**
     * Adds characters to the character matcher.
     * The format of the command determines how characters are added.
     *
     * @param command the command string, typically starting with "add" followed by the characters to add.
     * @throws InvalidAddFormatException if the format of the command is invalid.
     */
    private void addChars(String command) throws InvalidAddFormatException {
        if (command.split(WORDS_SEPARATOR).length < MIN_WORDS_FOR_ADD_FORMAT) {
            throw new InvalidAddFormatException();
        }

        String formatToAdd = command.split(WORDS_SEPARATOR)[1];
        if (this.isRangeFormat(formatToAdd)) {
            this.applyRangeOperation(formatToAdd, true);
        } else if (formatToAdd.equals(ALL_OPERAND)) {
            for (char c = FIRST_LEGAL_CHAR; c <= LAST_LEGAL_CHAR; c++) {
                this.addChar(c);
            }
        } else if (formatToAdd.equals(SPACE_OPERAND)) {
            this.addChar(SPACE_CHARACTER);
        } else if (formatToAdd.length() == 1 && isCharLegal(formatToAdd.charAt(0))) { // Format is valid char
            this.addChar(formatToAdd.charAt(0));
        } else {
            throw new InvalidAddFormatException();
        }
    }

    private void addChar(char c) {
        if (!this.charMatcher.containsChar(c)) {
            this.charMatcher.addChar(c);
        }
    }

    /**
     * Processes a command to remove characters.
     * The format of the command determines how characters are removed.
     *
     * @param command the command string, starting with "remove" followed by the characters to remove.
     * @throws InvalidRemoveFormatException if the format of the command is invalid.
     */
    private void removeChars(String command) throws InvalidRemoveFormatException {
        if (command.split(WORDS_SEPARATOR).length < MIN_WORDS_FOR_REMOVE_FORMAT) {
            throw new InvalidRemoveFormatException();
        }

        String formatToRemove = command.split(WORDS_SEPARATOR)[1];
        if (this.isRangeFormat(formatToRemove)) {
            this.applyRangeOperation(formatToRemove, false);
        } else if (formatToRemove.equals(ALL_OPERAND)) {
            for (char c = FIRST_LEGAL_CHAR; c <= LAST_LEGAL_CHAR; c++) {
                this.removeChar(c);
            }
        } else if (formatToRemove.equals(SPACE_OPERAND)) {
            this.removeChar(SPACE_CHARACTER);
        } else if (formatToRemove.length() == 1 && isCharLegal(formatToRemove.charAt(0))) {
            this.removeChar(formatToRemove.charAt(0));
        } else {
            throw new InvalidRemoveFormatException();
        }
    }

    private void removeChar(char c) {
        if (this.charMatcher.containsChar(c)) {
            this.charMatcher.removeChar(c);
        }
    }

    /**
     * Executes the ASCII art generation based on the current settings of the shell.
     *
     * @throws InvalidCharsetSizeException if the character set size is too small to perform ASCII art.
     */

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

    /**
     * The main entry point of the application.
     * Creates an instance of the `Shell` class, reads the image file path from the command-line arguments,
     * and initiates the interactive command loop to process the image into ASCII art.
     *
     * @param args command-line arguments. The first argument should be the path to the image file.
     *             We can assume args are valid.
     */
    public static void main(String[] args) {
        Shell shell = new Shell();
        String imageName = args[0]; // Assume valid input: path file
        shell.run(imageName);
    }
}