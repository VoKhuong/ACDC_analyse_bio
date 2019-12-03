package ihm;

public class GridPositionner {

    final static GridPositionner instance = new GridPositionner(10, 10);

    private double actualX;
    private double actualY;

    private GridPositionner(int actualX, int actualY) {
        this.actualX = actualX;
        this.actualY = actualY;
    }

    public static GridPositionner getInstance() {
        return instance;
    }

    public void addElement(double width, double height) {
        this.actualX += width;
        this.actualY += height;
        System.out.println(actualX + " " + actualY);
    }

    public double getX() {
        return actualX;
    }

    public double getY() {
        return actualY;
    }
}
