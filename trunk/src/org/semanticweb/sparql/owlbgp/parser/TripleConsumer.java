package org.semanticweb.sparql.owlbgp.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.AnnotationValue;
import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.Import;
import org.semanticweb.sparql.owlbgp.model.Ontology;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgp.model.dataranges.FacetRestriction;
import org.semanticweb.sparql.owlbgp.model.individuals.AnonymousIndividual;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.individuals.IndividualVariable;
import org.semanticweb.sparql.owlbgp.model.individuals.NamedIndividual;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationProperty;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.DataProperty;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectInverseOf;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.translators.ClassExpressionListItemTranslator;
import org.semanticweb.sparql.owlbgp.parser.translators.DataRangeListItemTranslator;
import org.semanticweb.sparql.owlbgp.parser.translators.FacetRestrictionListItemTranslator;
import org.semanticweb.sparql.owlbgp.parser.translators.IndividualListItemTranslator;
import org.semanticweb.sparql.owlbgp.parser.translators.ObjectPropertyExpressionListItemTranslator;
import org.semanticweb.sparql.owlbgp.parser.translators.OptimisedListTranslator;
import org.semanticweb.sparql.owlbgp.parser.translators.TypedConstantListItemTranslator;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPAllValuesFromHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPDataComplementOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPDataIntersectionOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPDataOneOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPDataUnionOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPDisjointUnionHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPDisjointWithHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPEquivalentClassHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPExactCardinalityHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPExactQualifiedCardinalityHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPHasSelfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPHasValueHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPImportsHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPMaxCardinalityHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPMaxQualifiedCardinalityHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPMinCardinalityHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPMinQualifiedCardinalityHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPObjectComplementOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPObjectIntersectionOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPObjectOneOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPObjectUnionOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPOnDatatypeHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPPropertyChainAxiomHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPSomeValuesFromHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPSubClassOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPSubPropertyOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.VersionIRIHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.AllDifferentHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.AllDisjointClassesHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.AllDisjointPropertiesHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.AnnotationHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.AnnotationPropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.AxiomHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.ClassHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.DatatypeHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.DatatypePropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.NamedIndividualHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.NegativePropertyAssertionHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.ObjectPropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.OntologyHandler;

public class TripleConsumer {
    public static final String LB=System.getProperty("line.separator");
    
    protected final Set<Import>     imports=new HashSet<Import>();
    protected Identifier            ontologyIRI;
    protected final Set<Identifier> versionIRIs=new HashSet<Identifier>();
    protected final Set<Axiom>      axioms=new HashSet<Axiom>();
    protected Ontology              ontology;
    
    protected final Set<Identifier>                              RIND=new HashSet<Identifier>();
    protected final Map<Identifier,ClassExpression>              CE=new HashMap<Identifier,ClassExpression>(); 
    protected final Map<Identifier,DataRange>                    DR=new HashMap<Identifier,DataRange>();
    protected final Map<Identifier,ObjectPropertyExpression>     OPE=new HashMap<Identifier,ObjectPropertyExpression>();
    protected final Map<Identifier,DataPropertyExpression>       DPE=new HashMap<Identifier,DataPropertyExpression>();
    protected final Map<Identifier,AnnotationPropertyExpression> APE=new HashMap<Identifier,AnnotationPropertyExpression>();
    protected final Map<Identifier,Individual>                   IND=new HashMap<Identifier,Individual>();
    protected final Map<Identifier,Set<Annotation>>              ANN=new HashMap<Identifier,Set<Annotation>>();
    
    protected final Map<Identifier,TriplePredicateHandler>                 streamingByPredicateHandlers=new HashMap<Identifier,TriplePredicateHandler>();
    protected final Map<Identifier,Map<Identifier,TriplePredicateHandler>> streamingByPredicateAndObjectHandlers=new HashMap<Identifier, Map<Identifier,TriplePredicateHandler>>();
    protected final Map<Identifier,TriplePredicateHandler>                 dataRangePredicateHandlers=new HashMap<Identifier,TriplePredicateHandler>();
    protected final Map<Identifier,TriplePredicateHandler>                 classExpressionPredicateHandlers=new HashMap<Identifier,TriplePredicateHandler>();
    protected final Map<Identifier,TriplePredicateHandler>                 byPredicateHandlers=new HashMap<Identifier,TriplePredicateHandler>();
    protected final Map<Identifier,Map<Identifier,TriplePredicateHandler>> byPredicateAndObjectHandlers=new HashMap<Identifier, Map<Identifier,TriplePredicateHandler>>();
    
    protected final Map<Identifier, Map<Identifier, Set<Identifier>>> subjToPredToObjects=new HashMap<Identifier, Map<Identifier,Set<Identifier>>>(); // subject, predicate, objects
    protected final Map<Identifier, Map<Identifier, Set<Identifier>>> builtInPredToBuiltInObjToSubjects=new HashMap<Identifier, Map<Identifier,Set<Identifier>>>(); // built-in predicate, built-in object, subjects
    protected final Map<Identifier, Map<Identifier, Set<Identifier>>> builtInPredToSubToObjects=new HashMap<Identifier, Map<Identifier,Set<Identifier>>>(); // built-in predicate, subject, object

    protected final OptimisedListTranslator<ClassExpression> classExpressionListTranslator=new OptimisedListTranslator<ClassExpression>(this, new ClassExpressionListItemTranslator(this));
    protected final OptimisedListTranslator<Individual> individualListTranslator=new OptimisedListTranslator<Individual>(this, new IndividualListItemTranslator(this));
    protected final OptimisedListTranslator<ObjectPropertyExpression> objectPropertyListTranslator=new OptimisedListTranslator<ObjectPropertyExpression>(this, new ObjectPropertyExpressionListItemTranslator(this));
    protected final OptimisedListTranslator<Literal> literalListTranslator=new OptimisedListTranslator<Literal>(this, new TypedConstantListItemTranslator(this));
    protected final OptimisedListTranslator<DataRange> dataRangeListTranslator=new OptimisedListTranslator<DataRange>(this, new DataRangeListItemTranslator(this));
    protected final OptimisedListTranslator<FacetRestriction> faceRestrictionListTranslator=new OptimisedListTranslator<FacetRestriction>(this, new FacetRestrictionListItemTranslator(this));
    
