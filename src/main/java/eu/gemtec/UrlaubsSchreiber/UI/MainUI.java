	 package eu.gemtec.UrlaubsSchreiber.UI;

import javax.inject.Inject;

import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import eu.gemtec.UrlaubsSchreiber.VIEW.ErrorView;
import eu.gemtec.UrlaubsSchreiber.VIEW.LoginView;
import eu.gemtec.UrlaubsSchreiber.VIEW.NormieView;

@CDIUI("")
public class MainUI extends UI{
	private static final long serialVersionUID = 1L;
	@Inject
    private CDIViewProvider viewProvider;
	
	@Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout contentArea = new VerticalLayout();
        contentArea.setMargin(false);
        setContent(contentArea);

        final Navigator navigator = new Navigator(this, contentArea);
        navigator.addProvider(viewProvider);
        navigator.setErrorView(ErrorView.class);

        if (isUserAuthenticated(vaadinRequest)) {
            navigator.navigateTo(NormieView.VIEW_NAME);
            vaadinRequest.getUserPrincipal().getName();
        } else navigator.navigateTo(LoginView.VIEW_NAME);
    }

    private boolean isUserAuthenticated(final VaadinRequest vaadinRequest) {
        return vaadinRequest.getUserPrincipal() != null;
    }

}
