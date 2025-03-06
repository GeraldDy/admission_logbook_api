package ph.gov.philhealth.admission_logbook.services;

import ph.gov.philhealth.admission_logbook.model.AdmissionModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class GenerateReferenceNumber {
    public String generateReferenceNumber(AdmissionModel admission_data) {
        //generate reference number based on the admission data
        String hospitalCode = admission_data.getHospital_code();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateSubmitted = today.format(formatter);
        int sequenceNumber = 1;
        return "eAD-" + hospitalCode + "-" + dateSubmitted + "-" + String.format("%04d", sequenceNumber);
    }

}
