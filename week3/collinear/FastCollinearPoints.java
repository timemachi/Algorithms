import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Iterator;

public class FastCollinearPoints {
    private int numOfSegment = 0;
    private final Stack<LineSegment> segments = new Stack<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        int numOfPoints = points.length;
        // Iterate all points
        for (int i = 0; i < numOfPoints; i++) {
            Point p = points[i];
            if (p == null) {
                throw new IllegalArgumentException();
            }
            // create array of index and slope
            IndexAndSlope[] arrayOfSlope = new IndexAndSlope[numOfPoints - 1 - i];
            // for all other points:
            for (int j = i + 1; j < numOfPoints; j++) {
                Point q = points[j];
                if (q == null) {
                    throw new IllegalArgumentException();
                }
                if (p.compareTo(q) == 0) {
                    throw new IllegalArgumentException();
                }
                IndexAndSlope thisIndex = new IndexAndSlope(j, p.slopeTo(q));
                arrayOfSlope[j - i - 1] = thisIndex;
            }
            // sorted all points
            Arrays.sort(arrayOfSlope);
            // length of array - last two items: check if 3 items are same
            for (int k = 0; k < numOfPoints - 1 - i - 2; k++) {
                if (arrayOfSlope[k].compareTo(arrayOfSlope[k + 1]) == 0
                        && arrayOfSlope[k].compareTo(arrayOfSlope[k + 2]) == 0) {
                    // get line and add in array
                    LineSegment line = getLine(points[i], points[arrayOfSlope[k].index],
                                               points[arrayOfSlope[k + 1].index],
                                               points[arrayOfSlope[k + 2].index]);
                    segments.push(line);
                    numOfSegment++;
                }
            }
        }
    }

    private class IndexAndSlope implements Comparable<IndexAndSlope> {
        private final int index;
        private final double slope;

        private IndexAndSlope(int index, double slope) {
            this.index = index;
            this.slope = slope;
        }

        // Only wants to know equal or not
        public int compareTo(IndexAndSlope e) {
            if (slope == e.slope) {
                return 0;
            }
            if (Double.compare(slope, e.slope) > 0) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }

    private LineSegment getLine(Point a, Point b, Point c, Point d) {
        Point minPoint = a;
        Point maxPoint = a;
        for (Point p : new Point[] { b, c, d }) {
            if (minPoint.compareTo(p) > 0) {
                minPoint = p;
            }
            if (maxPoint.compareTo(p) < 0) {
                maxPoint = p;
            }
        }
        LineSegment line = new LineSegment(minPoint, maxPoint);
        return line;
    }

    // the number of line segments
    public int numberOfSegments() {
        return numOfSegment;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lines = new LineSegment[numOfSegment];
        Iterator<LineSegment> myIterator = segments.iterator();
        for (int i = 0; i < numOfSegment; i++) {
            lines[i] = myIterator.next();
        }
        return lines;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
