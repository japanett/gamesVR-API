package gamesvrapi.rest.api.controller;

import static gamesvrapi.rest.api.security.SecurityConstants.HEADER_STRING;

import java.util.List;

import javax.validation.Valid;

import gamesvrapi.rest.api.model.PatientEntity;
import gamesvrapi.rest.api.model.TherapistEntity;
import gamesvrapi.rest.api.service.TherapistPatientService;
import gamesvrapi.rest.api.service.TherapistService;
import gamesvrapi.rest.api.web.request.PatchTherapistRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/therapist")
public class TherapistController {

    private final TherapistService therapistService;

    private final TherapistPatientService therapistPatientService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TherapistEntity createTherapist (@Valid @RequestBody @NonNull final TherapistEntity therapist) {
        return this.therapistService.createTherapist(therapist);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TherapistEntity getTherapist (@Valid @RequestHeader(HEADER_STRING) final String token) {
        return this.therapistService.getTherapist(token);
    }

    @PatchMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TherapistEntity patchTherapist (@Valid @RequestHeader(HEADER_STRING) final String token,
            @Valid @RequestBody final PatchTherapistRequest req) {
        return this.therapistService.patchTherapist(token, req);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TherapistEntity deleteTherapist (@Valid @RequestHeader(HEADER_STRING) final String token) {
        return this.therapistService.deleteTherapist(token);
    }

    @PostMapping(path = "/patient", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PatientEntity createPatient (@Valid @RequestHeader(HEADER_STRING) final String token,
            @Valid @RequestBody @NonNull final PatientEntity patient) {
        return this.therapistPatientService.createPatient(token, patient);
    }

    @GetMapping(path = "/patients", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PatientEntity> getPatients (@Valid @RequestHeader(HEADER_STRING) final String token) {
        return this.therapistPatientService.getPatients(token);
    }

    @GetMapping(path = "/patient", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PatientEntity getPatient (@Valid @RequestHeader(HEADER_STRING) final String token,
            @RequestParam @NonNull final String patientId) {
        return this.therapistPatientService.getPatient(token, patientId);
    }

    //@RequestHeader("Authorization") final String token
    //    @GetMapping
    //    public List<TherapistEntity> getAllTherapists() {
    ////        String[] splitted = token.split(" ");
    ////        log.info("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    ////        String decodedToken1 = StringUtils.newStringUtf8(Base64.decodeBase64(JWT.decode(splitted[1]).getPayload()));
    ////        ObjectMapper mapper = new ObjectMapper();
    ////        try {
    ////            Map<String,Object> map = mapper.readValue(decodedToken1, Map.class);
    //////            log.info(map.get());
    ////        } catch (IOException e) {
    ////            e.printStackTrace();
    ////        }
    //        log.info("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    //        return this.service.getAllTherapists();
    //    }
}
