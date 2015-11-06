package org.msh.xview.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.msh.etbm.entities.Workspace;
import org.msh.etbm.services.login.UserSession;
import org.msh.xview.FormContext;
import org.msh.xview.FormInterceptor;
import org.msh.xview.FormManager;
import org.msh.xview.components.XForm;

/**
 * Implementation of the {@link FormManager} interface that gets forms from files as local resource.
 * <p/>
 * When entering resources, the form ID must match the name of the resource in the resource file system 
 * located in the resourcePath parameter
 * 
 * @author Ricardo Memoria
 *
 */
public class LocalResourceFormManager implements FormManager {

	private String resourcePath;
	private Class formAdapterClass;
	private List<FormInterceptor> interceptors;

	
	/**
	 * Create an instance of the class
	 * @param resourcePath path in the local resource where forms in XML file format are located
	 * @param formAdapterClass
	 */
	public LocalResourceFormManager(String resourcePath, Class formAdapterClass) {
		this.resourcePath = resourcePath;
		this.formAdapterClass = formAdapterClass;
	}
	

	/** {@inheritDoc}
	 * @see org.msh.xview.FormManager#createFormAdapter(java.lang.String)
	 */
	@Override
	public FormContext createFormAdapter(String formId) {
		XForm form = loadFormFromLocalResource(resourcePath + formId + ".xview.xml");
		
		FormContext formAdapter = createFormAdapter(form);
		
		return formAdapter;
	}
	
	
	/**
	 * Create instance of the {@link FormContext} using the class passed as parameter in the constructor
	 * @param form
	 * @return
	 */
	protected FormContext createFormAdapter(XForm form) {
		FormContext frmAdapter = null; 
		try {
			frmAdapter = (FormContext)formAdapterClass.newInstance();
			frmAdapter.initialize(form);
			if (interceptors != null)
				for (FormInterceptor interceptor: interceptors)
					frmAdapter.addFormInterceptor(interceptor);
		} catch (Exception e) {
			throw new IllegalArgumentException("Error initializing class " + formAdapterClass.getClass().toString());
		}

		return frmAdapter;
	}


	/**
	 * Load and parse XML files containing form structure into a {@link XForm} class
	 * @param resourceName name of the XML file in the <code>resourcePath</code>
	 * @return
	 */
	public XForm loadFormFromLocalResource(String resourceName) {
        InputStream stream = null;

        // check if there is any customization for the workspace
        Workspace ws = UserSession.getWorkspace();
        if (ws.getExtension() != null) {
            String s = "/xview/" + ws.getExtension() + "/";
            s = resourceName.replace("/xview/", s);
            stream = this.getClass().getResourceAsStream(s);
        }

        // no customization found? get generic
        if (stream == null) {
            stream = this.getClass().getResourceAsStream(resourceName);
        }

		StringBuilder builder = new StringBuilder();
		Reader reader = new BufferedReader(new InputStreamReader(stream));
		char[] buffer = new char[8192];
		int read;
		try {
			while ((read = reader.read(buffer, 0, buffer.length)) > 0)
				builder.append(buffer, 0, read);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		XmlFormReader xmlReader = new XmlFormReader();
		XForm form = xmlReader.read(builder.toString());
		return form;
	}


	/** {@inheritDoc}
	 */
	@Override
	public void addInterceptor(FormInterceptor interceptor) {
		if (interceptors == null)
			interceptors = new ArrayList<FormInterceptor>();
		interceptors.add(interceptor);
	}


	/** {@inheritDoc}
	 */
	@Override
	public void removeInterceptor(FormInterceptor interceptor) {
		if (interceptors == null)
			return;
		interceptors.remove(interceptor);
	}

}
