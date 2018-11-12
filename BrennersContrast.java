package greenStand;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.PrintWriter;


// Class that utilizes Brenner's Focus metric.
// This function returns a real valued number where empirical values indicate
// that < 300 is questionable, 300-1000 is "good", and > 1000 is good. 
// More stringent restrictions can be decided upon.


public class BrennersContrast {


	public static void main(String[] args) {
		try {
			
			 
			String path = "D:\\Projects\\GreenStand\\ImageData\\10.jpg";
			
			File file = new File(path);

			BufferedImage image = null;

			image = ImageIO.read(file);
			int rows = image.getHeight();
			int cols = image.getWidth();
			
			int[][] pix = getGrayPixels(image);

			//// COMPUTE METRIC.
			//// subjective scale. < 300 is not good. 300-1000 is ok, > 1000 very good.
			////
			double focusMetric = brennersFocusMetric(pix,rows,cols);
			////
			
			System.out.println(focusMetric);

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
	}

	// Convert RGB to Gray intensity.
	private static int[][] getGrayPixels(BufferedImage image) {

		final byte[] pixels = ((java.awt.image.DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final int width = image.getWidth();
		final int height = image.getHeight();
		final boolean hasAlphaChannel = image.getAlphaRaster() != null;

		int offset = hasAlphaChannel ? 1 : 0;
		final int pixelLength = hasAlphaChannel ? 4 : 3;

		int[][] result = new int[height][width];


		for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {

			double B = ((double) (pixels[pixel + offset] & 0xFF)); // blue
			double G = ((double) (pixels[pixel + offset + 1] & 0xFF)); // green
			double R = ((double) (pixels[pixel + offset + 2]& 0xFF)); // red

			double gray = (0.2990 * R + 0.5870 * G + 0.1140 * B);

			result[row][col] = (int)(gray);
			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}

		return result;
	}

	// Compute average of sum of squares of the gradient in H and V directions.
	private static double brennersFocusMetric(int[][] input,int rows, int cols ) {

		int[][] V = new int[rows][cols];
		int[][] H = new int[rows][cols];
		for(int row = 0; row < rows; row++)
		{
			for (int col = 0; col < cols-2; col++) {
				int grad = input[row][col+2] - input[row][col];
				H[row][col] = grad;
			}
		}

		for(int row = 0; row < rows-2; row++)
		{
			for (int col = 0; col < cols; col++) {
				int grad = input[row+2][col] - input[row][col];
				V[row][col] = grad;
			}
		}

		double sum = 0;
		for(int row = 0; row < rows; row++)
		{
			for (int col = 0; col < cols; col++) {
				double HRC = H[row][col];
				double VRC = V[row][col];
				sum  += Math.abs(HRC) > Math.abs(VRC) ? HRC * HRC : VRC * VRC;
			}
		}
		return sum / (double)(rows * cols);
	}

	// for debugging.
	private static void writeImageAsCSV(int [][] img,int rows, int cols, String path) {
		try {
			PrintWriter pw = new PrintWriter(new File(path));
			StringBuilder sb = new StringBuilder();
			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < cols; col++) {
					sb.append(img[row][col]);
					if (col < cols - 1) {
						sb.append(",");
					}
				}
				sb.append("\n");
				pw.write(sb.toString());
				sb.setLength(0);
			}
			pw.close();
			System.out.println("done!");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 


	}
}
