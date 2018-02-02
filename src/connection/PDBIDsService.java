package connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class PDBIDsService extends Service<ObservableList<String>> {

    private static final Logger LOG = Logger.getLogger(PDBIDsService.class.getName());

    public PDBIDsService() {

    }

    @Override
    protected void succeeded() {
        super.succeeded();
        LOG.info("PDB IDs read in successfully.");
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        LOG.info("Read in PDB Ids cancelled.");
    }

    @Override
    protected void failed() {
        super.failed();
        LOG.info("Failed reading in PDB IDs read in successfully");
    }


    @Override
    protected Task<ObservableList<String>> createTask() {
        return new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws IOException {
                ObservableList<String> pdbIdList = FXCollections.observableArrayList();
                List<String> proteinList = Request.getFromURL("https://www.rcsb.org/pdb/rest/customReport.csv?pdbids=*&customReportColumns=structureId,structureTitle,classification&service=wsfile&format=csv");
                for (String protein : proteinList) {
                    String[] item = protein.split(",");
                    if (!item[2].contains("DNA")) {
                        pdbIdList.add(item[0].replace("\"", ""));
                    }
                }
                pdbIdList.remove(0);
                return pdbIdList;
            }
        };
    }
}
