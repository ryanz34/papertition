package pages;

import gui.utils.ImageTools;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Page {
    public ImageIcon icon;
    public BufferedImage image;
    public int documentID;
    public int page;
    public String path;

    public Page(String path, int page, int documentID) {
        this.documentID = documentID;
        this.page = page;
        this.path = path;

        try {
            this.image = ImageIO.read( new File(path));

            this.icon = new ImageIcon(ImageTools.resize(this.image, 300, (int) (this.image.getWidth() * (300.0 / this.image.getHeight()))));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return String.format("Path: %s Document ID: %d Page: %d", path, documentID, page);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
