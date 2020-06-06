package org.sid.metier;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.sid.entities.User;
import org.springframework.core.io.InputStreamResource;
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
	//InputStreamResource generateT1( Map<String , Object> data );
	InputStreamResource generateT1();
	public boolean saveDataFromUpload(MultipartFile file);
    boolean generatePdf(List<User> users, ServletContext context, HttpServletRequest request, HttpServletResponse response) ;
    public void createpdf();
    
    
}
