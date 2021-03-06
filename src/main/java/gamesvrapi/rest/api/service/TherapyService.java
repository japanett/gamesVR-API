package gamesvrapi.rest.api.service;

import java.util.List;

import gamesvrapi.rest.api.entities.PatientEntity;
import gamesvrapi.rest.api.entities.StageEntity;
import gamesvrapi.rest.api.entities.TherapistEntity;
import gamesvrapi.rest.api.entities.TherapyEntity;
import gamesvrapi.rest.api.enums.PlatformEnum;
import gamesvrapi.rest.api.exceptions.DuplicateEntryException;
import gamesvrapi.rest.api.exceptions.ExpectationFailedException;
import gamesvrapi.rest.api.exceptions.NotAllowedException;
import gamesvrapi.rest.api.exceptions.ResourceNotFoundException;
import gamesvrapi.rest.api.repository.PatientRepository;
import gamesvrapi.rest.api.repository.TherapyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TherapyService {

    @Autowired
    private final TokenInterceptorService tokenInterceptorService;

    @Autowired
    private final PatientRepository patientRepository;

    @Autowired
    private final TherapyRepository therapyRepository;

    @Transactional
    public TherapyEntity create (final String token,
            final String patientId,
            final TherapyEntity request) {

        TherapistEntity therapist = tokenInterceptorService.translateTherapistToken(token);

        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found!"));

        this.checkDuplicateTherapy(patient.getTherapies(), request.getName(), request.getPlatform());

        request.setPatient(patient);
        log.info("=============== CREATED THERAPY:{}, PLATFORM:{}, PATIENT: {} ===============", request.getName(),
                request.getPlatform().getDescription(), patient.getName());

        return therapyRepository.save(request);
    }

    public List<TherapyEntity> getPatientTherapies (final String token, final String patientId) {
        TherapistEntity therapist = tokenInterceptorService.translateTherapistToken(token);

        return therapyRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Therapies not found!"));
    }

    @Transactional
    public TherapyEntity changeTherapyStatus (final String token, final String patientId, final Long therapyId,
            String status) {
        TherapistEntity therapist = tokenInterceptorService.translateTherapistToken(token);

        TherapyEntity therapy = therapyRepository.findByPatientIdAndId(patientId, therapyId)
                .orElseThrow(() -> new ResourceNotFoundException("Therapy not found for this patient"));

        switch (status) {
            case "activate":
                therapy.setActive(Boolean.TRUE);
                return therapyRepository.save(therapy);
            case "deactivate":
                therapy.setActive(Boolean.FALSE);
                return therapyRepository.save(therapy);
            default:
                throw new ExpectationFailedException("activate / deactivate expected");
        }

    }

    @Transactional
    public TherapyEntity setStage (final String token, final String patientId, final Long therapyId, final StageEntity stage) {
        TherapistEntity therapist = tokenInterceptorService.translateTherapistToken(token);

        TherapyEntity therapy = therapyRepository.findByPatientIdAndId(patientId, therapyId)
                .orElseThrow(() -> new ResourceNotFoundException("Therapy not found for this patient"));

        if (therapy.getActive().equals(Boolean.FALSE)) {
            throw new NotAllowedException("This therapy is deactivated");
        }
        List<StageEntity> stages = therapy.getStages();
        stage.setStep(stages.size() + 1);
        stage.setTherapy(therapy);
        stages.add(stage);

        therapy.setStages(stages);
        return therapyRepository.save(therapy);
    }

    // Check if "Name" && "Platform" && "Active=True" already exists, if it does, throw error for duplicate entry
    private void checkDuplicateTherapy (List<TherapyEntity> therapies, String name, PlatformEnum platform) {
        therapies.forEach(therapy -> {
            if (therapy.getName().equals(name)
                    && therapy.getPlatform() == platform
                    && therapy.getActive() == Boolean.TRUE) {
                log.warn("TherapyService, m=addTherapy, e=Duplicate Therapy entry");
                throw new DuplicateEntryException("Therapy already exists!");
            }
        });
    }
}
