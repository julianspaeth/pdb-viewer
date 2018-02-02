package view.proteinview;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import model.proteinmodel.AminoAcidProperties;

public class RibbonView extends Group {

    private final int[] faces = {
            0, 0, 1, 0, 4, 0,
            0, 0, 4, 0, 5, 0,
            1, 0, 2, 0, 3, 0,
            1, 0, 3, 0, 4, 0,

            0, 0, 4, 0, 1, 0,
            0, 0, 5, 0, 4, 0,
            1, 0, 3, 0, 2, 0,
            1, 0, 4, 0, 3, 0,
    }, smoothing = {1, 1, 1, 1, 2, 2, 2, 2};
    private float[] points, texCoord = {0, 0};
    private AminoAcidView aa1, aa2;
    private Point3D v0, v1, v2, v3, v4, v5;
    private TriangleMesh mesh;
    private MeshView meshView;
    private PhongMaterial material = new PhongMaterial();
    private Color color = Color.web("YELLOW");
    private Character structure;

    public RibbonView(AminoAcidView aa1, AminoAcidView aa2, Character structure) {
        this.aa1 = aa1;
        this.aa2 = aa2;
        this.structure = structure;
        computeCoordinates();
        constructMesh();
        this.getChildren().add(meshView);
    }

    private void computeCoordinates() {
        for (AtomView atomView : aa1.getAtomsView()) {
            if (atomView.getAtom().getAtom().equals("CA")) {
                v1 = new Point3D(atomView.getTranslateX(), atomView.getTranslateY(), atomView.getTranslateZ());
            } else if (atomView.getAtom().getAtom().equals("CB")) {
                v2 = new Point3D(atomView.getTranslateX(), atomView.getTranslateY(), atomView.getTranslateZ());
            } else if (atomView.getAtom().getAa().getLetter() == 'G' && atomView.getAtom().getAtom().equals("C")) {
                // not perfect but at least it looks good like this
                v2 = new Point3D(atomView.getTranslateX(), atomView.getTranslateY(), atomView.getTranslateZ());
            }
        }

        for (AtomView atomView : aa2.getAtomsView()) {
            if (atomView.getAtom().getAtom().equals("CA")) {
                v4 = new Point3D(atomView.getTranslateX(), atomView.getTranslateY(), atomView.getTranslateZ());
            } else if (atomView.getAtom().getAtom().equals("CB")) {
                v5 = new Point3D(atomView.getTranslateX(), atomView.getTranslateY(), atomView.getTranslateZ());
            } else if (atomView.getAtom().getAa().getLetter() == 'G' && atomView.getAtom().getAtom().equals("C")) {
                // not perfect but at least it looks good like this
                v5 = new Point3D(atomView.getTranslateX(), atomView.getTranslateY(), atomView.getTranslateZ());
            }
        }

        v0 = reflectCoordinateByPoint(v2, v1);
        v3 = reflectCoordinateByPoint(v5, v4);

        points = new float[]{
                (float) v0.getX(), (float) v0.getY(), (float) v0.getZ(),
                (float) v1.getX(), (float) v1.getY(), (float) v1.getZ(),
                (float) v2.getX(), (float) v2.getY(), (float) v2.getZ(),
                (float) v3.getX(), (float) v3.getY(), (float) v3.getZ(),
                (float) v4.getX(), (float) v4.getY(), (float) v4.getZ(),
                (float) v5.getX(), (float) v5.getY(), (float) v5.getZ(),
        };
    }

    private Point3D reflectCoordinateByPoint(Point3D coordinate, Point3D reflectionPoint) {
        double x = (2 * reflectionPoint.getX() - coordinate.getX());
        double y = (2 * reflectionPoint.getY() - coordinate.getY());
        double z = (2 * reflectionPoint.getZ() - coordinate.getZ());

        return new Point3D(x, y, z);
    }

    private void constructMesh() {
        mesh = new TriangleMesh();
        mesh.getPoints().addAll(points);
        mesh.getTexCoords().addAll(texCoord);
        mesh.getFaces().addAll(faces);
        mesh.getFaceSmoothingGroups().addAll(smoothing);

        meshView = new MeshView(mesh);
        material.setDiffuseColor(Color.YELLOW);
        meshView.setMaterial(material);
    }

    public void setHighlighting(String highlighting) {
        switch (highlighting) {
            case "atoms":
                highlightAtoms();
                break;
            case "aminoacids":
                highlightAminoacids();
                break;
            case "structures":
                highlightStructures();
                break;
            case "chains":
                highlightChains();
                break;
        }
    }

    private void highlightAtoms() {
        color = Color.web("YELLOW");
        material.setDiffuseColor(color);
    }

    private void highlightAminoacids() {
        color = AminoAcidProperties.aminoAcidColors.get(aa1.getAa().getLetter());
        material.setDiffuseColor(color);
    }

    private void highlightChains() {
        switch (aa1.getAa().getChainId()) {
            case "A":
                this.color = Color.web("#7FDBFF");
                break;
            case "B":
                this.color = Color.web("#2ECC40");
                break;
            case "C":
                this.color = Color.web("#FFDC00");
                break;
            case "D":
                this.color = Color.web("#F012BE");
                break;
            case "E":
                this.color = Color.web("#39CCCC");
                break;
            case "F":
                this.color = Color.web("#01FF70");
                break;
            case "G":
                this.color = Color.web("#FF851B");
                break;
            default:
                this.color = Color.web("FFFFFF");
                break;
        }
        material.setDiffuseColor(color);
    }

    private void highlightStructures() {
        switch (aa1.getAa().getStructure()) {
            case 'H':
                color = Color.web("FF0080");
                material.setDiffuseColor(color);
                break;
            case 'E':
                color = Color.web("FFC800");
                material.setDiffuseColor(color);
                break;
            default:
                color = Color.web("FFFFFF");
                material.setDiffuseColor(color);
                break;
        }
    }

    public void setModel(String model) {
        this.visibleProperty().setValue(true);
        switch (model) {
            case "ribbon":
                this.visibleProperty().setValue(true);
                break;
            case "structure":
                if (this.aa1.getAa().getStructure() == '-') {
                   this.visibleProperty().setValue(false);
                }
                break;
            default:
                this.visibleProperty().setValue(false);
                break;
        }
    }

}
