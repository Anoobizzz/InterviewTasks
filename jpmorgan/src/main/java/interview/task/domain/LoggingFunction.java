package interview.task.domain;

@FunctionalInterface
public interface LoggingFunction {
    String log(String productType, int value);
}
