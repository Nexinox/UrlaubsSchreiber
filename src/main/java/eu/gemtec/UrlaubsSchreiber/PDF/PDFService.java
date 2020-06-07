package eu.gemtec.UrlaubsSchreiber.PDF;

import java.io.Serializable;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.enterprise.context.SessionScoped;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;

@SessionScoped
public class PDFService implements IPDFService, Serializable {
	private static final long serialVersionUID = 1L;
	private static final short VORNAME_X = 356, VORNAME_Y = 708;
	private static final short NACHNAME_X = 356, NACHNAME_Y = 738;
	private static final short START_X = 236, START_Y = 617;
	private static final short END_X = 405, END_Y = 617;
	private static final short TEXT_SIZE = 13;
	private static final String BASE_PDF_FILE_PATH = "UrlaubsSchreiber\\PDF\\Urlaubsantrag.pdf";
	private SimpleDateFormat dateFormater;
	private File pdfFile;
	private PDDocument doc;
	private PDPageContentStream contentStream;

	private void writeName(String vName, String nName) {
		try {
			contentStream.beginText();
			contentStream.newLineAtOffset(VORNAME_X, VORNAME_Y);
			contentStream.setFont(PDType1Font.COURIER, TEXT_SIZE);
			contentStream.showText(vName);
			contentStream.endText();

			contentStream.beginText();
			contentStream.newLineAtOffset(NACHNAME_X, NACHNAME_Y);
			contentStream.setFont(PDType1Font.COURIER, TEXT_SIZE);
			contentStream.showText(nName);
			contentStream.endText();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void writeDate(Calendar start, Calendar end) {
		try {
			contentStream.beginText();
			contentStream.newLineAtOffset(START_X, START_Y);
			contentStream.setFont(PDType1Font.COURIER, TEXT_SIZE);
			contentStream.showText(dateFormater.format(start.getTime()));
			contentStream.endText();

			contentStream.beginText();
			contentStream.newLineAtOffset(END_X, END_Y);
			contentStream.setFont(PDType1Font.COURIER, TEXT_SIZE);
			contentStream.showText(dateFormater.format(end.getTime()));
			contentStream.endText();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean checkFileState() {
		if (pdfFile.exists()) {
			updatePDFVariables();
			return true;
		} else {
			return false;
		}
	}

	private StreamResource generateDownload(File outputPdfFile) {
		return new StreamResource(new StreamSource() {
			private static final long serialVersionUID = 1L;

			public InputStream getStream() {
				try {
					return new ByteArrayInputStream(Files.readAllBytes(outputPdfFile.toPath()));
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
		}, "Urlaubsantrag.pdf");
	}

	public void reciveUpload(byte[] bytes) {
		FileOutputStream out;
		try {
			out = new FileOutputStream(BASE_PDF_FILE_PATH);
			out.write(bytes);
			out.close();
			this.pdfFile = new File(BASE_PDF_FILE_PATH);
			updatePDFVariables();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updatePDFVariables() {
		try {
			doc = PDDocument.load(pdfFile);
			contentStream = new PDPageContentStream(doc, doc.getPage(0), AppendMode.APPEND, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public StreamResource fillPDF(String nName, String vName, Calendar start, Calendar end) {
		String outputPath = "UrlaubsSchreiber\\PDFOutput\\" + nName + "." + vName + "\\Urlaubsantrag"
				+ dateFormater.format(start.getTime()) +"-"+ dateFormater.format(end.getTime()) + ".pdf";
		try {
			writeName(vName, nName);
			writeDate(start, end);

			File outputDir = new File("UrlaubsSchreiber\\PDFOutput\\" + nName + "." + vName);
			makeDirectorysIfNotExists(outputDir);
			
			if(new File(outputPath).exists()) {
				new File(outputPath).delete();
			}
			contentStream.close();
			doc.save(outputPath);
			doc.close();
			return generateDownload(new File(outputPath));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
	private void makeDirectorysIfNotExists(File directory) {
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}
	public PDFService() {
		dateFormater = new SimpleDateFormat("dd.MM.yyyy");
		pdfFile = new File(BASE_PDF_FILE_PATH);
		if (pdfFile.exists()) {
			updatePDFVariables();
		}else {
			File pdfDirectorys = new File("UrlaubsSchreiber\\PDF\\");
			File outputPDFDirectorys = new File("UrlaubsSchreiber\\PDFOutput\\");
			makeDirectorysIfNotExists(pdfDirectorys);
			makeDirectorysIfNotExists(outputPDFDirectorys);
		}
	}

}
