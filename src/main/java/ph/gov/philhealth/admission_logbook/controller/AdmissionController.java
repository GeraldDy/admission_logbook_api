package ph.gov.philhealth.admission_logbook.controller;
//models
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import ph.gov.philhealth.admission_logbook.model.AdmissionModel;

//services or business logic
import ph.gov.philhealth.admission_logbook.services.ValidateAdmission;
import ph.gov.philhealth.admission_logbook.services.GenerateReferenceNumber;




import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController

public class AdmissionController {

    @Value("${api_key}")
    private String apiKey;
    @Value("${app_key}")
    private String appKey;
    @RequestMapping("/home")
    public String index() {

        return "index.html";
    }

    @GetMapping("/get_admissions")
    public void getAllAdmissions() {
        String sql = "SELECT * FROM admission_logbook_tbl";

        List<Map<String, Object>> admissions = jdbcTemplate.queryForList(sql);

        if (admissions.isEmpty()) {
            System.out.println("No records found.");
        } else {
            for (Map<String, Object> admission : admissions) {
                System.out.println("Hospital Code: " + admission.get("hospital_code"));
                System.out.println("Patient Name: " + admission.get("patient_name"));
                System.out.println("Admission Date: " + admission.get("admission_date"));
                System.out.println("Reference Number: " + admission.get("reference_number"));
                System.out.println("---------------------------");
            }
        }
    }


    @Autowired
    private JdbcTemplate jdbcTemplate;
    @PostMapping
    @RequestMapping("/submit_admission")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> SubmitAdmission(
            @RequestBody AdmissionModel admission_data,
            @RequestParam String api_key,
            @RequestParam String app_key) {



        //ValidationService here
        ValidateAdmission validateAdmission = new ValidateAdmission();
        List<String> errorMessages =  validateAdmission.validateAdmission(admission_data);

        Map<String, Object> response = new HashMap<>();


        if (!isValidApiKey(api_key, app_key)) {
            response.put("success", false);
            response.put("message", "Invalid API key or App key.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }


        if (!errorMessages.isEmpty()) {
            response.put("success", false);
            response.put("errors", errorMessages);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        else {
            //to do generate reference_number here
            GenerateReferenceNumber generateReferenceNumber = new GenerateReferenceNumber();
            String reference_number = generateReferenceNumber.generateReferenceNumber(admission_data);
            System.out.println(reference_number);
            //to do then save it to oracle database

//            boolean isSaved = saveAdmissionToDatabase(admission_data, referenceNumber);
//
//            if (!isSaved) {
//                response.put("success", false);
//                response.put("message", "Failed to save admission record.");
//                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//            }


            response.put("status:",  HttpStatus.OK);
            response.put("reference_number:", reference_number);
            response.put("success:", true);
            response.put("message:", "Successfully submitted admission with reference number:" + reference_number);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    private boolean isValidApiKey(String providedApiKey, String providedAppKey) {
        // System.out.print(providedApiKey + ": " +apiKey);
        return apiKey.equals(providedApiKey) && appKey.equals(providedAppKey);
    }

    private boolean saveAdmissionToDatabase(AdmissionModel admission_data, String referenceNumber) {
        try {
            String sql = "INSERT INTO admission_table (hospital_code, admission_date, reference_number) VALUES (?, ?, ?)";

            jdbcTemplate.update(sql,
                    admission_data.getHospital_code(),
                    admission_data.getAdmission_date(),
                    referenceNumber);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
