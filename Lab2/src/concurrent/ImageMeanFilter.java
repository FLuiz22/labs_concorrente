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
        // Load image
        BufferedImage originalImage = ImageIO.read(new File(inputPath));
        List<Thread> threads = new ArrayList<>();
        
        // Create result image
        BufferedImage filteredImage = new BufferedImage(
            originalImage.getWidth(), 
            originalImage.getHeight(), 
            BufferedImage.TYPE_INT_RGB
        );
        
        // Image processing
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        // Process each pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x+=nThreads) {
                List<Task> ts = new ArrayList<>();
                // Calculate neighborhood average
                /* int[] avgColor = calculateNeighborhoodAverage(originalImage, x, y, kernelSize);
                
                // Set filtered pixel
                filteredImage.setRGB(x, y, 
                    (avgColor[0] << 16) | 
                    (avgColor[1] << 8)  | 
                    avgColor[2]
                ); */

                for (int i = 0; i < nThreads; i++) {
                    Task task = new Task(originalImage, x, y, kernelSize);

                    Thread thread = new Thread(task);
                    ts.add(task);
                    thread.start();
                }



                for (int i = 0; i < ts.size(); i++) {
                    int[] avgColor = ts.get(i).getOut();
                    
                    filteredImage.setRGB(x+i, y, 
                        (avgColor[0] << 16) | 
                        (avgColor[1] << 8)  | 
                        avgColor[2]
                    ); 
                }
            }
        }



        
        // Save filtered image
        ImageIO.write(filteredImage, "jpg", new File(outputPath));
    }

    //abordagem 1

    public static class Task implements Runnable {
        private final BufferedImage originalImage;
        private int x;
        private int y;
        private int kernelSize;
        private int[] out;

        public Task(BufferedImage originalImage, int x, int y, int kernelSize) {
            this.originalImage = originalImage;
            this.x = x;
            this.y = y;
            this.kernelSize = kernelSize;
        }

        @Override
        public void run() {
            int[] avgColor = calculateNeighborhoodAverage(originalImage, x, y, kernelSize);
                
            out = avgColor;
        }

        public int[] getOut() {
            return out;
        }
    }

    //abordagem 2

    public static class Task2 implements Runnable {
        private final BufferedImage originalImage;
        private int width;
        private int y;
        private int kernelSize;
        private int[] out;

        public Task2(BufferedImage originalImage, int width, int y, int kernelSize) {
            this.originalImage = originalImage;
            this.width = width;
            this.y = y;
            this.kernelSize = kernelSize;
        }

        @Override
        public void run() {
            for (int x = 0; x < width; x++) {
                List<Task> ts = new ArrayList<>();
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

        public int[] getOut() {
            return out;
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
        int nThreads = new Integer(args[1]);
        try {
            applyMeanFilter(inputFile, "filtered_output.jpg", 7, nThreads);
        } catch (IOException e) {
            System.err.println("Error processing image: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("F");
        }
    }
}
