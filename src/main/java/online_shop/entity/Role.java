package online_shop.entity;

import jakarta.persistence.*;
import liquibase.change.DatabaseChangeNote;
import lombok.Data;
import online_shop.entity.enums.RoleValue;

@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 128, nullable = false)
    private RoleValue roleValue;
}
