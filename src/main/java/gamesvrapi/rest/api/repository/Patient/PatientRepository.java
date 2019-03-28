package gamesvrapi.rest.api.repository.Patient;

import gamesvrapi.rest.api.model.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Long> {

}
