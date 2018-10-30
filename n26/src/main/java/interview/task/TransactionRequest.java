package interview.task;

public class TransactionRequest {
    private double amount;
    private long timestamp;

    public double getAmount() {
        return amount;
    }

    public TransactionRequest setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public TransactionRequest setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
