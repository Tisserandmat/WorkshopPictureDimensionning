package org.aenori.SpringBatchJobWorkshop.utils;

import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ImageUtilsTest {
    private static final String testImage = "src/test/resources/image_for_test.png";

    private BufferedImage bufferedImage;

    private BufferedImage getBufferedImage() {
        if(bufferedImage == null) {
            try {
                bufferedImage = ImageIO.read(new File(testImage));
            } catch (IOException e) {
                fail("Test image not found");
            }
        }

        return bufferedImage;
    }

    @Test
    void getImageSize() throws IOException {
        Pair<Integer, Integer> size = ImageUtils.getImageSize(getBufferedImage());

        assertEquals(480, size.getFirst());
        assertEquals(270, size.getSecond());
    }

    @Test
    void testCropToSquare() {
        BufferedImage croppedImg = ImageUtils.cropToSquare(getBufferedImage());
        Pair<Integer, Integer> croppedSize = ImageUtils.getImageSize(croppedImg);
        assertEquals(270, croppedSize.getFirst());
        assertEquals(270, croppedSize.getSecond());

        // Checking it didn't resize original image
        Pair<Integer, Integer> size = ImageUtils.getImageSize(getBufferedImage());
        assertEquals(480, size.getFirst());
    }

    @Test
    void resizeToSquareSize() {
        BufferedImage croppedImg = ImageUtils.cropToSquare(getBufferedImage());
        BufferedImage resizedImage = ImageUtils.resizeToSquareSize(croppedImg, 500);

        assertEquals(270, croppedImg.getWidth());
        assertEquals(270, croppedImg.getHeight());

        assertEquals(500, resizedImage.getWidth());
        assertEquals(500, resizedImage.getHeight());
    }
}