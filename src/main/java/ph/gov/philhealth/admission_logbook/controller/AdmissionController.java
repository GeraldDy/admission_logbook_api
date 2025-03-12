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

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.Base64;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.BadPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.MessageDigest;
import java.io.ByteArrayInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@RestController

public class AdmissionController {

    @Value("${api_key}")
    private String apiKey;
    @Value("${app_key}")
    private String appKey;
    private final DatabaseOperations databaseOperations;


    static int key1_length = 16;
    static int key2_length = 16;
    static int key_length = key1_length + key2_length;
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
            Integer isInserted = databaseOperations.Insert(admission_data);
            System.out.print("this is ID for admission_inserted " + isInserted);

            boolean saveReferenceNumber = databaseOperations.InsertRefNum(reference_number,isInserted);
            System.out.print(saveReferenceNumber);

            if (isInserted != 0 && saveReferenceNumber) {
                response.put("status", HttpStatus.OK);
                response.put("reference_number", reference_number);
                response.put("message", "Successfully submitted admission with reference number: " + reference_number);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            else {
                response.put("status:", HttpStatus.INTERNAL_SERVER_ERROR);
                response.put("message:", "Failed to submit admission. Please try again.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    }

    private boolean isValidApiKey(String providedApiKey, String providedAppKey) {
        // System.out.print(providedApiKey + ": " +apiKey);
        return apiKey.equals(providedApiKey) && appKey.equals(providedAppKey);
    }


    @PostMapping(value = "/DecryptDataUsingCipherKey", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> decryptDataCipherKey(
                                                       @RequestBody EncryptedData encryptedData ,
                                                       @RequestParam String Cipherkey) {
        try {

            String result = DecryptUsingCipherKey(encryptedData, Cipherkey).trim();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Decryption failed: " + e.getMessage());
        }
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


    private String DecryptUsingCipherKey(EncryptedData encryptedxml, String key) {
        String stringdoc = encryptedxml.getDoc().replaceAll("[\\t\\n\\r]+", "");
        String result = "";
        byte[] iv = Base64.getDecoder().decode(encryptedxml.getIv());
        //byte[] doc = Base64.getDecoder().decode(encryptedxml.getDoc());
        byte[] doc = Base64.getDecoder().decode(stringdoc);
        byte[] keybytes = KeyHash(key);
        byte[] decryptedstringbytes = DecryptUsingAES(doc, keybytes, iv);
        try {
            result = new String(decryptedstringbytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            System.out.print("error on decrypting");
        }
        return result;
    }

    private byte[] DecryptUsingAES(byte[] stringbytes, byte[] keybytes, byte[] ivbytes) {
        byte[] decryptedstringbytes = null;
        try {
            IvParameterSpec ips = new IvParameterSpec(ivbytes);
            SecretKeySpec sks = new SecretKeySpec(keybytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
            cipher.init(Cipher.DECRYPT_MODE, sks, ips);
            decryptedstringbytes = cipher.doFinal(stringbytes);
        } catch (NoSuchAlgorithmException ex) {
           System.out.print("error unidentified");
        } catch (NoSuchPaddingException ex) {
            System.out.print("error unidentified");
        } catch (InvalidKeyException ex) {
            System.out.print("error unidentified");
        } catch (InvalidAlgorithmParameterException ex) {
            System.out.print("error unidentified");
        } catch (IllegalBlockSizeException ex) {
            System.out.print("error unidentified");
        } catch (BadPaddingException ex) {
            System.out.print("error unidentified");
        }
        return decryptedstringbytes;
    }
    private byte[] KeyHash(String key) {
        byte[] keybyte = new byte[key_length];
        for (int i = 0; i < key_length; i++) {
            keybyte[i] = 0;
        }
        byte[] keybytes = key.getBytes();
        byte[] keyhashbytes = SHA256HashBytes(keybytes);
        System.arraycopy(keyhashbytes, 0, keybyte, 0, Math.min(keyhashbytes.length, key_length));
        return keybyte;
    }
    private byte[] SHA256HashBytes(byte[] key) {
        byte[] hash = null;
        try {
            hash = MessageDigest.getInstance("SHA-256").digest(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }
    private static class EncryptedData {

        public EncryptedData() {
        }

        private String iv;
        private String doc;

        public String getIv() {
            return iv;
        }

        public String getDoc() {
            return doc;
        }

        public void setDoc(String doc) {
            this.doc = doc;
        }

    }

}
