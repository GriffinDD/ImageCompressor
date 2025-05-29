package gui;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;

import model.*;

/** CSCI-140: CSAPX Project 2
 *
 * Runs a program creating a GUI that can run the other commands in the project, including
 * compress, uncompress, and viewer.
 *
 * Uses Javafx to create the gui.
 *  * @author Matthew Orlic
 *  * @author Griffin Danner-Doran
 */

public class RITGUI extends Application {
    /**
     * Creates the user interface and sets everything up on it, runs the other functions when
     * the user takes actions
     * @param stage the stage the gui uses
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new Group(),1000,825);

        ComboBox GUIFunctions = new ComboBox();
        GUIFunctions.getItems().addAll(
                "RITViewer",
                "RITUncompress",
                "RITCompress",
                "Clear",
                "Close"
        );

        BorderPane core= new BorderPane();
        BorderPane UI = new BorderPane();
        GridPane operations = new GridPane();
        UI.setTop(operations);

        //creation and insertion of drop-down menu, label, and confirm button
        operations.setVgap(4);
        operations.setHgap(10);
        operations.setPadding(new Insets(5, 5, 5, 5));
        operations.add(new Label("Operations: "), 0, 0);
        operations.add(new Label("Choose files manually or with the chooser, then select an action and confirm your choice"), 3, 0);
        operations.add(GUIFunctions, 1, 0);
        Button confirm = new Button();
        confirm.setText("confirm action");
        operations.add(confirm, 2, 0);
        GridPane input = new GridPane();
        UI.setCenter(input);

        //creation and insertion of input button and text field
        Button inputFile = new Button();
        inputFile.setText("Input file");
        TextField inputbox = new TextField ();
        inputbox.setPrefColumnCount(80);
        input.setVgap(4);
        input.setHgap(10);
        input.setPadding(new Insets(5, 5, 5, 5));
        input.add(inputFile, 0, 0);
        input.add(inputbox, 1, 0);

        //creation and insertion of output button and text field
        GridPane output = new GridPane();
        UI.setBottom(output);
        Button outputFile = new Button();
        outputFile.setText("Output file");
        TextField outputbox = new TextField ();
        outputbox.setPrefColumnCount(80);
        output.setVgap(4);
        output.setHgap(10);
        output.setPadding(new Insets(5, 5, 5, 5));
        output.add(outputFile, 0, 0);
        output.add(outputbox, 1, 0);

        //implementation of input and output file choosers
        FileChooser inputChooser = new FileChooser();
        inputFile.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = inputChooser.showOpenDialog(stage);
                        if (file != null) {
                               inputbox.setText(file.getAbsolutePath());
                        }
                    }
                });
        FileChooser outputChooser = new FileChooser();
        outputFile.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = outputChooser.showOpenDialog(stage);
                        if (file != null) {
                            outputbox.setText(file.getAbsolutePath());
                        }
                    }
                });

        //creation and insertion output FlowPane and TextArea
        FlowPane status = new FlowPane();
        TextArea outputStatus = new TextArea();
        outputStatus.setWrapText(true);
        outputStatus.setPrefWidth(1000);
        outputStatus.setPrefHeight(200);
        outputStatus.setEditable(false);
        status.getChildren().add(outputStatus);

        //creation of placeholder blank canvas
        Canvas placeholder = new Canvas();
        placeholder.setWidth(1000);
        placeholder.setHeight(520);

        //implementation of confirm button
        confirm.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        actionTaken((String)GUIFunctions.getValue(),outputStatus, stage, inputbox,outputbox, core);
                    }
                });
        //insertion of key elements and final GUI set-up.
        core.setCenter(placeholder);
        core.setBottom(status);
        core.setTop(UI);
        Group root = (Group)scene.getRoot();
        root.getChildren().add(core);
        stage.setScene(scene);
        stage.setTitle("RITGUI");
        stage.show();
    }

    /**
     * Launches the gui
     * @param args the arguments it's running
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * How the program recognizes what the users wants done and runs it
     * @param action the action the user wants run
     * @param ex the text area to print to
     * @param stage the stage the gui uses
     * @param input the input file
     * @param output the file being output to
     * @param borderpane the pane the gui is on
     */
    public void actionTaken(String action, TextArea ex, Stage stage, TextField input, TextField output,BorderPane borderpane){
        rawData executer = new rawData();
        ex.setText(" ");
        if(action==null || action.length() == 0) {
                ex.setText("Please choose an operation before confirming");
        }else{
            if (action.equals("RITViewer")) {
                try {
                    Canvas image = executer.RITViewer(input.getText());
                    image.setWidth(1000);
                    image.setHeight(520);
                    borderpane.setCenter(image);
                    ex.setText("Viewing: " + input.getText() + "\n" + "Use clear to clear the image");
                }catch(FileNotFoundException e){
                    ex.setText(e.getMessage());
                }catch (rawData.CustomExceptions e) {
                    ex.setText(e.getMessage());
                } catch (InputMismatchException nonint) {
                    ex.setText("non-integer value found. Please use a valid file.");
                }
            } else if (action.equals("RITUncompress")) {
                ex.setText(executer.GUIUncompress(input.getText(),output.getText()));
            } else if (action.equals("RITCompress")) {
                ex.setText(executer.GUICompress(input.getText(),output.getText()));
            } else if (action.equals("Clear")) {
                input.setText("");
                output.setText("");
                Canvas placeholder = new Canvas();
                placeholder.setWidth(1000);
                placeholder.setHeight(520);
                borderpane.setCenter(placeholder);
            } else if (action.equals("Close")) {
                stage.close();
            }
        }
    }
}
