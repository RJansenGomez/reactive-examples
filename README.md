# reactive-examples
Different modules with reactive flows + all modules connected


###Clarifications
Resources will be shared across Ms to make easy the testing and faster the development.
It's known that in most cases, each Ms should have its own data set and data sources to make them independent from one to another.

For the frameworkless examples we use coroutines because it's made in kotlin, but it's also possible in Java with threads and the [CompletableFuture API](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html). Maybe in the future I will make the java version.

## TODO
### reactive+non-reactive event module:  
   - Controller
   - Stores in DB
   - process sleep 500ms (service)
   - Publish on kafka with avro

### reactive+non-reactive spreader module


### Credits
A lot of inspiration and support from this resources

Github: https://github.com/kmandalas/webclient-showcase

[Article](https://dzone.com/articles/spring-reactive-microservices-a-showcase) related to the previous repository