package eu.gemtec.UrlaubsSchreiber.VIEW;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.ServletException;

import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.access.JaasAccessControl;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

import eu.gemtec.UrlaubsSchreiber.PDF.IPDFService;

@CDIView(SettingsView.VIEW_NAME)
@RolesAllowed("felix")
@Theme("mainTheme")
public class SettingsView extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "SETTINGS_VIEW";
	
	@Inject
	private IPDFService pdfService;
	
	Upload uploadPDF;
	Button logout;
	Button back;
	

	class PDFUploader implements Receiver, SucceededListener {
		private static final long serialVersionUID = 1L;

		public ByteArrayOutputStream bos;

		public OutputStream receiveUpload(String filename, String mimeType) {
			bos = new ByteArrayOutputStream();
			return bos;
		}

		public void uploadSucceeded(SucceededEvent event) {
			pdfService.reciveUpload(bos.toByteArray());
			Notification.show("Upload Succseded");
		}
	};

	@Override
	public void enter(ViewChangeEvent event) {
		logout = new Button("Logout");
		back = new Button("Zurück");
		PDFUploader uploader = new PDFUploader();
		uploadPDF = new Upload("Upload PDF", uploader);
		uploadPDF.addSucceededListener(uploader);
		
		logout.addClickListener(e->{
			try {
				JaasAccessControl.getCurrentRequest().logout();
			} catch (ServletException e1) {
				e1.printStackTrace();
			}
			getUI().getNavigator().navigateTo(LoginView.VIEW_NAME);
		});
		back.addClickListener(e->{
			getUI().getNavigator().navigateTo(NormieView.VIEW_NAME);
		});

		addComponents(new VerticalLayout(back, logout) ,uploadPDF);
	}

}
