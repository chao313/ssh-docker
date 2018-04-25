package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *@author Leo Zhang
 */
public class GraphicUtil {

	private GraphicUtil() {

	}

	// the image format, JPEG extension .jpg MIME: image/jpeg
	public static String GRAPHIC_JPEG = "JPEG";

	// the image format, PNG extension .png MIME: image/png
	public static String GRAPHIC_PNG = "PNG";

	private static String randNumber() {
		// default character number
		final int DEFAULT_CHAR_COUNT = 6;

		Random r = new Random();
		StringBuffer number = new StringBuffer(DEFAULT_CHAR_COUNT);
		for (int i = 0; i < DEFAULT_CHAR_COUNT; i++) {
			number.append(r.nextInt(10));
		}
		return number.toString();
	}

	private static String randAlpha() {
		// default character number
		final int DEFAULT_CHAR_COUNT = 5;

		Random r = new Random();
		StringBuffer alpha = new StringBuffer(DEFAULT_CHAR_COUNT);

		for (int i = 0; i < DEFAULT_CHAR_COUNT; i++) {
			alpha.append((char) (r.nextInt(26) + 97));
		}
		return alpha.toString();
	}

	/**
	 * Draws a random image
	 * 
	 * @param charValue
	 * @param graphicFormat
	 *            Image format <code>GRAPHIC_JPEG</code> or
	 *            <code>GRAPHIC_PNG</code>
	 * @param out
	 * @return random string, equals <code>charValue</code>
	 * @throws IOException
	 */
	private static String draw(String charValue, String graphicFormat,
			OutputStream out) throws IOException {

		// font size
		final int FONT_SIZE = 18;
		// One character's height and width
		final int WORD_HEIGHT = 13;
		final int WORD_WIDTH = 15;
		// position in vertical direction
		final int Y_POS = 21;
		// initial position in plane direction
		final int INIT_X_POS = 12;
		final Color color = Color.BLACK;
		final int[] fontStyles = new int[] { Font.BOLD, Font.CENTER_BASELINE,
				Font.HANGING_BASELINE, Font.ITALIC, Font.LAYOUT_LEFT_TO_RIGHT,
				Font.LAYOUT_NO_LIMIT_CONTEXT, Font.LAYOUT_NO_START_CONTEXT,
				Font.LAYOUT_RIGHT_TO_LEFT, Font.PLAIN, Font.ROMAN_BASELINE };

		Random r = new Random();

		// set image height and width
		int w = (charValue.length() + 1) * WORD_WIDTH;
		int h = WORD_HEIGHT * 2;

		// create image in memory
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();

		// set background color
		// Color backColor = Color.WHITE;
		// g.setBackground(backColor);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		// draw 70 disturbing lines
		g.setColor(Color.GRAY);
		for (int i = 0; i < 70; i++) {
			int x = r.nextInt(w - 5);
			int y = r.nextInt(h - 5);
			int xl = r.nextInt(15);
			int yl = r.nextInt(15);
			g.drawLine(x, y, x + xl, y + yl);
		}

		// set font
		g.setFont(new Font(null, Font.BOLD, FONT_SIZE));

		// draw every char
		for (int i = 0; i < charValue.length(); i++) {
			String c = charValue.substring(i, i + 1).toUpperCase();
			g.setColor(color);
			int fontStyle = fontStyles[r.nextInt(fontStyles.length)];
			g.setFont(new Font(null, fontStyle, FONT_SIZE));
			g.drawString(c, (WORD_WIDTH * i + INIT_X_POS), Y_POS);
		}
		g.dispose();
		bi.flush();
		// output
		ImageIO.write(bi, graphicFormat, out);

		return charValue;
	}

	/**
	 * Draws a random image include number
	 * 
	 * @param graphicFormat
	 *            Image format <code>GRAPHIC_JPEG</code> or
	 *            <code>GRAPHIC_PNG</code>
	 * @param out
	 * @return random number string
	 * @throws IOException
	 */
	public static String drawNumber(String graphicFormat, OutputStream out)
			throws IOException {
		String charValue = randNumber();
		return draw(charValue, graphicFormat, out);

	}

	/**
	 * Draws a random image include alphabets
	 * 
	 * @param graphicFormat
	 *            Image format <code>GRAPHIC_JPEG</code> or
	 *            <code>GRAPHIC_PNG</code>
	 * @param out
	 * @return random alphbets string
	 * @throws IOException
	 */
	public static String drawAlpha(String graphicFormat, OutputStream out)
			throws IOException {
		String charValue = randAlpha();
		return draw(charValue, graphicFormat, out);

	}

