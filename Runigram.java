import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
	    
		//// Hide / change / add to the testing code below, as needed.
		
		// Tests the reading and printing of an image:	
		// Color[][] tinypic = read("tinypic.ppm");
		Color[][] tinypic = read("tinypic.ppm");
		Color[][] cake = read("cake.ppm");
		Color[][] thor = read("thor.ppm");

		print(tinypic);
		System.out.println();
		System.out.println();

		Color[][] blended = blend(cake, scaled(thor, cake.length, cake[0].length), 0.2);
		// print(thor);
		setCanvas(blended);
		display(blended);
		

	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		// Reads the file header, ignoring the first and the third lines.
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		// Creates the image array
		Color[][] image = new Color[numRows][numCols];
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
					image[r][c] = new Color (in.readInt(), in.readInt(), in.readInt());
				
			}
		}

		return image;
	}

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting image.
	private static void print(Color[][] image) {
		for (int r = 0; r < image.length; r++) {
			for (int c = 0; c < image[0].length; c++) {
				print(image[r][c]);
			}
			System.out.println();
		}
		//// Notice that all you have to so is print every element (i,j) of the array using the print(Color) function.
	}
	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
		Color [][] newImage = new Color[image.length][image[0].length];
		for (int r = 0; r < image.length; r++) {
			for (int c = 0; c < image[0].length; c++) {
				newImage[r][c] = image[r][image[0].length - c - 1];
			}
		}

		return newImage;
	}
	
	/**
	 * Returns an image which is the vertically flipped version of the given image. 
	 */
	public static Color[][] flippedVertically(Color[][] image){
		Color [][] newImage = new Color[image.length][image[0].length];
		for (int c = 0; c < image[0].length; c++) {
			for (int r = 0; r < image.length; r++) {
				newImage[r][c] = image[image.length - r - 1][c];
			}
		}
		
		return newImage;
	}
	
	// Computes the luminance of the RGB values of the given pixel, using the formula 
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object consisting
	// the three values r = lum, g = lum, b = lum.
	private static Color luminance(Color pixel) {
		double redV = (double) pixel.getRed() * 0.299;
		double greenV = (double) pixel.getGreen() * 0.587;
		double blueV = (double) pixel.getBlue() * 0.114;
		int lum = (int)(redV + greenV + blueV);
		return new Color(lum, lum, lum);
	}
	
	/**
	 * Returns an image which is the grayscaled version of the given image.
	 */
	public static Color[][] grayScaled(Color[][] image) {
		Color [][] newImage = new Color[image.length][image[0].length];
		for (int c = 0; c < image[0].length; c++) {
			for (int r = 0; r < image.length; r++) {
				newImage[r][c] = luminance(image[r][c]);
			}
		}

		return newImage;
	}	
	
	/**
	 * Returns an image which is the scaled version of the given image. 
	 * The image is scaled (resized) to have the given width and height.
	 */ 
	// width is the number of columns () and height is the number or rows
	//width 5 height 10, we want 10 rows and 5 columns
	public static Color[][] scaled(Color[][] image, int height, int width) {
		Color[][] newImage = new Color[height][width];
		
		// Original dimensions
		int h0 = image.length;
		int w0 = image[0].length;

		// Ratios: how many original pixels per new pixel
		double yScale = (double) h0 / height;
		double xScale = (double) w0 / width;

		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				// Map the target (r, c) to source (i, j)
				// We cast to int to "floor" the value and get the nearest pixel
				int sourceR = (int) (r * yScale);
				int sourceC = (int) (c * xScale);

				// Safety check: ensure we don't exceed original bounds 
				// (though mathematically r * (h0/height) should stay < h0)
				sourceR = Math.min(sourceR, h0 - 1);
				sourceC = Math.min(sourceC, w0 - 1);

				newImage[r][c] = image[sourceR][sourceC];
			}
	}

	return newImage;
	}
	
	/**
	 * Computes and returns a blended color which is a linear combination of the two given
	 * colors. Each r, g, b, value v in the returned color is calculated using the formula 
	 * v = alpha * v1 + (1 - alpha) * v2, where v1 and v2 are the corresponding r, g, b
	 * values in the two input color.
	 */
	public static Color blend(Color c1, Color c2, double alpha) {
		int blendedRed = (int) (c1.getRed() * alpha + c2.getRed() * (1 - alpha));
		int blendedGreen = (int) (c1.getGreen() * alpha + c2.getGreen() * (1 - alpha));
		int blendedBlue = (int) (c1.getBlue() * alpha + c2.getBlue() * (1 - alpha));
		
		return new Color (blendedRed, blendedGreen, blendedBlue);
	}
	
	/**
	 * Cosntructs and returns an image which is the blending of the two given images.
	 * The blended image is the linear combination of (alpha) part of the first image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 */
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		Color [][] blendedImage = new Color [image1.length][image1[0].length];
		
		if (image1.length == image2.length & image1[0].length == image2[0].length) {
			for (int r = 0; r < image1.length; r++) {
				for (int c = 0; c < image1[0].length; c++) {
					blendedImage[r][c] = blend(image1[r][c], image2[r][c], alpha);
				}
			}


		} else {
			System.out.println("Invalid Dimensions");
		}


		return blendedImage;
	}

	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) {
		Color [][] scaledTarget = scaled(target, source.length, source[0].length);
		
		for (int s = 0; s <= n; s++) {
			Color [][] blendedImg = blend(source, scaledTarget, 1.0 - (double) s / n);
			Runigram.display(blendedImg);
			StdDraw.pause(500); 
		}
		
	}
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(height, width);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}
