package gamesvrapi.rest.api.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gamesvrapi.rest.api.enums.PlatformEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "therapy")
@Entity(name = "Therapy")
public class TherapyEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "platform", nullable = false)
    private PlatformEnum platform;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PATIENT_ID")
    private PatientEntity patient;

    @OneToMany(mappedBy = "therapy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StageEntity> stages;

    @OneToMany(mappedBy = "therapy", fetch = FetchType.LAZY)
    private List<SessionEntity> sessions;

    @CreatedDate
    @Column(name = "dat_creation", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @LastModifiedDate
    @Column(name = "dat_update", nullable = false)
    private LocalDateTime updateDate;

    @PrePersist
    public void preInsert () {
        if (this.active == null) {
            this.setActive(Boolean.TRUE);
        }
    }

    @JsonIgnore
    public PatientEntity getPatient () {
        return patient;
    }
}
