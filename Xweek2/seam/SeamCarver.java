import edu.princeton.cs.algs4.AcyclicSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Picture is null");
        }
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= picture.width() || y < 0 || y >= picture.height()) {
            throw new IllegalArgumentException("Pixel out of range");
        }
        if (x == 0 || x == picture.width() - 1 || y == 0 || y == picture.height() - 1) {
            return 1000.00;
        }
        Color high = picture.get(x, y - 1);
        Color low = picture.get(x, y + 1);
        Color left = picture.get(x - 1, y);
        Color right = picture.get(x + 1, y);
        double gradientX = (Math.pow(left.getRed() - right.getRed(), 2) + Math.pow(
                left.getGreen() - right.getGreen(), 2) + Math.pow(left.getBlue() - right.getBlue(),
                                                                  2));
        double gradientY = (Math.pow(high.getRed() - low.getRed(), 2) + Math.pow(
                high.getGreen() - low.getGreen(), 2) + Math.pow(high.getBlue() - low.getBlue(), 2));
        return Math.sqrt(gradientX + gradientY);
    }

    // sequence of indices for Vertical seam
    public int[] findVerticalSeam() {
        int width = width();
        int height = height();
        int vertices = width * height + 2;
        int top = vertices - 2;
        int bottom = vertices - 1;
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(vertices);
        for (int i = 0; i < width; i++) {
            graph.addEdge(new DirectedEdge(top, i, energy(i, 0)));
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height - 1; j++) {
                int curr = j * width + i;
                graph.addEdge(new DirectedEdge(curr, curr + width, energy(i, j + 1)));
                if (i > 0) {
                    graph.addEdge(new DirectedEdge(curr, curr + width - 1, energy(i - 1, j + 1)));
                }
                if (i < width - 1) {
                    graph.addEdge(new DirectedEdge(curr, curr + width + 1, energy(i + 1, j + 1)));
                }
            }
        }
        for (int i = 0; i < width; i++) {
            graph.addEdge(
                    new DirectedEdge((height - 1) * width + i, bottom, 0));
        }
        AcyclicSP acsp = new AcyclicSP(graph, top);
        int[] path = new int[height];
        int i = 0;
        for (DirectedEdge e : acsp.pathTo(bottom)) {
            if (i < height) {
                path[i] = e.to() % width;
            }
            i += 1;
        }
        return path;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // sequence of indices for horizontal seam
        int width = height();
        int height = width();
        int vertices = width * height + 2;
        int top = vertices - 2;
        int bottom = vertices - 1;
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(vertices);
        // add top
        for (int i = 0; i < width; i++) {
            int s = i;
            graph.addEdge(new DirectedEdge(top, s, energy(0, i)));
        }
        // add intermediate
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height - 1; j++) {
                int current = j * width + i;
                int s = current + width;
                graph.addEdge(new DirectedEdge(current, s, energy(j + 1, i)));
                // left or right case
                if (i > 0) {
                    int sw = current + width - 1;
                    graph.addEdge(new DirectedEdge(current,
                                                   sw, energy(j + 1, i - 1)));
                }
                if (i < width - 1) {
                    int se = current + width + 1;
                    graph.addEdge(new DirectedEdge(current,
                                                   se, energy(j + 1, i + 1)));
                }
            }
        }
        // add bottom
        for (int i = 0; i < width; i++) {
            int current = (height - 1) * width + i;
            graph.addEdge(new DirectedEdge(current, bottom, 0));
        }
        AcyclicSP sp = new AcyclicSP(graph, top);
        int[] path = new int[height];
        int i = 0;
        for (DirectedEdge e : sp.pathTo(bottom)) {
            if (i < height) {
                path[i] = e.to() % width;
            }
            i += 1;
        }
        return path;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("seam is null");
        }
        if (seam.length != width() || height() <= 1) {
            throw new IllegalArgumentException("seam length wrong or picture too small");
        }
        for (int i = 0; i < seam.length; i++) {
            if (i > 0) {
                if (Math.abs(seam[i] - seam[i - 1]) > 1) {
                    throw new IllegalArgumentException("Wrong path");
                }
            }
            if (seam[i] < 0 || seam[i] > height() - 1) {
                throw new IllegalArgumentException("Wrong path");
            }
        }
        int width = width();
        int height = height();
        Picture newPicture = new Picture(width, height - 1);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int removeY = seam[x];
                if (y < removeY) {
                    newPicture.set(x, y, picture.get(x, y));
                }
                if (y > removeY) {
                    newPicture.set(x, y - 1, picture.get(x, y));
                }
            }
        }
        this.picture = newPicture;

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("seam is null");
        }
        if (seam.length != height() || width() <= 1) {
            throw new IllegalArgumentException("seam length wrong or picture too small");
        }
        for (int i = 0; i < seam.length; i++) {
            if (i > 0) {
                if (Math.abs(seam[i] - seam[i - 1]) > 1) {
                    throw new IllegalArgumentException("Wrong path");
                }
            }
            if (seam[i] < 0 || seam[i] > width() - 1) {
                throw new IllegalArgumentException("Wrong path");
            }
        }
        int width = width();
        int height = height();
        Picture newPicture = new Picture(width - 1, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int removeX = seam[y];
                if (x < removeX) {
                    newPicture.set(x, y, picture.get(x, y));
                }
                if (x > removeX) {
                    newPicture.set(x - 1, y, picture.get(x, y));
                }
            }
        }
        this.picture = newPicture;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver seam = new SeamCarver(picture);
        System.out.println(seam.energy(0, 1));
        System.out.println(seam.energy(1, 1));
        System.out.println(seam.energy(1, 2));
        System.out.println(seam.energy(2, 3));
        int[] path = seam.findHorizontalSeam();
        for (int i : path) {
            System.out.println(i);
        }
        seam.removeHorizontalSeam(path);
        seam.findVerticalSeam();
    }
}
