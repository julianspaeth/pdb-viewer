package view.fxmlview;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;

public class ChartViewerView {

    @FXML
    private PieChart aaChart;

    @FXML
    private Button cancelBu;

    @FXML
    private Button saveBu;

    public Button getCancelBu() {
        return cancelBu;
    }

    public PieChart getAaChart() {
        return aaChart;
    }

    public Button getSaveBu() {
        return saveBu;
    }
}

