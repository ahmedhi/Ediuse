package org.sid.metier;

import java.util.List;

import org.sid.entities.User;
import org.springframework.web.multipart.MultipartFile;

public interface IUserMetier {
	User createUser(User user );
	User updateUser(User user);
	List<User> getAllUsers();
	User getUserById(long id);
	void deleteUser(User user);
	public boolean saveDataFromUpload(MultipartFile file);

}
