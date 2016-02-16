package lab02;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Lab02 {
	public static void run(BufferedImage in) {
		try {
			BufferedImage out = Lab02.skalowanie(in, (float) (0.4));
			out = Lab02.skalowanie(out, (float) (2.5));
			ImageIO.write(out, "jpg", new File("05.jpg"));
			out = Lab02.progowanieSG(in);
			ImageIO.write(out, "jpg", new File("06a.jpg"));

//			int size[] = { 3, 5, 7, 9, 11 };
//			for (int pom_licz = 0; pom_licz < size.length; pom_licz++) {
//				out = Lab02.progowanieL(in, size[pom_licz]);
//				ImageIO.write(out, "jpg", new File("06b-" + (2 * size[pom_licz] + 1) + ".jpg"));
//			}

//			int size2[] = { 15, 25, 35 };
//			for (int pom_licz = 0; pom_licz < size2.length; pom_licz++) {
//				out = Lab02.progowanieLSG(in, 15, size2[pom_licz]);
//				ImageIO.write(out, "jpg", new File("06c-" + size2[pom_licz] + ".jpg"));
//			}

			int size3[][][] = { { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } }, { { 1, 1, 1 }, { 1, 2, 1 }, { 1, 1, 1 } },
					{ { 1, 2, 1 }, { 2, 4, 2 }, { 1, 2, 1 } }, { { 0, -1, 0 }, { -1, 5, -1 }, { 0, -1, 0 } },
					{ { -1, -1, -1 }, { -1, 9, -1 }, { -1, -1, -1 } }, { { 1, -2, 1 }, { -2, 5, -2 }, { 1, -2, 1 } } };
			for (int pom_licz = 0; pom_licz < size3.length; pom_licz++) {
				out = Lab02.filtrSplotowy(in, size3[pom_licz]);
				ImageIO.write(out, "jpg", new File("07-" + pom_licz + ".jpg"));
			}

		} catch (IOException e) {
			System.out.println("W module Lab02 padło: " + e.toString());
		}
	}

	// skalowanie obrazu
	public static BufferedImage skalowanie(BufferedImage in, float skala) {
		BufferedImage out = new BufferedImage(Math.round(skala * in.getWidth()), Math.round(skala * in.getHeight()),
				in.getType());
		int r, g, b, height, width;
		width = out.getWidth();
		height = out.getHeight();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// Tu wypełnij właściwym kodem
			}
		}
		return out;
	}

	// progowanie po średniej globalnej
	public static BufferedImage progowanieSG(BufferedImage in) {
		BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
		BufferedImage szary = Lab01.szarosciN(in); // Tu potrzebujesz kodu z
													// pierwszych zajęć
		int r, g, b, height, width;
		width = out.getWidth();
		height = out.getHeight();
		int czarny = 0x00000000;
		int bialy = 0x00ffffff;
		// obliczanie średniej
		int srednia = 0;
		int min = 255;
		int max = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int color = in.getRGB(i, j);
				r = RGB.getR(color);
				if (r > max) {
					max = r;
				}
				if (r < min) {
					min = r;
				}
			}
		}
		srednia = (max + min) / 2;

		// właściwe progowanie
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int color = in.getRGB(i, j);
				r = RGB.getR(color);
				if (r > srednia) {
					r = 255;
				} else {
					r = 0;
				}
				color = RGB.toRGB(r, r, r);
				out.setRGB(i, j, color);
			}
		}
		return out;
	}

	// progowanie lokalne
	public static BufferedImage progowanieL(BufferedImage in, int rozmiar) {
		BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
		BufferedImage szary = Lab01.szarosciN(in); // Tu potrzebujesz kodu z
													// pierwszych zajęć
		int height, width;
		int srednia_lokalna;
		int i_dol, i_gora, j_dol, j_gora;
		int sumcia = 0;
		width = out.getWidth();
		height = out.getHeight();
		int czarny = 0x00000000;
		int bialy = 0x00ffffff;
		BufferedImage temp = RGB.powiekszBialymi(szary, rozmiar);
		for (int i = (rozmiar / 2); i < width - (rozmiar / 2); i++) {
			for (int j = (rozmiar / 2); j < height - (rozmiar / 2); j++) {

				for (int k = i - (rozmiar / 2); k <= i + (rozmiar / 2); k++) {
					for (int l = j - (rozmiar / 2); l <= j + (rozmiar / 2); l++) {
						int color = temp.getRGB(k, l);
						sumcia += RGB.getR(color);
					}
				}
				int prog = sumcia / (rozmiar * rozmiar);
				int color = in.getRGB(i, j);
				int r = RGB.getR(color);
				if (r > prog) {
					r = 255;
				} else {
					r = 0;
				}
				color = RGB.toRGB(r, r, r);
				temp.setRGB(i, j, color);
				sumcia=0;
			}
		}

		return temp;
	}

	// progowanie lokalne ze średnią globalną
	public static BufferedImage progowanieLSG(BufferedImage in, int rozmiar, int odbieganie) {
		BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
		BufferedImage szary = Lab01.szarosciN(in); //Tu potrzebujesz kodu z
		// pierwszych zajęć
		int height, width;
		int srednia_lokalna;
		int i_dol, i_gora, j_dol, j_gora;
		width = out.getWidth();
		height = out.getHeight();
		int czarny = 0x00000000;
		int bialy = 0x00ffffff;

		BufferedImage temp = RGB.powiekszBialymi(szary, rozmiar);
		int srednia = 0;
		int min = 255;
		int max = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int color = in.getRGB(i, j);
				int r = RGB.getR(color);
				if (r > max) {
					max = r;
				}
				if (r < min) {
					min = r;
				}
			}
		}
		srednia = (max + min) / 2;
		
		for (int i = (rozmiar / 2); i < width - (rozmiar / 2); i++) {
			for (int j = (rozmiar / 2); j < height - (rozmiar / 2); j++) {
				int sumcia =0;
				for (int k = i - (rozmiar / 2); k <= i + (rozmiar / 2); k++) {
					for (int l = j - (rozmiar / 2); l <= j + (rozmiar / 2); l++) {
						int color = temp.getRGB(k, l);
						sumcia += RGB.getR(color);
					}
				}
				int color = temp.getRGB(i, j);
				int r = RGB.getR(color);
				int prog;
				int lokalna = sumcia / (rozmiar * rozmiar);
				if (Math.abs(srednia - lokalna)<odbieganie){
					prog=lokalna;
				}else{
					prog=srednia;
				}
				if(r<prog){
					r=0;
				}else{
					r=255;
				}
				color = RGB.toRGB(r, r, r);
				temp.setRGB(i, j, color);
			}
		}

		return temp;
	}

	// filtry splotowe
	public static BufferedImage filtrSplotowy(BufferedImage in, int maska[][]) {
		BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
		int height, width, mheight, mwidth;
		int r, g, b;
		mheight = maska[0].length - 1;
		mwidth = maska.length - 1;
		width = out.getWidth() - (int) (maska.length / 2);
		height = out.getHeight() - (int) (maska[0].length / 2);
		int suma =0 ;
		int ilosc = 0;
		for (int i = 0; i <= mwidth; i++) {
			for (int j = 0; j <= mheight; j++) {
				suma+=maska[i][j];
			}
		}

		// Tu wypełnij właściwym kodem
		int sumR;
		int sumG;
		int sumB;
		for (int i = 1; i < width; i++) {
			for (int j = 1; j < height; j++) {
				int color = in.getRGB(i-1, j-1);
				sumR = RGB.getR(color)*maska[0][0];
				sumG = RGB.getG(color)*maska[0][0];
				sumB = RGB.getB(color)*maska[0][0];
				color = in.getRGB(i-1, j);
				sumR += RGB.getR(color)*maska[0][1];
				sumG += RGB.getG(color)*maska[0][1];
				sumB += RGB.getB(color)*maska[0][1];

				color = in.getRGB(i-1, j+1);
				sumR += RGB.getR(color)*maska[0][2];
				sumG += RGB.getG(color)*maska[0][2];
				sumB += RGB.getB(color)*maska[0][2];

				color = in.getRGB(i, j-1);
				sumR += RGB.getR(color)*maska[1][0];
				sumG += RGB.getG(color)*maska[1][0];
				sumB += RGB.getB(color)*maska[1][0];

				color = in.getRGB(i, j);
				sumR += RGB.getR(color)*maska[1][1];
				sumG += RGB.getG(color)*maska[1][1];
				sumB += RGB.getB(color)*maska[1][1];

				color = in.getRGB(i, j+1);
				sumR += RGB.getR(color)*maska[1][2];
				sumG += RGB.getG(color)*maska[1][2];
				sumB += RGB.getB(color)*maska[1][2];

				color = in.getRGB(i+1, j-1);
				sumR += RGB.getR(color)*maska[2][0];
				sumG += RGB.getG(color)*maska[2][0];
				sumB += RGB.getB(color)*maska[2][0];
				
				color = in.getRGB(i+1, j);
				sumR += RGB.getR(color)*maska[2][1];
				sumG += RGB.getG(color)*maska[2][1];
				sumB += RGB.getB(color)*maska[2][1];
				
				color = in.getRGB(i+1, j+1);
				sumR += RGB.getR(color)*maska[2][2];
				sumG += RGB.getG(color)*maska[2][2];
				sumB += RGB.getB(color)*maska[2][2];
				
				sumR/=suma;
				sumG/=suma;
				sumB/=suma;
				
				color = RGB.toRGB(Math.abs(sumR), Math.abs(sumG), Math.abs(sumB));
				out.setRGB(i, j, color);
			}
		}

		return out;
	}
}
