package org.sid.metier;

import java.util.List;

import org.sid.entities.PartCapitalSocial;


public interface IPartSocialMetier {
	
	PartCapitalSocial addCapitalSocial(PartCapitalSocial capital );
	PartCapitalSocial updateCapitalSocial(PartCapitalSocial capital);
	List<PartCapitalSocial> getAllParts();
	PartCapitalSocial getPartById(long id);
	void deletePart(PartCapitalSocial capital);
	
}
