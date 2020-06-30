package org.sid.metier;

import java.util.List;

import org.sid.entities.Company;
import org.sid.entities.PartSocial;


public interface IPartSocialMetier {
	
	PartSocial addCapitalSocial(PartSocial capital );
	PartSocial updateCapitalSocial(PartSocial capital);
	List<PartSocial> getAllParts();
	List<PartSocial> getAllPartsOfCompany(Company company);
	PartSocial getPartById(long id);
	void deletePart(PartSocial capital);
}
