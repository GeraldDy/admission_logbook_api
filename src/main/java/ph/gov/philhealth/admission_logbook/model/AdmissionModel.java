package ph.gov.philhealth.admission_logbook.model;

public class AdmissionModel {
    private String hospital_code;

    public String getCase_number() {
        return case_number;
    }

    public void setCase_number(String case_number) {
        this.case_number = case_number;
    }

    private String case_number;

    public String getPatient_pin() {
        return patient_pin;
    }

    public void setPatient_pin(String patient_pin) {
        this.patient_pin = patient_pin;
    }

    public String getP_mononym() {
        return p_mononym;
    }

    public void setP_mononym(String p_mononym) {
        this.p_mononym = p_mononym;
    }

    public String getP_first_name() {
        return p_first_name;
    }

    public void setP_first_name(String p_first_name) {
        this.p_first_name = p_first_name;
    }

    public String getP_last_name() {
        return p_last_name;
    }

    public void setP_last_name(String p_last_name) {
        this.p_last_name = p_last_name;
    }

    public String getP_middle_name() {
        return p_middle_name;
    }

    public void setP_middle_name(String p_middle_name) {
        this.p_middle_name = p_middle_name;
    }

    public String getP_suffix() {
        return p_suffix;
    }

    public void setP_suffix(String p_suffix) {
        this.p_suffix = p_suffix;
    }

    public String getP_birthday() {
        return p_birthday;
    }

    public void setP_birthday(String p_birthday) {
        this.p_birthday = p_birthday;
    }

    public String getP_gender() {
        return p_gender;
    }

    public void setP_gender(String p_gender) {
        this.p_gender = p_gender;
    }

    public int getP_age() {
        return p_age;
    }

    public void setP_age(int p_age) {
        this.p_age = p_age;
    }

    public String getP_nationality() {
        return p_nationality;
    }

    public void setP_nationality(String p_nationality) {
        this.p_nationality = p_nationality;
    }

    public String getP_address() {
        return p_address;
    }

    public void setP_address(String p_address) {
        this.p_address = p_address;
    }

    public String getP_contact_number() {
        return p_contact_number;
    }

    public void setP_contact_number(String p_contact_number) {
        this.p_contact_number = p_contact_number;
    }

    public String getP_email_address() {
        return p_email_address;
    }

    public void setP_email_address(String p_email_address) {
        this.p_email_address = p_email_address;
    }

    public String getPatient_type() {
        return patient_type;
    }

    public void setPatient_type(String patient_type) {
        this.patient_type = patient_type;
    }

    public String getM_first_name() {
        return m_first_name;
    }

    public void setM_first_name(String m_first_name) {
        this.m_first_name = m_first_name;
    }

    public String getM_last_name() {
        return m_last_name;
    }

    public void setM_last_name(String m_last_name) {
        this.m_last_name = m_last_name;
    }

    public String getM_middle_name() {
        return m_middle_name;
    }

    public void setM_middle_name(String m_middle_name) {
        this.m_middle_name = m_middle_name;
    }

    public String getM_suffix() {
        return m_suffix;
    }

    public void setM_suffix(String m_suffix) {
        this.m_suffix = m_suffix;
    }

    public String getM_email_address() {
        return m_email_address;
    }

    public void setM_email_address(String m_email_address) {
        this.m_email_address = m_email_address;
    }

    public String getM_contact_number() {
        return m_contact_number;
    }

    public void setM_contact_number(String m_contact_number) {
        this.m_contact_number = m_contact_number;
    }

    public String getBenefit_availment() {
        return benefit_availment;
    }

    public void setBenefit_availment(String benefit_availment) {
        this.benefit_availment = benefit_availment;
    }

    public String getReason_for_availment() {
        return reason_for_availment;
    }

    public void setReason_for_availment(String reason_for_availment) {
        this.reason_for_availment = reason_for_availment;
    }

    public String getChief_complaint() {
        return chief_complaint;
    }

    public void setChief_complaint(String chief_complaint) {
        this.chief_complaint = chief_complaint;
    }

    public String getAdmission_code() {
        return admission_code;
    }

    public void setAdmission_code(String admission_code) {
        this.admission_code = admission_code;
    }

    public String getAdmission_time() {
        return admission_time;
    }

    public void setAdmission_time(String admission_time) {
        this.admission_time = admission_time;
    }

    private String patient_pin;
    private String p_mononym;
    private String p_first_name;
    private String p_last_name;
    private String p_middle_name;
    private String p_suffix;
    private String p_birthday;
    private String p_gender;
    private int p_age;
    private String p_nationality;
    private String p_address;
    private String p_contact_number;
    private String p_email_address;
    private String patient_type;
    private String m_first_name;
    private String m_last_name;
    private String m_middle_name;
    private String m_suffix;
    private String m_email_address;
    private String m_contact_number;
    private String benefit_availment;
    private String reason_for_availment;
    private String chief_complaint;
    private String admission_code;

    public String getAdmission_date() {
        return admission_date;
    }

    public void setAdmission_date(String admission_date) {
        this.admission_date = admission_date;
    }

    private String admission_date;
    private String admission_time;


    public String getHospital_code() {
        return hospital_code;
    }

    public void setHospital_code(String hospital_code) {
        this.hospital_code = hospital_code;
    }

    @Override
    public String toString() {
        return "AdmissionData{" +
                "hospital_code='" + hospital_code + '\'' +
                '}';
    }
}
