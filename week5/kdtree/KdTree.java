import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;

public class KdTree {
    private static final int VERTICAL = 1;
    private static final int HORIZONTAL = -1;

    private Node root;    // the root of KdTree
    private int size;     // the number of points in the KdTree

    private Point2D nearst;

    public KdTree() {
        size = 0;
        root = null;
    }

    private static class Node {
        private final Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int direction; // the direction to compare

        public Node(Point2D p) {
            this.p = p;
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        insert(root, p);
    }

    private void insert(Node h, Point2D p) {
        if (h == null) {
            Node n = new Node(p);
            n.direction = VERTICAL;
            n.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            root = n;
            if (size == 0) {
                size = 1;
            }
            return;
        }
        if (contains(p)) {
            return;
        }
        Node n = new Node(p);
        if (h.direction == VERTICAL) {
            if (p.x() < h.p.x()) {
                if (h.lb == null) {
                    h.lb = n;
                    n.direction = HORIZONTAL;
                    n.rect = new RectHV(h.rect.xmin(), h.rect.ymin(), h.p.x(), h.rect.ymax());
                    size++;
                }
                else {
                    insert(h.lb, p);
                }
            }
            else {
                if (h.rt == null) {
                    h.rt = n;
                    n.direction = HORIZONTAL;
                    n.rect = new RectHV(h.p.x(), h.rect.ymin(), h.rect.xmax(), h.rect.ymax());
                    size++;
                }
                else {
                    insert(h.rt, p);
                }
            }
        }
        if (h.direction == HORIZONTAL) {
            if (p.y() < h.p.y()) {
                if (h.lb == null) {
                    h.lb = n;
                    n.direction = VERTICAL;
                    n.rect = new RectHV(h.rect.xmin(), h.rect.ymin(), h.rect.xmax(), h.p.y());
                    size++;
                }
                else {
                    insert(h.lb, p);
                }
            }
            else {
                if (h.rt == null) {
                    h.rt = n;
                    n.direction = VERTICAL;
                    n.rect = new RectHV(h.rect.xmin(), h.p.y(), h.rect.xmax(), h.rect.ymax());
                    size++;
                }
                else {
                    insert(h.rt, p);
                }
            }
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return contains(root, p);
    }

    private boolean contains(Node h, Point2D p) {
        if (h == null) {
            return false;
        }
        if (h.p.equals(p)) {
            return true;
        }
        if (h.direction == VERTICAL) {
            if (p.x() < h.p.x()) {
                return contains(h.lb, p);
            }
            else {
                return contains(h.rt, p);
            }
        }
        else {  // h.direction == HORIZONTAL
            if (p.y() < h.p.y()) {
                return contains(h.lb, p);
            }
            else {
                return contains(h.rt, p);
            }
        }
    }

    public void draw() {
        draw(root);
    }

    private void draw(Node h) {
        if (h == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(h.p.x(), h.p.y());

        if (h.direction == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(h.p.x(), h.rect.ymax(), h.p.x(), h.rect.ymin());
        }
        else if (h.direction == HORIZONTAL) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(h.rect.xmin(), h.p.y(), h.rect.xmax(), h.p.y());
        }
        draw(h.lb);
        draw(h.rt);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        LinkedList<Point2D> answer = new LinkedList<>();
        range(root, rect, answer);
        return answer;
    }

    private void range(Node node, RectHV rect, LinkedList<Point2D> list) {
        if (node == null) {
            return;
        }
        if (!node.rect.intersects(rect)) {
            return;
        }
        if (rect.contains(node.p)) {
            list.add(node.p);
            range(node.lb, rect, list);
            range(node.rt, rect, list);
        }
        else {
            if (node.direction == VERTICAL) {
                if (rect.xmax() < node.p.x()) {
                    range(node.lb, rect, list);
                }
                else if (rect.xmin() >= node.p.x()) {
                    range(node.rt, rect, list);
                }
                else {
                    range(node.lb, rect, list);
                    range(node.rt, rect, list);
                }
            }
            if (node.direction == HORIZONTAL) {
                if (rect.ymax() < node.p.y()) {
                    range(node.lb, rect, list);
                }
                else if (rect.ymin() >= node.p.y()) {
                    range(node.rt, rect, list);
                }
                else {
                    range(node.lb, rect, list);
                    range(node.rt, rect, list);
                }
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null || root == null) {
            throw new IllegalArgumentException();
        }
        nearst = null;
        nearst(root, p);
        return nearst;
    }

    private void nearst(Node node, Point2D target) {
        if (node == null) {
            return;
        }
        if (nearst == null) {
            nearst = node.p;
        }
        else if (nearst.distanceSquaredTo(target) > node.p.distanceSquaredTo(target)) {
            nearst = node.p;
        }
        if (nearst.distanceSquaredTo(target) < node.rect.distanceSquaredTo(target)) {
            return;
        }

        if (node.direction == VERTICAL) {
            if (target.x() < node.rect.xmin()) {
                nearst(node.lb, target);
                nearst(node.rt, target);
            }
            else if (target.x() > node.rect.xmax()) {
                nearst(node.rt, target);
                nearst(node.lb, target);
            }
            else {
                nearst(node.lb, target);
                nearst(node.rt, target);
            }
        }
        if (node.direction == HORIZONTAL) {
            if (target.y() < node.rect.ymin()) {
                nearst(node.lb, target);
                nearst(node.rt, target);
            }
            else if (target.y() > node.rect.ymax()) {
                nearst(node.rt, target);
                nearst(node.lb, target);
            }
            else {
                nearst(node.lb, target);
                nearst(node.rt, target);
            }
        }
    }


    public static void main(String[] args) {
        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        kdtree.draw();
        StdDraw.show();
    }
}
