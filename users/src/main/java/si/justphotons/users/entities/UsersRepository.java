package si.justphotons.users.entities;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    @Query("SELECT a FROM User a WHERE a.email = ?1")
    List<User> getByEmail(String email);

    // @Query("SELECT a FROM Album a WHERE a.organisation.id = ?1")
    // List<Album> getByOrganisationIdPaged(Long orgId, Pageable pageable);
}
