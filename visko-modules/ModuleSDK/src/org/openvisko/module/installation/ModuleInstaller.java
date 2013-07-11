package org.openvisko.module.installation;

import java.io.File;
import java.io.FileInputStream;

import org.openvisko.module.registration.AbstractModuleRDFRegistration;
import org.openvisko.module.registration.ModuleWriter;

import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.visko.util.FileUtils;

/**
 * TODO: when installing service owl file, first get the old owl file for the service if it 
 * exists.  Then query all the old methods and delete those owl files first before you run
 * the installer. This will prevent obsolete methods from leaving orphaned owl files on the
 * server.
 *
 */
public class ModuleInstaller {
	public static final String PACKAGE_REGISTRATION_CLASS = "org.openvisko.module.ModuleRDFRegistration";
	public static final String PACKAGE_PREFIX = "package"; 
	
  private String packageFolderPath;
	private String globalDataTypesFolderPath;
	private String moduleDataTypesFolderPath;
	private ContentManager contentManager;
	private String wsdlFilePath;
	
	public ModuleInstaller(String dataTypesFolder, String packageFolder, String wsdlFilePath, ContentManager manager){
		this.globalDataTypesFolderPath = dataTypesFolder;
		this.moduleDataTypesFolderPath = packageFolder + "/webapp/dataTypes";
		this.packageFolderPath = packageFolder;
		this.wsdlFilePath = wsdlFilePath;
		
		contentManager = manager;
	}
	
	public void installPackages(){
		
		// read datatypes from dataTypes directory
		File globalDataTypesFolder = new File(globalDataTypesFolderPath);
		File localDataTypesFolder = new File(moduleDataTypesFolderPath);
		OntModel dataTypesModel = getDataTypesFromPackageOntology(globalDataTypesFolder, localDataTypesFolder);
		ModuleWriter.setDataTypesModel(dataTypesModel);
    
		// install the package
		File viskoPackage = new File(packageFolderPath);
		boolean installSuccess = installPackage(viskoPackage);
    
		if(installSuccess)
			System.out.println("package: " + viskoPackage.getName() + " installed successfully!");
		else
			System.out.println("package: " + viskoPackage.getName() + " failed to install!");
	}
	
	private boolean installPackage(File aPackageDirectory){
		System.out.println("Installing package: " + aPackageDirectory.getName());
		
		ContentManager.setWorkspacePath(aPackageDirectory.getAbsolutePath());
		
		Class<?> rdfWriterClass;
		try{
			rdfWriterClass = Class.forName(PACKAGE_REGISTRATION_CLASS);
			AbstractModuleRDFRegistration rdfPackage = (AbstractModuleRDFRegistration)rdfWriterClass.newInstance();
			rdfPackage.setWebappName(aPackageDirectory.getName());
			
			System.out.println("Initializing wsdl to file: " + wsdlFilePath);
			rdfPackage.setWsdlFile(new File(wsdlFilePath));
			
			initializeRDFPackage(rdfPackage, aPackageDirectory);
			
			rdfPackage.populateToolkit();
			System.out.println("populated toolkit");
			
			rdfPackage.populateViewerSets();
			System.out.println("populated viewer sets");
			
			rdfPackage.populateServices();
			System.out.println("populated services");
	
			rdfPackage.populateParameterBindings();
			System.out.println("populated parameter bindings");
			
			finalizeRDFPackage(rdfPackage, aPackageDirectory);
			
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	private void initializeRDFPackage(AbstractModuleRDFRegistration rdfPackage, File packageDirectory){
		ModuleWriter packageWriter = new ModuleWriter(contentManager.getBaseURL(), packageDirectory.getName() + ".owl");
		rdfPackage.setModuleWriter(packageWriter);
	}

	private OntModel createOntModel(){
		OntDocumentManager mgr = new OntDocumentManager();
		mgr.setProcessImports(false);
		
		OntModelSpec s = new OntModelSpec(OntModelSpec.RDFS_MEM);
		s.setDocumentManager(mgr);
		OntModel model = ModelFactory.createOntologyModel(s);
		
		return model;
	}
	
	private OntModel getDataTypesFromPackageOntology(File globalDataTypesDirectory, File moduleDataTypesDirectory){
		OntModel dataTypesModel = createOntModel();
		File[] dataTypeOWLs = globalDataTypesDirectory.listFiles();
		
		for(File dataTypeFile : dataTypeOWLs){
			if(dataTypeFile.getName().endsWith(".owl")){
				System.out.println("Adding DataType File: " + dataTypeFile.getAbsolutePath());
				try{dataTypesModel.read(new FileInputStream(dataTypeFile), null);}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		System.out.println("Looking for custom module data types in: " + moduleDataTypesDirectory.getAbsolutePath());
		dataTypeOWLs = moduleDataTypesDirectory.listFiles();
		if(dataTypeOWLs != null) {
		  for(File dataTypeFile : dataTypeOWLs){
		    if(dataTypeFile.getName().endsWith(".owl")){
		      System.out.println("Adding DataType File: " + dataTypeFile.getAbsolutePath());
		      try{dataTypesModel.read(new FileInputStream(dataTypeFile), null);}
		      catch(Exception e){
		        e.printStackTrace();
		      }
		    }
		  }
		}
		
		return dataTypesModel;
	}
	
	private void finalizeRDFPackage(AbstractModuleRDFRegistration rdfPackage, File packageDirectory){
		ContentManager.setWorkspacePath(packageDirectory.getAbsolutePath());
		rdfPackage.getModuleWriter().dumpPackageRDF(contentManager);
		
		ModuleIndex index = new ModuleIndex(packageDirectory);
		String packageHTML = index.getHTMLIndex();
		FileUtils.writeTextFile(packageHTML, packageDirectory.getAbsolutePath(), packageDirectory.getName() + ".html");
	}
}