package eu.gemtec.UrlaubsSchreiber.VIEW;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.ServletException;

import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.access.JaasAccessControl;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import eu.gemtec.UrlaubsSchreiber.DB.IEntryDataacessService;
import eu.gemtec.UrlaubsSchreiber.MODEL.Entry;
import eu.gemtec.UrlaubsSchreiber.PDF.IPDFService;

@CDIView(NormieView.VIEW_NAME)
@Theme("mainTheme")
@RolesAllowed({ "user", "felix" })
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
	Button manageView;
	Button settingsView;
	Button logout;

//	FileDownloader
	FileDownloader fileDownloader;

	private VerticalLayout makeAdminButtons() {
		return new VerticalLayout(new Button("Manage " + entryDataacessService.getEntryList().size(), e -> {
			getUI().getNavigator().navigateTo(FelixView.VIEW_NAME);
		}), new Button("Settings", e -> {
			getUI().getNavigator().navigateTo(SettingsView.VIEW_NAME);
		}));
	}

	@Override
	public void enter(ViewChangeEvent event) {
//		Initializing of Fields, Buttons, Layouts
		vorname = new TextField();
		nachname = new TextField();
		start = new DateField();
		end = new DateField();
		saveAsPDF = new Button("Make PDF");
		downloadPDF = new Button("Download PDF");
		manageView = new Button("Manage");
		logout = new Button("Logout");
		settingsView = new Button("Settings");
		fieldGroupLayout = new HorizontalLayout();
		baseLayout = new VerticalLayout();
		group1Layout = new VerticalLayout();
		group2Layout = new VerticalLayout();

//      Setting Captions of Fields
		vorname.setCaption("Vorname:");
		nachname.setCaption("Nachname:");
		start.setCaption("Start Datum:");
		end.setCaption("End Datum:");

//      Setting clickListeners
		saveAsPDF.addClickListener(e -> {
			if (!nachname.isEmpty() || !vorname.isEmpty() || !start.isEmpty() || !end.isEmpty()) {
				if (!pdfService.checkFileState()) {
					addComponent(new Label("Setup Not yet Complete Upload base PDF"));
				} else {
					fileDownloader = new FileDownloader(pdfService.fillPDF(nachname.getValue(), vorname.getValue(),
							start.getValue(), end.getValue()));
					fileDownloader.extend(downloadPDF);
					downloadPDF.setVisible(true);
					Entry entry = new Entry(nachname.getValue(), vorname.getValue(), start.getValue(), end.getValue());
					entryDataacessService.newEntry(entry);
				}
			}
		});
		downloadPDF.setVisible(false);
		downloadPDF.addClickListener(e -> {
			downloadPDF.setVisible(false);
		});
		logout.addClickListener(e -> {
			try {
				JaasAccessControl.getCurrentRequest().logout();
			} catch (ServletException e1) {
				e1.printStackTrace();
			}
			getUI().getNavigator().navigateTo(LoginView.VIEW_NAME);
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
		if (JaasAccessControl.getCurrentRequest().isUserInRole("felix")) {
			baseLayout.addComponent(makeAdminButtons());
		}
		baseLayout.addComponent(logout);
		group1Layout.addComponents(vorname, nachname, saveAsPDF);
		group2Layout.addComponents(start, end, downloadPDF);
		fieldGroupLayout.addComponent(group1Layout);
		fieldGroupLayout.addComponent(group2Layout);
		baseLayout.addComponents(fieldGroupLayout);

		if (!pdfService.checkFileState()) {
			addComponent(new Label("Setup Not yet Complete Upload base PDF"));
		} else {
			addComponent(baseLayout);
		}

	}

}
