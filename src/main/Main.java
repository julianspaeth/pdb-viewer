package main;

import connection.PDBIDsService;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.proteinmodel.Protein;
import presenter.ProteinPresenter;
import view.fxmlview.PDBViewerView;
import view.proteinview.ProteinView;

import java.io.IOException;
import java.util.logging.Logger;

public class Main extends Application {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());
    private int WIDTH = 1200, HEIGHT = 800;
    private PDBViewerView pdbViewerView;
    private Parent mainView;
    private ProteinPresenter proteinPresenter;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("PDBViewer");
        Scene scene = new Scene(mainView, WIDTH, HEIGHT, false, SceneAntialiasing.BALANCED);
        primaryStage.setScene(scene);
        primaryStage.show();

        Protein proteinModel = new Protein();
        ProteinView proteinView = new ProteinView();
        proteinPresenter = new ProteinPresenter(proteinModel, proteinView, pdbViewerView, scene);

        pdbViewerView.getRetryButton().setOnAction(e -> connectToPDB());
    }

    @Override
    public void init() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        mainView = fxmlLoader.load(getClass().getResource("../view/fxml/pdbViewer.fxml").openStream());
        pdbViewerView = fxmlLoader.getController();
        connectToPDB();
    }

    public void connectToPDB() {
        PDBIDsService pdbiDsService = new PDBIDsService();
        pdbiDsService.setOnSucceeded(t -> {
            LOG.info("PDB IDs read in successfully");
            pdbViewerView.getStatusLabel().setText("  Connection to PDB established!");
            pdbViewerView.getStatusLabel().setTextFill(Color.DARKGREEN);
            pdbViewerView.getLoadFromPDBMenuItem().setDisable(false);
            proteinPresenter.initPDBLoader((ObservableList<String>) t.getSource().getValue());
            pdbViewerView.getProgressIndicator().setVisible(false);
            pdbViewerView.getRetryButton().setDisable(false);
        });
        pdbiDsService.setOnRunning(t -> {
            LOG.info("Establishing connection to PDB");
            pdbViewerView.getStatusLabel().setText("  Establishing connection to PDB ...");
            pdbViewerView.getStatusLabel().setTextFill(Color.DARKORANGE);
            pdbViewerView.getProgressIndicator().setVisible(true);
            pdbViewerView.getRetryButton().setDisable(true);
        });
        pdbiDsService.setOnFailed(t -> {
            pdbViewerView.getStatusLabel().setText(" Connection to PDB failed. Please check your internet connection and retry!");
            pdbViewerView.getStatusLabel().setTextFill(Color.DARKRED);
            pdbViewerView.getLoadFromPDBMenuItem().setDisable(true);
            pdbViewerView.getProgressIndicator().setVisible(false);
            pdbViewerView.getRetryButton().setDisable(false);
        });
        pdbiDsService.start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }

}
