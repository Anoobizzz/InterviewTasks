package interview.task;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;
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
	public void contextLoads() {
	}

	@Test
	public void getStatisticsWithNoData(){
        ResponseEntity responseEntity = restTemplate.getForEntity(LOCAL_LINK + port + "/statistics", StatResponse.class);
        assertEquals(200, responseEntity.getStatusCodeValue());
        StatResponse statResponse = (StatResponse) responseEntity.getBody();
        assertEquals(0.0, statResponse.getAvg(), 0);
        assertEquals(0.0, statResponse.getSum(), 0);
        assertEquals(0.0, statResponse.getMax(), 0);
        assertEquals(0.0, statResponse.getMin(), 0);
        assertEquals(0, statResponse.getCount());
	}

	@Test
    public void putExpiredTransaction(){
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity httpEntity = new HttpEntity(new TransactionRequest().setTimestamp(0), headers);
	    ResponseEntity responseEntity = restTemplate.postForEntity(LOCAL_LINK + port + "/transactions", httpEntity, ResponseEntity.class);
	    assertEquals(204, responseEntity.getStatusCodeValue());
    }

	@Test
	public void putTransactionAndGetStatistics(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(new TransactionRequest().setAmount(1.0).setTimestamp(currentTimeMillis()), headers);
        ResponseEntity responseEntity = restTemplate.postForEntity(LOCAL_LINK + port + "/transactions", httpEntity, ResponseEntity.class);
        assertEquals(201, responseEntity.getStatusCodeValue());

        responseEntity = restTemplate.getForEntity(LOCAL_LINK + port + "/statistics", StatResponse.class);
        assertEquals(200, responseEntity.getStatusCodeValue());
        StatResponse statResponse = (StatResponse) responseEntity.getBody();
        assertEquals(1.0, statResponse.getAvg(), 0);
        assertEquals(1.0, statResponse.getSum(), 0);
        assertEquals(1.0, statResponse.getMax(), 0);
        assertEquals(1.0, statResponse.getMin(), 0);
        assertEquals(1, statResponse.getCount());
	}

    @Test
    public void putTransactionWaitAndGetStatistics() throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(new TransactionRequest().setAmount(1.0).setTimestamp(currentTimeMillis()), headers);
        ResponseEntity responseEntity = restTemplate.postForEntity(LOCAL_LINK + port + "/transactions", httpEntity, ResponseEntity.class);
        assertEquals(201, responseEntity.getStatusCodeValue());

        Thread.sleep(60000);

        responseEntity = restTemplate.getForEntity(LOCAL_LINK + port + "/statistics", StatResponse.class);
        assertEquals(200, responseEntity.getStatusCodeValue());
        StatResponse statResponse = (StatResponse) responseEntity.getBody();
        assertEquals(0.0, statResponse.getAvg(), 0);
        assertEquals(0.0, statResponse.getSum(), 0);
        assertEquals(0.0, statResponse.getMax(), 0);
        assertEquals(0.0, statResponse.getMin(), 0);
        assertEquals(0, statResponse.getCount());
    }

	@Test
	public void putTransactionsAsyncAndGetStatistic() throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        List<ForkJoinTask<ResponseEntity>> tasks = new ArrayList<>();
        for (int i=0; i<8; i++){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity httpEntity = new HttpEntity(new TransactionRequest().setAmount(1.0).setTimestamp(currentTimeMillis()), headers);
            tasks.add(forkJoinPool.submit(() -> restTemplate.postForEntity(LOCAL_LINK + port + "/transactions", httpEntity, ResponseEntity.class)));
        }

        forkJoinPool.awaitQuiescence(1, TimeUnit.SECONDS);
        for (ForkJoinTask<ResponseEntity> task: tasks){
            ResponseEntity responseEntity = task.get();
            assertEquals(201, responseEntity.getStatusCodeValue());
        }

        ResponseEntity responseEntity = restTemplate.getForEntity(LOCAL_LINK + port + "/statistics", StatResponse.class);
        assertEquals(200, responseEntity.getStatusCodeValue());
        StatResponse statResponse = (StatResponse) responseEntity.getBody();
        assertEquals(1.0, statResponse.getAvg(), 0);
        assertEquals(8.0, statResponse.getSum(), 0);
        assertEquals(1.0, statResponse.getMax(), 0);
        assertEquals(1.0, statResponse.getMin(), 0);
        assertEquals(8, statResponse.getCount());
	}

	@Test
    @Ignore
	public void performanceTest(){
	    long timestamp = currentTimeMillis() + 600000;
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
	    for (int i=0; i<200000; i++){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity httpEntity = new HttpEntity(new TransactionRequest().setAmount(1.0).setTimestamp(timestamp), headers);
            forkJoinPool.submit(() -> restTemplate.postForEntity(LOCAL_LINK + port + "/transactions", httpEntity, ResponseEntity.class));
        }
        forkJoinPool.awaitQuiescence(30, TimeUnit.SECONDS);

	    restTemplate.getForEntity(LOCAL_LINK + port + "/statistics", StatResponse.class);
	}
}
