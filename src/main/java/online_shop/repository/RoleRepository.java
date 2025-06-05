package online_shop.repository;

import online_shop.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "select r from Role r where r.roleValue='ROLE_USER'")
    Role findUserRole();
}
