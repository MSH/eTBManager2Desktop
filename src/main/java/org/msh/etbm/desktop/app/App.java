package org.msh.etbm.desktop.app;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManager;

import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.eventbus.EventBusService;
import org.msh.xview.VariableFactory;
import org.msh.xview.impl.VariableCreator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Static methods class that performs application initialization and
 * expose some common application methods.
 * 
 * @author Ricardo Memoria
 *
 */
public class App 
{

    final private static ApplicationContext appContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
    
    final private static App _instance = new App();
    
    private String versionNumber;
    private String buildNumber;

    // flag to avoid app being initialized twice
    private boolean initialized;

    // if true, the app is synchronizing its data with the server
    private boolean synchronizing;

	
	/**
	 * Read the MANIFEST.MF properties loaded in a {@link Properties} object
	 */
	private void readManifestProperties() {
		Properties prop = new Properties();
		try {
			prop.load( getClass().getResourceAsStream("/META-INF/MANIFEST.MF") );
			versionNumber = (String)prop.get("Implementation-Version"); 
			buildNumber = (String)prop.get("Implementation-Build"); 
		} catch (IOException e) {
			System.out.println("It was not possible to read version and build number");
			e.printStackTrace();
		}
	}
    
    /**
     * Initialize the application data, resources, variables used in forms (XView library), etc.
     */
    public static void initialize() {
    	if (_instance.initialized) {
    		return;
    	}
   
    	// initialize the context
    	getContext();
    	
    	// initialize font awesome
    	AwesomeIcon.initialize();
    	
    	// configure variables of the forms
    	VariableCreator vr = VariableCreator.instance();

    	// create the variable factory to the forms
    	VariableFactory containerVariableFactory = new VariableFactory() {
			@Override
			public Object createVariableInstance(String varname) {
				return App.getComponent(varname);
			}
		};
		vr.registerVariableFactory(containerVariableFactory);
    	
    	// links between variables
    	vr.addFieldLink("tbcase.patient", "patient");
    	vr.addFieldLink("medicalexamination.tbcase", "tbcase");
    	vr.addFieldLink("examculture.tbcase", "tbcase");
    	vr.addFieldLink("exammicroscopy.tbcase", "tbcase");
    	vr.addFieldLink("examdst.tbcase", "tbcase");
    	vr.addFieldLink("examhiv.tbcase", "tbcase");
    	vr.addFieldLink("examxray.tbcase", "tbcase");
    	vr.addFieldLink("examxpert.tbcase", "tbcase");
    	vr.addFieldLink("contact.tbcase", "tbcase");
    	vr.addFieldLink("sideeffect.tbcase", "tbcase");
    	vr.addFieldLink("startTreatmentService.tbcase", "tbcase");

    	instance().readManifestProperties();
    	
//    	DatabaseManager dbman = DatabaseManager.instance(); //getComponent(DatabaseManager.class);
//    	dbman.setSelectedDatabase(dbman.getDatabases().get(0));
//    	dbman.createDatabase("https://www.etbmanager.org/etbmanager");
//    	DatabaseStarter starter = new DatabaseStarter();
//    	File file = new File("C:\\Projetos\\etbmanager\\desktop\\T.B_HOSPITAL.etbm.pkg");
//    	starter.start(file, null);
    	
    	_instance.initialized = true;
    }

    
    /**
     * Quit the application releasing resources
     */
    public static void exit() {
    	EventBusService.raiseEvent(AppEvent.REQUEST_APP_EXIT);
    }
    
    /**
     * Return the working directory of the application. Usually it is the own
     * application directory, but it may be overwritten by the <code>working.dir</code>
     * system parameter
     * @return
     */
    public static File getWorkingDirectory() {
		String path = System.getProperty("working.dir");
		if (path == null)
			path = System.getProperty("user.dir");
		return new File(path);
    }
    
    /**
     * Return the instance of the {@link ApplicationContext} used in the application
     * @return
     */
    public static ApplicationContext getContext() {
        return appContext;
    }


    /**
     * Return an instance of a Spring component by its name
     * @param name
     * @return
     */
    public static Object getComponent(String name) {
    	return appContext.getBean(name); 
    }


    /**
     * Return an instance of a Spring component by its class
     * @param clazz
     * @return
     */
    public static <E> E getComponent(Class<E> clazz) {
    	return (E)getContext().getBean(clazz);
    }
    
    
    /**
     * Translate a key into a system messages using the current locale
     * @param key
     * @return
     */
    public static String getMessage(String key) {
    	return Messages.getString(key);
    }
    
    
    /**
     * Return the instance of the {@link EntityManager} in use by the application context
     * @return
     */
    public static EntityManager getEntityManager() {
    	return (EntityManager)appContext.getBean("entityManager");
    }
    
    /**
     * Return the singleton instance of the {@link App} class
     * @return
     */
    public static App instance() {
    	return _instance;
    }

	/**
	 * @return the versionNumber
	 */
	public String getVersionNumber() {
		return versionNumber;
	}

	/**
	 * @return the buildNumber
	 */
	public String getBuildNumber() {
		return buildNumber;
	}

    public boolean isSynchronizing() {
        return synchronizing;
    }

    public void setSynchronizing(boolean synchronizing) {
        this.synchronizing = synchronizing;
    }
}
