package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import exceptions.*;
import image_char_matching.SubImgCharMatcher;

// TODO: change documentation because class has benn changed
/**
 * The Shell class serves as the main control interface for handling and processing images into ASCII art.
 * It provides commands to add, remove characters, change settings like resolution, and convert images to
 * ASCII art.
 *
 * <p>This class uses a `CharBrightnessMap` to manage the character set and brightness mappings,
 * and an `Image` object to perform image processing.</p>
 *
 * @author Or Tarazi, Agam Hershko
 */
public class Shell {
    // Constants for ascii
    private static final char DEFAULT_FIRST_CHAR = '0';
    private static final char DEFAULT_LAST_CHAR = '9';
    private static final char FIRST_LEGAL_CHAR = 32;
    private static final char LAST_LEGAL_CHAR = 126;
    private static final char SPACE_CHARACTER = ' ';

    // Constants for algorithm
    private static final int DEFAULT_RESOLUTION = 2;
    private static final int RESOLUTION_SCALE_FACTOR = 2;
    private static final String HTML_OUTPUT_PATH = "out.html";
    private static final String HTML_FONT = "Courier New";

    private static final String WORDS_SEPARATOR = " ";
    private static final int MIN_CHARSET_SIZE = 2; // Min size for ascii art
    private static final int MIN_WORDS_FOR_OPERAND_COMMAND = 2; // For add, remove, resolution, round, output
    private static final int COMMAND_INDEX = 0;
    private static final int OPERAND_INDEX = 1;
    private static final int IMAGE_PATH_ARGS_INDEX = 0;

    private static final char RANGE_SEPARATOR = '-';
    private static final int RANGE_START_CHAR_INDEX = 0;
    private static final int RANGE_SEPARATOR_INDEX = 1;
    private static final int RANGE_END_CHAR_INDEX = 2;

    // Input Messages
    private static final String ENTER_COMMAND_MESSAGE = ">>> ";
    private static final String RESOLUTION_SET_MESSAGE = "Resolution set to ";
    private static final String EXIT_COMMAND_MESSAGE = "exit";
    private static final String VIEW_CHARS = "chars";
    private static final String ADD_CHAR = "add";
    private static final String REMOVE_CHAR = "remove";
    private static final String CHANGE_RESOLUTION = "res";
    private static final String CHANGE_OUTPUT = "output";
    private static final String CHANGE_ROUND = "round";
    private static final String ASCII_ART = "asciiArt";
    private static final String SPACE_OPERAND = "space"; // Space for adding or removal
    private static final String ALL_OPERAND = "all"; // Phrase for adding/removing all legal chars
    private static final String HTML_OUTPUT = "html";
    private static final String CONSOLE_OUTPUT = "console";
    private static final String RESOLUTION_UPSCALE = "up";
    private static final String RESOLUTION_DOWNSCALE = "down";
    private static final String UP_ROUND_TYPE = "up";
    private static final String DOWN_ROUND_TYPE = "down";
    private static final String ABS_ROUND_TYPE = "abs";
    private static final String INVALID_COMMAND_MESSAGE = "Did not execute due to incorrect command.";

    // private fields
    private int resolution;
    private Image image;
    private final SubImgCharMatcher charMatcher;
    private RoundType roundType;
    private OutputMethod outputMethod;
    private AsciiOutput asciiOutput;
    private final BrightnessMemento brightnessMemento;
    private AsciiArtAlgorithm algorithm;

    // TODO: change documentation because function has benn changed
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
        this.outputMethod = OutputMethod.CONSOLE; // Default output method
        this.asciiOutput = new ConsoleAsciiOutput();
        this.roundType = RoundType.ABS;

        // Init default chars set
        char[] charset = new char[DEFAULT_LAST_CHAR - DEFAULT_FIRST_CHAR + 1];
        for (int charIndex = DEFAULT_FIRST_CHAR; charIndex <= DEFAULT_LAST_CHAR; charIndex++) {
            charset[charIndex - DEFAULT_FIRST_CHAR] = (char) charIndex;
        }

