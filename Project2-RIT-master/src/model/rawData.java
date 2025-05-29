package model;

import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/** CSCI-140: CSAPX Project 2
 * The rawData class, which handles the bulk of file modification and processes used. Represents the quadtree data
 * structure.
 *
 * Contains functions that can read and interpret uncompressed and compressed files, as well as fields for storing the
 * data recieved.
 *
 * Also contains alternate versions of the different classes used exclusively in formatting and outputting the GUI.
 *
 * @author Matthew Orlic
 * @author Griffin Danner-Doran
 */

public class rawData {

    /**
     * stores a 2d array of the values given in the uncompressed file
     */
    private int[][] rawdata;

    /**
     * stores the dimensions obtained from reading through the uncompressed file
     */
    private int dimensions;

    /**
     * stores the total number of pixels in the rawData 2d array
     */
    private int size;

    /**
     * stores the filename of the raw data read into the model
     */
    private String rawfilename;

    /**
     * default constructor for rawData
     */
    public rawData() {
    }

    /**
     * constructor for rawData, takes in a dimension value
     *
     * @param d, the dimensions of the uncompressed image
     */
    public rawData(int d) {
        rawdata = new int[d][d];
    }

    /**
     * adds all the data to a string with each value on a new line, used to read the rawdata[][] array as an uncompressed
     * file.
     *
     * @return the data as a string
     */
    @Override
    public String toString() {
        String listofpixels = "";
        for (int i = 0; i < rawdata.length; i++) {
            for (int j = 0; j < rawdata[0].length; j++) {
                listofpixels += rawdata[i][j] + "\n";
            }
        }
        return listofpixels;
    }

    /**
     * sets the dimensions of image
     *
     * @param d the total size
     */
    public void setSize(int d) {
        this.size = d;
        this.dimensions = (int) Math.sqrt(d);
    }

    /**
     * @return the total pixels in the image
     */
    public int getSize() {
        return this.size;
    }


    /**
     * Scans through the given uncompressed file
     * Scans through the given file once, obtaining the total number of entries.
     * Finds the square root of this number, and if the file is a square, sets the dimensions to match this sqrt.
     * <p>
     * Scans through the file again, adding each scanned int to a 2d array.
     * <p>
     * rawdata, dimensions, and rawfilename are all assigned their correct value.
     *
     * @param filename, the string containing the given file from the command line
     */
    public void ScanPixels(String filename) throws FileNotFoundException,CustomExceptions,InputMismatchException {
            Scanner f = new Scanner(new File(filename));
            int dimcounter = 0;

            while (f.hasNext()) {
                dimcounter += 1;
                f.nextLine();
            }
            f.close();
            if ((Math.sqrt(dimcounter) % 1 != 0 || !((int) (Math.ceil((Math.log(dimcounter) / Math.log(2))))
                    == (int) (Math.floor(((Math.log(dimcounter) / Math.log(2)))))))) {
                throw new CustomExceptions("Image is not a square");
            }
            this.size = dimcounter;
            dimcounter = (int) (Math.sqrt(dimcounter));
            int[][] pixels = new int[dimcounter][dimcounter];
            Scanner p = new Scanner(new File(filename));
            for (int y = 0; y < dimcounter; y++) {
                for (int x = 0; x < dimcounter; x++) {
                    pixels[y][x] = p.nextInt();
                    if (pixels[y][x] < 0 || pixels[y][x] > 255) {
                        throw new CustomExceptions("value is not a pixel color");
                    }
                }
            }
            // close the input file
            p.close();
            this.dimensions = dimcounter;
            this.rawdata = pixels;
            this.rawfilename = filename;
    }

    /**
     * Get the dimensions of the image
     *
     * @return the dimensions of the image
     */
    public int getDimensions() {
        return this.dimensions;
    }

    /**
     * Get the filename of the raw file that was read
     *
     * @return the name of the raw file
     */
    public String getRawFilename() {
        return this.rawfilename;
    }


    /**
     * drawGreyscale
     * <p>
     * Loops through the 2d array storing the pixel values of the image and adds each value as an rgb rectangle to the
     * graphics context, using the pixel values position to choose its respective location on the canvas.
     *
     * @param gc, the GraphicsContext of the canvas being used
     */
    public void drawGreyscale(GraphicsContext gc) {
        for (int y = 0; y < this.dimensions; y++) {
            for (int x = 0; x < this.dimensions; x++) {
                Color c = Color.rgb(this.rawdata[y][x], this.rawdata[y][x], this.rawdata[y][x]);
                gc.setFill(c);
                gc.fillRect(x, y, 1, 1);
            }
        }
    }

    /**
     * CustomExceptions class
     * <p>
     * Creates a custom exception for this program that contains the message specified when the exception is thrown.
     */
    public static class CustomExceptions extends Exception {
        public CustomExceptions(String msg) {
            super(msg);
        }
    }

