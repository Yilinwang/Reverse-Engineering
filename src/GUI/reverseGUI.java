package GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.io.*;

public class reverseGUI extends Application {
    private void submitFunc(GridPane gridPane) {
        int count = 0;
		try{
			System.setOut(new PrintStream(new FileOutputStream("output.py")));
		}
		catch(Exception e){
			System.err.printf("file not found.");
		}
		System.out.printf("from LogicUnit.flipflop import *\nfrom LogicUnit.gate import *\nfrom LogicUnit.inputx import *\nfrom LogicUnit.outputz import *\nfrom LogicUnit.table import *\nstate = table()\nx = inputx()\nz=outputz()\nstate.setInput(x)\nstate.setOutput(z)\n");
        for(Node tmp: gridPane.getChildren()) {
            GateButton gate = (GateButton)tmp;
            switch(gate.getGate()) {
				case "jkFF":
					System.out.printf("g%d = JKflipflop()\n", count);
                    System.out.printf("state.addFlipFlop(g%d)\n", count);
                    gate.setName("g" + String.valueOf(count));
					count++;
					break;
				case "rsFF":
					System.out.printf("g%d = RSflipflop()\n", count);
                    System.out.printf("state.addFlipFlop(g%d)\n", count);
                    gate.setName("g" + String.valueOf(count));
					count++;
					break;
				case "dFF":
					System.out.printf("g%d = Dflipflop()\n", count);
                    System.out.printf("state.addFlipFlop(g%d)\n", count);
                    gate.setName("g" + String.valueOf(count));
                    count++;
                    break;
                case "tFF":
                    System.out.printf("g%d = Tflipflop()\n", count);
                    System.out.printf("state.addFlipFlop(g%d)\n", count);
                    gate.setName("g" + String.valueOf(count));
                    count++;
                    break;
                case "and":
                    System.out.printf("g%d = andGate()\n", count);
                    gate.setName("g" + String.valueOf(count));
                    count++;
                    break;
                case "or":
                    System.out.printf("g%d = orGate()\n", count);
                    gate.setName("g" + String.valueOf(count));
                    count++;
                    break;
                case "xor":
                    System.out.printf("g%d = xorGate()\n", count);
                    gate.setName("g" + String.valueOf(count));
                    count++;
                    break;
                case "nand":
                    System.out.printf("g%d = nandGate()\n", count);
                    gate.setName("g" + String.valueOf(count));
                    count++;
                    break;
                case "nor":
                    System.out.printf("g%d = norGate()\n", count);
                    gate.setName("g" + String.valueOf(count));
                    count++;
                    break;
                case "not":
                    System.out.printf("g%d = notGate()\n", count);
                    gate.setName("g" + String.valueOf(count));
                    count++;
                    break;
                default:
                    break;
            }
        }

        for(Node tmp: gridPane.getChildren()) {
            GateButton gate = (GateButton)tmp;
            if(gate.getType() == "obj") {
                for(GateButton inputGate: gate.getInput())
                    System.out.printf(gate.getName()+".set("+inputGate.getName()+")\n");
            }
        }
		System.out.printf("result = state.run()\ntable.toGraph(result)\n");
		System.out.flush();
		try{
			Process p = Runtime.getRuntime().exec("python3.4 output.py");
		} catch(Exception e){
			System.err.printf("something wrong");
		}
    }

    public void start(Stage stage) {
        ArrayList<GateButton> queue = new ArrayList<>();

        BorderPane borderPane = new BorderPane();
        Canvas canvas = new Canvas(780, 380);
        HBox hBox = new HBox();
        HBox bottom = new HBox();
        StackPane stackPane = new StackPane();
        GridPane gridPane = new GridPane();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(1);

        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);
        hBox.getChildren().add(new GateButton("cmd", "jkFF", queue, gc));
        hBox.getChildren().add(new GateButton("cmd", "rsFF", queue, gc));
        hBox.getChildren().add(new GateButton("cmd", "dFF", queue, gc));
        hBox.getChildren().add(new GateButton("cmd", "tFF", queue, gc));
        hBox.getChildren().add(new GateButton("cmd", "and", queue, gc));
        hBox.getChildren().add(new GateButton("cmd", "or", queue, gc));
        hBox.getChildren().add(new GateButton("cmd", "xor", queue, gc));
        hBox.getChildren().add(new GateButton("cmd", "nand", queue, gc));
        hBox.getChildren().add(new GateButton("cmd", "not", queue, gc));

        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        GateButton x = new GateButton("obj", "x", queue, gc);
        x.setName("x");
        gridPane.add(x, 0, 4);
        GateButton z = new GateButton("obj", "z", queue, gc);
        z.setName("z");
        gridPane.add(z, 10, 4);
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                Button tmp = new GateButton("obj", "", queue, gc);
                tmp.setStyle("-fx-border-width: 0px");
                gridPane.add(tmp, i + 1, j);
            }
        }

        bottom.setAlignment(Pos.CENTER_RIGHT);
        bottom.setPadding(new Insets(10));
        bottom.setSpacing(10);

        Button clearBuffer = new Button("Clear Buffer");
        clearBuffer.setStyle("-fx-max-width: 120px");
        clearBuffer.setOnAction((event1 -> {
            queue.clear();
        }));
        bottom.getChildren().add(clearBuffer);

        Button clear = new Button("Clear=(");
        clear.setStyle("-fx-max-width: 120px");
        clear.setOnAction((event -> {
            for(Node tmp: gridPane.getChildren()) {
                GateButton tmpGate = (GateButton)tmp;
                if(tmpGate.getType() == "obj" && tmpGate.getGate() != "x" && tmpGate.getGate() != "z") {
                    tmpGate.clean();
                }
                if(tmpGate.getGate() == "z")
                    tmpGate.cleanInput();
            }
        }));
        bottom.getChildren().add(clear);

        Button submit = new Button("Submit!!!!!");
        submit.setStyle("-fx-max-width: 120px");
        submit.setOnAction((event -> {
            submitFunc(gridPane);
        }));
        bottom.getChildren().add(submit);

        stackPane.getChildren().add(canvas);
        stackPane.getChildren().add(gridPane);

        borderPane.setTop(hBox);
        borderPane.setCenter(stackPane);
        borderPane.setBottom(bottom);

        Scene scene = new Scene(borderPane, 780, 480);
        scene.getStylesheets().add("GUI/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String argv[]) {
        launch();
    }
}
