//package blog.api.infra;
//
//import jakarta.annotation.PostConstruct;
//import org.flywaydb.core.Flyway;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FlywayRepairConfig {
//
//    @Autowired
//    private Flyway flyway;
//
//    @PostConstruct
//    public void repairFlyway() {
//        flyway.repair();
//    }
//}
