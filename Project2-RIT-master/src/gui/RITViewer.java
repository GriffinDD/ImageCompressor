package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import model.*;

/** CSCI-140: CSAPX Project 2
 * the RITViewer class and GUI
 * Uses uncompressed, raw data from a file to create an image.
 *
 * Uses javafx to show a picture based on the data.
 * @author Matthew Orlic
 * @author Griffin Danner-Doran
 */

public class RITViewer extends Application {
    private rawData viewer = new rawData();

    /**
     * init
     *
     *Inititalizes values relevant to making the GUI.
     *
     * Reads in the arguments the GUI was launched with and calls the scanner method to go through the given file and
     * set the classes pixelchart
     *
     */
    public void init(){
        List<String> args = getParameters().getRaw();
        try{
            viewer.ScanPixels(args.get(0));
        }catch(FileNotFoundException ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }catch (rawData.CustomExceptions ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        } catch (InputMismatchException nonint) {
            System.out.println("non-integer value found. Please use a valid file.");
            System.exit(1);
        }
    }

    /**
     * start
     *
     *Sets up the neccessary requirements for using the canvas class in Java and calls the greyscale method to loop
     * through the pixel chart and puts the resulting canvas on the scene.
     *
     * @param stage, the Stage being used by this GUI
     */
    @Override
    public void start(Stage stage) throws Exception {
        Group g = new Group();
        Canvas canvas = new Canvas(viewer.getDimensions(), viewer.getDimensions());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        viewer.drawGreyscale(gc);
        g.getChildren().add(canvas);
        stage.setScene(new Scene(g));
        stage.setTitle("RITViewer " + viewer.getRawFilename());
        stage.show();
    }
    /**
     * main method
     *
     *Launches the GUI is command line arguments are present
     *
     * @param args, an array of strings representing the command line arguments.
     */
    public static void main(String[] args) {
        try{
            if(args.length != 1){
                rawData.CustomExceptions CMDmissing = new rawData.CustomExceptions("incorrect " +
                        "arguments given. Usage: java RITViewer filename.txt");
                throw CMDmissing;
            }
            Application.launch(args);
        }catch(rawData.CustomExceptions noCMD ){
            System.out.println(noCMD.getMessage());
            System.exit(1);
        }
    }

    }
