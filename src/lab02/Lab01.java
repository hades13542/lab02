package lab02;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import lab02.RGB;

public class Lab01 {
	public static void run(BufferedImage in) {
		try {
			BufferedImage out = Lab01.negatyw(in);
			ImageIO.write(out, "jpg", new File("02.jpg"));
			out = Lab01.histnorm(in);
			ImageIO.write(out, "jpg", new File("01b.jpg"));
			out = Lab01.szarosciS(in);
			ImageIO.write(out, "jpg", new File("03.jpg"));
			out = Lab01.histnorm(out);
			ImageIO.write(out, "jpg", new File("03b.jpg"));
			out = Lab01.szarosciN(in);
			ImageIO.write(out, "jpg", new File("04.jpg"));
			out = Lab01.histnorm(out);
			ImageIO.write(out, "jpg", new File("04b.jpg"));
		} catch (IOException e) {
			System.out.println("W module Lab01 pad³o: " + e.toString());
		}
	}

	// tworzenie negatywu - spacer po wszystkich pikselach i zamiana ich
	// wartoœci R, G i B na 255 - wartoœæ
	public static BufferedImage negatyw(BufferedImage in) {
		BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
		int height, width;
		width = out.getWidth();
		height = out.getHeight();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int color = in.getRGB(i, j);
				int r = RGB.getR(color);
				int g = RGB.getG(color);
				int b = RGB.getB(color);
				color = RGB.toRGB(255 - r, 255 - g, 255 - b);
				out.setRGB(i, j, color);
			}
		}
		return out;
	}

	// tworzenie odcieni szaroœci - (R+G+B)/3
	public static BufferedImage szarosciS(BufferedImage in) {
		BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
		int width, height;
		width = out.getWidth();
		height = out.getHeight();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int color = in.getRGB(i, j);
				int r = RGB.getR(color);
				int g = RGB.getG(color);
				int b = RGB.getB(color);
				color = RGB.toRGB((r + g + b) / 3, (r + g + b) / 3, (r + g + b) / 3);
				out.setRGB(i, j, color);
			}
		}
		return out;
	}

	// tworzenie odcieni szaroœci - 0.3*R+0.59*G+0.11*B
	public static BufferedImage szarosciN(BufferedImage in) {
		BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
		int width, height;
		width = out.getWidth();
		height = out.getHeight();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int color = in.getRGB(i, j);
				int r = RGB.getR(color);
				int g = RGB.getG(color);
				int b = RGB.getB(color);
				int newColor = (int) (0.3 * r + 0.59 * g + 0.11 * b);
				color = RGB.toRGB(newColor, newColor, newColor);
				out.setRGB(i, j, color);
			}
		}
		return out;
	}

	// normalizacja histogramu
	public static BufferedImage histnorm(BufferedImage in) {
		BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
		int width, height, R, G, B;
		width = out.getWidth();
		height = out.getHeight();
		int colorF = in.getRGB(0, 0);
		int minR = RGB.getR(colorF);
		int minG = RGB.getR(colorF);
		int minB = RGB.getR(colorF);
		int maxR = RGB.getR(colorF);
		int maxG = RGB.getR(colorF);
		int maxB = RGB.getR(colorF);
		// szukanie minimów i maksimów
		for (int i = 1; i < width; i++) {
			for (int j = 1; j < height; j++) {
				int color = in.getRGB(i, j);
				int r = RGB.getR(color);
				int g = RGB.getG(color);
				int b = RGB.getB(color);
				minR = (r < minR ? r : minR);
				minG = (g < minG ? g : minG);
				minB = (b < minB ? b : minB);
				maxR = (r > maxR ? r : maxR);
				maxG = (g > maxG ? g : maxG);
				maxB = (b > maxB ? b : maxB);
			}
		}
		// w³aœciwa normalizacja

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int color = in.getRGB(i, j);
				int r = RGB.getR(color);
				int g = RGB.getG(color);
				int b = RGB.getB(color);
				int nR = (int) ((((double) r - (double) minR) / ((double) maxR - (double) minR)) * 255);
				int nG = (int) ((((double) g - (double) minG) / ((double) maxG - (double) minG)) * 255);
				int nB = (int) ((((double) b - (double) minB) / ((double) maxB - (double) minB)) * 255);
				color = RGB.toRGB(nR, nG, nB);
				// System.out.println(nR+" "+nG+ " "+nB);
				out.setRGB(i, j, color);
			}
		}
		System.out.println(minR + " " + minG + " " + minB + " " + maxR + " " + maxG + " " + maxB);
		return out;
	}
}