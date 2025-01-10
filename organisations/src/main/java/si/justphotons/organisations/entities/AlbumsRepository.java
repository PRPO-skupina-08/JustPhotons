package si.justphotons.organisations.entities;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumsRepository extends JpaRepository<Album, Long> {

    @Query("SELECT a FROM Album a WHERE a.organisation.id = ?1")
    List<Album> getByOrganisationId(Long orgId);

    @Query("SELECT a FROM Album a WHERE a.organisation.id = ?1")
    List<Album> getByOrganisationIdPaged(Long orgId, Pageable pageable);

}
