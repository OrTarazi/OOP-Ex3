package image;

import java.awt.*;

/**
 * Provides functionality to divide an image into smaller sub-images based on a given resolution.
 * The resolution determines the number of sub-images in a row.
 *
 * @author Agam Hershko and Or Tarazi
 */
public class ImageDivision {
    /**
     * Divides the given image into smaller sub-images based on the specified resolution.
     * We assume the image dimensions are divisible by the resolution (assume resolution validity)
     *
     * @param image      the image to be divided.
     * @param resolution the number of sub-images in a single row.
     * @return a 2D array of sub-images.
     */
    public static Image[][] divideToImages(Image image, int resolution) {
        int subPicturesSize = getSubPicturesSize(image, resolution);
        int rowsOfSubPictures = image.getHeight() / subPicturesSize;

        // Create a 2D array to store sub-images
        Image[][] subImages = new Image[rowsOfSubPictures][resolution];
        fillSubImages(image, subImages, subPicturesSize);

        return subImages;
    }

    // Calculates the size of each sub-image based on the resolution
    private static int getSubPicturesSize(Image image, int resolution) {
        return image.getWidth() / resolution;
    }

    /**
     * Fills the 2D array of sub-images by extracting pixels from the original image.
     *
     * @param image           the original image to divide.
     * @param subImages       the 2D array to store the sub-images.
     * @param subPicturesSize the size of each sub-image.
     */
    private static void fillSubImages(Image image, Image[][] subImages, int subPicturesSize) {
        for (int row = 0; row < subImages.length; row++) {
            for (int col = 0; col < subImages[row].length; col++) {
                subImages[row][col] = createSubImage(image, subPicturesSize, row, col);
            }
        }
    }

    /**
     * Creates a sub-image by extracting a square of pixels from the original image.
     *
     * @param image           the original image.
     * @param subPicturesSize the size of each sub-image.
     * @param subImageRow     the row index of the sub-image in the 2D array.
     * @param subImageCol     the column index of the sub-image in the 2D array.
     * @return the sub-image as a new image object.
     */
    private static Image createSubImage(Image image, int subPicturesSize, int subImageRow, int subImageCol) {
        Color[][] subImage = new Color[subPicturesSize][subPicturesSize];
        for (int i = 0; i < subImage.length; i++) {
            for (int j = 0; j < subImage[i].length; j++) {
                int imagePixelRow = subImageRow * subPicturesSize + i;
                int imagePixelCol = subImageCol * subPicturesSize + j;
                subImage[i][j] = image.getPixel(imagePixelRow, imagePixelCol);
            }
        }

        return new Image(subImage, subImage.length, subImage[0].length);
    }
}
