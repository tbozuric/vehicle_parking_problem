package hr.fer.tki.optimization;

import hr.fer.tki.function.GoalFunctionEvaluator;
import hr.fer.tki.models.Garage;

import java.io.PrintStream;

public class ResultPrintListener implements OptimisationAlgorithmListener {

    private static final String LINE = "------------------------------------------------------------------------------";
    private PrintStream printStream;

    public ResultPrintListener(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void algorithmStarted(String algorithmName) {
        printStream.println(algorithmName + " started.");
        printStream.println(LINE);
    }

    @Override
    public void newSolutionFound(String header, Garage currentSolution) {
        GoalFunctionEvaluator evaluator = new GoalFunctionEvaluator(currentSolution);

        printStream.println(header + ": current solution(min p) = " + evaluator.evaluateMinimizationProblem());
        printStream.println(header + ": current solution(max p) = " + evaluator.evaluateMaximizationProblem());
        printStream.println(header + ": current solution(total) = " + evaluator.evaluateTotalProblem());
        printStream.println();
    }

    @Override
    public void algorithmOver(String algorithmName) {
        printStream.println(LINE);
        printStream.println(algorithmName + " over!");
        printStream.println();
    }
}
