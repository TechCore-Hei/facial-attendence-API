package school.hei.haapi.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import school.hei.haapi.repository.types.PostgresEnumType;

@Entity
@Table(name = "\"event_participant\"")
@TypeDef(name = "pgsql_enum", typeClass = PostgresEnumType.class)
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EventParticipant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @OneToOne
    @JoinColumn(name = "\"user\"")
    private User user;

    @ManyToOne
    private Event event;

    @NotNull
    @Type(type = "pgsql_enum")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        EventParticipant that = (EventParticipant) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public enum StatusEnum {
        EXPECTED, HERE, MISSING
    }
}
