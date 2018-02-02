package view.fxmlview;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class PDBLoaderView {

    @FXML
    private ListView<?> pdbIdListView;

    @FXML
    private Button cancelBu;

    @FXML
    private Button loadBu;

    @FXML
    private TextField filterTF;

    public ListView getPdbIdListView() {
        return pdbIdListView;
    }

    public Button getCancelBu() {
        return cancelBu;
    }

    public Button getLoadBu() {
        return loadBu;
    }

    public TextField getFilterTF() {
        return filterTF;
    }

}

