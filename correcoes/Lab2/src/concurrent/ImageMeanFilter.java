import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * This class provides functionality to apply a mean filter to an image.
 * The mean filter is used to smooth images by averaging the pixel values
 * in a neighborhood defined by a kernel size.
 * 
 * <p>Usage example:</p>
 * <pre>
 * {@code
 * ImageMeanFilter.applyMeanFilter("input.jpg", "output.jpg", 3);
 * }
 * </pre>
 * 
 * <p>Supported image formats: JPG, PNG</p>
 * 
 * <p>Author: temmanuel@comptuacao, BufferedImage filBufferedImage.ufcg.edu.br</p>
 */
public class ImageMeanFilter {
    
    /**
     * Applies mean filter to an image
     * 
     * @param inputPath  Path to input image
     * @param outputPath Path to output image 
     * @param kernelSize Size of mean kernel
     * @throws IOException If there is an error reading/writing
     */
    public static void applyMeanFilter(String inputPath, String outputPath, int kernelSize, int nThreads) throws IOException, InterruptedException {
        List<Thread> threads = new ArrayList<>();
        // Load image
        BufferedImage originalImage = ImageIO.read(new File(inputPath));
        
        // Create result image
        BufferedImage filteredImage = new BufferedImage(
            originalImage.getWidth(), 
            originalImage.getHeight(), 
            BufferedImage.TYPE_INT_RGB
        );
        
        // Image processing
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        
        for (int i = 0; i < nThreads; i++) {
            int tHeight = (height / nThreads) * (i+1);
            int start = (height / nThreads) * i;

            Task task = new Task(filteredImage, originalImage, start, width, tHeight, kernelSize);

            Thread pThread = new Thread(task);

            pThread.start();

            threads.add(pThread);
        }

        for (Thread t : threads) {
            t.join();
        }

        /* for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Calculate neighborhood average
                int[] avgColor = calculateNeighborhoodAverage(originalImage, x, y, kernelSize);
                
                // Set filtered pixel
                filteredImage.setRGB(x, y, 
                    (avgColor[0] << 16) | 
                    (avgColor[1] << 8)  | 
                    avgColor[2]
                );
            }
        } */
        
        // Save filtered image
        ImageIO.write(filteredImage, "jpg", new File(outputPath));
    }


    //abordagem 2

    public static class Task implements Runnable {
        private BufferedImage filteredImage;
        private BufferedImage originalImage;
        private int width;
        private int height;
        private int kernelSize;
        private int start;
        
        public Task(BufferedImage filteredImage, BufferedImage originalImage, int start, int width, int height, int kernelSize) {
            this.filteredImage = filteredImage;
            this.originalImage = originalImage;
            this.width = width;
            this.height = height;
            this.kernelSize = kernelSize;
            this.start = start;
        }

        @Override
        public void run() {
            for (int y = start; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Calculate neighborhood average
                    int[] avgColor = calculateNeighborhoodAverage(originalImage, x, y, kernelSize);
                    
                    // Set filtered pixel
                    filteredImage.setRGB(x, y, 
                        (avgColor[0] << 16) | 
                        (avgColor[1] << 8)  | 
                        avgColor[2]
                    );
                }
            }
        }
    }
    
    /**
     * Calculates average colors in a pixel's neighborhood
     * 
     * @param image      Source image
     * @param centerX    X coordinate of center pixel
     * @param centerY    Y coordinate of center pixel
     * @param kernelSize Kernel size
     * @return Array with R, G, B averages
     */
    private static int[] calculateNeighborhoodAverage(BufferedImage image, int centerX, int centerY, int kernelSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        int pad = kernelSize / 2;
        
        // Arrays for color sums
        long redSum = 0, greenSum = 0, blueSum = 0;
        int pixelCount = 0;
        
        // Process neighborhood
        for (int dy = -pad; dy <= pad; dy++) {
            for (int dx = -pad; dx <= pad; dx++) {
                int x = centerX + dx;
                int y = centerY + dy;
                
                // Check image bounds
                if (x >= 0 && x < width && y >= 0 && y < height) {
                    // Get pixel color
                    int rgb = image.getRGB(x, y);
                    
                    // Extract color components
                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = rgb & 0xFF;
                    
                    // Sum colors
                    redSum += red;
                    greenSum += green;
                    blueSum += blue;
                    pixelCount++;
                }
            }
        }
        
        // Calculate average
        return new int[] {
            (int)(redSum / pixelCount),
            (int)(greenSum / pixelCount),
            (int)(blueSum / pixelCount)
        };
    }
    
    /**
     * Main method for demonstration
     * 
     * Usage: java ImageMeanFilter <input_file>
     * 
     * Arguments:
     *   input_file - Path to the input image file to be processed
     *                Supported formats: JPG, PNG
     * 
     * Example:
     *   java ImageMeanFilter input.jpg
     * 
     * The program will generate a filtered output image named "filtered_output.jpg"
     * using a 7x7 mean filter kernel
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java ImageMeanFilter <input_file>");
            System.exit(1);
        }

        String inputFile = args[0];
        int nThreads = Integer.valueOf(args[1]);
        try {
            applyMeanFilter(inputFile, "filtered_output.jpg", 7, nThreads);
        } catch (IOException e) {
            System.err.println("Error processing image: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("F");
        }
    }
}
