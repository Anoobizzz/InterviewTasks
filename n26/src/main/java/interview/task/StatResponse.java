package interview.task;

public class StatResponse {
    private double sum;
    private double avg;
    private double max;
    private double min;
    private long count;

    public double getSum() {
        return sum;
    }

    public StatResponse setSum(double sum) {
        this.sum = sum;
        return this;
    }

    public double getAvg() {
        return avg;
    }

    public StatResponse setAvg(double avg) {
        this.avg = avg;
        return this;
    }

    public double getMax() {
        return max;
    }

    public StatResponse setMax(double max) {
        this.max = max;
        return this;
    }

    public double getMin() {
        return min;
    }

    public StatResponse setMin(double min) {
        this.min = min;
        return this;
    }

    public long getCount() {
        return count;
    }

    public StatResponse setCount(long count) {
        this.count = count;
        return this;
    }
}
