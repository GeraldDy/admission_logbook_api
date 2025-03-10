package ph.gov.philhealth.admission_logbook.services;
import ph.gov.philhealth.admission_logbook.config.DatabaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ph.gov.philhealth.admission_logbook.model.AdmissionModel;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class DatabaseOperations {

    private final DatabaseConfig databaseConfig;

    @Autowired
    public DatabaseOperations(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }
    public Integer Insert(AdmissionModel admission_data){
        System.out.print("Insert Data here");
        Integer generatedId;
        try (Connection conn = databaseConfig.databaseConnection()) {
            if (conn != null) {
                System.out.println("Connected");
                String sql = "{call admission_logbook_pkg.insert_admission_logbook(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)}";
                try (CallableStatement stmt = conn.prepareCall(sql)) {

                    LocalDateTime  today = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
                    String DateTimeToday = formatter.format(today);

                    stmt.setString(1, admission_data.getHospital_code()); // p_hospital_code
                    stmt.setString(2, admission_data.getCase_number()); // p_case_number
                    stmt.setString(3, admission_data.getPatient_pin()); // p_philhealth_id_num
                    stmt.setString(4, admission_data.getP_first_name()); // p_first_name
                    stmt.setString(5, admission_data.getP_middle_name()); // p_middle_name
                    stmt.setString(6, admission_data.getP_last_name()); // p_last_name
                    stmt.setString(7, admission_data.getP_suffix()); // p_suffix
                    stmt.setString(8, admission_data.getP_gender()); // p_gender
                    stmt.setString(9, admission_data.getP_birthday()); // p_birthday
                    stmt.setShort(10, admission_data.getP_age()); // p_age
                    stmt.setString(11, admission_data.getP_address()); // p_address
                    stmt.setString(12, admission_data.getP_nationality()); // p_nationality
                    stmt.setString(13, admission_data.getP_contact_number()); // p_contact_num
                    stmt.setString(14, admission_data.getP_email_address()); // p_email_address
                    stmt.setString(15, admission_data.getPatient_type()); // p_patient_type
                    stmt.setString(16, admission_data.getM_first_name()); // p_m_first_name
                    stmt.setString(17, admission_data.getM_middle_name()); // p_m_middle_name
                    stmt.setString(18, admission_data.getM_last_name()); // p_m_last_name
                    stmt.setString(19, admission_data.getM_suffix()); // p_m_suffix
                    stmt.setString(20, admission_data.getM_email_address()); // p_m_email_add
                    stmt.setString(21, admission_data.getM_contact_number()); // p_m_contact_num
                    stmt.setString(22, admission_data.getBenefit_availment()); // benefit_availment
                    stmt.setString(23, admission_data.getReason_for_availment()); // reason_availment
                    stmt.setString(24, admission_data.getChief_complaint()); // chief_complaint
                    stmt.setString(25, admission_data.getAdmission_code()); // admission_code
                    stmt.setString(26, "0.00"); // p_admission_amount (default value if not provided)
                    stmt.setString(27, DateTimeToday); // p_date_submitted (assuming reference_number is used for this)
                    stmt.setString(28, DateTimeToday); // p_date_created
                    stmt.setString(29, DateTimeToday); // p_date_updated
                    stmt.setString(30, admission_data.getP_mononym()); // p_p_mononym
                    stmt.setString(31, admission_data.getAdmission_date()); // p_admission_date
                    stmt.setString(32, admission_data.getAdmission_time()); // p_admission_time

                    // Output parameter (Index 33, should match the procedure definition)
                    stmt.registerOutParameter(33, java.sql.Types.VARCHAR);

                    // Execute the stored procedure
                    stmt.execute();
                    // Retrieve the generated admission_id
                    generatedId = Integer.valueOf(stmt.getString(33));
                    System.out.println("Generated ID: " + generatedId);
                    return generatedId;
                }
            } else {
                System.out.println("Database connection failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public boolean InsertRefNum(String reference_number, Integer admission_id){
        boolean isSuccess = false;
        try (Connection conn = databaseConfig.databaseConnection()) {
            if (conn != null) {
                System.out.println("Connected");
                String sql = "{call ADMISSION_LOGBOOK_INSERT_REFERENCE_NUMBER.INSERT_REF_NUM(?,?,?,?)}";
                try (CallableStatement stmt = conn.prepareCall(sql)) {
                    LocalDateTime  today = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
                    String DateTimeToday = formatter.format(today);

                    stmt.setString(1, reference_number);
                    stmt.setString(2,DateTimeToday);
                    stmt.setString(3, DateTimeToday);
                    stmt.setInt(4, admission_id);

                    int rows = stmt.executeUpdate();
                    if(rows > 0){
                        isSuccess = true;
                    }
                }
            }
            else{
                System.out.println("Database connection failed.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }
    public String Select(String reference_number){
        System.out.println("save reference number: "+ reference_number);
        return "Success";
    }
}
