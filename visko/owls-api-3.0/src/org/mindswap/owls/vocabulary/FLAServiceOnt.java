//The MIT License
//
// Copyright (c) 2004 Evren Sirin
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to
// deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
// sell copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
// IN THE SOFTWARE.

package org.mindswap.owls.vocabulary;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.vocabulary.Vocabulary;
import org.mindswap.utils.URIUtils;

/**
 * Version independent vocabulary
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public abstract class FLAServiceOnt extends Vocabulary
{
	public static final String baseURI = "http://on.cs.unibas.ch/owl-s/";
	public static final String version = OWLS_1_2.version;
	public final static String NS = baseURI + version + "/FLAService.owl#";

	public static final Set<OWLDataProperty> flaDataProperties;
	public static final Map<OWLObjectProperty, OWLClass> flaObjectProperties;


	// Profile hierarchical structure
	public final static OWLClass TranslatorProfile;
	public final static OWLClass ProducerProfile;
	public final static OWLClass ConsumerProfile;
	public final static OWLClass DeviceProfile;
	public final static OWLClass InstanceProvidingServiceProfile;
	public final static OWLClass ViewerProfile;
	public final static OWLClass PrinterProfile;
	public final static OWLClass DigitalCameraProfile;
	public final static OWLClass InternalServiceProfile;

	// multimedia description
	public final static OWLDataProperty imageDescription;
	public final static OWLDataProperty iconDescription;
	public final static OWLDataProperty voiceDescription;
	public final static OWLDataProperty videoDescription;

	// Location related
	//	  <tce-service:locatedAt>
	//	    <tce-service:Location>
	//	      <profile:sParameter>
	//	        <geoF:Point rdf:ID="ViewServicePosition">
	//	          <rdfs:label>On the table in the FLA, CP Conference Room</rdfs:label>
	//	          <geoF:xyzCoordinates>2,3,0.8</geoF:xyzCoordinates>
	//	          <geoC:hasCoordinateSystem rdf:resource="http://www.flacp.fujitsulabs.com/tce/ontologies/2004/03/geo.owl#MyCoordinateSystem"/>
	//	        </geoF:Point>
	//	      </profile:sParameter>
	//	   </tce-service:Location>
	//	  </tce-service:locatedAt>
	public final static OWLClass Location;
	public final static OWLObjectProperty locatedAt;

	//	Service Control UI
	//	  <tce-service:hasServiceControlUI>
	//	    <tce-service:ServiceControlUI>
	//	      <profile:sParameter rdf:resource="http://128.8.244.15/DisplayURL2/controller.aspx" />
	//	   </tce-service:ServiceControlUI>
	//	  </tce-service:hasServiceControlUI>
	public final static OWLObjectProperty hasServiceControlUI;
	public final static OWLClass ServiceControlUI;

	// Owner
	//	  <fla:ownedBy xmlns:fla="http://www.flacp.fujitsulabs.com/tce/ontologies/2005/01/service.owl">
	//	    <fla:Owner>
	//	      <profile:sParameter>
	//	        <fla:OwnerEntity rdf:ID="Owner">
	//	          <rdfs:label>Jon Doe</rdfs:label>
	//	          <fla:ownerEntityID>jdoe</fla:ownerEntityID>
	//	        </fla:OwnerEntity>
	//	      </profile:sParameter>
	//	    </fla:Owner>
	//	  </fla:ownedBy>
	public final static OWLObjectProperty ownedBy;
	public final static OWLClass Owner;
	public final static OWLClass OwnerEntity;
	public final static OWLDataProperty ownerEntityID;

	// Security Setting
	//	  <fla:hasSecuritySetting xmlns:fla="http://www.flacp.fujitsulabs.com/tce/ontologies/2005/01/service.owl">
	//	    <fla:SecurityParameter>
	//	      <profile:sParameter>
	//	        <fla:SecuritySetting rdf:ID="securitySetting">
	//	          <rdfs:label>Fujitsu Lab of America</rdfs:label>
	//	          <fla:certificateAuthorityName>FLA</fla:certificateAuthorityName>
	//	        </fla:SecuritySetting>
	//	      </profile:sParameter>
	//	    </fla:SecurityParameter>
	//	  </fla:hasSecuritySetting>
	public final static OWLObjectProperty hasSecuritySetting;
	public final static OWLClass SecurityParameter;
	public final static OWLClass SecuritySetting;
	public final static OWLDataProperty certificateAuthorityName;

	// self destruction function
	//    <tce-service:hasSelfDestructionService>
	//    <tce-service:SelfDestructionService>
	//      <profile:sParameter rdf:resource="#DestroyObject"/>
	//    </tce-service:SelfDestructionService>
	//  </tce-service:hasSelfDestructionService>
	public final static OWLClass SelfDestructionService;
	public final static OWLObjectProperty  hasSelfDestructionService;

	// Process parameter strict type
	//	<process:Input rdf:ID="URLInput">
	//	  <fla:strictType>http://www.flacp.fujitsulabs.com/tce/ontologies/2004/03/object.owl#ViewableFile</fla:strictType>
	//	</process:Input>
	public final static OWLDataProperty strictType;

	// UPnP grounding
	//<fla:UPnPGrounding rdf:ID="SemanticStreamProviderGrounding">
	//    <service:supportedBy rdf:resource="#SemanticStreamProviderService" />
	//    <fla:hasUPnPAtomicProcessGrounding rdf:resource="#SemanticStreamProviderProcessGrounding" />
	//</fla:UPnPGrounding>
	//<fla:UPnPAtomicProcessGrounding rdf:ID="SemanticStreamProviderProcessGrounding">
	//    <grounding:damlsProcess rdf:resource="#SemanticStreamProvider"/>
	//    <fla:upnpCommand>getStreamURL</fla:upnpCommand>
	//    <fla:UPnPOutputMapping rdf:parseType="Collection">
	//        <fla:UPnPMap>
	//            <grounding:damlsParameter rdf:resource="#SemanticStreamOutput" />
	//            <fla:upnpParameter>_ReturnValue</fla:upnpParameter>
	//        </fla:UPnPMap>
	//    </fla:UPnPOutputMapping>
	//    <fla:upnpServiceID>upnp:id:stream-service-1.0:128.8.244.170:2</fla:upnpServiceID>
	//    <fla:upnpDeviceURL>http://streaming.flacp.fujitsulabs.com:35002/</fla:upnpDeviceURL>
	//</fla:UPnPAtomicProcessGrounding>
	public final static OWLClass UPnPGrounding;
	public final static OWLClass UPnPAtomicProcessGrounding;

	public final static OWLObjectProperty upnpOutputMapping;
	public final static OWLObjectProperty upnpInputMapping ;

	public final static OWLObjectProperty owlsParameter;
	public final static OWLDataProperty upnpParameter;
	public final static OWLDataProperty upnpServiceID;
	public final static OWLDataProperty upnpDeviceURL;
	public final static OWLDataProperty upnpCommand;
	public static final OWLClass UPnPMap;

	//	REST invocation indication
	public final static OWLDataProperty restInvocation;
	// whether or not transform the input to its own accept type
	public static final OWLDataProperty transformInputType;
	// whether or not create a random input for the field during execution
	public static final OWLDataProperty useRandomInput;

	// Meta-data properties
	// Service -> Profile
//	public final static OWLObjectProperty presents;
	// Service -> Process
//	public final static OWLObjectProperty describedBy;
	// Service -> service names
//	public final static OWLDataProperty serviceName;
	// service -> service description
//	public final static OWLDataProperty textDescription;
	// service -> service description URL
	public final static OWLDataProperty serviceDescriptionURL;
	// service -> service description text
	public final static OWLDataProperty serviceDescriptionText;
	// service -> input
//	public final static OWLObjectProperty input;
	// service -> service input count
//	public final static OWLDataProperty inputCount;
	// service -> output
//	public final static OWLObjectProperty output;
	// service -> service output count
//	public final static OWLDataProperty outputCount;
	// service -> location name
	public final static OWLDataProperty location;
	// service -> coordinates
	public final static OWLDataProperty coordinates;
	// service -> control UI
	public final static OWLDataProperty controlUI;
	// service -> service owner
	public final static OWLDataProperty owner;
	// service -> service discovery time
	public final static OWLDataProperty discoveryTime;
	// service -> service type
	// two-letter type. First letter is selected from {L, P, R} (local, pervasive, remote},
	// second letter is selected from {G, T, I} {general, translator, internal}
	public final static OWLDataProperty serviceType;
	// service -> grounding type
//	public final static OWLDataProperty groundingType;
	// service -> grounding URL (device URL for UPnP Service, WSDL URL for Web Service
//	public final static OWLDataProperty groundingURL;
	// service -> deletable
	public final static OWLDataProperty deletable;
	// service -> Certificate Authority name
	public final static OWLDataProperty caName;
	// service -> sphere id (where the service is located) root for the default sphere
	public final static OWLDataProperty sphereID;
	// service -> service description version
	public final static OWLDataProperty descriptionVersion;

	static
	{
		OWLOntology ont = loadOntology(NS);

		HashSet<OWLDataProperty> dPTmp = new HashSet<OWLDataProperty>();
		Map<OWLObjectProperty, OWLClass> oPtoClTmp = new HashMap<OWLObjectProperty, OWLClass>();

		TranslatorProfile = ont.getClass(URIUtils.createURI(NS + "TranslatorProfile"));
		ProducerProfile   = ont.getClass(URIUtils.createURI(NS + "ProducerProfile"));
		ConsumerProfile   = ont.getClass(URIUtils.createURI(NS + "ConsumerProfile"));
		DeviceProfile     = ont.getClass(URIUtils.createURI(NS + "DeviceProfile"));
		InstanceProvidingServiceProfile = ont.getClass(URIUtils.createURI(NS + "InstanceProvidingServiceProfile"));
		ViewerProfile     = ont.getClass(URIUtils.createURI(NS + "ViewerProfile"));
		PrinterProfile    = ont.getClass(URIUtils.createURI(NS + "PrinterProfile"));
		DigitalCameraProfile   = ont.getClass(URIUtils.createURI(NS + "DigitalCameraProfile"));
		InternalServiceProfile = ont.getClass(URIUtils.createURI(NS + "InternalServiceProfile"));

		imageDescription = ont.getDataProperty(URIUtils.createURI(NS + "imageDescription"));
		iconDescription  = ont.getDataProperty(URIUtils.createURI(NS + "iconDescription"));
		voiceDescription = ont.getDataProperty(URIUtils.createURI(NS + "voiceDescription"));
		videoDescription = ont.getDataProperty(URIUtils.createURI(NS + "videoDescription"));
		dPTmp.add(imageDescription);
		dPTmp.add(iconDescription);
		dPTmp.add(voiceDescription);
		dPTmp.add(videoDescription);

		Location  = ont.getClass(URIUtils.createURI(NS + "Location"));
		locatedAt = ont.getObjectProperty(URIUtils.createURI(NS + "locatedAt"));
		oPtoClTmp.put(locatedAt, Location);

		hasServiceControlUI = ont.getObjectProperty(URIUtils.createURI(NS + "hasServiceControlUI"));
		ServiceControlUI    = ont.getClass(URIUtils.createURI(NS + "ServiceControlUI"));
		oPtoClTmp.put(hasServiceControlUI, ServiceControlUI);

		ownedBy       = ont.getObjectProperty(URIUtils.createURI(NS + "ownedBy"));
		Owner         = ont.getClass(URIUtils.createURI(NS + "Owner"));
		OwnerEntity   = ont.getClass(URIUtils.createURI(NS + "OwnerEntity"));
		ownerEntityID = ont.getDataProperty(URIUtils.createURI(NS + "ownerEntityID"));
		oPtoClTmp.put(ownedBy, Owner);

		hasSecuritySetting       = ont.getObjectProperty(URIUtils.createURI(NS + "hasSecuritySetting"));
		SecurityParameter        = ont.getClass(URIUtils.createURI(NS + "SecurityParameter"));
		SecuritySetting          = ont.getClass(URIUtils.createURI(NS + "SecuritySetting"));
		certificateAuthorityName = ont.getDataProperty(URIUtils.createURI(NS + "certificateAuthorityName"));
		oPtoClTmp.put(hasSecuritySetting, SecurityParameter);

		SelfDestructionService    = ont.getClass(URIUtils.createURI(NS + "SelfDestructionService"));
		hasSelfDestructionService = ont.getObjectProperty(URIUtils.createURI(NS + "hasSelfDestructionService"));
		oPtoClTmp.put(hasSelfDestructionService, SelfDestructionService);

		strictType  = ont.getDataProperty(URIUtils.createURI(NS + "strictType"));

		UPnPGrounding     = ont.getClass(URIUtils.createURI(NS + "UPnPGrounding"));
		UPnPAtomicProcessGrounding    = ont.getClass(URIUtils.createURI(NS + "UPnPAtomicProcessGrounding"));
		owlsParameter     = ont.getObjectProperty(URIUtils.createURI(NS + "owlsParameter"));
		upnpOutputMapping = ont.getObjectProperty(URIUtils.createURI(NS + "upnpOutputMapping"));
		upnpInputMapping  = ont.getObjectProperty(URIUtils.createURI(NS + "upnpInputMapping"));
		upnpParameter     = ont.getDataProperty(URIUtils.createURI(NS + "upnpParameter"));
		upnpServiceID     = ont.getDataProperty(URIUtils.createURI(NS + "upnpServiceID"));
		upnpDeviceURL     = ont.getDataProperty(URIUtils.createURI(NS + "upnpDeviceURL"));
		upnpCommand       = ont.getDataProperty(URIUtils.createURI(NS + "upnpCommand"));
		UPnPMap           = ont.getClass(URIUtils.createURI(NS + "UPnPMap"));

		restInvocation     = ont.getDataProperty(URIUtils.createURI(NS + "restInvocation"));
		transformInputType = ont.getDataProperty(URIUtils.createURI(NS + "transformInputType"));
		useRandomInput     = ont.getDataProperty(URIUtils.createURI(NS + "useRandomInput"));
		dPTmp.add(restInvocation);
		dPTmp.add(transformInputType);
		dPTmp.add(useRandomInput);

//		presents           = ont.getObjectProperty(URIUtils.createURI(NS + "presents"));
//		describedBy        = ont.getObjectProperty(URIUtils.createURI(NS + "describedBy"));
//		serviceName        = ont.getDataProperty(URIUtils.createURI(NS + "serviceName"));
//		textDescription    = ont.getDataProperty(URIUtils.createURI(NS + "textDescription"));
		serviceDescriptionURL  = ont.getDataProperty(URIUtils.createURI(NS + "serviceDescriptionURL"));
		serviceDescriptionText = ont.getDataProperty(URIUtils.createURI(NS + "serviceDescriptionText"));
//		input              = ont.getObjectProperty(URIUtils.createURI(NS + "input"));
//		inputCount         = ont.getDataProperty(URIUtils.createURI(NS + "inputCount"));
//		output             = ont.getObjectProperty(URIUtils.createURI(NS + "output"));
//		outputCount        = ont.getDataProperty(URIUtils.createURI(NS + "outputCount"));
		location           = ont.getDataProperty(URIUtils.createURI(NS + "location"));
		coordinates        = ont.getDataProperty(URIUtils.createURI(NS + "coordinates"));
		controlUI          = ont.getDataProperty(URIUtils.createURI(NS + "controlUI"));
		owner              = ont.getDataProperty(URIUtils.createURI(NS + "owner"));
		discoveryTime      = ont.getDataProperty(URIUtils.createURI(NS + "discoveryTime"));
		serviceType        = ont.getDataProperty(URIUtils.createURI(NS + "serviceType"));
//		groundingType      = ont.getDataProperty(URIUtils.createURI(NS + "groundingType"));
//		groundingURL       = ont.getDataProperty(URIUtils.createURI(NS + "groundingURL"));
		deletable          = ont.getDataProperty(URIUtils.createURI(NS + "deletable"));
		caName             = ont.getDataProperty(URIUtils.createURI(NS + "caName"));
		sphereID           = ont.getDataProperty(URIUtils.createURI(NS + "sphereID"));
		descriptionVersion = ont.getDataProperty(URIUtils.createURI(NS + "descriptionVersion"));

		flaDataProperties = Collections.unmodifiableSet(dPTmp);
		flaObjectProperties = Collections.unmodifiableMap(oPtoClTmp);
	}

}
