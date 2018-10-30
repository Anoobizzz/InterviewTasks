package interview.task;

import interview.task.domain.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TaskApplicationTests {
    private static final String LOCAL_LINK = "http://localhost:";
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void submit10MessagesWithSameProduct() {
        for (int i = 0; i < 9; i++) {
            ResponseEntity responseEntity = restTemplate.exchange(LOCAL_LINK + port + "/submit",
                    HttpMethod.PUT, createRequestEntity(), String.class);
            assertEquals("Message processed.", responseEntity.getBody());
        }
        assertEquals("Product{count=10, value=1, productType='product1'}\n",
                restTemplate.exchange(LOCAL_LINK + port + "/submit", HttpMethod.PUT, createRequestEntity(), String.class).getBody());
    }

    @Test
    public void submit50MessagesWithSameProduct() {
        for (int i = 0; i < 49; i++) {
            ResponseEntity responseEntity = restTemplate.exchange(LOCAL_LINK + port + "/submit",
                    HttpMethod.PUT, createRequestEntity(), String.class);
            if ((i + 1) % 10 == 0) {
                assertEquals("Product{count=" + (i + 1) + ", value=1, productType='product1'}\n", responseEntity.getBody());
            } else {
                assertEquals("Message processed.", responseEntity.getBody());
            }
        }
        assertEquals("No operations were applied.", restTemplate.exchange(LOCAL_LINK + port + "/submit", HttpMethod.PUT, createRequestEntity(), String.class).getBody());
    }

    @Test
    public void submit10MessagesWithDifferentProduct() {
        for (int i = 0; i < 9; i++) {
            ResponseEntity responseEntity = restTemplate.exchange(LOCAL_LINK + port + "/submit",
                    HttpMethod.PUT, createRequestEntity("product" + i, i, 1, null), String.class);
            assertEquals("Message processed.", responseEntity.getBody());
        }
        assertEquals("Product{count=1, value=0, productType='product0'}\n" +
                        "Product{count=1, value=1, productType='product1'}\n" +
                        "Product{count=1, value=2, productType='product2'}\n" +
                        "Product{count=1, value=3, productType='product3'}\n" +
                        "Product{count=1, value=4, productType='product4'}\n" +
                        "Product{count=1, value=5, productType='product5'}\n" +
                        "Product{count=1, value=6, productType='product6'}\n" +
                        "Product{count=1, value=7, productType='product7'}\n" +
                        "Product{count=1, value=8, productType='product8'}\n" +
                        "Product{count=1, value=9, productType='product9'}\n",
                restTemplate.exchange(LOCAL_LINK + port + "/submit", HttpMethod.PUT, createRequestEntity("product9", 9, 1, null), String.class).getBody());
    }

    @Test
    public void applyActionToAnEmptyProductList() {
        assertEquals("Can't apply action, product list is empty.", restTemplate.exchange(LOCAL_LINK + port + "/submit",
                HttpMethod.PUT, createRequestEntity("product1", 1, 1, Message.Action.add), String.class).getBody());
    }

    @Test
    public void applyActionToNonExistingProduct() {
        assertEquals("Message processed.",
                restTemplate.exchange(LOCAL_LINK + port + "/submit", HttpMethod.PUT, createRequestEntity(), String.class).getBody());
        assertEquals("No product of give type was found, action will not be applied or logged.", restTemplate.exchange(LOCAL_LINK + port + "/submit",
                HttpMethod.PUT, createRequestEntity("product2", 1, 1, Message.Action.add), String.class).getBody());
    }

    @Test
    public void applyActionSubtractMoreThanCurrentValue() {
        for (int i = 0; i < 49; i++) {
            restTemplate.exchange(LOCAL_LINK + port + "/submit", HttpMethod.PUT, createRequestEntity(), String.class);
        }
        assertEquals("2 was subtracted from product 'product1'\n", restTemplate.exchange(LOCAL_LINK + port + "/submit", HttpMethod.PUT, createRequestEntity("product1", 2, 1, Message.Action.subtract), String.class).getBody());
    }

    @Test
    public void submitMessagesWithSameProductAndApplyMultiplication() {
        for (int i = 0; i < 49; i++) {
            ResponseEntity responseEntity = restTemplate.exchange(LOCAL_LINK + port + "/submit",
                    HttpMethod.PUT, createRequestEntity(), String.class);
            if ((i + 1) % 10 == 0) {
                assertEquals("Product{count=" + (i + 1) + ", value=1, productType='product1'}\n", responseEntity.getBody());
            } else {
                assertEquals("Message processed.", responseEntity.getBody());
            }
        }
        assertEquals("Product 'product1' was multiplied by 2\n", restTemplate.exchange(LOCAL_LINK + port + "/submit", HttpMethod.PUT, createRequestEntity("product1", 2, 1, Message.Action.multiply), String.class).getBody());
    }

    @Test
    public void submitMessagesWithSameProductAndApplyAllActions() {
        for (int i = 0; i < 47; i++) {
            ResponseEntity responseEntity = restTemplate.exchange(LOCAL_LINK + port + "/submit",
                    HttpMethod.PUT, createRequestEntity(), String.class);
            if ((i + 1) % 10 == 0) {
                assertEquals("Product{count=" + (i + 1) + ", value=1, productType='product1'}\n", responseEntity.getBody());
            } else {
                assertEquals("Message processed.", responseEntity.getBody());
            }
        }
        assertEquals("Message processed.", restTemplate.exchange(LOCAL_LINK + port + "/submit", HttpMethod.PUT, createRequestEntity("product1", 1, 1, Message.Action.multiply), String.class).getBody());
        assertEquals("Message processed.", restTemplate.exchange(LOCAL_LINK + port + "/submit", HttpMethod.PUT, createRequestEntity("product1", 1, 1, Message.Action.add), String.class).getBody());
        assertEquals("Product 'product1' was multiplied by 1\n" +
                        "1 was added to product 'product1'\n" +
                        "1 was subtracted from product 'product1'\n",
                restTemplate.exchange(LOCAL_LINK + port + "/submit", HttpMethod.PUT, createRequestEntity("product1", 1, 1, Message.Action.subtract), String.class).getBody());
    }

    private HttpEntity createRequestEntity() {
        return createRequestEntity("product1", 1, 1, null);
    }

    private HttpEntity createRequestEntity(String productType, int value, int count, Message.Action action) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Message message = new Message();
        message.setValue(value);
        message.setAction(action);
        message.setCount(count);
        message.setProductType(productType);
        return new HttpEntity(message, headers);
    }
}