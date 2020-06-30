package org.sid.metier;

import java.util.List;

import javax.transaction.Transactional;

import org.sid.dao.PartCapitalSocialRepository;
import org.sid.dao.UserRepository;
import org.sid.entities.Company;
import org.sid.entities.PartSocial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PartSocialMetierImpl implements IPartSocialMetier   {

	
	@Autowired
	private PartCapitalSocialRepository partRepository ;
	
	@Override
	public PartSocial addCapitalSocial(PartSocial capital) {
		return partRepository.save( capital );
	}

	@Override
	public PartSocial updateCapitalSocial(PartSocial capital) {
		return this.partRepository.save(capital);
	}

	@Override
	public List<PartSocial> getAllParts() {
		return partRepository.findAll() ;
	}

	@Override
	public List<PartSocial> getAllPartsOfCompany(Company company) {
		return this.partRepository.findTopByCompany( company.getIdCompany() );
	}

	@Override
	public PartSocial getPartById(long id) {
		return partRepository.findById(id);
	}

	@Override
	public void deletePart(PartSocial capital) {
		partRepository.delete(capital);
	}

	

}
