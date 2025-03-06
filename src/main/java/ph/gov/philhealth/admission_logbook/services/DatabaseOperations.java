package ph.gov.philhealth.admission_logbook.services;
import org.springframework.beans.factory.annotation.Autowired;
import ph.gov.philhealth.admission_logbook.model.AdmissionModel;
import org.springframework.jdbc.core.JdbcTemplate;
import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.util.Base64;
import java.util.UUID;

public class DatabaseOperations {

    private JdbcTemplate jdbcTemplate;

    public String Insert(AdmissionModel admission_data, String reference_number){
        System.out.println(reference_number);
        System.out.print("Insert Data here");
//        CallableStatement callableStatement = connection.prepareCall("{call proc (?,?)}");
//        try {
//            String sql = "INSERT INTO admission_table (hospital_code, admission_date, reference_number) VALUES (?, ?, ?)";
//
//            jdbcTemplate.update(sql,
//                    admission_data.getHospital_code(),
//                    admission_data.getAdmission_date(),
//                    reference_number);
//
//            return "true";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "true";
//        }
        return "true";
    }

    public String Select(String reference_number){
        System.out.println(reference_number);
        return "Success";
    }
}
