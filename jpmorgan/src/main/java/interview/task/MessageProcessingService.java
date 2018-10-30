package interview.task;

import interview.task.domain.Message;
import interview.task.domain.Product;
import interview.task.exception.EmptyProductListException;
import interview.task.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.text.MessageFormat.format;


@Service
public class MessageProcessingService {
    private static final Logger LOG = Logger.getLogger(MessageProcessingService.class.getName());
    private AtomicBoolean available = new AtomicBoolean(true);
    private int messageCounter;
    private List<Product> products = new ArrayList<>();
    private List<String> actionLog = new ArrayList<>();

    //Type 1: Type + Value
    //Type 2: Amount of sales (count) + Type + Value
    //Type 3: Type + Value + Action
    public String processMessage(Message message) {
        if (available.get()) {
            messageCounter++;
            if (message.getAction() == null) {
                storeProductOrUpdateExisting(message);
            } else {
                applyAction(message);
            }

            if (messageCounter == 50) {
                messageCounter = 0;
                return outputAllAppliedActions();
            } else if (messageCounter % 10 == 0) {
                return outputAllCurrentProducts();
            }

            return "Message processed.";
        }
        return "Service doesn't execute messages at the moment, try again later.";
    }

    private void storeProductOrUpdateExisting(Product newProduct) {
        Optional<Product> existingProduct = products.stream()
                .filter(product -> product.equals(newProduct)).findFirst();

        if (existingProduct.isPresent()) {
            LOG.info("Found existing product, increasing its count.");
            existingProduct.get().increaseCount(newProduct.getCount());
        } else {
            LOG.info("Adding new product.");
            products.add(newProduct);
        }
    }

    private void applyAction(Message message) {
        if (products.isEmpty()) {
            throw new EmptyProductListException("Can't apply action, product list is empty.");
        }
        Message.Action action = message.getAction();
        List<Product> matchingProducts = products.stream()
                .filter(product -> product.getProductType().equals(message.getProductType()))
                .collect(Collectors.toList());
        if (!matchingProducts.isEmpty()) {
            actionLog.add(format(action.getLoggingFormat(), message.getProductType(), message.getValue()));
            matchingProducts.forEach(product -> action.applyAction(message.getValue(), product));
        } else {
            throw new ProductNotFoundException("No product of give type was found, action will not be applied or logged.");
        }
    }

    private String outputAllCurrentProducts() {
        StringBuilder responseBuilder = new StringBuilder();
        products.forEach(product -> responseBuilder.append(product.toString()));
        String response = responseBuilder.toString();
        LOG.info("Current products:\n" + response);
        return response;
    }

    private String outputAllAppliedActions() {
        available.set(false);
        StringBuilder responseBuilder = new StringBuilder();
        if (!actionLog.isEmpty()) {
            actionLog.forEach(s -> responseBuilder.append(s).append("\n"));
            LOG.info("Applied operations:\n" + responseBuilder.toString());
            available.set(true);
        } else {
            responseBuilder.append("No operations were applied.");
        }
        return responseBuilder.toString();
    }
}
