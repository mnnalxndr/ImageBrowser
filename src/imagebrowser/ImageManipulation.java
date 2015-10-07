package imagebrowser;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author alexmann
 */
public class ImageManipulation {

    /**
     *
     * @param ref
     * @return
     */
    public static BufferedImage loadImage(String ref) {
        BufferedImage bimg = null;
        try {

            bimg = ImageIO.read(new File(ref));
        } catch (IIOException iioe) {
            System.out.println(iioe);
            JOptionPane.showMessageDialog(null, "The image file could not be loaded.\nIt is either the wrong format of the file path does not exist.\n Please try again.", "Load operation failed", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.out.print(e);
        }
        return bimg;
    }

    /**
     * 
     * @param pane
     * @param panel
     * @param load
     * @param filePath
     * @return 
     */
    public LoadedImage loadAndDisplayImage(JScrollPane pane, JImagePanel panel, boolean load, String filePath) {
        //load Image
        LoadedImage img = null;
        if (load) {
            FileNameExtensionFilter imgFilter = new FileNameExtensionFilter("All supported image formats\n(.jpg, .gif, .png, .bmp)", "jpg", "jpeg", "gif", "png", "bmp");
            FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("JPEG/JPG file", "jpg", "jpeg");
            FileNameExtensionFilter gifFilter = new FileNameExtensionFilter("GIF file", "gif");
            FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG file", "png");
            FileNameExtensionFilter bmpFilter = new FileNameExtensionFilter("Bitmap file", "bmp");

            JFileChooser chooser = new JFileChooser();

            chooser.setAcceptAllFileFilterUsed(false);
            chooser.addChoosableFileFilter(imgFilter);
            chooser.addChoosableFileFilter(jpgFilter);
            chooser.addChoosableFileFilter(gifFilter);
            chooser.addChoosableFileFilter(pngFilter);
            chooser.addChoosableFileFilter(bmpFilter);

            int retInt = chooser.showOpenDialog(null);
            if (retInt == JFileChooser.APPROVE_OPTION) {
                String loadFilePath = chooser.getSelectedFile().getPath();
                String loadFileName = chooser.getSelectedFile().getName();
                img = new LoadedImage(loadImage(loadFilePath), loadFilePath, loadFileName);

            }
        } else {
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            System.out.println(fileName);
            img = new LoadedImage(loadImage(filePath), filePath, fileName);
        }
        displayImage(pane, panel, img.image);
        return img;
    }

    /**
     *
     * @param img
     * @return newImg
     */
    public LoadedImage saveImage(BufferedImage img) {
        LoadedImage newImg = null;
        try {
            FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("jpg", "jpg", "jpeg");
            FileNameExtensionFilter gifFilter = new FileNameExtensionFilter("gif", "gif");
            FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("png", "png");
            FileNameExtensionFilter bmpFilter = new FileNameExtensionFilter("bmp", "bmp");
            JFileChooser chooser = new JFileChooser();

            chooser.setAcceptAllFileFilterUsed(false);
            chooser.addChoosableFileFilter(jpgFilter);
            chooser.addChoosableFileFilter(gifFilter);
            chooser.addChoosableFileFilter(pngFilter);
            chooser.addChoosableFileFilter(bmpFilter);

            int retInt = chooser.showSaveDialog(null);

            if (retInt == JFileChooser.APPROVE_OPTION) {
                String ref = chooser.getSelectedFile().getPath();

                String fileName = chooser.getSelectedFile().getName();
                String format = "";

                if (fileName.contains(".") == false) {

                    format = chooser.getFileFilter().getDescription();
                    ImageIO.write(img, format, new File(ref + "." + format));
                    newImg = new LoadedImage(img, ref, fileName);

                } else {
                    int fullStopCount = 0;
                    for (int i = 0; i < fileName.length(); i++) {
                        if (fileName.charAt(i) == '.') {
                            fullStopCount++;
                        }
                    }

                    if (fullStopCount > 1) {
                        JOptionPane.showMessageDialog(chooser, "Please do not use full stops in your filename");
                    } else {
                        if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")) {
                            format = "jpg";
                        } else if (fileName.endsWith(".gif")) {
                            format = "gif";
                        } else if (fileName.endsWith(".png")) {
                            format = "png";
                        } else if (fileName.endsWith(".bmp")) {
                            format = "bmp";
                        } else {
                            JOptionPane.showMessageDialog(chooser,
                                    "Please use a supported file format\n(.jpg, .gif, .png or .bmp");
                        }
                    }
                    ImageIO.write(img, format, new File(ref));
                    newImg = new LoadedImage(img, ref, fileName);
                }

            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return newImg;
    }
    
    /**
     * 
     * @param pane
     * @param panel
     * @param img 
     */
    public void displayImage(JScrollPane pane, JImagePanel panel, BufferedImage img) {
        panel = new JImagePanel(img);
        panel.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        pane.getViewport().add(panel);
    }

    //Image manipulation
    /**
     * 
     * @param pane
     * @param panel
     * @param img
     * @param vertical
     * @return 
     */
    public BufferedImage flipImage(JScrollPane pane, JImagePanel panel, BufferedImage img, boolean vertical) {

        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage newImg = new BufferedImage(w, h, img.getType());
        Graphics2D g = newImg.createGraphics();
        if (vertical) {
            System.out.println("FLIPVERT");
            g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
        } else {
            System.out.println("FLIPHORIZ");
            g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
        }
//        displayImage(pane, panel, newImg);
        g.dispose();
        return newImg;
    }

    /**
     * 
     * @param img
     * @param angle
     * @return 
     */
    public BufferedImage rotateImage(BufferedImage img, int angle) {

        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage newImg = new BufferedImage(h, w, img.getType());
        Graphics2D g = newImg.createGraphics();
        g.rotate(Math.toRadians(angle), w / 2, h / 2);
        int diff = (w - h) / 2;
        if (angle == 90) {
            g.translate(diff, diff);
        } else if (angle == 270) {
            g.translate(-diff, -diff);
        }
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return newImg;

    }

    /**
     * 
     * @param img
     * @param newWidth
     * @param newHeight
     * @return 
     */
    public BufferedImage zoomImage(BufferedImage img, int newWidth, int newHeight) {

        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage newImg = new BufferedImage(newWidth, newHeight, img.getType());
        Graphics2D g = newImg.createGraphics();
        g.drawImage(img, 0, 0, newWidth, newHeight, 0, 0, w, h, null);
        g.dispose();
        return newImg;

    }

    /**
     *
     */
    public class JImagePanel extends JPanel {

        BufferedImage image;

        /**
         *
         * @param image
         */
        public JImagePanel(BufferedImage image) {
            super();
            this.image = image;
            this.setSize(image.getWidth(), image.getHeight());

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null);

        }

    }

    /**
     * 
     */
    public class LoadedImage implements Serializable {

        BufferedImage image;
        String filePath, fileName;

        public LoadedImage(BufferedImage img, String filePath, String fileName) {
            this.image = img;
            this.filePath = filePath;
            this.fileName = fileName;

        }
    }

}