    /**
     * reads through the values of a node in preorder and prints them
     *
     * @param ex      the node it is reading from
     * @param format, the form the string will be in, vertical or horizontal
     * @return listofnodes, the list format of the RITQTNode in preorder form
     */
    public String RITQTNodePreorder(RITQTNode ex, String format) {
        String listofnodes = "";
        if (format.equals("transcribe")) {
            listofnodes += "\n";
        }
        if (ex.getVal() == -1) {
            listofnodes += ex.getVal() + " ";
            listofnodes += RITQTNodePreorder(ex.getUpperLeft(), format);
            listofnodes += RITQTNodePreorder(ex.getUpperRight(), format);
            listofnodes += RITQTNodePreorder(ex.getLowerLeft(), format);
            listofnodes += RITQTNodePreorder(ex.getLowerRight(), format);
        } else {
            listofnodes += ex.getVal() + " ";
        }
        return listofnodes;
    }

    /**
     * reads the data from a ritqtnode and adds it to a 2d array
     *
     * @param cur        the node being read
     * @param dimensions the dimensions the tree represents
     * @param cornerX    the horizontal location in the tree of the node
     * @param cornerY    the vertical location in the tree of the node.
     */
    public void read(RITQTNode cur, int dimensions, int cornerX, int cornerY) {
        int reald = dimensions / 2;
        if (cur.getVal() == -1) {
            read(cur.getUpperLeft(), reald, cornerX, cornerY);
            read(cur.getUpperRight(), reald, cornerX + reald, cornerY);
            read(cur.getLowerLeft(), reald, cornerX, cornerY + reald);
            read(cur.getLowerRight(), reald, cornerX + reald, cornerY + reald);
        } else {
            for (int y = cornerY; y < cornerY + dimensions; y++) {
                for (int x = cornerX; x < cornerX + dimensions; x++) {
                    rawdata[y][x] = cur.getVal();
                }
            }
        }
    }

