import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Scanner;

public class BaseballElimination {

    private final String[] names;
    private final int[] wins;
    private final int[] losses;
    private final int[] remains;
    private final int[][] remainOthers;
    private int numOfTeam;
    private int capacity = 0;

    private Queue<String> queue;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        numOfTeam = Integer.parseInt(in.readLine());
        names = new String[numOfTeam];
        wins = new int[numOfTeam];
        losses = new int[numOfTeam];
        remains = new int[numOfTeam];
        remainOthers = new int[numOfTeam][numOfTeam];
        queue = new Queue<String>();
        for (int team = 0; team < numOfTeam; team++) {
            String line = in.readLine();
            Scanner linescan = new Scanner(line);
            names[team] = linescan.next();
            wins[team] = linescan.nextInt();
            losses[team] = linescan.nextInt();
            remains[team] = linescan.nextInt();
            for (int i = 0; i < numOfTeam; i++) {
                remainOthers[team][i] = linescan.nextInt();
            }
        }
        in.close();
    }

    public int numberOfTeams() {
        return numOfTeam;
    }

    public Iterable<String> teams() {
        Stack<String> teams = new Stack<String>();
        for (String s : names) {
            teams.push(s);
        }
        return teams;
    }

    public int wins(String team) {
        for (int i = 0; i < numOfTeam; i++) {
            if (team.equals(names[i])) {
                return wins[i];
            }
        }
        throw new IllegalArgumentException();
    }

    public int losses(String team) {
        for (int i = 0; i < numOfTeam; i++) {
            if (team.equals(names[i])) {
                return losses[i];
            }
        }
        throw new IllegalArgumentException();
    }

    public int remaining(String team) {
        for (int i = 0; i < numOfTeam; i++) {
            if (team.equals(names[i])) {
                return remains[i];
            }
        }
        throw new IllegalArgumentException();
    }

    public int against(String team1, String team2) {
        int t1 = -1;
        int t2 = -1;

        for (int i = 0; i < numOfTeam; i++) {
            if (team1.equals(names[i])) {
                t1 = i;
            }
            if (team2.equals(names[i])) {
                t2 = i;
            }
        }
        if (t1 == -1 || t2 == -1) {
            throw new IllegalArgumentException();
        }
        int i = remainOthers[t1][t2];
        return i;
    }

    private boolean trivialSolution(int team) {
        boolean elim = false;
        for (int i = 0; i < numOfTeam; i++) {
            if (team != i && wins[team] + remains[team] < wins[i]) {
                elim = true;
                queue.enqueue(names[i]);
                break;
            }
        }
        return elim;
    }

    private FordFulkerson nontrivialSolution(int team) {
        int games = ((numOfTeam - 1) * numOfTeam) / 2;
        FlowNetwork graph = new FlowNetwork(games + numOfTeam + 2);
        final int S = games + numOfTeam;
        final int T = games + numOfTeam + 1;
        int vertex = 0;
        capacity = 0;
        for (int i = 0; i < numOfTeam; i++) {
            graph.addEdge(
                    new FlowEdge(games + i, T, Math.max(0, remains[team] + wins[team] - wins[i])));
            for (int j = i + 1; j < numOfTeam; j++) {
                graph.addEdge(new FlowEdge(vertex, games + i, Double.POSITIVE_INFINITY));
                graph.addEdge(new FlowEdge(vertex, games + j, Double.POSITIVE_INFINITY));
                graph.addEdge(new FlowEdge(S, vertex, remainOthers[i][j]));
                vertex++;
                capacity += remainOthers[i][j];
            }
        }
        int index = 0;
        FordFulkerson graph2 = new FordFulkerson(graph, S, T);
        for (int i = games; i < games + numOfTeam; i++) {
            if (graph2.inCut(i)) {
                queue.enqueue(names[index]);
            }
            index++;
        }
        return graph2;
    }

    public boolean isEliminated(String team) {
        queue = new Queue<>();
        int index = -1;
        for (int i = 0; i < numOfTeam; i++) {
            if (team.equals(names[i])) {
                index = i;
            }
        }
        if (index == -1) {
            throw new IllegalArgumentException();
        }
        if (trivialSolution(index)) {
            return true;
        }

        FordFulkerson graph = nontrivialSolution(index);
        if (capacity != graph.value()) {
            return true;
        }
        return false;
    }

    public Iterable<String> certificateOfElimination(String team) {
        queue = new Queue<>();
        int index = -1;
        for (int i = 0; i < numOfTeam; i++) {
            if (team.equals(names[i])) {
                index = i;
            }
        }
        if (index == -1) {
            throw new IllegalArgumentException();
        }
        if (trivialSolution(index)) {
            return queue;
        }
        nontrivialSolution(index);
        if (queue.size() == 0) {
            return null;
        }
        return queue;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
