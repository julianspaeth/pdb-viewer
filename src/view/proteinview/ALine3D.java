package view.proteinview;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class ALine3D extends Group {

    private DoubleProperty startXProperty = new SimpleDoubleProperty(),
            startYProperty = new SimpleDoubleProperty(),
            startZProperty = new SimpleDoubleProperty(),
            endXProperty = new SimpleDoubleProperty(),
            endYProperty = new SimpleDoubleProperty(),
            endZProperty = new SimpleDoubleProperty();
    private Point3D startPoint, endPoint;
    private Color color;
    private Cylinder cylinder;

    public ALine3D(Double startXProperty, Double startYProperty, Double startZProperty,
                   Double endXProperty, Double endYProperty, Double endZProperty,
                   Color color) {
        this.startXProperty.set(startXProperty);
        this.startYProperty.set(startYProperty);
        this.startZProperty.set(startZProperty);
        startPoint = new Point3D(this.startXProperty.doubleValue(), this.startYProperty.doubleValue(), this.startZProperty.doubleValue());
        this.endXProperty.set(endXProperty);
        this.endYProperty.set(endYProperty);
        this.endZProperty.set(endZProperty);
        endPoint = new Point3D(this.endXProperty.doubleValue(), this.endYProperty.doubleValue(), this.endZProperty.doubleValue());

        this.color = color;
        cylinder = new Cylinder();
        cylinder.setRadius(3);
        setColor(color);

        calculate();

        addInvalidationListener();

        this.getChildren().add(cylinder);
    }

    private void calculate() {
        // Compute a point that represents the direction in which you want to orient the cylinder (based on the start and end points).
        Point3D diff = endPoint.subtract(startPoint);
        // Use the crossProduct() method of Point3D to compute the axis that is perpendicular to your cylinder (which has the direction of the y-axis) and the desired direction.
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D rotationAxis = diff.crossProduct(yAxis);
        // Compute the angle between the desired direction and the y-axis
        double angle = diff.angle(yAxis);
        // Use the values to set the rotation axis and the rotate value of the cylinder
        Rotate rotate = new Rotate(-angle, rotationAxis);

        // Translate the cylinder to the midpoint between the start and end
        Point3D midpoint = endPoint.midpoint(startPoint);
        Translate translate = new Translate(midpoint.getX(), midpoint.getY(), midpoint.getZ());

        // Scale the cylinder in y- direction so that its height corresponds to the distance between start and end point.
        double height = diff.magnitude();
        this.cylinder.setHeight(height);

        // Apply transforms
        this.getTransforms().addAll(translate, rotate);
    }

    private void addInvalidationListener() {
        // Implement InvalidationListener that listens to the six given properties and recalculates the rotation, translation and scake properties of the cylinder whenever one of its endpoints is moved
        this.startXProperty.addListener(observable -> calculate());
        this.startYProperty.addListener(observable -> calculate());
        this.startZProperty.addListener(observable -> calculate());
        this.endXProperty.addListener(observable -> calculate());
        this.endYProperty.addListener(observable -> calculate());
        this.endZProperty.addListener(observable -> calculate());
    }

    public void changeRadius(String type) {
        if (type.equals("increase")) {
            this.cylinder.setRadius(this.cylinder.getRadius() + this.cylinder.getRadius() * 0.1);
        } else if (type.equals("decrease")) {
            this.cylinder.setRadius(this.cylinder.getRadius() - this.cylinder.getRadius() * 0.1);
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        cylinder.setMaterial(new PhongMaterial(color));
    }

    public void setRadius(Integer radius) {
        cylinder.setRadius(radius);
    }
}
