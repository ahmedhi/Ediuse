package org.sid.metier;

import java.io.*;
import java.util.*;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.lowagie.text.DocumentException;

import groovy.util.logging.Log;
import lombok.RequiredArgsConstructor;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sid.dao.UserRepository;
import org.sid.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
@Transactional
@Log
@RequiredArgsConstructor
public class UserMetierImpl implements IUserMetier {

	@Autowired
	private TemplateEngine templateEngine ;
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

	
	@Override
	public InputStreamResource generateT1(List<User> users) {
	    Context context = new Context();
	    context.setVariable(null,users);
		//context.setVariables((Map<String, Object>) users);
		String html = templateEngine.process("modeleT1", context);
		System.out.println(html);
		try {
			HtmlConverter.convertToPdf(html, new FileOutputStream("target/modeleT1.pdf"));
			return new InputStreamResource( new FileInputStream("target/modeleT1.pdf"));
		} catch (IOException e) {
			//log.log(Level.SEVERE,e.getMessage(),e);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean saveDataFromUpload(MultipartFile file) {
		boolean isFlag = false ; 
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		if(extension.equalsIgnoreCase("json")) {
			isFlag = readDataFromJson(file);
		}else if (extension.equalsIgnoreCase("csv")) {
			isFlag = readDataFromCsv(file);
		}else if (extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("xlsx") ) {
			isFlag = readDataFromExcel(file);
		}
		return isFlag ;
	}
	private boolean readDataFromExcel(MultipartFile file) {
		Workbook workbook = getWorkBook(file);
		Sheet sheet =  workbook.getSheetAt(0);
		Iterator<Row> rows = sheet.iterator();
		rows.next();
		while(rows.hasNext()) {
			Row row = rows.next();
			User user  = new User() ;
			
				user.setLoginUser(row.getCell(0).getStringCellValue());
			
				user.setLastNameUser(row.getCell(1).getStringCellValue());
			
				user.setFirstNameUser(row.getCell(2).getStringCellValue());
			
				user.setPwdUser(row.getCell(3).getStringCellValue());
			
				user.setRole(row.getCell(4).getStringCellValue());
			
			user.setFileType(FilenameUtils.getExtension(file.getOriginalFilename()));
			userRepository.save(user);
		}
		return false;
	}

	private Workbook getWorkBook(MultipartFile file) {
		Workbook  workbook = null ;
		String  extension = FilenameUtils.getExtension(file.getOriginalFilename());
		try{
			if(extension.equalsIgnoreCase("xlsx")) {
				workbook  = new XSSFWorkbook(file.getInputStream());
			}else if (extension.equalsIgnoreCase("xls")) {
				workbook  = new HSSFWorkbook(file.getInputStream());
			}
		}catch(Exception e ) {
			e.printStackTrace();
		}
		return workbook;
	}

	private boolean readDataFromCsv(MultipartFile file) {
		try {
			InputStreamReader reader = new InputStreamReader(file.getInputStream());
			CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
			List<String[]> rows = csvReader.readAll();
			for (String [] row : rows) {

				userRepository.save(new User(row[0], row[1],row[2],row[3],row[4],FilenameUtils.getExtension(file.getOriginalFilename())));
			}
			return true ;
		}
		catch(Exception e) {
			return false ;
		}
	}

	private boolean readDataFromJson(MultipartFile file) {
		try {
			InputStream inputStream = file.getInputStream();
			ObjectMapper mapper  = new  ObjectMapper() ;
			List<User> users = Arrays.asList(mapper.readValue(inputStream, User[].class));
			if(users != null && users.size()>0) {
				for(User user: users) {
					user.setFileType(FilenameUtils.getExtension(file.getOriginalFilename()));
					userRepository.save(user);
				}
			}
			return true ;
		}catch(Exception e ) {
			return false;
		}
	}

}