package ptui;

import model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;

/** CSCI-140: CSAPX Project 2
 * The RITUncompress class
 *
 * Takes a text file containing a quadtree representing a compressed image and puts it in an RTIQTNode object.
 * The RITQTNode is read out and uncompressed into the images original format, which is transcribed onto a text file.
 *
 * @author Matthew Orlic
 * @author Griffin Danner-Doran
 */

public class RITUncompress {

    /**
     *
     * @param args the inputs, the compressed file and the file the uncompressed version is sent to
     *
     * runs everything to uncompress a compressed image and transcribe into onto a new file
     */
    public static void main(String[] args){
        try {
            if (args.length != 2) {
                System.out.println("Usage: java RITUncompress compressed.rit uncompressed.txt");
                System.exit(1);
            }
            Scanner file = new Scanner(new File(args[0]));
            int pix = file.nextInt();
            rawData model = new rawData((int) Math.sqrt(pix));
            model.setSize(pix);
            ArrayList<Integer> vals = new ArrayList<>();
            while (file.hasNextInt()) {
                vals.add(file.nextInt());
            }
            RITQTNode uncompress = model.parse(vals);
            System.out.println("Uncompressing " + args[0]);
            System.out.print("QTree: " + model.RITQTNodePreorder(uncompress, "display"));
            model.read(uncompress, model.getDimensions(), 0, 0);
            System.out.println("\n" + "Output file: " + args[1]);
            model.createFile(args[1], model.toString());
            System.out.println(model.checkfiles(args[1], "uncompress"));
        }catch(FileNotFoundException ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }catch (IOException e) {
            System.out.println("An error occurred in writing or creating the file.");
            System.exit(1);
        }
    }

}


