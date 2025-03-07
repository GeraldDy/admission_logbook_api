package ph.gov.philhealth.admission_logbook.services;
import ph.gov.philhealth.admission_logbook.config.DatabaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ph.gov.philhealth.admission_logbook.model.AdmissionModel;

import java.sql.Connection;
import java.sql.PreparedStatement;


@Service
public class DatabaseOperations {

    private final DatabaseConfig databaseConfig;

    @Autowired
    public DatabaseOperations(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }
    public void Insert(AdmissionModel admission_data, String reference_number){
        System.out.println(reference_number);
        System.out.print("Insert Data here");

        try (Connection conn = databaseConfig.databaseConnection()) {
            if (conn != null) {
                System.out.println("Connected");
                String sql = "{call admission_logbook_pkg.insert_admission(?, ?, ?)}";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, reference_number);

                    int rowsInserted = stmt.executeUpdate();
                    System.out.println(rowsInserted + " row(s) inserted.");
                }
            } else {
                System.out.println("Database connection failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String Select(String reference_number){
        System.out.println(reference_number);
        return "Success";
    }
}
