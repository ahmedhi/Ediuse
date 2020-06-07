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

	public void createpdf() {
		try {
				Context context = new Context() ;
				context.setVariable ("name","developeradda.com");
				String processHtml = templateEngine.process("403", context);
				OutputStream outputStream = new FileOutputStream("message.pdf");
				ITextRenderer renderer = new ITextRenderer();
				renderer.setDocumentFromString (processHtml);
				renderer.layout();
		        //Image image = Image.getInstance ("img/logo_empty.png"); 
				renderer.createPDF(outputStream,false);
				renderer.finishPDF();
				outputStream.close();
		} catch (Exception e) {
				e.printStackTrace();
		}
		
	}
	
	@Override
	public InputStreamResource generateT1() {
	    Context context = new Context();
	    context.setVariable("Name","ASKOUR");
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

	@Override
	public boolean generatePdf(List<User> users, ServletContext context, HttpServletRequest request, HttpServletResponse response){

		Document document = new  Document(PageSize.A4, 15 , 15 , 45 , 30 );

		try {
			String filePath  = context.getRealPath("/resources/tmp");
			String fileSrc  = context.getRealPath("/resources/tmp");
			PdfReader pdfReader = new PdfReader("/resources/tmp/test.pdf");

			//Modify file using PdfReader
			PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("HelloWorld-modified.pdf"));

			File file = new File(fileSrc);
			boolean exists = new File(fileSrc).exists();
		    if(!exists) {
		    	new File(filePath).mkdirs();
			}
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath+"/"+"users"+".pdf"));
			document.open() ;
				
				Font mainFont  = FontFactory.getFont("Arial",10, BaseColor.BLACK);
				Paragraph paragraph  = new Paragraph("All users",mainFont);
				paragraph.setAlignment(Element.ALIGN_CENTER);
				paragraph.setIndentationLeft(50);
				paragraph.setIndentationRight(50);
				paragraph.setSpacingAfter(10);
			
				
				document.add(paragraph);
				User utilisateur = new User();
				for(User user : users ) {
					utilisateur = user;
				}
				Paragraph test  = new Paragraph(utilisateur.getFirstNameUser(),mainFont);
				test.setAlignment(Element.ALIGN_CENTER);
				test.setIndentationLeft(60);
				test.setIndentationRight(50);
				test.setSpacingAfter(500);
				document.add(test);

			Paragraph taxe  = new Paragraph(utilisateur.getRole(),mainFont);
			test.setAlignment(Element.ALIGN_CENTER);
			test.setIndentationLeft(30);
			test.setIndentationRight(150);
			test.setSpacingAfter(300);
			document.add(taxe);

				PdfPTable table = new PdfPTable(4);
				table.setWidthPercentage(100);
				table.setSpacingBefore(10f);
				table.setSpacingAfter(10);
				
				Font tableHeader  = FontFactory.getFont("Arial",10, BaseColor.BLACK);
				Font tableBody  = FontFactory.getFont("Arial",9, BaseColor.BLACK);
				
				float[] columnWidths = {2f , 2f , 2f ,2f} ;
				table.setWidths(columnWidths);
				
				PdfPCell firstName = new PdfPCell(new Paragraph("FirsT Name", tableHeader));
				firstName.setBorderColor(BaseColor.BLACK);
				firstName.setPaddingLeft(10);
				firstName.setHorizontalAlignment(Element.ALIGN_CENTER);
				firstName.setVerticalAlignment(Element.ALIGN_CENTER);
				firstName.setBackgroundColor(BaseColor.GRAY);
				firstName.setExtraParagraphSpace(5);
				table.addCell(firstName);
				
				PdfPCell lastName = new PdfPCell(new Paragraph("Last Name", tableHeader));
				lastName.setBorderColor(BaseColor.BLACK);
				lastName.setPaddingLeft(10);
				lastName.setHorizontalAlignment(Element.ALIGN_CENTER);
				lastName.setVerticalAlignment(Element.ALIGN_CENTER);
				lastName.setBackgroundColor(BaseColor.GRAY);
				lastName.setExtraParagraphSpace(5);
				table.addCell(lastName);
				
				PdfPCell login = new PdfPCell(new Paragraph("Login", tableHeader));
				login.setBorderColor(BaseColor.BLACK);
				login.setPaddingLeft(10);
				login.setHorizontalAlignment(Element.ALIGN_CENTER);
				login.setVerticalAlignment(Element.ALIGN_CENTER);
				login.setBackgroundColor(BaseColor.GRAY);
				login.setExtraParagraphSpace(5);
				table.addCell(login);
				
				PdfPCell role = new PdfPCell(new Paragraph("Role", tableHeader));
				role.setBorderColor(BaseColor.BLACK);
				role.setPaddingLeft(10);
				role.setHorizontalAlignment(Element.ALIGN_CENTER);
				role.setVerticalAlignment(Element.ALIGN_CENTER);
				role.setBackgroundColor(BaseColor.GRAY);
				role.setExtraParagraphSpace(5);
				table.addCell(role);
				
				for(User user : users ) {
					PdfPCell firstNameValue = new PdfPCell(new Paragraph(user.getFirstNameUser(),tableHeader));
					firstNameValue.setBorderColor(BaseColor.BLACK);
					firstNameValue.setPaddingLeft(10);
					firstNameValue.setHorizontalAlignment(Element.ALIGN_CENTER);
					firstNameValue.setVerticalAlignment(Element.ALIGN_CENTER);
					firstNameValue.setBackgroundColor(BaseColor.WHITE);
					firstNameValue.setExtraParagraphSpace(5);
					table.addCell(firstNameValue);
					
					PdfPCell lastNameValue = new PdfPCell(new Paragraph(user.getLastNameUser(),tableHeader));
					lastNameValue.setBorderColor(BaseColor.BLACK);
					lastNameValue.setPaddingLeft(10);
					lastNameValue.setHorizontalAlignment(Element.ALIGN_CENTER);
					lastNameValue.setVerticalAlignment(Element.ALIGN_CENTER);
					lastNameValue.setBackgroundColor(BaseColor.WHITE);
					lastNameValue.setExtraParagraphSpace(5);
					table.addCell(lastNameValue);
					
					PdfPCell loginValue = new PdfPCell(new Paragraph(user.getLoginUser(),tableHeader));
					loginValue.setBorderColor(BaseColor.BLACK);
					loginValue.setPaddingLeft(10);
					loginValue.setHorizontalAlignment(Element.ALIGN_CENTER);
					loginValue.setVerticalAlignment(Element.ALIGN_CENTER);
					loginValue.setBackgroundColor(BaseColor.WHITE);
					loginValue.setExtraParagraphSpace(5);
					table.addCell(loginValue);
					
					PdfPCell RoleValue = new PdfPCell(new Paragraph(user.getRole(),tableHeader));
					RoleValue.setBorderColor(BaseColor.BLACK);
					RoleValue.setPaddingLeft(10);
					RoleValue.setHorizontalAlignment(Element.ALIGN_CENTER);
					RoleValue.setVerticalAlignment(Element.ALIGN_CENTER);
					RoleValue.setBackgroundColor(BaseColor.WHITE);
					RoleValue.setExtraParagraphSpace(5);
					table.addCell(RoleValue);
				}
				
				document.add(table);
				document.close();
				pdfStamper.close();

				writer.close();
				return true ;
		}catch(Exception e ) {
			return false;
		}
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