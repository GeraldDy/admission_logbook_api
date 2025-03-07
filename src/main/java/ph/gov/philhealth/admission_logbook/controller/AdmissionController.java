package ph.gov.philhealth.admission_logbook.controller;
//models
import ph.gov.philhealth.admission_logbook.model.AdmissionModel;

//services or business logic
import ph.gov.philhealth.admission_logbook.services.ValidateAdmission;
import ph.gov.philhealth.admission_logbook.services.GenerateReferenceNumber;
import ph.gov.philhealth.admission_logbook.services.DatabaseOperations;
//config
import ph.gov.philhealth.admission_logbook.config.DatabaseConfig;



import org.springframework.web.bind.annotation.*;
import java.sql.Connection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


@RestController

public class AdmissionController {

    @Value("${api_key}")
    private String apiKey;
    @Value("${app_key}")
    private String appKey;
    private final DatabaseOperations databaseOperations;

    @Autowired
    public AdmissionController(DatabaseOperations databaseOperations) {
        this.databaseOperations = databaseOperations;
    }

    @PostMapping
    @RequestMapping("/submit_admission")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> SubmitAdmission(
            @RequestBody AdmissionModel admission_data,
            @RequestParam String api_key,
            @RequestParam String app_key) {

        //response messages
        Map<String, Object> response = new HashMap<>();

        //Check Valid APP_KEY & API_KEY
        if (!isValidApiKey(api_key, app_key)) {
            response.put("success", false);
            response.put("message", "Invalid API key or App key.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }



        //ValidationService Validate Admission Data
        ValidateAdmission validateAdmission = new ValidateAdmission();
        List<String> errorMessages =  validateAdmission.validateAdmission(admission_data);
        if (!errorMessages.isEmpty()) {
            response.put("success", false);
            response.put("errors", errorMessages);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        else {
            //generate reference_number
            GenerateReferenceNumber generateReferenceNumber = new GenerateReferenceNumber();
            String reference_number = generateReferenceNumber.generateReferenceNumber(admission_data);

            //after generate ref number save it to database (database operation)
            databaseOperations.Insert(admission_data,reference_number);


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



//    @GetMapping("/get_admissions")
//    public void getAllAdmissions() {
//        String sql = "SELECT * FROM admission_logbook_tbl";
//
//        List<Map<String, Object>> admissions = jdbcTemplate.queryForList(sql);
//
//        if (admissions.isEmpty()) {
//            System.out.println("No records found.");
//        } else {
//            for (Map<String, Object> admission : admissions) {
//                System.out.println("Hospital Code: " + admission.get("hospital_code"));
//                System.out.println("Patient Name: " + admission.get("patient_name"));
//                System.out.println("Admission Date: " + admission.get("admission_date"));
//                System.out.println("Reference Number: " + admission.get("reference_number"));
//                System.out.println("---------------------------");
//            }
//        }
//    }

}
