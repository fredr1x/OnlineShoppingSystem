package online_shop.repository;

import online_shop.entity.Role;
import online_shop.entity.enums.RoleValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "select r from Role r where r.roleValue='ROLE_USER'")
    Role findUserRole();

    @Query(value = "select r from Role r where r.roleValue='ROLE_MODERATOR'")
    Role findRoleModerator();

    @Query(value = "select r from Role r where r.roleValue='ROLE_ADMIN'")
    Role findRoleAdmin();

    @Query(value = "select r from Role r where r.roleValue=:roleValue")
    Role findRole(RoleValue roleValue);
}
