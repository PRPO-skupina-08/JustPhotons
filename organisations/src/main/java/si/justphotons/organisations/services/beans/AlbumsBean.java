package si.justphotons.organisations.services.beans;

import java.util.List;

import java.util.Optional;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import si.justphotons.organisations.entities.Album;
import si.justphotons.organisations.entities.Organisation;
import si.justphotons.organisations.entities.OrganisationsRepository;
import si.justphotons.organisations.entities.AlbumsRepository;

@Service
public class AlbumsBean {
    OrganisationsRepository organisationsRepository;
    AlbumsRepository albumsRepository;

    public AlbumsBean(OrganisationsRepository o, AlbumsRepository a) {
        this.organisationsRepository = o;
        this.albumsRepository = a;
    }

    public List<Album> getAlbums(Long orgId) {
        Optional<Organisation> orgOptional = organisationsRepository.findById(orgId);
        if (orgOptional.isPresent()) {
            Organisation org = orgOptional.get();
            return org.getAlbums();
        }
        return null;
    } 

    @Transactional
    public Album insertAlbum(Long orgId, Album album) {
        Optional<Organisation> orgOptional = organisationsRepository.findById(orgId);
        if (orgOptional.isPresent()) {
            Organisation org = orgOptional.get();
            album.setOrganisation(org);;
            Album al = albumsRepository.save(album);
            return al;
        }
        return null;
    } 
}
