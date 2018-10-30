package interview.task.domain;

@FunctionalInterface
public interface ActionFunction {
    void execute(int target, Product amount);
}
