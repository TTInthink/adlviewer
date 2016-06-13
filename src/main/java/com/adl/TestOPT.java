package com.adl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import openEHR.v1.template.Statement;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.openehr.am.archetype.Archetype;
import org.openehr.am.archetype.constraintmodel.ArchetypeConstraint;
import org.openehr.am.archetype.constraintmodel.CComplexObject;
import org.openehr.am.archetype.constraintmodel.CObject;
import org.openehr.am.archetype.ontology.ArchetypeOntology;
import org.openehr.am.archetype.ontology.ArchetypeTerm;
import org.openehr.am.openehrprofile.datatypes.quantity.CDvOrdinal;
import org.openehr.am.openehrprofile.datatypes.text.CCodePhrase;
import org.openehr.am.serialize.ADLSerializer;
import org.openehr.am.template.Flattener;
import org.openehr.am.template.OETParser;
import org.openehr.binding.XMLBinding;
import org.openehr.rm.binding.DADLBinding;
import org.openehr.rm.support.measurement.SimpleMeasurementService;
import org.openehr.rm.util.GenerationStrategy;
import org.openehr.rm.util.SkeletonGenerator;

import openEHR.v1.template.Archetyped;
import openEHR.v1.template.COMPOSITION;
import openEHR.v1.template.INSTRUCTION;
import openEHR.v1.template.CLUSTER;
import openEHR.v1.template.SECTION;
import openEHR.v1.template.TEMPLATE;
import se.acode.openehr.parser.ADLParser;

public class TestOPT {
	
	protected DADLBinding dadlBinding; 	
	private XMLBinding xmlBinding;
	private OETParser oetParser;
	private Flattener flattener;
	private ADLSerializer adlSerializer;
	private XmlOptions xmlOptions;
	protected Map<String, Archetype> archetypeMap;	
	protected SkeletonGenerator generator;
	
	private String templatePath="eReferral_2015_12_19-16_46_46/templates/composition/";
	
	public TestOPT()throws Exception{
		dadlBinding = new DADLBinding();	
		xmlBinding = new XMLBinding();
		generator = SkeletonGenerator.getInstance();
		oetParser = new OETParser();
		flattener = new Flattener();
		adlSerializer = new ADLSerializer();
		setXmlOptions();
		loadArchetypeMap();
	}
	
	private void setXmlOptions() {
		xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		xmlOptions.setSaveAggressiveNamespaces();
		xmlOptions.setSaveOuter();
		xmlOptions.setSaveInner();
	}
	
	protected void loadArchetypeMap() throws Exception {
		
//		File file=new File("src/main/resources/eReferral_2015_12_19-16_46_46");
//		System.out.println("File: "+file.getAbsolutePath());
//		listFilesAndFilesSubDirectories("src/main/resources/eReferral_2015_12_19-16_46_46");
//		String[] ids = {
//				"eReferral_2015_12_19-16_46_46/archetypes/cluster/openEHR-EHR-CLUSTER.address.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/cluster/openEHR-EHR-CLUSTER.imaging.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/cluster/openEHR-EHR-CLUSTER.individual_personal.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/cluster/openEHR-EHR-CLUSTER.individual_professional.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/cluster/openEHR-EHR-CLUSTER.organisation.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/cluster/openEHR-EHR-CLUSTER.person_name.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/cluster/openEHR-EHR-CLUSTER.professional_role.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/cluster/openEHR-EHR-CLUSTER.telecom_details.v0.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/composition/openEHR-EHR-COMPOSITION.request.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/entry/evaluation/openEHR-EHR-EVALUATION.adverse_reaction.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/entry/evaluation/openEHR-EHR-EVALUATION.clinical_synopsis.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/entry/evaluation/openEHR-EHR-EVALUATION.exclusion-adverse.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/entry/evaluation/openEHR-EHR-EVALUATION.exclusion-medication.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/entry/evaluation/openEHR-EHR-EVALUATION.exclusion-problem_diagnosis.v0.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/entry/evaluation/openEHR-EHR-EVALUATION.problem-diagnosis.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/entry/evaluation/openEHR-EHR-EVALUATION.problem_diagnosis.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/entry/instruction/openEHR-EHR-INSTRUCTION.medication.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/entry/instruction/openEHR-EHR-INSTRUCTION.request-referral.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/entry/observation/openEHR-EHR-OBSERVATION.imaging.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/entry/observation/openEHR-EHR-OBSERVATION.lab_test.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/section/openEHR-EHR-SECTION.adverse_list.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/section/openEHR-EHR-SECTION.diagnostic_reports.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/section/openEHR-EHR-SECTION.medication_order_list.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/section/openEHR-EHR-SECTION.problem_list.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/section/openEHR-EHR-SECTION.referral_details.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/structure/openEHR-EHR-ITEM_TREE.medication.v1.adl"
//
//		};
		
		String[] ids = {
				"eReferral_2015_12_19-16_46_46/archetypes/cluster/openEHR-EHR-CLUSTER.person_name.v1.adl"
//				"eReferral_2015_12_19-16_46_46/archetypes/cluster/openEHR-EHR-CLUSTER.professional_role.v1.adl",
//				"eReferral_2015_12_19-16_46_46/archetypes/cluster/openEHR-EHR-CLUSTER.telecom_details.v0.adl"
//				

		};
		
		archetypeMap = new HashMap<String, Archetype>();
		
		for(String id : ids) {
			Archetype archetype = loadArchetype(id);
//			Given Name
//			String name=archetype.getPathByNodeId("at0002");
			ArchetypeConstraint name=archetype.node("/items[at0013]/items[at0002]/items[at0003]");
			CComplexObject definition=archetype.getDefinition();
			
			System.out.println("MAP"+name.getClass());
			CComplexObject cObj=(CComplexObject)name;
			ArchetypeOntology ontology = archetype.getOntology();
			ArchetypeTerm term = ontology.termDefinition("en", "at0003");
//			String name2=name.getAnnotation();
//			String name=archetype.inputByPath("/[at0002]");
			System.out.println("---NAME: "+term.getItem("text"));
			archetypeMap.put(archetype.getArchetypeId().toString(), archetype);
		}			
	}
	
//	public void listFilesAndFilesSubDirectories(String directoryName){
//		try {
//			Files.walk(Paths.get(directoryName))
//			.filter(Files::isRegularFile)
//			.forEach(System.out::println);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//    }
	
