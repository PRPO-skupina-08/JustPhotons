package si.justphotons.coordinator.entities.external;

import java.util.ArrayList;
import java.util.List;

public class EssentialsList {
    private List<OrganisationEssentials> essentials;

    public EssentialsList() {
        essentials = new ArrayList<>();
    }

	public List<OrganisationEssentials> getEssentials() {
		return essentials;
	}

	public void setEssentials(List<OrganisationEssentials> essentials) {
		this.essentials = essentials;
	}
}
