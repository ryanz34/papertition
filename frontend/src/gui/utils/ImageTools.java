package gui.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageTools {
    public static BufferedImage resize(BufferedImage srcImg, int h, int w) {
        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = newImage.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return newImage;
    }
}