	/**
	 * Draws a reduced and wartermark image. See functions:
	 * <code>reduce(String filename, String outFilename, int width, int height)</code>
	 * and
	 * <code>watermark(String filename, String outFilename, String wartmarkWords, String wartmarkFilename)</code>
	 * 
	 * @param filename
	 * @param outFilename
	 * @param wartmarkWords
	 * @param wartmarkFilename
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	public static void reduceAndWatermark(String filename, String outFilename,
			String wartmarkWords, String wartmarkFilename, int width, int height)
			throws IOException {
		final Color frontColor = Color.RED;
		final Color backgroundColor = Color.WHITE;

		// TODO calculate location
		int watermarkLocationX = 100;
		int watermarkLocationY = 100;
		int wordLocationX = 100;
		int wordLocationY = 100;

		// Read source file
		ImageIcon srcFile = new ImageIcon(filename);
		// Build Image instance
		Image srcImage = srcFile.getImage();
		// Build BufferedImage instance
		BufferedImage image = new BufferedImage(srcImage.getWidth(null),
				srcImage.getHeight(null), BufferedImage.TYPE_INT_RGB);

		Graphics2D g = image.createGraphics();
		g.setColor(frontColor);
		g.setBackground(backgroundColor);
		g.drawImage(srcImage, 0, 0, width, height, null);

		if (wartmarkWords != null) {
			g.drawString(wartmarkWords, wordLocationX, wordLocationY);
		} else if (wartmarkFilename != null) {
			ImageIcon watermarkFile = new ImageIcon(wartmarkFilename);
			Image watermarkImage = watermarkFile.getImage();
			g.drawImage(watermarkImage, watermarkLocationX, watermarkLocationY,
					null);
		}

		paint(image, GRAPHIC_JPEG, outFilename);
	}

	/**
	 * Draws a reduced image
	 * 
	 * @param filename
	 *            source file name
	 * @param outFilename
	 *            output file name, out file is a jpeg image.
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	public static void reduce(String filename, String outFilename, int width,
			int height) throws IOException {

		// Read source file
		ImageIcon srcFile = new ImageIcon(filename);
		// Build Image instance
		Image srcImage = srcFile.getImage();
		int srcWidth = srcImage.getWidth(null);
		int srcHeight = srcImage.getHeight(null);
		if (srcWidth > 0 && srcHeight > 0 && width > srcWidth
				&& height > srcHeight) {
			width = srcWidth;
			height = srcHeight;
		}
		// Build BufferedImage instance
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		// draw the reduced image
		image.getGraphics().drawImage(srcImage, 0, 0, width, height, null);
		paint(image, GRAPHIC_JPEG, outFilename);
	}

	/**
	 * Draws a wartermark image.
	 * 
	 * @param filename
	 *            source file name
	 * @param outFilename
	 *            output file name, out file is a jpeg image.
	 * @param wartmarkWords
	 *            wartmark words. Either
	 *            <code>wartmarkWords<code> or <code>wartmarkFilename<code> is useful.
	 *            <code>wartmarkWords<code> is preferred.
     *            if <code>wartmarkWords<code> and <code>wartmarkFilename<code> are null, it will not wartermark
	 * @param wartmarkFilename
	 *            wartmark image file name. Either <code>wartmarkWords<code> or
	 *            <code>wartmarkFilename<code> is required and valid.
	 *            <code>wartmarkWords<code> is preferred
	 * @throws IOException
	 */
	public static void watermark(String filename, String outFilename,
			String wartmarkWords, String wartmarkFilename) throws IOException {
		final Color frontColor = Color.RED;
		final Color backgroundColor = Color.WHITE;

		// TODO calculate location
		int watermarkLocationX = 100;
		int watermarkLocationY = 100;
		int wordLocationX = 100;
		int wordLocationY = 100;

		// Read source file
		ImageIcon srcFile = new ImageIcon(filename);
		// Build Image instance
		Image srcImage = srcFile.getImage();
		// Build BufferedImage instance
		BufferedImage image = new BufferedImage(srcImage.getWidth(null),
				srcImage.getHeight(null), BufferedImage.TYPE_INT_RGB);

		Graphics2D g = image.createGraphics();
		g.setColor(frontColor);
		g.setBackground(backgroundColor);
		g.drawImage(srcImage, 0, 0, null);

		if (wartmarkWords != null) {
			g.drawString(wartmarkWords, wordLocationX, wordLocationY);
		} else if (wartmarkFilename != null) {
			ImageIcon watermarkFile = new ImageIcon(wartmarkFilename);
			Image watermarkImage = watermarkFile.getImage();
			g.drawImage(watermarkImage, watermarkLocationX, watermarkLocationY,
					null);
		}

		g.dispose();
		paint(image, GRAPHIC_JPEG, outFilename);
	}

	private static void paint(BufferedImage image, String graphicFormat,
			String filename) throws IOException {

		FileOutputStream tagFile = null;
		try {
			tagFile = new FileOutputStream(filename);
			ImageIO.write(image, graphicFormat, tagFile);
		} finally {
			if (tagFile != null) {
				tagFile.close();
			}
		}
	}

	public static void main(String... args) throws Exception {
		FileOutputStream out = new FileOutputStream("/Users/melvin/1.jpg");
		drawNumber(GRAPHIC_JPEG, out);
		out.close();
	}
}