    /**
     * reads a string to a file, or creates one and writes to it if it does not exist. Must be given an existing
     * folder to add a text file to, cannot create new directories.
     *
     * @param filename the file being read to
     * @param text     the string being put in the file
     */
    public static void createFile(String filename, String text) throws IOException  {
            File newFile = new File(filename);
            if (newFile.createNewFile()) {
                System.out.println("File created: " + newFile.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(text);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
    }


    /**
     * turns a compressed quadtree into nodes so they can be read properly
     *
     * @param cur the list of values from the qtree
     * @return the node
     */
    public static RITQTNode parse(ArrayList<Integer> cur) {
        int i = cur.remove(0);
        RITQTNode node;
        if (i == -1) {
            node = new RITQTNode(i, parse(cur), parse(cur), parse(cur), parse(cur));
        } else {
            node = new RITQTNode(i);
        }
        return node;
    }

    /**
     * @param file, the name of the file that is being written to and needs to be checked.
     * @param type, the type of file being checked, uncompressed or compressed
     * @return true if the files match, false if they do not
     * <p>
     * Takes in a given output file and compares the image example given by CSAPX to check correctness of transcription.
     */
    public boolean checkfiles(String file, String type) {
        String[] filenameparser = file.split("/", 3);
        String realfile = filenameparser[filenameparser.length - 1];
        String f1 = "";
        String f2 = "";
        ArrayList<String> f1s = new ArrayList<>();
        ArrayList<String> f2s = new ArrayList<>();
        if (type.equals("uncompress")) {
            f1 = "images/uncompressed/" + realfile;
            f2 = "output/uncompressed/" + realfile;

        } else if (type.equals("compress")) {
            f1 = "images/compressed/" + realfile;
            f2 = "output/compressed/" + realfile;
        }
        try {
            Scanner s1 = new Scanner(new File(f1));
            while (s1.hasNext()) {
                f1s.add(Integer.toString(s1.nextInt()));
            }
            s1.close();
            Scanner s2 = new Scanner(new File(f2));
            while (s2.hasNext()) {
                f2s.add(Integer.toString(s2.nextInt()));
            }
            s2.close();
            if (f1s.size() == f2s.size()) {
                for (int i = 0; i < f1s.size(); i++) {
                    if (!(f1s.get(i).equals(f2s.get(i)))) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } catch (FileNotFoundException fn) {
            System.out.println(fn.getMessage());
            System.exit(1);
        }
        return true;
    }

    /**
     * Compresses a set of data into a quadtree using RITQTNodes
     * @param dimensions the dimensions of the image
     * @param cornerX The horizontal location in the image of the corner being started on
     * @param cornerY The vertical location in the image of the corner being started on
     * @return A RITQTNode at the start of the compressed tree
     */
    public RITQTNode compress(int dimensions, int cornerX, int cornerY) {
        int reald = dimensions / 2;
        int last = rawdata[cornerY][cornerX];
        RITQTNode node;
        for (int y = cornerY; y < cornerY + dimensions; y++) {
            for (int x = cornerX; x < cornerX + dimensions; x++) {
                if (rawdata[y][x] != last) {
                    node = new RITQTNode(-1, compress(reald, cornerX, cornerY),
                            compress(reald, cornerX + reald, cornerY),
                            compress(reald, cornerX, cornerY + reald),
                            compress(reald, cornerX + reald, cornerY + reald));
                    return node;
                }
                last = rawdata[y][x];
            }
        }
        node = new RITQTNode(last);
        return node;
    }

    /**
     * Determines what percent smaller the compressed image is than the original
     * @param unl how big the uncompressed file is
     * @param cl how big the compressed file is
     * @return the percent difference between the file sizes
     */
    public double compressionpercent(int unl, int cl) {
        double decrease = unl - cl;
        return (decrease / (double) unl) * 100.0;
    }

    /** a modified version of the main code in RITUncompress.
     *
     * Rather than printing directly to terminal, this version returns a string containing the relevant information,
     * or in the case of an error in this function or any functions called, the error message.
     *
     * @param file1 the file being uncompressed
     * @param file2 the file uncompressing to
     * @return the print string
     */
    public String GUIUncompress(String file1, String file2) {
        String output = "";
        try {

            if (file1 == null || file2 == null || file1.equals("") || file2.equals("")) {
                rawData.CustomExceptions CMDmissing = new rawData.CustomExceptions("Usage: java RITCompress uncompressed-file.txt compressed-file.rit");
                throw CMDmissing;
            }
            Scanner file = new Scanner(new File(file1));
            int pix = file.nextInt();
            rawData model = new rawData((int) Math.sqrt(pix));
            model.setSize(pix);
            ArrayList<Integer> vals = new ArrayList<>();
            while (file.hasNextInt()) {
                vals.add(file.nextInt());
            }
            RITQTNode uncompress = model.parse(vals);
            output += ("Uncompressing " + file1 + "\n");
            output += ("QTree: " + model.RITQTNodePreorder(uncompress, "display"));
            model.read(uncompress, model.getDimensions(), 0, 0);
            output += ("\n" + "Output file: " + file2 + "\n");
            model.createFile(file2, model.toString());
        } catch (rawData.CustomExceptions ex) {
            output=(ex.getMessage());
        }catch (FileNotFoundException ex) {
            output=(ex.getMessage());
        }catch (IOException e) {
            output=("An error occurred in writing or creating the file.");
        }
        return output;
    }

    /** a modified version of the main code in RITCompress
     *
     * Rather than printing directly to terminal, this version returns a string containing the relevant information,
     * or in the case of an error in this function or any functions called, the error message.
     *
     * @param file1 the file being compressed
     * @param file2 the file compressing to
     * @return the print string
     */
    public String GUICompress(String file1, String file2) {
        String output = "";
        try {
            if (file1 == null || file2 == null || file1.equals("") || file2.equals("")){
                rawData.CustomExceptions CMDmissing = new rawData.CustomExceptions("Usage: java RITCompress uncompressed-file.txt compressed-file.rit");
                throw CMDmissing;
            }
            rawData model = new rawData();
            model.ScanPixels(file1);
            output += ("Compressing " + file1 + "\n");
            RITQTNode compressed = model.compress(model.getDimensions(), 0, 0);
            String dis = model.RITQTNodePreorder(compressed, "display");
            output += ("QTree: " + dis);
            output += ("\n" + "Output file: " + file2 + "\n");
            output += ("Raw Image Size: " + model.getSize() + "\n");
            String[] lengthchecker = dis.split(" ");
            //+1 represents the size, which is not shown in the display string but is in the length of the file write
            output += ("Compressed Image Size: " + (lengthchecker.length + 1) + "\n");
            output += ("Compression%: " + model.compressionpercent(model.getSize(), (lengthchecker.length + 1)) + "\n");
            model.createFile(file2, (model.getSize() + " " + model.RITQTNodePreorder(compressed, "transcribe")));
        } catch (FileNotFoundException ex){
            output=(ex.getMessage());
        }catch (rawData.CustomExceptions ex) {
            output=(ex.getMessage());
        } catch (InputMismatchException nonint) {
            output=("non-integer value found. Please use a valid file.");
        }catch (IOException e) {
            output=("An error occurred in writing or creating the file.");
        }
        return output;
    }

    /**
     * a modified version of the main code in RITViewer
     *
     * Returns a filled in canvas to be used by the GUI. In the case of an error, errors are thrown directly to the
     * GUI's code, where they can be dealt with.
     *
     * @param filename the name of the file being read
     * @return the canvas with the image on it
     * @throws FileNotFoundException
     * @throws CustomExceptions
     * @throws InputMismatchException
     */
    public Canvas RITViewer(String filename)throws FileNotFoundException,CustomExceptions,InputMismatchException{
        rawData viewer = new rawData();
            if(filename == null || filename.equals("")){
                rawData.CustomExceptions CMDmissing = new rawData.CustomExceptions("incorrect " +
                        "arguments given. Usage: java RITViewer filename.txt");
                throw CMDmissing;
            }
            viewer.ScanPixels(filename);

        Canvas canvas = new Canvas(viewer.getDimensions(), viewer.getDimensions());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        viewer.drawGreyscale(gc);
        return canvas;
    }
}
