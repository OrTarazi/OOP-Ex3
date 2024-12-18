package ascii_art;

/**
 * Specifies the rounding strategies used for selecting ASCII characters based on brightness values.
 *
 * <p>This enum defines the methods for handling brightness differences when mapping pixels to ASCII
 * characters:</p>
 * <ul>
 *     <li>UP: Always round up to the closest higher brightness value.</li>
 *     <li>DOWN: Always round down to the closest lower brightness value.</li>
 *     <li>ABS: Select the brightness value with the smallest absolute difference.</li>
 * </ul>
 *
 * @author Or Tarazi, Agam Hershko
 */
public enum RoundType {
    /**
     * Rounds up to the closest higher brightness value.
     */
    UP,

    /**
     * Rounds down to the closest lower brightness value.
     */
    DOWN,

    /**
     * Selects the brightness value with the smallest absolute difference.
     */
    ABS
}