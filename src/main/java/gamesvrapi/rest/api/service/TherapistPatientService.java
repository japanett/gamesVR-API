package gamesvrapi.rest.api.service;

import java.util.List;
import java.util.UUID;

import gamesvrapi.rest.api.exceptions.ResourceNotFoundException;
import gamesvrapi.rest.api.model.PatientEntity;
import gamesvrapi.rest.api.model.TherapistEntity;
import gamesvrapi.rest.api.repository.Therapist.TherapistPatientEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TherapistPatientService {

    @Autowired
    private final TokenInterceptorService tokenInterceptorService;

    @Autowired
    private final TherapistPatientEntityRepository therapistPatientEntityRepository;

    @Transactional
    public PatientEntity createPatient (final String token, final PatientEntity patient) {
        TherapistEntity therapist = tokenInterceptorService.translateTherapistToken(token);
        patient.setTherapist(therapist);
        patient.setId(generateId());
        return therapistPatientEntityRepository.save(patient);

    }

    public List<PatientEntity> getPatients (final String token) {
        TherapistEntity therapist = tokenInterceptorService.translateTherapistToken(token);
        return therapistPatientEntityRepository.findByTherapistId(therapist.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Therapist " + therapist.getName() + " has no patients !")
                );
    }

    public PatientEntity getPatient (final String token,
            final String patientId) {
        TherapistEntity therapist = tokenInterceptorService.translateTherapistToken(token);
        return therapistPatientEntityRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("TherapistPatientService, m=getPatient, patientId=" + patientId)
                );
    }

    private String generateId () {
        String id = (UUID.randomUUID().toString().substring(0, 6));
        therapistPatientEntityRepository.findById(id)
                .ifPresent(entity -> {
                    this.generateId();
                });
        return id;
    }

}
