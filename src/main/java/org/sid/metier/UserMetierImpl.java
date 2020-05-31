package org.sid.metier;

import java.util.List;

import javax.transaction.Transactional;

import org.sid.dao.UserRepository;
import org.sid.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserMetierImpl implements IUserMetier {
	@Autowired
	UserRepository userRepository;

	@Override
	public User createUser(User user) {
		return userRepository.save( user );
	}
	
	@Override
	public User updateUser(User user ) {
		return this.userRepository.save(user);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User getUserById(long id) {
		return userRepository.findById( id );
	}

	@Override
	public void deleteUser(User user) {
		userRepository.delete(user);
	}

}
