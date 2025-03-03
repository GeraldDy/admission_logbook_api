package ph.gov.philhealth.admission_logbook.services;

import ph.gov.philhealth.admission_logbook.model.AdmissionModel;
import java.util.ArrayList;
import java.util.List;

public class ValidateAdmission {

    public List<String> validateAdmission(AdmissionModel admission_data) {
        List<String> errorMessages = new ArrayList<>();

        if (admission_data.getAdmission_code().isEmpty()) {
            errorMessages.add("admission_code is empty");
        }

        return errorMessages;
    }
}
