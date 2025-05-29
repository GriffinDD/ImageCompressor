package ptui;

import model.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/** CSCI-140: CSAPX Project 2
 *
 * Take a text file containing integer representations of pixels in a image and compresses it
 * into a RITQTNode format. Displays uncompressed and compressed size, as well as compression percentage.
 *
 *  * @author Matthew Orlic
 *  * @author Griffin Danner-Doran
 */

public class RITCompress {
    /**
     *
     * @param args the inputs, the compressed file and the file the uncompressed version is sent to
     *
     * runs everything to compress an uncompressed image and transcribe into onto a new file, as well as display
     * relevant statistics.
     */
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.out.println("Usage: java RITCompress uncompressed-file.txt compressed-file.rit");
                System.exit(1);
            }
            rawData model = new rawData();
            model.ScanPixels(args[0]);
            System.out.println("Compressing " + args[0]);
            RITQTNode compressed = model.compress(model.getDimensions(), 0, 0);
            String dis = model.RITQTNodePreorder(compressed, "display");
            System.out.print("QTree: " + dis);
            System.out.println("\n" + "Output file: " + args[1]);
            System.out.println("Raw Image Size: " + model.getSize());
            String[] lengthchecker = dis.split(" ");
            //+1 represents the size, which is not shown in the display string but is in the length of the file write
            System.out.println("Compressed Image Size: " + (lengthchecker.length+ 1));
            System.out.println("Compression%: " + model.compressionpercent(model.getSize(),(lengthchecker.length+ 1)));
            model.createFile(args[1],(model.getSize() + " " + model.RITQTNodePreorder(compressed, "transcribe")));
            System.out.println(model.checkfiles(args[1], "compress"));
        }catch (FileNotFoundException ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }catch (rawData.CustomExceptions ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        } catch (InputMismatchException nonint) {
            System.out.println("non-integer value found. Please use a valid file.");
            System.exit(1);
        }catch (IOException e) {
            System.out.println("An error occurred in writing or creating the file.");
            System.exit(1);
        }
    }
}
