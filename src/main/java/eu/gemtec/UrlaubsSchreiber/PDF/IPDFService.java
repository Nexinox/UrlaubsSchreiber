package eu.gemtec.UrlaubsSchreiber.PDF;

import java.util.Date;

import com.vaadin.server.StreamResource;

public interface IPDFService{

	public boolean checkFileState();
	
	public void reciveUpload(byte[] bytes);
	
	public StreamResource fillPDF(String nName, String vName, Date start, Date end);

	public StreamResource fillFelixPDF(String vorname, String nachname, Date start, Date end);

}