        this.charMatcher = new SubImgCharMatcher(charset, this.roundType);
        this.brightnessMemento = new BrightnessMemento(null);
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
            this.algorithm = new AsciiArtAlgorithm(
                    this.image, this.brightnessMemento, this.charMatcher, this.resolution);

            String command = this.getCommand();
            while (!command.equals(EXIT_COMMAND_MESSAGE)) {
                this.runCommand(command);
                command = this.getCommand();
            }
        } catch (java.io.IOException e) {
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
            String commandType = command.split(WORDS_SEPARATOR)[COMMAND_INDEX];
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
                    this.changeResolution(command);
                    break;
                case CHANGE_OUTPUT:
                    this.changeOutputMethod(command);
                    break;
                case CHANGE_ROUND:
                    this.changeRoundType(command);
                    break;
                case ASCII_ART:
                    this.runAsciiArt();
                    break;
                default:
                    System.out.println(INVALID_COMMAND_MESSAGE);
            }
        } catch (InvalidCharsetSizeException | InvalidAddFormatException | InvalidRemoveFormatException |
                 InvalidOutputFormatException | InvalidRoundFormatException |
                 InvalidResolutionFormatException | InvalidResolutionValueException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean isWordsNumberInvalid(String command) {
        return command.split(WORDS_SEPARATOR).length < MIN_WORDS_FOR_OPERAND_COMMAND;
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
        return format.length() == (RANGE_END_CHAR_INDEX + 1) &&
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
        if (isWordsNumberInvalid(command)) {
            throw new InvalidAddFormatException();
        }

        String formatToAdd = command.split(WORDS_SEPARATOR)[OPERAND_INDEX];
        if (this.isRangeFormat(formatToAdd)) {
            this.applyRangeOperation(formatToAdd, true);
        } else if (formatToAdd.equals(ALL_OPERAND)) {
            for (char c = FIRST_LEGAL_CHAR; c <= LAST_LEGAL_CHAR; c++) {
                this.addChar(c);
            }
        } else if (formatToAdd.equals(SPACE_OPERAND)) {
            this.addChar(SPACE_CHARACTER);
        } else if (formatToAdd.length() == 1 && isCharLegal(formatToAdd.charAt(COMMAND_INDEX))) {
            this.addChar(formatToAdd.charAt(COMMAND_INDEX)); // Add if format is valid char
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
        if (isWordsNumberInvalid(command)) {
            throw new InvalidRemoveFormatException();
        }

        String formatToRemove = command.split(WORDS_SEPARATOR)[OPERAND_INDEX];
        if (this.isRangeFormat(formatToRemove)) {
            this.applyRangeOperation(formatToRemove, false);
        } else if (formatToRemove.equals(ALL_OPERAND)) {
            for (char c = FIRST_LEGAL_CHAR; c <= LAST_LEGAL_CHAR; c++) {
                this.removeChar(c);
            }
        } else if (formatToRemove.equals(SPACE_OPERAND)) {
            this.removeChar(SPACE_CHARACTER);
        } else if (formatToRemove.length() == 1 && isCharLegal(formatToRemove.charAt(COMMAND_INDEX))) {
            this.removeChar(formatToRemove.charAt(COMMAND_INDEX));  // Remove if format is valid char
        } else {
            throw new InvalidRemoveFormatException();
        }
    }

    private void removeChar(char c) {
        if (this.charMatcher.containsChar(c)) {
            this.charMatcher.removeChar(c);
        }
    }

    // TODO: change documentation because function has benn changed
    /**
     * changes the resolution by multiplying the current resolution by 2 or dividing it by 2.
     *
     * @param command "up" for upscale by 2, "down" for downscale by 2.
     * @throws InvalidResolutionValueException  if the new resolution after the change exceeds the limits
     *                                          defined in the exercise.
     * @throws InvalidResolutionFormatException if the user inserted any string other than "up" or "down".
     */
    private void changeResolution(String command)
            throws InvalidResolutionValueException, InvalidResolutionFormatException {
        if (isWordsNumberInvalid(command)) {
            throw new InvalidResolutionValueException();
        }

        // TODO: add comments to function
        String resolution = command.split(WORDS_SEPARATOR)[OPERAND_INDEX];
        int newResolution = switch (resolution) {
            case RESOLUTION_UPSCALE -> this.resolution * RESOLUTION_SCALE_FACTOR;
            case RESOLUTION_DOWNSCALE -> this.resolution / RESOLUTION_SCALE_FACTOR;
            default -> throw new InvalidResolutionFormatException();
        };

        if (this.isResolutionLegal(newResolution)) {
            this.resolution = newResolution;
        } else {
            throw new InvalidResolutionValueException();
        }

        this.brightnessMemento.setLastStateValidity(false);
        this.algorithm = new AsciiArtAlgorithm(
                this.image, this.brightnessMemento, this.charMatcher, this.resolution);

        System.out.println(RESOLUTION_SET_MESSAGE + this.resolution);
    }

    /**
     * checks if the desired resolution stands within the resolution boundaries defined in the exercise.
     *
     * @param inspectedResolution the new resolution being inspected if legal.
     * @return true if legal, false if not
     */
    private boolean isResolutionLegal(int inspectedResolution) {
        int maxResolution = this.image.getWidth();
        int minResolution = Math.max(1, this.image.getWidth() / this.image.getHeight());
        return inspectedResolution >= minResolution && inspectedResolution <= maxResolution;
    }

    /**
     * changes the output in which the program will print the final result of the ascii-art.
     *
     * @param command either "html" or "console" (console is default)
     * @throws InvalidOutputFormatException if the user inserted any string other than "html" or "console".
     */
    private void changeOutputMethod(String command) throws InvalidOutputFormatException {
        if (isWordsNumberInvalid(command)) {
            throw new InvalidOutputFormatException();
        }

        String output = command.split(WORDS_SEPARATOR)[OPERAND_INDEX];
        if (output.equals(CONSOLE_OUTPUT)) {
            this.outputMethod = OutputMethod.CONSOLE;
            this.asciiOutput = new ConsoleAsciiOutput();
        } else if (output.equals(HTML_OUTPUT)) {
            this.outputMethod = OutputMethod.HTML;
            this.asciiOutput = new HtmlAsciiOutput(HTML_OUTPUT_PATH, HTML_FONT);
        } else {
            throw new InvalidOutputFormatException();
        }
    }

    /**
     * changes the way the SubImageCharMatcher will match the closest char to a given brightness-
     * 1) absolute "abs" - will return the char in the charset closest in absolute distance.
     * 2) up "up" - will return the char in the charset closest from the top.
     * 3) down "down" - will return the char in the charset closest from bottom.
     *
     * @param command "up" for up-rounding, "down" for down-rounding, "abs" for rounding in absolute distance.
     * @throws InvalidRoundFormatException if user inserted any other string rather than "up", "down" or "abs".
     */
    private void changeRoundType(String command) throws InvalidRoundFormatException {
        if (isWordsNumberInvalid(command)) {
            throw new InvalidRoundFormatException();
        }

        String roundType = command.split(WORDS_SEPARATOR)[OPERAND_INDEX];
        switch (roundType) {
            case UP_ROUND_TYPE:
                this.roundType = RoundType.UP;
                break;
            case DOWN_ROUND_TYPE:
                this.roundType = RoundType.DOWN;
                break;
            case ABS_ROUND_TYPE:
                this.roundType = RoundType.ABS;
                break;
            default:
                throw new InvalidRoundFormatException();
        }

        this.charMatcher.setRoundType(this.roundType);
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

        char[][] asciiImage = this.algorithm.run();
        this.asciiOutput.out(asciiImage);
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
        String imageName = args[IMAGE_PATH_ARGS_INDEX]; // Assume valid input: path file
        shell.run(imageName);
    }
}