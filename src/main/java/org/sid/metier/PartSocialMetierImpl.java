package org.sid.metier;

import java.util.List;

import javax.transaction.Transactional;

import org.sid.dao.PartCapitalSocialRepository;
import org.sid.entities.PartCapitalSocial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PartSocialMetierImpl implements IPartSocialMetier   {

	@Autowired
	private PartCapitalSocialRepository partRepository ;
	
	@Override
	public PartCapitalSocial addCapitalSocial(PartCapitalSocial capital) {
		return partRepository.save( capital );
	}

	@Override
	public PartCapitalSocial updateCapitalSocial(PartCapitalSocial capital) {
		return this.partRepository.save(capital);
	}

	@Override
	public List<PartCapitalSocial> getAllParts() {
		return partRepository.findAll() ;
	}

	@Override
	public PartCapitalSocial getPartById(long id) {
		return partRepository.findById(id);
	}

	@Override
	public void deletePart(PartCapitalSocial capital) {
		partRepository.delete(capital);
	}

}
