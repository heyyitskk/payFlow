Request Lifecycle - DispatcherServlet intercepts the request, HandlerAdapter deserializes JSON via Jackson, routes to your controller method

Serialisation - Jackson's ObjectMapper converts JSON to Java objects; wrong key names cause mapping failures or are ignored

Spring Boot Features - Auto-configuration (H2 setup), Embedded Server (Tomcat), Starter Dependencies (spring-boot-starter-data-jpa)

Spring vs Spring Boot - Plain Spring needs manual XML config, bean definitions, application server setup; Boot handles all automatically

Stateless REST - Each request is independent (no session); critical for load balancers to route requests to any server without losing context

Persistence - Java List loses data on restart (unacceptable); H2 database ensures durability required for payments compliance