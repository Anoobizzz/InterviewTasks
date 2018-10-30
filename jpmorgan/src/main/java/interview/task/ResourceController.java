package interview.task;

import interview.task.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Controller
public class ResourceController {
    @Autowired
    private MessageProcessingService service;

    @RequestMapping(path = "/submit", method = RequestMethod.PUT,
            consumes = APPLICATION_JSON_VALUE, produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String submitMessage(@RequestBody Message message) {
        return service.processMessage(message);
    }
}
