import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;

public class BruteCollinearPoints {
    private int numOfSegments = 0;
    private final Stack<LineSegment> allLines = new Stack<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        if (hasEqualOrNullPoint(points)) {
            throw new IllegalArgumentException();
        }
        int numOfPoint = points.length;

        for (int a = 0; a < numOfPoint - 3; a++) {
            Point aPoint = points[a];
            Comparator<Point> slopeCom = aPoint.slopeOrder();
            for (int b = a + 1; b < numOfPoint - 2; b++) {
                Point bPoint = points[b];
                for (int c = b + 1; c < numOfPoint - 1; c++) {
                    Point cPoint = points[c];
                    for (int d = c + 1; d < numOfPoint; d++) {
                        Point dPoint = points[d];
                        if (slopeCom.compare(bPoint, cPoint) == 0
                                && slopeCom.compare(bPoint, dPoint) == 0) {
                            allLines.push(getLine(aPoint, bPoint, cPoint, dPoint));
                            numOfSegments++;
                        }

                    }
                }
            }
        }
    }

    private boolean hasEqualOrNullPoint(Point[] points) {
        int num = points.length;
        for (int i = 0; i < num - 1; i++) {
            for (int j = i + 1; j < num; j++) {
                if (points[i] == null) {
                    return true;
                }
                if (points[i].compareTo(points[j]) == 0) {
                    return true;
                }
            }
        }
        if (points[num - 1] == null) {
            return true;
        }
        return false;
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
        return this.numOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lines = new LineSegment[numOfSegments];
        Iterator<LineSegment> myIterator = allLines.iterator();
        for (int i = 0; i < numOfSegments; i++) {
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
