package interview.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    private StatService statService;

    @RequestMapping(method = RequestMethod.POST, path = "/transactions", consumes = MediaType.APPLICATION_JSON_VALUE)

    @ResponseBody
    public ResponseEntity submitTransaction(@RequestBody TransactionRequest transactionRequest){
        return statService.submitTransaction(transactionRequest);
    }

    @RequestMapping(method= RequestMethod.GET, path = "/statistics")
    @ResponseBody
    public StatResponse getStatistics(){
        return statService.calculateStats();
    }
}
