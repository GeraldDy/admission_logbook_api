package ph.gov.philhealth.admission_logbook.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class DatabaseConfig {

    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.url}")
    private String url;

    public Connection  databaseConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection(
                url,username,password
            );
            System.out.println("Connected to database successfully");
            return conn;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
