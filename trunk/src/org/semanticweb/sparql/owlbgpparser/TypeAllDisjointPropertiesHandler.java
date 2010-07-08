package org.semanticweb.sparql.owlbgpparser;

import java.util.HashSet;
import java.util.List;

import org.semanticweb.sparql.owlbgp.model.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.DisjointDataProperties;
import org.semanticweb.sparql.owlbgp.model.DisjointObjectProperties;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public class TypeAllDisjointPropertiesHandler extends BuiltInTypeHandler {

    public TypeAllDisjointPropertiesHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_ALL_DISJOINT_PROPERTIES.getIRI());
    }

    public void handleTriple(String subject, String predicate, String object) {
        consumeTriple(subject, predicate, object);
        String listNode = consumer.getResourceObject(subject, Vocabulary.OWL_MEMBERS.getIRI(), true);
        if (getConsumer().isObjectPropertyOnly(getConsumer().getFirstResource(listNode, false))) {
            translateAndSetPendingAnnotations(subject);
            List<ObjectPropertyExpression> props=consumer.translateToObjectPropertyList(listNode);
            consumer.addAxiom(DisjointObjectProperties.create(new HashSet<ObjectPropertyExpression>(props)));
        } else {
            translateAndSetPendingAnnotations(subject);
            List<DataPropertyExpression> props = consumer.translateToDataPropertyList(listNode);
            consumer.addAxiom(DisjointDataProperties.create(new HashSet<DataPropertyExpression>(props)));
        }
    }
    protected void translateAndSetPendingAnnotations(String subject) {
        getConsumer().translateAnnotations(subject);
    }
    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return false;
    }
}
