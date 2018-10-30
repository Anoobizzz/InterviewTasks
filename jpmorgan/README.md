Task description:

* A sale has a product type field and a value – you should choose sensible types for these.
* Any number of different product types can be expected. There is no fixed set.
* A message notifying you of a sale could be one of the following types
    - Message Type 1 – contains the details of 1 sale E.g apple at 10p
    - Message Type 2 – contains the details of a sale and the number of occurrences of
    that sale. E.g 20 sales of apples at 10p each.
    - Message Type 3 – contains the details of a sale and an adjustment operation to be
    applied to all stored sales of this product type. Operations can be add, subtract, or
    multiply e.g Add 20p apples would instruct your application to add 20p to each sale
    of apples you have recorded.
 
Comments:
 
Task itself seems to only provide quite vague requirements for the the implementation.
There were no limitations to used technologies and implementation being written in Java.
Even though there was an expectation of minimal dependency usage I decided to go for Core Spring Boot dependencies.
This way I could make the task a bit more exciting and interesting to implement :)

Since there was no representation\expectation of input delivery - I assumed it would be somewhat of a web request to
a REST API. As for request format I decided to use JSON. It is easily mapped onto native Java object when used together
with Spring Boot.

Some other requirements were not clear as well, so I've let myself play around with implementation of the task.
There are several tests included, but of course it is possible to simply fire an application and submit request to
'/submit' with header "Content-Type: application/json" and JSON body, e.g.:

Adding product:
{
	"value": 2,
	"productType": "apple"
}

Applying action (possibly actions, ass required by task: add, subtract, multiply):
{
	"action": "add",
	"value": 2,
	"productType": "apple"
}

Response is plain text for easier representation of result.
Suggested tools for testing: curl, postman.

Example of request using curl: 
curl -X PUT localhost:8080/submit -H "Content-Type: application/json" -d '{"value": 2,"productType": "apple"}'