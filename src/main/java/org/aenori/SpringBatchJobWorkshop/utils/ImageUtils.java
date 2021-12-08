package org.aenori.SpringBatchJobWorkshop.utils;

import org.springframework.data.util.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class ImageUtils {
    public static Pair<Integer, Integer> getImageSize(String filename) throws IOException {
        return getImageSize(ImageIO.read(new File(filename)));
    }

    public static Pair<Integer, Integer> getImageSize(BufferedImage img) {
        return Pair.of(img.getWidth(), img.getHeight());
    }

    public static BufferedImage cropToSquare(String filename) throws IOException {
        return cropToSquare(ImageIO.read(new File(filename)));
    }

    public static BufferedImage cropToSquare(BufferedImage img) {
        Integer minDimension = Math.min(img.getWidth(), img.getHeight());

        return img.getSubimage(0, 0, minDimension, minDimension);
    }

    public static BufferedImage resizeToSquareSize(BufferedImage img, Integer sideSize) {
        Image resultingImage = img.getScaledInstance(sideSize, sideSize, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(sideSize, sideSize, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

        return outputImage;
    }
}