    {
        CE.put(Vocabulary.OWL_THING, Clazz.create(Vocabulary.OWL_THING));
        CE.put(Vocabulary.OWL_NOTHING, Clazz.create(Vocabulary.OWL_NOTHING));
        OPE.put(Vocabulary.OWL_TOP_OBJECT_PROPERTY, ObjectProperty.create(Vocabulary.OWL_TOP_OBJECT_PROPERTY));
        OPE.put(Vocabulary.OWL_BOTTOM_OBJECT_PROPERTY, ObjectProperty.create(Vocabulary.OWL_BOTTOM_OBJECT_PROPERTY));
        DPE.put(Vocabulary.OWL_TOP_DATA_PROPERTY, DataProperty.create(Vocabulary.OWL_TOP_DATA_PROPERTY));
        DPE.put(Vocabulary.OWL_BOTTOM_DATA_PROPERTY, DataProperty.create(Vocabulary.OWL_BOTTOM_DATA_PROPERTY));
        APE.put(Vocabulary.OWL_PRIOR_VERSION, AnnotationProperty.create(Vocabulary.OWL_PRIOR_VERSION));
        APE.put(Vocabulary.OWL_BACKWARD_COMPATIBLE_WITH, AnnotationProperty.create(Vocabulary.OWL_BACKWARD_COMPATIBLE_WITH));
        APE.put(Vocabulary.OWL_INCOMPATIBLE_WITH, AnnotationProperty.create(Vocabulary.OWL_INCOMPATIBLE_WITH));
        APE.put(Vocabulary.OWL_VERSION_INFO, AnnotationProperty.create(Vocabulary.OWL_VERSION_INFO));
        APE.put(Vocabulary.RDFS_LABEL, AnnotationProperty.create(Vocabulary.RDFS_LABEL));
        APE.put(Vocabulary.RDFS_COMMENT, AnnotationProperty.create(Vocabulary.RDFS_COMMENT));
        APE.put(Vocabulary.RDFS_SEE_ALSO, AnnotationProperty.create(Vocabulary.RDFS_SEE_ALSO));
        APE.put(Vocabulary.RDFS_IS_DEFINED_BY, AnnotationProperty.create(Vocabulary.RDFS_IS_DEFINED_BY));
        for (Datatype dt : Datatype.OWL2_DATATYPES)
            DR.put(dt.getIdentifier(), dt);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public TripleConsumer() {
        streamingByPredicateHandlers.put(Vocabulary.OWL_IMPORTS,new TPImportsHandler(this));
        IRI rdftype=Vocabulary.RDF_TYPE;
        Map<Identifier, TriplePredicateHandler> typeHandlers=new HashMap<Identifier, TriplePredicateHandler>();
        typeHandlers.put(Vocabulary.OWL_CLASS, new ClassHandler(this));
        typeHandlers.put(Vocabulary.RDFS_DATATYPE, new DatatypeHandler(this));
        typeHandlers.put(Vocabulary.OWL_OBJECT_PROPERTY, new ObjectPropertyHandler(this));
        typeHandlers.put(Vocabulary.OWL_DATA_PROPERTY, new DatatypePropertyHandler(this));
        typeHandlers.put(Vocabulary.OWL_ANNOTATION_PROPERTY, new AnnotationPropertyHandler(this));
        typeHandlers.put(Vocabulary.OWL_NAMED_INDIVIDUAL, new NamedIndividualHandler(this));
        typeHandlers.put(Vocabulary.OWL_ONTOLOGY, new OntologyHandler(this));
        typeHandlers.put(Vocabulary.OWL_AXIOM, new AxiomHandler(this));
        typeHandlers.put(Vocabulary.OWL_ANNOTATION, new AnnotationHandler(this));
        typeHandlers.put(Vocabulary.OWL_ALL_DISJOINT_CLASSES, new AllDisjointClassesHandler(this));
        typeHandlers.put(Vocabulary.OWL_ALL_DISJOINT_PROPERTIES, new AllDisjointPropertiesHandler(this));
        typeHandlers.put(Vocabulary.OWL_ALL_DIFFERENT, new AllDifferentHandler(this));
        typeHandlers.put(Vocabulary.OWL_NEGATIVE_PROPERTY_ASSERTION, new NegativePropertyAssertionHandler(this));
        typeHandlers.put(Vocabulary.OWL_VERSION_IRI, new VersionIRIHandler(this));
        streamingByPredicateAndObjectHandlers.put(rdftype,typeHandlers);
        dataRangePredicateHandlers.put(Vocabulary.OWL_INTERSECTION_OF, new TPDataIntersectionOfHandler(this));
        dataRangePredicateHandlers.put(Vocabulary.OWL_UNION_OF, new TPDataUnionOfHandler(this));
        dataRangePredicateHandlers.put(Vocabulary.OWL_DATATYPE_COMPLEMENT_OF, new TPDataComplementOfHandler(this));
        dataRangePredicateHandlers.put(Vocabulary.OWL_ONE_OF, new TPDataOneOfHandler(this));
        dataRangePredicateHandlers.put(Vocabulary.OWL_ON_DATA_TYPE, new TPOnDatatypeHandler(this));
        classExpressionPredicateHandlers.put(Vocabulary.OWL_INTERSECTION_OF, new TPObjectIntersectionOfHandler(this));
        classExpressionPredicateHandlers.put(Vocabulary.OWL_UNION_OF, new TPObjectUnionOfHandler(this));
        classExpressionPredicateHandlers.put(Vocabulary.OWL_COMPLEMENT_OF, new TPObjectComplementOfHandler(this));
        classExpressionPredicateHandlers.put(Vocabulary.OWL_ONE_OF, new TPObjectOneOfHandler(this));
        classExpressionPredicateHandlers.put(Vocabulary.OWL_SOME_VALUES_FROM, new TPSomeValuesFromHandler(this));
        classExpressionPredicateHandlers.put(Vocabulary.OWL_ALL_VALUES_FROM, new TPAllValuesFromHandler(this));
        classExpressionPredicateHandlers.put(Vocabulary.OWL_HAS_VALUE, new TPHasValueHandler(this));
        classExpressionPredicateHandlers.put(Vocabulary.OWL_HAS_SELF, new TPHasSelfHandler(this));
        classExpressionPredicateHandlers.put(Vocabulary.OWL_MIN_CARDINALITY, new TPMinCardinalityHandler(this));
        classExpressionPredicateHandlers.put(Vocabulary.OWL_MAX_CARDINALITY, new TPMaxCardinalityHandler(this));
        classExpressionPredicateHandlers.put(Vocabulary.OWL_CARDINALITY, new TPExactCardinalityHandler(this));
        classExpressionPredicateHandlers.put(Vocabulary.OWL_MIN_QUALIFIED_CARDINALITY, new TPMinQualifiedCardinalityHandler(this));
        classExpressionPredicateHandlers.put(Vocabulary.OWL_MAX_QUALIFIED_CARDINALITY, new TPMaxQualifiedCardinalityHandler(this));
        classExpressionPredicateHandlers.put(Vocabulary.OWL_QUALIFIED_CARDINALITY, new TPExactQualifiedCardinalityHandler(this));
        
        byPredicateHandlers.put(Vocabulary.RDFS_SUBCLASS_OF,new TPSubClassOfHandler(this));
        byPredicateHandlers.put(Vocabulary.OWL_EQUIVALENT_CLASS,new TPEquivalentClassHandler(this));
        byPredicateHandlers.put(Vocabulary.OWL_DISJOINT_WITH,new TPDisjointWithHandler(this));
        byPredicateHandlers.put(Vocabulary.OWL_DISJOINT_UNION_OF,new TPDisjointUnionHandler(this));
        byPredicateHandlers.put(Vocabulary.RDFS_SUB_PROPERTY_OF,new TPSubPropertyOfHandler(this));
        byPredicateHandlers.put(Vocabulary.OWL_PROPERTY_CHAIN_AXIOM,new TPPropertyChainAxiomHandler(this));
        typeHandlers=new HashMap<Identifier, TriplePredicateHandler>();
        typeHandlers.put(Vocabulary.OWL_CLASS, new ClassHandler(this));
        typeHandlers.put(Vocabulary.RDFS_DATATYPE, new DatatypeHandler(this));
        typeHandlers.put(Vocabulary.OWL_OBJECT_PROPERTY, new ObjectPropertyHandler(this));
        typeHandlers.put(Vocabulary.OWL_DATA_PROPERTY, new DatatypePropertyHandler(this));
        typeHandlers.put(Vocabulary.OWL_ANNOTATION_PROPERTY, new AnnotationPropertyHandler(this));
        typeHandlers.put(Vocabulary.OWL_NAMED_INDIVIDUAL, new NamedIndividualHandler(this));
        typeHandlers.put(Vocabulary.OWL_ALL_DISJOINT_CLASSES, new AllDisjointClassesHandler(this));
        byPredicateAndObjectHandlers.put(rdftype,typeHandlers);
    }
    public void setClassesInOntologySignature(Set<Clazz> classes) {
        for (Clazz cls : classes)
            CE.put(cls, cls);
    }
    public void setObjectPropertiesInOntologySignature(Set<ObjectProperty> objectProperties) {
        for (ObjectProperty op : objectProperties)
            OPE.put(op, op); 
    }
    public void setDataPropertiesInOntologySignature(Set<DataProperty> dataProperties) {
        for (DataProperty dp : dataProperties)
            DPE.put(dp, dp);
    }
    public void setIndividualsInOntologySignature(Set<Individual> individuals) {
        for (Individual ind : individuals)
            IND.put(ind, ind);
    }
    public void setCustomDatatypesInOntologySignature(Set<Datatype> customDatatypes) {
        for (Datatype dt : customDatatypes)
            DR.put(dt, dt);
    }
    public void mapClassIdentifierToClassExpression(Identifier id, ClassExpression classExpression) {
        CE.put(id,classExpression);
    }
    public ClassExpression getClassExpressionForClassIdentifier(Identifier id) {
        return CE.get(id);
    }
    public void mapObjectPropertyIdentifierToObjectProperty(Identifier id, ObjectPropertyExpression objectPropertyExpression) {
        OPE.put(id, objectPropertyExpression); 
    }
    public ObjectPropertyExpression getObjectPropertyExpressionForObjectPropertyIdentifier(Identifier id) {
        return OPE.get(id);
    }
    public void mapDataPropertyIdentifierToDataProperty(Identifier id, DataPropertyExpression dataProperty) {
        DPE.put(id, dataProperty);
    }
    public DataPropertyExpression getDataPropertyExpressionForDataPropertyIdentifier(Identifier id) {
        return DPE.get(id);
    }
    public void mapAnnotationPropertyIdentifierToAnnotationProperty(Identifier id, AnnotationPropertyExpression annotationProperty) {
        APE.put(id, annotationProperty);
    }
    public AnnotationPropertyExpression getAnnotationPropertyExpressionForAnnotationPropertyIdentifier(Identifier id) {
        return APE.get(id);
    }
    public void mapIndividualIdentifierToindividual(Identifier id, Individual individual) {
        IND.put(id, individual);
    }
    public Individual getIndividualForIndividualIdentifier(Identifier id) {
        return IND.get(id);
    }
    public void mapDataRangeIdentifierToDataRange(Identifier id, DataRange datatype) {
        DR.put(id, datatype);
    }
    public DataRange getDataRangeForDataRangeIdentifier(Identifier id) {
        return DR.get(id);
    }
    public Individual getIndividual(Identifier id) {
        if (isVariable(id)) return IndividualVariable.create(id.toString());
        else if (isAnonymous(id)) return AnonymousIndividual.create(id.toString());
        return NamedIndividual.create((IRI)id);
    }
    public NamedIndividual getNamedIndividual(Identifier id) {
        if (id instanceof IRI) return NamedIndividual.create((IRI)id);
        if (isVariable(id)) {
            // TODO: error handling
            System.err.println("error");
        } else if (isAnonymous(id)) {
         // TODO: error handling
            System.err.println("error");
        } 
        return null;    
    }
    
    public void addVersionIRI(Identifier versionIRI) {
        versionIRIs.add(versionIRI);
    }
    
    public boolean isAnonymous(Identifier iri) {
        return (iri instanceof AnonymousIndividual);
    }
    public boolean isVariable(Identifier iri) {
        return (iri instanceof Variable);
    }

    public void addAxiom(Axiom axiom) {
        axioms.add(axiom);
    }
    public Set<Axiom> getAxioms() {
        return axioms;
    }
    public Ontology getOntology() {
        return ontology;
    }
    public boolean isOntologyIRI(Identifier iri) {
        return ontologyIRI==iri;
    }
    public void setOntologyIRI(Identifier iri) {
        if (ontologyIRI!=null) throw new RuntimeException("Error: The ontology has more than one IRI. It already has the IRI "+ontologyIRI+" and now it also got "+iri+". ");
        else ontologyIRI=iri;
    }
    public void addImport(Import imported) {
        imports.add(imported);
    }
    public void addReifiedSubject(Identifier subject) {
        RIND.add(subject);
    }
    protected void handleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        // Table 6, add additional declaration triples
        if ((object==Vocabulary.OWL_INVERSE_FUNCTIONAL_PROPERTY || object==Vocabulary.OWL_TRANSITIVE_PROPERTY || object==Vocabulary.OWL_SYMMETRIC_PROPERTY) && predicate==Vocabulary.RDF_TYPE)
            handleStreaming(subject, predicate, Vocabulary.OWL_OBJECT_PROPERTY);
        if (object==Vocabulary.OWL_ONTOLOGY_PROPERTY&&predicate==Vocabulary.RDF_TYPE)
            // Table 6, correct declaration triples
            handleStreaming(subject, predicate, Vocabulary.OWL_ANNOTATION_PROPERTY);
        else {
            // Table 7, only one triple declarations, reified ones are dealt with later
            // axioms not yet created and triples not removed
            // Table 8, collect bnode subjects of reified axioms 
            TriplePredicateHandler handler=streamingByPredicateHandlers.get(predicate);
            if (handler==null) { 
                Map<Identifier,TriplePredicateHandler> handlerMap=streamingByPredicateAndObjectHandlers.get(predicate);
                if (handlerMap!=null) handler=handlerMap.get(object);
            }
            if (handler!=null) handler.handleStreaming(subject, predicate, object);
            else addTriple(subject,predicate,object);
        }
    }
    public void addTriple(Identifier subject, Identifier predicate, Identifier object) {
        Map<Identifier, Set<Identifier>> map;
        if (Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(predicate)) {
            if (Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(object)) {
                map=builtInPredToBuiltInObjToSubjects.get(predicate);
                if (map==null) {
                    map=new HashMap<Identifier, Set<Identifier>>();
                    builtInPredToBuiltInObjToSubjects.put(predicate, map);
                }
                Set<Identifier> subjects=map.get(object);
                if (subjects==null) {
                    subjects=new HashSet<Identifier>();
                    map.put(object,subjects);
                }
                subjects.add(subject);
            } else {
                map=builtInPredToSubToObjects.get(predicate);
                if (map==null) {
                    map=new HashMap<Identifier, Set<Identifier>>();
                    builtInPredToSubToObjects.put(predicate, map);
                }
                Set<Identifier> objects=map.get(subject);
                if (objects==null) {
                    objects=new HashSet<Identifier>();
                    map.put(subject,objects);
                }
                objects.add(object);
            }
        } else {
            map=subjToPredToObjects.get(subject);
            if (map==null) {
                map=new HashMap<Identifier, Set<Identifier>>();
                subjToPredToObjects.put(subject, map);
            }
            Set<Identifier> objects=map.get(predicate);
            if (objects==null) {
                objects=new HashSet<Identifier>();
                map.put(predicate,objects);
            }
            objects.add(object);
        }
    }
    public void removeTriple(Identifier subject, Identifier predicate, Identifier object) {
        Map<Identifier, Set<Identifier>> map;
        if (Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(predicate)) {
            if (Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(object)) {
                map=builtInPredToBuiltInObjToSubjects.get(predicate);
                if (map!=null) {
                    Set<Identifier> subjects=map.get(object);
                    if (subjects!=null) {
                        subjects.remove(subject);
                        if (subjects.isEmpty()) {
                            map.remove(object);
                            if (map.isEmpty())
                                builtInPredToBuiltInObjToSubjects.remove(predicate);
                        }
                    }
                }
            } else {
                map=builtInPredToSubToObjects.get(predicate);
                if (map!=null) {
                    Set<Identifier> objects=map.get(subject);
                    if (objects!=null) {
                        objects.remove(object);
                        if (objects.isEmpty()) {
                            map.remove(subject);
                            if (map.isEmpty()) 
                                builtInPredToSubToObjects.remove(predicate);
                        }
                    }
                }
            }
        } else {
            map=subjToPredToObjects.get(subject);
            if (map!=null) {
                Set<Identifier> objects=map.get(predicate);
                if (objects!=null) {
                    objects.remove(object);
                    if (objects.isEmpty()) {
                        map.remove(predicate);
                        if (map.isEmpty())
                            subjToPredToObjects.remove(subject);
                    }
                }
            }
        }
    }
    public void handleEnd() {
        //if (ontologyIRI==null) throw new RuntimeException("The ontology did not have an ontology IRI, i.e., it is missing a triple of the form: ontologyIRI rdf:type owl:Ontology . ");
        checkVersionIRIIsForOntologyIRI(); // Table 4
        checkImportsOnlyForOntologyIRI(); // Table 4
        // TODO: Check what happens if two ontology IRIs are given, but the forbidden triples are not fully matched.  
        checkOnytologyIRIIsNeverObject(); // Table 4
        removeOWL1DoubleTypes(); // Table 5
        addReifiedDeclarations(); // Table 7
        parseAnnotations(); // Table 10
        parseObjectProperties(); // Table 11
        parseDataRanges(); // Table 12
        parseClassExpressions(); // Table 13
        parseOWL1DataRanges(); // Table 14
        parseOWL1ClassExpressions(); // Table 15
        parseAxioms();
    }
    protected void parseAxioms() {
        parseAnnotatedAxioms();
        // now all annotated ones are consumed, and we can translate the non-annotated ones
        parseNonAnnotatedAxioms();
    }
    protected void parseNonAnnotatedAxioms() {
        TriplePredicateHandler handler=null;
        for (Identifier predicate : builtInPredToBuiltInObjToSubjects.keySet()) {
            Map<Identifier,Set<Identifier>> objToSubjects=builtInPredToBuiltInObjToSubjects.get(predicate);
            for (Identifier object : new HashSet<Identifier>(objToSubjects.keySet())) {
                Map<Identifier,TriplePredicateHandler> objToHandler=byPredicateAndObjectHandlers.get(predicate);
                if (objToHandler!=null) {
                    handler=objToHandler.get(object);
                    if (handler!=null) {
                        for (Identifier subject : new HashSet<Identifier>(objToSubjects.get(object))) {
                            handler.handleTriple(subject, predicate, object);
                            removeTriple(subject, predicate, object);
                        }
                    }
                }
            }
        }
        for (Identifier predicate : builtInPredToSubToObjects.keySet()) {
            Map<Identifier,Set<Identifier>> subjToObjects=builtInPredToSubToObjects.get(predicate);
            handler=byPredicateHandlers.get(predicate);
            if (handler!=null) {
                for (Identifier subject : subjToObjects.keySet()) {
                    for (Identifier object : subjToObjects.get(subject)) {
                        handler.handleTriple(subject, predicate, object);
                        removeTriple(subject, predicate, object);
                    }
                }
           }
        }
    }
    protected void parseAnnotatedAxioms() {
        Map<Identifier,Set<Identifier>> objToSubjects=builtInPredToBuiltInObjToSubjects.get(Vocabulary.RDF_TYPE);
        if (objToSubjects!=null) {
            Set<Identifier> subjects=objToSubjects.get(Vocabulary.OWL_AXIOM);
            if (subjects!=null) {
                for (Identifier subject : new HashSet<Identifier>(subjects)) {
                    if (isAnonymous(subject)) {
                        Identifier[] triple=getReifiedTriple(subject, Vocabulary.OWL_AXIOM);
                        if (triple!=null) {
                            removeTriple(subject, Vocabulary.RDF_TYPE, Vocabulary.OWL_AXIOM);
                            removeTriple(subject, Vocabulary.OWL_ANNOTATED_SOURCE, triple[0]);
                            removeTriple(subject, Vocabulary.OWL_ANNOTATED_PROPERTY, triple[1]);
                            removeTriple(subject, Vocabulary.OWL_ANNOTATED_TARGET, triple[2]);
                            Set<Annotation> axiomAnnotations=ANN.get(subject);
                            parseAxiom(triple[0], triple[1], triple[2], axiomAnnotations);
                        }
                    }
                }
            }
        }
    }
    protected void parseAxiom(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> axiomAnnotations) {
        TriplePredicateHandler handler=byPredicateHandlers.get(predicate);
        if (handler==null) { 
            Map<Identifier,TriplePredicateHandler> handlerMap=byPredicateAndObjectHandlers.get(predicate);
            if (handlerMap!=null) handler=handlerMap.get(object);
        }
        if (handler!=null) {
            handler.handleTriple(subject, predicate, object, axiomAnnotations);
            removeTriple(subject, predicate, object);
        }
    }
    protected void parseOWL1DataRanges() {
        // Table 14
        // TODO: implement
    }
    protected void parseOWL1ClassExpressions() {
        // Table 15
        // TODO: implement
    }
    protected void parseClassExpressions() {
        Map<Identifier,Set<Identifier>> objToSubjects=builtInPredToBuiltInObjToSubjects.get(Vocabulary.RDF_TYPE);
        if (objToSubjects!=null) {
            Set<Identifier> identifiers=objToSubjects.get(Vocabulary.OWL_CLASS);
            if (identifiers!=null) {
                for (Identifier subject : new HashSet<Identifier>(identifiers)) {
                    if (isAnonymous(subject)) {
                        translateClassExpression(subject);
                        removeTriple(subject, Vocabulary.RDF_TYPE, Vocabulary.OWL_CLASS);
                    }
                }
            }
            
            identifiers=objToSubjects.get(Vocabulary.OWL_RESTRICTION);
            if (identifiers!=null) {
                for (Identifier subject : new HashSet<Identifier>(identifiers)) {
                    if (isAnonymous(subject)) {
                        translateClassExpression(subject);
                        removeTriple(subject, Vocabulary.RDF_TYPE, Vocabulary.OWL_RESTRICTION);
                    }
                }
            }
        }
    } 
    public void translateClassExpression(Identifier subject) {
        if (CE.get(subject)==null) {
            boolean isTranslated=false;
            for (Identifier predicate : classExpressionPredicateHandlers.keySet()) {
                if (builtInPredToSubToObjects.containsKey(predicate) && builtInPredToSubToObjects.get(predicate).containsKey(subject)) {
                    isTranslated=true;
                    translateClassExpression(subject, predicate);
                    break;
                }
            }
            if (!isTranslated)  
                throw new RuntimeException("Error: Could not translate class expression: "+subject+". Maybe a class declaration is missing? ");
        }
    }
    public void translateClassExpression(Identifier subject, Identifier predicate) {
        Identifier object=getObject(subject, predicate, false);
        if (object!=null) {
            removeTriple(subject, predicate, object);
            classExpressionPredicateHandlers.get(predicate).handleTriple(subject, predicate, object);
        } else {
            // TODO: error handling
            System.err.println("error");
        }
    }
    public List<ObjectPropertyExpression> translateToObjectPropertyExpressionList(Identifier listMainNode) {
        return objectPropertyListTranslator.translateToList(listMainNode);
    }
    public Set<ClassExpression> translateToClassExpressionSet(Identifier listMainNode) {
        return classExpressionListTranslator.translateToSet(listMainNode);
    }
    public Set<Individual> translateToIndividualSet(Identifier listMainNode) {
        return individualListTranslator.translateToSet(listMainNode);
    }
    protected void parseDataRanges() {
        if (builtInPredToBuiltInObjToSubjects.containsKey(Vocabulary.RDF_TYPE)) {
            Set<Identifier> datatypeIdentifiers=builtInPredToBuiltInObjToSubjects.get(Vocabulary.RDF_TYPE).get(Vocabulary.RDFS_DATATYPE);
            if (datatypeIdentifiers!=null) {
                for (Identifier subject : new HashSet<Identifier>(datatypeIdentifiers)) {
                    if (isAnonymous(subject)) {
                        translateDataRange((AnonymousIndividual)subject);
                        removeTriple(subject, Vocabulary.RDF_TYPE, Vocabulary.RDFS_DATATYPE);
                    }
                }
            }
        }
    }   
    public void translateDataRange(Identifier subject) {
        if (DR.get(subject)==null) {
            boolean isTranslated=false;
            for (Identifier predicate : dataRangePredicateHandlers.keySet()) {
                if (builtInPredToSubToObjects.containsKey(predicate) && builtInPredToSubToObjects.get(predicate).containsKey(subject)) {
                    isTranslated=true;
                    translateDataRange(subject, predicate);
                    break;
                }
            }
            if (!isTranslated)  
                throw new RuntimeException("Error: Could not translate data range: "+subject+". Maybe a declaration for a custom datatype is missing? ");
        }
    }
    public void translateDataRange(Identifier subject, Identifier predicate) {
        Identifier object=builtInPredToSubToObjects.get(predicate).get(subject).iterator().next();
        if (builtInPredToSubToObjects.get(predicate).get(subject).size()==1 && object!=null) {
            removeTriple(subject, predicate, object);
            dataRangePredicateHandlers.get(predicate).handleTriple(subject, predicate, object);
        } else {
            // TODO: error handling
            System.err.println("error");
        }
    }
    public Set<DataRange> translateToDataRangeSet(Identifier listMainNode) {
        return dataRangeListTranslator.translateToSet(listMainNode);
    }
    public Set<Literal> translateToLiteralSet(Identifier listMainNode) {
        return literalListTranslator.translateToSet(listMainNode);
    }
    public Set<FacetRestriction> translateToFacetRestrictionSet(Identifier subject) {
        // subject owl:withRestrictions listMainNode
        Identifier predicate=Vocabulary.OWL_WITH_RESTRICTIONS;
        Set<Identifier> listMainNodes=getObjects(subject, predicate);
        if (listMainNodes!=null && listMainNodes.size()==1) {
            Identifier mainNode=listMainNodes.iterator().next();
            removeTriple(subject, predicate, mainNode);
            return faceRestrictionListTranslator.translateToSet(mainNode);
        } else {
            // TODO: error handling
            System.err.println("error");
            return new HashSet<FacetRestriction>();
        }
    }
    protected void parseObjectProperties() {
        // Table 11
        Map<Identifier,Set<Identifier>> inverses=builtInPredToSubToObjects.get(Vocabulary.OWL_INVERSE_OF);
        if (inverses!=null) {
            for (Identifier subject : new HashSet<Identifier>(inverses.keySet())) {
                if (isAnonymous(subject)) {
                    translateObjectPropertyExpression(subject, inverses);
                } else 
                    throw new RuntimeException("Error: the subject of a triple with predicate owl:inverseOf should be a blank node, but here we have: "+subject); 
            }
            if (builtInPredToSubToObjects.get(Vocabulary.OWL_INVERSE_OF)!=null&&builtInPredToSubToObjects.get(Vocabulary.OWL_INVERSE_OF).isEmpty())
                builtInPredToSubToObjects.remove(Vocabulary.OWL_INVERSE_OF);
        }
    }
    public void translateObjectPropertyExpression(Identifier subject, Map<Identifier,Set<Identifier>> inverses) {
        if (!OPE.containsKey(subject)) {
            Set<Identifier> inverseProperties=inverses.get(subject);
            if (inverseProperties!=null) {
                if (inverseProperties.size()!=1)
                    throw new RuntimeException("A property represented by blank node "+subject+" is the invers of more than one property, which is not allowed. ");
                else {
                    Identifier object=inverseProperties.iterator().next();
                    removeTriple(subject, Vocabulary.OWL_INVERSE_OF, object);
                    translateObjectPropertyExpression(object, inverses);
                    ObjectPropertyExpression inverseOf=getObjectPropertyExpressionForObjectPropertyIdentifier(object);
                    if (inverseOf!=null)
                        OPE.put(subject, ObjectInverseOf.create(inverseOf));
                    else 
                        //TODO: error handling
                        System.err.println("error");
                }
            } else
                throw new RuntimeException("An inverse object property represented by blank node "+subject+" cannot be resolved to an object property expression. ");
        }
    }
    protected void parseAnnotations() {
        // Table 10
        for (Identifier annotationProperty : APE.keySet()) {
            for (Identifier[] subjObj : getSubjectObjectMap(annotationProperty)) {
                // x *:y xlt <-> subjObj[0] annotationProperty subjObj[1]
                if (subjObj[1] instanceof Literal) {
                    Set<Annotation> annotationsForX=ANN.get(subjObj[0]);
                    if (annotationsForX==null) {
                        annotationsForX=new HashSet<Annotation>();
                        ANN.put(subjObj[0], annotationsForX);
                    }
                    annotationsForX.add(Annotation.create(APE.get(annotationProperty), (AnnotationValue)subjObj[1], getAnnotationAnnotations(subjObj[0], annotationProperty, subjObj[1])));
                    removeTriple(subjObj[0], (Identifier)annotationProperty, subjObj[1]);
                }
            }
        }
    }
    protected Set<Annotation> getAnnotations(Identifier subject) {
        Set<Annotation> annotations=ANN.get(subject);
        if (annotations==null) {
            annotations=new HashSet<Annotation>();
            Map<Identifier,Set<Identifier>> predToObjects=getPredicateToObjects(subject);
            if (!predToObjects.isEmpty()) {
                for (Identifier predicate : predToObjects.keySet()) {
                    AnnotationPropertyExpression ape=APE.get(predicate);
                    if (ape!=null) {
                        for (Identifier object : predToObjects.get(predicate)) {
                            if (object instanceof AnnotationValue) {
                                annotations.add(Annotation.create(ape, (AnnotationValue)object, getAnnotationAnnotations(subject,predicate,object)));
                                ANN.put(subject, annotations);
                                removeTriple(subject,predicate,object);
                            }
                        }
                    } else {
                        // TODO: error handling
                        System.err.println("error");
                    }
                }
            }
        }
        return annotations;
    }
    protected Set<Annotation> getAnnotationAnnotations(Identifier subject, Identifier predicate, Identifier object) {
        Set<Annotation> annotationAnnotations=new HashSet<Annotation>();
        Identifier reificationSubject=getReificationSubjectFor(subject, predicate, object, Vocabulary.OWL_ANNOTATION);
        if (reificationSubject!=null) {
            // it has itself annotations
            removeTriple(reificationSubject, Vocabulary.RDF_TYPE, Vocabulary.OWL_ANNOTATION);
            removeTriple(reificationSubject, Vocabulary.OWL_ANNOTATED_SOURCE, subject);
            removeTriple(reificationSubject, Vocabulary.OWL_ANNOTATED_PROPERTY, predicate);
            removeTriple(reificationSubject, Vocabulary.OWL_ANNOTATED_TARGET, object);
            annotationAnnotations.addAll(getAnnotations(reificationSubject));
//            if (containsTripleWithSubjectOrObject(reificationSubject))
//                throw new RuntimeException("The subject of a reified axiom also occures as an object or subject of triples that do not belong to the annotation, which is not allowed: "+reificationSubject);
        }
        return annotationAnnotations;
    }
    public Identifier getFirst(Identifier subject) {
        Map<Identifier,Set<Identifier>> subjToObjects=builtInPredToSubToObjects.get(Vocabulary.RDF_FIRST);
        if (subjToObjects==null)
            throw new RuntimeException("There is no triple with predicate rdf:first, but we have to translate a list with main node: "+subject);
        Set<Identifier> objects=subjToObjects.get(subject);
        if (objects==null || objects.size()!=1) 
            throw new RuntimeException("Error: Could not translate list with main node: "+subject+" because there is no triple with subject "+subject+" and predicate rdf:first or there is more thna one such triple. ");
        
        Identifier object=objects.iterator().next();
        removeTriple(subject, Vocabulary.RDF_FIRST, object);
        return object;
    }
    public Identifier getRest(Identifier subject) {
        Identifier object=null;
        Map<Identifier,Set<Identifier>> subjToObjects=builtInPredToSubToObjects.get(Vocabulary.RDF_REST);
        if (subjToObjects!=null) {
            Set<Identifier> objects=subjToObjects.get(subject);
            if (objects!=null) {
                if (objects.size()!=1) 
                    throw new RuntimeException("Error: Could not translate list with main node: "+subject+" because there is more than one triple with subject "+subject+" and predicate rdf:rest. ");
                object=objects.iterator().next();
            }
        } 
        if (object==null) {
            // maybe rdf:nil?
            Map<Identifier,Set<Identifier>> objToSubjects=builtInPredToBuiltInObjToSubjects.get(Vocabulary.RDF_REST);
            if (objToSubjects!=null && objToSubjects.containsKey(Vocabulary.RDF_NIL) && objToSubjects.get(Vocabulary.RDF_NIL).contains(subject))
                object=Vocabulary.RDF_NIL;
            else 
                throw new RuntimeException("There is no triple with predicate rdf:rest, but we have to translate a list with main node: "+subject);
        }
        removeTriple(subject, Vocabulary.RDF_REST, object);
        return object;
    }
    protected boolean containsTripleWithSubjectOrObject(Identifier id) {
        if (subjToPredToObjects.keySet().contains(id)) return true;
        for (Identifier pred : builtInPredToSubToObjects.keySet()) {
            Map<Identifier,Set<Identifier>> subj2objects=builtInPredToSubToObjects.get(pred);
            if (subj2objects.keySet().contains(id)) return true;
            for (Identifier subj: subj2objects.keySet())
                if (subj2objects.get(subj).contains(id)) return true;
        }
        for (Identifier pred : builtInPredToBuiltInObjToSubjects.keySet()) {
            Map<Identifier,Set<Identifier>> objToSubj=builtInPredToBuiltInObjToSubjects.get(pred);
            if (objToSubj.keySet().contains(id)) return true;
            for (Identifier obj: objToSubj.keySet())
                if (objToSubj.get(obj).contains(id)) return true;
        }
        for (Identifier subj : subjToPredToObjects.keySet()) {
            Map<Identifier,Set<Identifier>> pred2objects=subjToPredToObjects.get(subj);
            for (Identifier pred : pred2objects.keySet()) {
                if (pred2objects.get(pred).contains(id)) return true;
            }
        }
        return false;
    }
    protected Identifier getReificationSubjectFor(Identifier subject, Identifier predicate, Identifier object, Identifier axiomType) {
        Set<Identifier> reificationSubjects=getSubjects(Vocabulary.RDF_TYPE, axiomType);
        for (Identifier reificationSubject : reificationSubjects) {
            if (containsTriple(reificationSubject, Vocabulary.OWL_ANNOTATED_PROPERTY, predicate)
                    && containsTriple(reificationSubject, Vocabulary.OWL_ANNOTATED_TARGET, object)
                    && containsTriple(reificationSubject, Vocabulary.OWL_ANNOTATED_SOURCE, subject)) 
                return reificationSubject;
        }
        return null;
    }
    protected Identifier[] getReifiedTriple(Identifier subject, Identifier axiomType) {
        if (!isAnonymous(subject)) 
            throw new RuntimeException("Only blank nodes can be the subject of reified triples, but here we have: "+subject+" rdf:type "+axiomType.toString());
        Identifier reifiedSubject;
        Identifier reifiedPredicate;
        Identifier reifiedObject;
        Map<Identifier,Set<Identifier>> objToSubjects=builtInPredToBuiltInObjToSubjects.get(Vocabulary.RDF_TYPE);
        if (objToSubjects!=null && objToSubjects.containsKey(axiomType)) {
            if (objToSubjects.get(axiomType).contains(subject)) {
                Set<Identifier> reifiedSubjects=getObjects(subject, Vocabulary.OWL_ANNOTATED_SOURCE);
                if (reifiedSubjects.size()>1) throw new RuntimeException("Error: We got more than one reified subject for a reification axiom: "+subject+" rdf:type "+axiomType);
                else if (reifiedSubjects.size()<1) throw new RuntimeException("Error: We didn't get a reified subject for a reification axiom: "+subject+" rdf:type "+axiomType);
                else reifiedSubject=reifiedSubjects.iterator().next();
                Set<Identifier> reifiedPredicates=getObjects(subject, Vocabulary.OWL_ANNOTATED_PROPERTY);
                if (reifiedPredicates.size()>1) throw new RuntimeException("Error: We got more than one reified predicate for a reification axiom: "+subject+" rdf:type "+axiomType);
                else if (reifiedPredicates.size()<1) throw new RuntimeException("Error: We didn't get a reified predicate for a reification axiom: "+subject+" rdf:type "+axiomType);
                else reifiedPredicate=reifiedPredicates.iterator().next();
                Set<Identifier> reifiedObjects=getObjects(subject, Vocabulary.OWL_ANNOTATED_TARGET);
                if (reifiedObjects.size()>1) throw new RuntimeException("Error: We got more than one reified object for a reification axiom: "+subject+" rdf:type "+axiomType);
                else if (reifiedObjects.size()<1) throw new RuntimeException("Error: We didn't get a reified object for a reification axiom: "+subject+" rdf:type "+axiomType);
                else reifiedObject=reifiedObjects.iterator().next();
                if (isAnonymous(reifiedSubject)) throw new RuntimeException("Error: The reified predicate for the reification axiom: "+subject+" rdf:type "+axiomType+" is anonymous. ");
                if (isAnonymous(reifiedSubject)) throw new RuntimeException("Error: The reified object for the reification axiom: "+subject+" rdf:type "+axiomType+" is anonymous. ");
                return new Identifier[] {reifiedSubject, reifiedPredicate, reifiedObject};
            }
        }
        return null;
    }
    protected Map<Identifier,Set<Identifier>> getPredicateToObjects(Identifier subject) {
        Map<Identifier,Set<Identifier>> predToObjects=new HashMap<Identifier, Set<Identifier>>();
        if (subjToPredToObjects.containsKey(subject)) {
            predToObjects=subjToPredToObjects.get(subject);
        }
        for (Identifier predicate : builtInPredToSubToObjects.keySet()) {
            Map<Identifier,Set<Identifier>> subjToObjects=builtInPredToSubToObjects.get(predicate);
            Set<Identifier> objects=subjToObjects.get(subject);
            if (objects!=null) {
                Set<Identifier> objs=predToObjects.get(predicate);
                if (objs==null)
                    predToObjects.put(predicate, objects);
                else
                    objs.addAll(objects);
            }
        }
        for (Identifier predicate : builtInPredToBuiltInObjToSubjects.keySet()) {
            Map<Identifier,Set<Identifier>> objToSubjects=builtInPredToBuiltInObjToSubjects.get(predicate);
            for (Identifier obj : objToSubjects.keySet()) {
                Set<Identifier> subjects=objToSubjects.get(subject);
                if (subjects!=null && subjects.contains(subject)) {
                    Set<Identifier> objs=predToObjects.get(predicate);
                    if (objs==null) {
                        objs=new HashSet<Identifier>();
                        predToObjects.put(predicate, objs);
                    } 
                    objs.add(obj);
                }
            }
        }
        return predToObjects;
    }
    protected Set<Identifier[]> getSubjectObjectMap(Identifier predicate) {
        Set<Identifier[]> map=new HashSet<Identifier[]>();
        Identifier[] subjObj;
        if (Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(predicate)) {
            if (builtInPredToSubToObjects.containsKey(predicate)) {
                Map<Identifier,Set<Identifier>> subjToObjects=builtInPredToSubToObjects.get(predicate);
                for (Identifier subject : subjToObjects.keySet()) {
                    for (Identifier object : subjToObjects.get(subject)) {
                        subjObj=new Identifier[2]; 
                        subjObj[0]=subject;
                        subjObj[1]=object;
                        map.add(subjObj);
                    }
                }
            }
            if (builtInPredToBuiltInObjToSubjects.containsKey(predicate)) {
                Map<Identifier,Set<Identifier>> builtInObjectToSubjects=builtInPredToBuiltInObjToSubjects.get(predicate);
                for (Identifier builtInObject : builtInObjectToSubjects.keySet()) {
                    for (Identifier subject : builtInObjectToSubjects.get(builtInObject)) {
                        subjObj=new Identifier[2]; 
                        subjObj[0]=subject;
                        subjObj[1]=builtInObject;
                        map.add(subjObj);
                    }
                }
            }
        } else {
            for (Identifier subject : subjToPredToObjects.keySet()) {
                Map<Identifier,Set<Identifier>> predToObjects=subjToPredToObjects.get(subject);
                if (predToObjects.containsKey(predicate)) {
                    for (Identifier object : predToObjects.get(predicate)) {
                        subjObj=new Identifier[2]; 
                        subjObj[0]=subject;
                        subjObj[1]=object;
                        map.add(subjObj);
                    }
                }
            }
        }
        return map;
    }
    public Set<Identifier> getObjects(Identifier subject, Identifier predicate) {
        Set<Identifier> objects=new HashSet<Identifier>();
        if (Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(predicate)) {
            if (builtInPredToSubToObjects.containsKey(predicate)) {
                Map<Identifier,Set<Identifier>> subjToObjects=builtInPredToSubToObjects.get(predicate);
                if (subjToObjects.containsKey(subject)) objects.addAll(subjToObjects.get(subject));
            }
            if (builtInPredToBuiltInObjToSubjects.containsKey(predicate)) {
                Map<Identifier,Set<Identifier>> builtInObjectToSubjects=builtInPredToBuiltInObjToSubjects.get(predicate);
                for (Identifier builtInObject : builtInObjectToSubjects.keySet()) {
                    if (builtInObjectToSubjects.get(builtInObject).contains(subject)) {
                        objects.add(builtInObject);
                    }
                }
            }
            return objects;
        } else {
            Map<Identifier,Set<Identifier>> predToObjects=subjToPredToObjects.get(subject);
            if (predToObjects!=null&&predToObjects.containsKey(predicate))
                return predToObjects.get(predicate);
            else 
                return objects;
        }
    }
    public Identifier getObject(Identifier subject, Identifier predicate,boolean consume) {
        Set<Identifier> objects=getObjects(subject, predicate);
        if (objects.size()==1) {
            Identifier object=objects.iterator().next();
            if (consume) removeTriple(subject, predicate, object);
            return object;
        } else {
            // TODO: error handling
            System.err.println("error");
            return null;
        }
    }
    public Literal getLiteralObject(Identifier subject, Identifier predicate) {
        Set<Literal> literals=getLiteralObjects(subject,predicate);
        if (literals.isEmpty())
            return null;
        else if (literals.size()==1) {
            Literal literal=literals.iterator().next();
            removeTriple(subject, predicate, literal);
            return literal;
        } else { 
            // TODO: error handling
            System.err.println("error");
            return null;
        }
    }
    public Set<Literal> getLiteralObjects(Identifier subject, Identifier predicate) {
        Set<Identifier> objects=getObjects(subject,predicate);
        Set<Literal> literals=new HashSet<Literal>();
        for (Identifier object : objects) {
            if (object instanceof Literal) 
                literals.add((Literal)object);
            else {
                // TODO: error handling
                System.err.println("error");
            }
        }
        return literals;
    }
    protected Set<Identifier> getSubjects(Identifier predicate, Identifier object) {
        Set<Identifier> subjects=new HashSet<Identifier>();
        if (Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(predicate)) {
            if (Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(object) && builtInPredToBuiltInObjToSubjects.containsKey(predicate)) {
                Map<Identifier,Set<Identifier>> builtInObjectToSubjects=builtInPredToBuiltInObjToSubjects.get(predicate);
                if (builtInObjectToSubjects.containsKey(object)) 
                    return builtInObjectToSubjects.get(object);
            } else if (builtInPredToSubToObjects.containsKey(predicate)) {
                Map<Identifier,Set<Identifier>> subjectsToObjects=builtInPredToSubToObjects.get(predicate);
                for (Identifier subject : subjectsToObjects.keySet()) {
                    if (subjectsToObjects.get(subject).contains(object)) 
                        subjects.add(subject);
                }
            }
        } else {
            for (Identifier subject : subjToPredToObjects.keySet()) {
                Map<Identifier,Set<Identifier>> predToObjects=subjToPredToObjects.get(subject);
                Set<Identifier> objects=predToObjects.get(predicate);
                if (objects!=null&&objects.contains(object)) 
                    subjects.add(subject);
            }
        }
        return subjects;
    }
    protected boolean containsTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(predicate)) {
            if (Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(object) && builtInPredToBuiltInObjToSubjects.containsKey(predicate)) {
                Map<Identifier,Set<Identifier>> builtInObjectToSubjects=builtInPredToBuiltInObjToSubjects.get(predicate);
                if (builtInObjectToSubjects.containsKey(object)) 
                    return builtInObjectToSubjects.get(object).contains(subject);
            } else if (builtInPredToSubToObjects.containsKey(predicate)) {
                Map<Identifier,Set<Identifier>> subjectsToObjects=builtInPredToSubToObjects.get(predicate);
                if (subjectsToObjects.containsKey(subject))
                     return subjectsToObjects.get(subject).contains(object);
            }
        } else if (subjToPredToObjects.containsKey(subject)) {
            Map<Identifier,Set<Identifier>> predicatesToObjects=subjToPredToObjects.get(subject);
            if (predicatesToObjects.containsKey(predicate))
                 return predicatesToObjects.get(predicate).contains(object);
        }
        return false;
    }
    protected void addReifiedDeclarations() {
        Map<Identifier,Set<Identifier>> objToSubjects=builtInPredToBuiltInObjToSubjects.get(Vocabulary.RDF_TYPE);
        if (objToSubjects!=null) {
            Set<Identifier> subjects=objToSubjects.get(Vocabulary.OWL_AXIOM);
            if (subjects!=null) {
                for (Identifier subject : subjects) {
                    if (isAnonymous(subject)) {
                        if (builtInPredToBuiltInObjToSubjects.containsKey(Vocabulary.OWL_ANNOTATED_PROPERTY)) {
                            Map<Identifier,Set<Identifier>> apObjToSubjects=builtInPredToBuiltInObjToSubjects.get(Vocabulary.OWL_ANNOTATED_PROPERTY);
                            for (Identifier reifiedPredicate : apObjToSubjects.keySet()) {
                                if (apObjToSubjects.get(reifiedPredicate).contains(subject)) {
                                    Map<Identifier,TriplePredicateHandler> handlerMap=streamingByPredicateAndObjectHandlers.get(reifiedPredicate);
                                    if (handlerMap!=null && builtInPredToBuiltInObjToSubjects.containsKey(Vocabulary.OWL_ANNOTATED_TARGET)) {
                                        Map<Identifier,Set<Identifier>> annotatedTargetToDecl=builtInPredToBuiltInObjToSubjects.get(Vocabulary.OWL_ANNOTATED_TARGET);
                                        for (Identifier reifiedObject : annotatedTargetToDecl.keySet()) {
                                            TriplePredicateHandler handler=handlerMap.get(reifiedObject);
                                            if (handler!=null && annotatedTargetToDecl.get(reifiedObject).contains(subject) && builtInPredToSubToObjects.containsKey(Vocabulary.OWL_ANNOTATED_SOURCE) && builtInPredToSubToObjects.get(Vocabulary.OWL_ANNOTATED_SOURCE).containsKey(subject)) {
                                                for (Identifier reifiedSubject : builtInPredToSubToObjects.get(Vocabulary.OWL_ANNOTATED_SOURCE).get(subject))  
                                                    handler.handleTriple(reifiedSubject, reifiedPredicate, reifiedObject);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    protected void checkOnytologyIRIIsNeverObject() {
        for (Identifier pred : builtInPredToSubToObjects.keySet()) {
            Map<Identifier,Set<Identifier>> subj2objects=builtInPredToSubToObjects.get(pred);
            for (Identifier subj : subj2objects.keySet()) {
                for (Identifier obj : subj2objects.get(subj)) {
                    if (obj==ontologyIRI) 
                        throw new RuntimeException("The ontology IRI "+ontologyIRI+" cannot be used as an object of a triple, but here we have: "+subj+" "+pred+" "+obj+" . ");
                }
            }
        } 
        for (Identifier subj : subjToPredToObjects.keySet()) {
            Map<Identifier,Set<Identifier>> pred2objects=subjToPredToObjects.get(subj);
            for (Identifier pred : pred2objects.keySet()) {
                for (Identifier obj : pred2objects.get(pred)) {
                    if (obj==ontologyIRI) 
                        throw new RuntimeException("The ontology IRI "+ontologyIRI+" cannot be used as an object of a triple, but here we have: "+subj+" "+pred+" "+obj+" . ");
                }
            }
        }
    }
    protected void checkImportsOnlyForOntologyIRI() {
        Map<Identifier, Set<Identifier>> map=builtInPredToSubToObjects.get(Vocabulary.OWL_IMPORTS);
        if (map!=null&&!map.isEmpty()) {            
            for (Identifier iri : map.keySet()) {
                if (iri!=ontologyIRI) 
                    throw new RuntimeException("The ontology has IRI "+ontologyIRI+" but an import triple uses a different subject, which is not allowed: "+iri+" owl:imports "+map.get(iri).iterator().next()+". and "+ontologyIRI+" rdf:type owl:ontology.");
            }
        }
    }
    protected void checkVersionIRIIsForOntologyIRI() {
        if (!versionIRIs.isEmpty()) {
            if (isAnonymous(ontologyIRI)) {
                StringBuffer buffer=new StringBuffer();
                buffer.append("The parsed ontology does not have an ontology IRI or a blank node ontology iri, but it has version IRIs, which is not allowed. Version IRIs are from the triples: ");
                buffer.append(LB);
                Map<Identifier, Set<Identifier>> map=builtInPredToSubToObjects.get(Vocabulary.OWL_VERSION_IRI);
                for (Identifier ontIRI : map.keySet()) {
                    for (Identifier iri : map.get(ontIRI)) {
                        buffer.append(ontIRI);
                        buffer.append(" owl:versionInfo ");
                        buffer.append(iri);
                        buffer.append(LB);
                    }
                }
                throw new RuntimeException(buffer.toString());
            } else {
                Map<Identifier, Set<Identifier>> map=builtInPredToSubToObjects.get(Vocabulary.OWL_VERSION_IRI);
                for (Identifier ontIRI : map.keySet()) {
                    if (ontIRI!=ontologyIRI) 
                        throw new RuntimeException("The ontology has IRI "+ontologyIRI+" but a versionIRI triple uses a different subject, which is not allowed: "+ontIRI+" owl:versionIRI "+map.get(ontIRI).iterator().next());
                }
            }
        }
    }
    protected void removeOWL1DoubleTypes() {
        // Table 5
        Map<Identifier, Set<Identifier>> obj2subjects=builtInPredToBuiltInObjToSubjects.get(Vocabulary.RDF_TYPE);
        if (obj2subjects!=null) {
            Set<Identifier> subjects=obj2subjects.get(Vocabulary.RDFS_CLASS);
            if (subjects!=null) {
                Set<Identifier> subjectsOfRedundantTriples=new HashSet<Identifier>();
                for (Identifier subj : subjects) {
                    Set<Identifier> owl2type=obj2subjects.get(Vocabulary.OWL_CLASS);
                    if (owl2type!=null&&owl2type.contains(subj)) {
                        subjectsOfRedundantTriples.add(subj);
                        break;
                    }
                    owl2type=obj2subjects.get(Vocabulary.RDFS_DATATYPE);
                    if (owl2type!=null&&owl2type.contains(subj)) {
                        subjectsOfRedundantTriples.add(subj);
                        break;
                    }
                    owl2type=obj2subjects.get(Vocabulary.OWL_DATA_RANGE);
                    if (owl2type!=null&&owl2type.contains(subj)) {
                        subjectsOfRedundantTriples.add(subj);
                        break;
                    }
                    owl2type=obj2subjects.get(Vocabulary.OWL_RESTRICTION);
                    if (owl2type!=null&&owl2type.contains(subj)) {
                        subjectsOfRedundantTriples.add(subj);
                        break;
                    }
                    owl2type=obj2subjects.get(Vocabulary.RDFS_DATATYPE);
                    if (owl2type!=null&&owl2type.contains(subj)) {
                        subjectsOfRedundantTriples.add(subj);
                        break;
                    }
                }
                subjects.removeAll(subjectsOfRedundantTriples);
                if (subjects.isEmpty()) obj2subjects.remove(Vocabulary.RDFS_CLASS);
                if (obj2subjects.isEmpty()) builtInPredToBuiltInObjToSubjects.remove(Vocabulary.RDF_TYPE);
            }
            subjects=obj2subjects.get(Vocabulary.OWL_CLASS);
            if (subjects!=null) {
                Set<Identifier> subjectsOfRedundantTriples=new HashSet<Identifier>();
                for (Identifier subj : subjects) {
                    Set<Identifier> owl2type=obj2subjects.get(Vocabulary.OWL_RESTRICTION);
                    if (owl2type!=null&&owl2type.contains(subj)) {
                        subjectsOfRedundantTriples.add(subj);
                    }
                }
                subjects.removeAll(subjectsOfRedundantTriples);
                if (subjects.isEmpty()) obj2subjects.remove(Vocabulary.OWL_CLASS);
                if (obj2subjects.isEmpty()) builtInPredToBuiltInObjToSubjects.remove(Vocabulary.RDF_TYPE);
            }
            subjects=obj2subjects.get(Vocabulary.RDF_PROPERTY);
            if (subjects!=null) {
                Set<Identifier> subjectsOfRedundantTriples=new HashSet<Identifier>();
                for (Identifier subj : subjects) {
                    Set<Identifier> owl2type=obj2subjects.get(Vocabulary.OWL_OBJECT_PROPERTY);
                    if (owl2type!=null&&owl2type.contains(subj)) {
                        subjectsOfRedundantTriples.add(subj);
                        break;
                    }
                    owl2type=obj2subjects.get(Vocabulary.OWL_FUNCTIONAL_PROPERTY);
                    if (owl2type!=null&&owl2type.contains(subj)) {
                        subjectsOfRedundantTriples.add(subj);
                        break;
                    }
                    owl2type=obj2subjects.get(Vocabulary.OWL_INVERSE_FUNCTIONAL_PROPERTY);
                    if (owl2type!=null&&owl2type.contains(subj)) {
                        subjectsOfRedundantTriples.add(subj);
                        break;
                    }
                    owl2type=obj2subjects.get(Vocabulary.OWL_TRANSITIVE_PROPERTY);
                    if (owl2type!=null&&owl2type.contains(subj)) {
                        subjectsOfRedundantTriples.add(subj);
                        break;
                    }
                    owl2type=obj2subjects.get(Vocabulary.OWL_DATA_PROPERTY);
                    if (owl2type!=null&&owl2type.contains(subj)) {
                        subjectsOfRedundantTriples.add(subj);
                        break;
                    }
                    owl2type=obj2subjects.get(Vocabulary.OWL_ANNOTATION_PROPERTY);
                    if (owl2type!=null&&owl2type.contains(subj)) {
                        subjectsOfRedundantTriples.add(subj);
                        break;
                    }
                    owl2type=obj2subjects.get(Vocabulary.OWL_ONTOLOGY_PROPERTY);
                    if (owl2type!=null&&owl2type.contains(subj)) {
                        subjectsOfRedundantTriples.add(subj);
                        break;
                    }
                }
                subjects.removeAll(subjectsOfRedundantTriples);
                if (subjects.isEmpty()) obj2subjects.remove(Vocabulary.RDF_PROPERTY);
                if (obj2subjects.isEmpty()) builtInPredToBuiltInObjToSubjects.remove(Vocabulary.RDF_TYPE);
            }
            subjects=obj2subjects.get(Vocabulary.RDF_LIST);
            Map<Identifier, Set<Identifier>> firstSubjObjects=builtInPredToSubToObjects.get(Vocabulary.RDF_FIRST);
            Map<Identifier, Set<Identifier>> restSubjObjects=builtInPredToSubToObjects.get(Vocabulary.RDF_REST);
            Set<Identifier> restNilSubjects=new HashSet<Identifier>();
            if (builtInPredToBuiltInObjToSubjects.containsKey(Vocabulary.RDF_REST)) {
                Map<Identifier, Set<Identifier>> restObjSubjects=builtInPredToBuiltInObjToSubjects.get(Vocabulary.RDF_REST);
                if (restObjSubjects!=null && restObjSubjects.containsKey(Vocabulary.RDF_NIL)) 
                    restNilSubjects=restObjSubjects.get(Vocabulary.RDF_NIL);
            }
            if (subjects!=null) {
                Set<Identifier> subjectsOfRedundantTriples=new HashSet<Identifier>();
                for (Identifier subj : subjects) {
                    if (firstSubjObjects.containsKey(subj) && (restSubjObjects.containsKey(subj) || restNilSubjects.contains(subj))) 
                        subjectsOfRedundantTriples.add(subj);
                }
                subjects.removeAll(subjectsOfRedundantTriples);
                if (subjects.isEmpty()) obj2subjects.remove(Vocabulary.RDF_LIST);
                if (obj2subjects.isEmpty()) builtInPredToBuiltInObjToSubjects.remove(Vocabulary.RDF_TYPE);
            }
        }
    }
    public boolean allTriplesConsumed() {
        return builtInPredToBuiltInObjToSubjects.isEmpty() && builtInPredToSubToObjects.isEmpty() && subjToPredToObjects.isEmpty();
    }
}