package interview.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Logger;

import static java.lang.System.currentTimeMillis;

@Service
public class StatService {
    private static final Logger LOG = Logger.getLogger(StatService.class.getName());
    private static final int ONE_MINUTE = 60000;
    private static final RowMapper<StatResponse> RESPONSE_MAPPER = (rs, rowNum) -> {
        StatResponse statResponse = new StatResponse();
        try {
            statResponse.setAvg(rs.getDouble("avg(values)"));
            statResponse.setSum(rs.getDouble("sum(values)"));
            statResponse.setMax(rs.getDouble("max(values)"));
            statResponse.setMin(rs.getDouble("min(values)"));
            statResponse.setCount(rs.getLong("count(values)"));
        } catch (SQLException e) {
            LOG.info("Mapper encountered issue: " + e);
        }
        return statResponse;
    };

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Scheduled(cron = "*/30 * * * * *")
    public void cleanStorage() {
        LOG.info("Cleaning up...");
        jdbcTemplate.update("delete from statistics where timestamp < ?", currentTimeMillis() - ONE_MINUTE);
    }

    public ResponseEntity submitTransaction(TransactionRequest request) {
        long requestTimestamp = request.getTimestamp();
        long untilTime = currentTimeMillis();
        long fromTime = untilTime - ONE_MINUTE;
        if (requestTimestamp >= fromTime) {
            jdbcTemplate.update("insert into statistics (amount, timestamp) values (?,?)", request.getAmount(), requestTimestamp);
            return new ResponseEntity(HttpStatus.CREATED);
        }
        LOG.info("Transaction request contained timestamp from the past, skipping." +
                " Valid time range: " + Date.from(Instant.ofEpochMilli(fromTime)) + "-" + Date.from(Instant.ofEpochMilli(untilTime)) +
                " Transaction timestamp: " + Date.from(Instant.ofEpochMilli(requestTimestamp)));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    public StatResponse calculateStats() {
        //But can there be transaction with future timestamp?
        return jdbcTemplate.queryForObject("select sum(values), avg(values),  max(values), min(values), count(values)" +
                "from (select amount as values from statistics where timestamp >= ?)", RESPONSE_MAPPER, currentTimeMillis() - ONE_MINUTE);
    }
}