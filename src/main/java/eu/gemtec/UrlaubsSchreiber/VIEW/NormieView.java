package eu.gemtec.UrlaubsSchreiber.VIEW;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

import eu.gemtec.UrlaubsSchreiber.DB.IEntryDataacessService;
import eu.gemtec.UrlaubsSchreiber.MODEL.Entry;
import eu.gemtec.UrlaubsSchreiber.PDF.IPDFService;

@CDIView("NORMIE_VIEW")
@RolesAllowed("")
@Theme("mainTheme")
public class NormieView extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "NORMIE_VIEW";
	

	@Inject
	private IPDFService pdfService;
	@Inject
	private IEntryDataacessService entryDataacessService;

//	Layouts
	HorizontalLayout fieldGroupLayout = new HorizontalLayout();
	VerticalLayout baseLayout = new VerticalLayout();
	VerticalLayout group1Layout = new VerticalLayout();
	VerticalLayout group2Layout = new VerticalLayout();

//	Fields, Buttons
	TextField vorname;
	TextField nachname;
	DateField start;
	DateField end;
	Button saveAsPDF;
	Button downloadPDF;
	Upload uploadPDF;
	
//	FileDownloader
	FileDownloader fileDownloader;

	class PDFUploader implements Receiver, SucceededListener {
		private static final long serialVersionUID = 1L;

		public ByteArrayOutputStream bos;

		public OutputStream receiveUpload(String filename, String mimeType) {
			bos = new ByteArrayOutputStream();
			return bos;
		}

		public void uploadSucceeded(SucceededEvent event) {
			pdfService.reciveUpload(bos.toByteArray());
			if (!pdfService.checkFileState()) {
				addComponent(uploadPDF);
			} else {
				addComponent(baseLayout);
			}
		}
	};

	@Override
	public void enter(ViewChangeEvent event) {
		PDFUploader uploader = new PDFUploader();
//		Initializing of Fields, Buttons, Layouts
		vorname = new TextField();
		nachname = new TextField();
		start = new DateField();
		end = new DateField();
		saveAsPDF = new Button("Make PDF");
		downloadPDF = new Button("Download PDF");
		fieldGroupLayout = new HorizontalLayout();
		baseLayout = new VerticalLayout();
		group1Layout = new VerticalLayout();
		group2Layout = new VerticalLayout();

		uploadPDF = new Upload("Upload PDF", uploader);
		uploadPDF.addSucceededListener(uploader);

//      Setting Captions of Fields
		vorname.setCaption("Vorname:");
		nachname.setCaption("Nachname:");
		start.setCaption("Start Datum:");
		end.setCaption("End Datum:");

//      Setting clickListeners
		saveAsPDF.addClickListener(e -> {
			if (!pdfService.checkFileState()) {
				addComponent(uploadPDF);
			} else {
				fileDownloader = new FileDownloader(pdfService.fillPDF(nachname.getValue(), vorname.getValue(),
						new Calendar.Builder().setInstant(start.getValue()).build(),
						new Calendar.Builder().setInstant(end.getValue()).build()));
				fileDownloader.extend(downloadPDF);
				downloadPDF.setVisible(true);
				Entry entry = new Entry(nachname.getValue(), vorname.getValue(),
						new Calendar.Builder().setInstant(start.getValue()).build(),
						new Calendar.Builder().setInstant(end.getValue()).build());
				entryDataacessService.newEntry(entry);
			}
		});
		downloadPDF.setVisible(false);
		downloadPDF.addClickListener(e -> {
			downloadPDF.setVisible(false);
		});

//      Setting Margins and Spacing of Layouts
		baseLayout.setMargin(true);
		baseLayout.setSpacing(true);
		group1Layout.setMargin(true);
		group1Layout.setSpacing(true);
		group2Layout.setMargin(true);
		group2Layout.setSpacing(true);
		fieldGroupLayout.setMargin(true);
		fieldGroupLayout.setSpacing(true);

//      Layout
		group1Layout.addComponents(vorname, nachname, saveAsPDF);
		group2Layout.addComponents(start, end, downloadPDF);
		fieldGroupLayout.addComponent(group1Layout);
		fieldGroupLayout.addComponent(group2Layout);
		baseLayout.addComponents(fieldGroupLayout);

		if (!pdfService.checkFileState()) {
			addComponent(uploadPDF);
		} else {
			addComponent(baseLayout);
		}
		
	}

}