	protected Archetype archetype;
	protected Archetype loadArchetype(String id) throws Exception {
		ADLParser adlParser = new ADLParser(fromClasspath(id));
		archetype = adlParser.parse();
		System.out.println("ArchetypeID: "+archetype.getArchetypeId());
		return archetype;
    }
	
	public void generateDDL(){
		try {
			Object obj = generator.create(archetype, null, archetypeMap,
					GenerationStrategy.MAXIMUM);
			List<String> lines = dadlBinding.toDADL(obj);
			for(String line:lines)
				System.out.println(line);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		
		try {
			TestOPT test=new TestOPT();
//			test.generateDDL();
			test.loadTemplate("eReferral");
//			Archetype flattened = test.flattenTemplate("eReferral");
//			test.generate(flattened);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void generate(Archetype flattened){
		try {
			Object obj = generator.create(flattened, archetypeMap);
			printXML(obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void printXML(Object obj) throws Exception {
		Object value = xmlBinding.bindToXML(obj);
		XmlObject xmlObj = (XmlObject) value;
		xmlObj.save(System.out, xmlOptions);
	}
	
	protected Archetype flattenTemplate(String templateFilename) throws Exception {
		TEMPLATE template = loadTemplate(templateFilename);
		Archetype flattened = flattener.toFlattenedArchetype(template, 
				archetypeMap, null);
		return flattened;
	}
	
	protected TEMPLATE loadTemplate(String name) throws Exception {
		String path = templatePath+name + ".oet";
		TEMPLATE template = oetParser.parseTemplate(
				fromClasspath(path)).getTemplate();
		
		Archetyped def = template.getDefinition();
		
		System.out.println("TEmplate Name: "+def.getClass());
		COMPOSITION composition = (COMPOSITION) def;
		SECTION section = (SECTION) composition.getContentArray()[0];
		INSTRUCTION instruction = (INSTRUCTION) section.getItemArray()[0];
		System.out.println("INStruction PAth: "+instruction.getPath());
		CLUSTER cluster=(CLUSTER)instruction.getItemsArray()[0];
		System.out.println("CLusterNAME: "+cluster.getName());
		CLUSTER cluster1=(CLUSTER)cluster.getItemsArray()[0];
		String archetypeId=cluster1.getArchetypeId();
		System.out.println("Archetype ID: "+archetypeId);
		Archetype arch=archetypeMap.get(archetypeId);
		System.out.println("CLusterNAME1: "+cluster1.getRuleArray()[0]);
		Statement statement=cluster1.getRuleArray()[0];
		System.out.println("Rule Path: "+statement.getPath());
		ArchetypeConstraint constraint=arch.node(statement.getPath()+"/value/defining_code");
		CCodePhrase codephase=(CCodePhrase)constraint;
//		CComplexObject com=(CComplexObject)constraint;
		System.out.println("CONSR+TRAINT: "+codephase.getCodeList());
		return template;
	}
	
	protected InputStream fromClasspath(String filename) throws Exception {
		return this.getClass().getClassLoader().getResourceAsStream(filename);
	}
}
