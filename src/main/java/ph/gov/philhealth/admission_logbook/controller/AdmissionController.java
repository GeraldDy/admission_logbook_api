package ph.gov.philhealth.admission_logbook.controller;
//models
import ph.gov.philhealth.admission_logbook.model.AdmissionModel;

//services or business logic
import ph.gov.philhealth.admission_logbook.services.ValidateAdmission;


import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@RestController
public class AdmissionController {


    @RequestMapping("/")
    public String index() {
        return "index.html";
    }


    @PostMapping
    @RequestMapping("/v1/submit_admission")
    @ResponseBody
    public  Map<String, Object> SubmitAdmission(@RequestBody AdmissionModel admission_data) {

        //ValidationService here
        ValidateAdmission validateAdmission = new ValidateAdmission();
        List<String> errorMessages =  validateAdmission.validateAdmission(admission_data);

        Map<String, Object> response = new HashMap<>();
        if (!errorMessages.isEmpty()) {
            response.put("success", false);
            response.put("errors", errorMessages);
        }

        else {
            //to do generate reference_number here
            String referenceNumber = generateReferenceNumber(admission_data);
            //to do then save it to oracle database
            response.put("reference_number", referenceNumber);
            response.put("success", true);
        }
        return response;
    }

    private String generateReferenceNumber(AdmissionModel admission_data) {
        // Your logic to generate reference number based on the admission data
        String hospitalCode = admission_data.getHospital_code();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateSubmitted = today.format(formatter);
        int sequenceNumber = 1;
        return "eAD-" + hospitalCode + "-" + dateSubmitted + "-" + String.format("%04d", sequenceNumber);
    }
}
