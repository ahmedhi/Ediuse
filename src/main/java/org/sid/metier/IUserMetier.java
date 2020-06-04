package org.sid.metier;

import java.io.IOException;
import java.util.List;

import org.sid.entities.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IUserMetier {
	User createUser(User user );
	User updateUser(User user);
	List<User> getAllUsers();
	User getUserById(long id);
	void deleteUser(User user);
	public boolean saveDataFromUpload(MultipartFile file);
    boolean generatePdf(List<User> users, ServletContext context, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
