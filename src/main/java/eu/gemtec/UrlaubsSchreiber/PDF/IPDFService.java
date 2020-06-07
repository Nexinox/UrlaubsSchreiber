package eu.gemtec.UrlaubsSchreiber.PDF;

import java.util.Calendar;

import com.vaadin.server.StreamResource;

public interface IPDFService{

	public boolean checkFileState();
	
	public void reciveUpload(byte[] bytes);
	
	public StreamResource fillPDF(String nName, String vName, Calendar start, Calendar end);

}
