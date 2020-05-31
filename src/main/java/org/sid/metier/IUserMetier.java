package org.sid.metier;

import java.util.List;

import org.sid.entities.User;

public interface IUserMetier {
	User createUser(User user );
	User updateUser(User user);
	List<User> getAllUsers();
	User getUserById(long id);
	void deleteUser(User user);

}
