package interview.task.domain;

public class Message extends Product {
    public enum Action {
        add((amount, product) -> product.setValue(product.getValue() + amount), "{1} was added to product ''{0}''"),
        subtract((amount, product) -> product.setValue(Math.max(0, product.getValue() - amount)), "{1} was subtracted from product ''{0}''"),
        multiply((amount, product) -> product.setValue(product.getValue() * amount), "Product ''{0}'' was multiplied by {1}");
        ActionFunction action;
        String loggingFormat;

        Action(ActionFunction action, String loggingFormat) {
            this.action = action;
            this.loggingFormat = loggingFormat;
        }

        public void applyAction(Integer amount, Product product) {
            action.execute(amount, product);
        }

        public String getLoggingFormat() {
            return loggingFormat;
        }
    }

    private Action action;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
