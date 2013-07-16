// The MIT License
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

/*
 * Created on Dec 10, 2004
 */
package impl.jena;

import impl.owl.CombinedOWLConverter;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.mindswap.exceptions.CastingException;
import org.mindswap.owl.OWLAnnotationProperty;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataType;
import org.mindswap.owl.OWLEntity;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLObjectConverter;
import org.mindswap.owl.OWLObjectConverterRegistry;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLProperty;
import org.mindswap.owl.OWLType;

import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
class OWLConverters
{
	static final void registerConverters(final OWLObjectConverterRegistry registry)
	{
		final AnnotationPropertyConverter annPropConverter = new AnnotationPropertyConverter();
		final ObjectPropertyConverter objPropConverter = new ObjectPropertyConverter();
		final DataPropertyConverter dataPropConverter = new DataPropertyConverter();

		final List<OWLObjectConverter<? extends OWLProperty>> pCs =
			new ArrayList<OWLObjectConverter<? extends OWLProperty>>(3);
		pCs.add(objPropConverter);
		pCs.add(dataPropConverter);
		pCs.add(annPropConverter);
		final OWLObjectConverter<OWLProperty> propConverter = new CombinedOWLConverter<OWLProperty>(OWLProperty.class, pCs);

		final ClassConverter classConverter = new ClassConverter();
		final DataTypeConverter dataTypeConverter = new DataTypeConverter();

		final List<OWLObjectConverter<? extends OWLType>> tCs =
			new ArrayList<OWLObjectConverter<? extends OWLType>>(2);
		tCs.add(classConverter);
		tCs.add(dataTypeConverter);
		final OWLObjectConverter<OWLType> typeConverter = new CombinedOWLConverter<OWLType>(OWLType.class, tCs);

		final IndividualConverter indConverter = new IndividualConverter();

		final List<OWLObjectConverter<? extends OWLEntity>> eCs =
			new ArrayList<OWLObjectConverter<? extends OWLEntity>>(5);
		eCs.add(classConverter);
//		eCs.add(dataTypeConverter);
		eCs.add(objPropConverter);
		eCs.add(dataPropConverter);
		eCs.add(indConverter);
		eCs.add(annPropConverter);
		final OWLObjectConverter<OWLEntity> entityConverter = new CombinedOWLConverter<OWLEntity>(OWLEntity.class, eCs);

		registry.registerConverter(OWLIndividual.class, indConverter);

		registry.registerConverter(OWLAnnotationProperty.class, annPropConverter);
		registry.registerConverter(OWLObjectProperty.class, objPropConverter);
		registry.registerConverter(OWLDataProperty.class, dataPropConverter);
		registry.registerConverter(OWLProperty.class, propConverter);

		registry.registerConverter(OWLClass.class, classConverter);
		registry.registerConverter(OWLDataType.class, dataTypeConverter);
		registry.registerConverter(OWLType.class, typeConverter);

		registry.registerConverter(OWLEntity.class, entityConverter);
	}

	static final class AnnotationPropertyConverter implements OWLObjectConverter<OWLAnnotationProperty>
	{
		/* @see org.mindswap.owl.OWLObjectConverter#canCast(org.mindswap.owl.OWLObject, boolean) */
		public boolean canCast(final OWLObject source, final boolean strictConversion)
		{
			if (source instanceof OWLAnnotationProperty) return true;

			if (source instanceof OWLEntityImpl<?>)
			{
				final OWLEntityImpl<?> entity = (OWLEntityImpl<?>) source;
				final Resource res = entity.getImplementation();
				return !strictConversion || res.canAs(AnnotationProperty.class);
			}

			return false;
		}

		/* @see org.mindswap.owl.OWLObjectConverter#cast(org.mindswap.owl.OWLObject, boolean) */
		public OWLAnnotationProperty cast(final OWLObject source, final boolean strictConversion)
		{
			if (source instanceof OWLAnnotationProperty) return (OWLAnnotationProperty) source; // shortcut --> cast is superfluous

			if (canCast(source, strictConversion))
			{
				final OWLEntityImpl<?> entity = (OWLEntityImpl<?>) source;
				final Resource res = entity.getImplementation();
				final AnnotationProperty prop = res.as(AnnotationProperty.class);
				return new OWLAnnotationPropertyImpl(entity.getOntology(), prop);
			}

			throw CastingException.create(source , OWLAnnotationProperty.class);
		}

		/* @see java.lang.Object#toString() */
		@Override
		public String toString()
		{
			return "Converter " + OWLEntity.class.getSimpleName() + " -> " + OWLAnnotationProperty.class.getSimpleName();
		}
	}

	static final class ObjectPropertyConverter implements OWLObjectConverter<OWLObjectProperty>
	{
		/* @see org.mindswap.owl.OWLObjectConverter#canCast(org.mindswap.owl.OWLObject, boolean) */
		public boolean canCast(final OWLObject object, final boolean strictConversion)
		{
			if (object instanceof OWLObjectProperty) return true;

			if (object instanceof OWLEntityImpl<?>)
			{
				final OWLEntityImpl<?> entity = (OWLEntityImpl<?>) object;
				final Resource res = entity.getImplementation();
				return !strictConversion || res.canAs(ObjectProperty.class);
			}

			return false;
		}

		/* @see org.mindswap.owl.OWLObjectConverter#cast(org.mindswap.owl.OWLObject, boolean) */
		public OWLObjectProperty cast(final OWLObject object, final boolean strictConversion)
		{
			if (object instanceof OWLObjectProperty) return (OWLObjectProperty) object; // shortcut --> cast is superfluous

			if (canCast(object, strictConversion))
			{
				final OWLEntityImpl<?> entity = (OWLEntityImpl<?>) object;
				final Resource res = entity.getImplementation();
				final ObjectProperty prop = res.as(ObjectProperty.class);
				return new OWLObjectPropertyImpl(entity.getOntology(), prop);
			}

			throw CastingException.create(object , OWLObjectProperty.class);
		}

		/* @see java.lang.Object#toString() */
		@Override
		public String toString()
		{
			return "Converter " + OWLEntity.class.getSimpleName() + " -> " + OWLObjectProperty.class.getSimpleName();
		}
	}

	static final class DataPropertyConverter implements OWLObjectConverter<OWLDataProperty>
	{
		/* @see org.mindswap.owl.OWLObjectConverter#canCast(org.mindswap.owl.OWLObject, boolean) */
		public boolean canCast(final OWLObject object, final boolean strictConversion)
		{
			if (object instanceof OWLDataProperty) return true;

			if (object instanceof OWLEntityImpl<?>)
			{
				final OWLEntityImpl<?> entity = (OWLEntityImpl<?>) object;
				final Resource res = entity.getImplementation();
				return !strictConversion || res.canAs(DatatypeProperty.class);
			}

			return false;
		}

		/* @see org.mindswap.owl.OWLObjectConverter#cast(org.mindswap.owl.OWLObject, boolean) */
		public OWLDataProperty cast(final OWLObject object, final boolean strictConversion)
		{
			if (object instanceof OWLDataProperty) return (OWLDataProperty) object; // shortcut --> cast is superfluous

			if (canCast(object, strictConversion))
			{
				final OWLEntityImpl<?> entity = (OWLEntityImpl<?>) object;
				final Resource res = entity.getImplementation();
				final DatatypeProperty prop = res.as(DatatypeProperty.class);
				return new OWLDataPropertyImpl(entity.getOntology(), prop);
			}

			throw CastingException.create(object, OWLDataProperty.class);
		}

		/* @see java.lang.Object#toString() */
		@Override
		public String toString()
		{
			return "Converter " + OWLEntity.class.getSimpleName() + " -> " + OWLDataProperty.class.getSimpleName();
		}
	}

	static final class ClassConverter implements OWLObjectConverter<OWLClass>
	{
		/* @see org.mindswap.owl.OWLObjectConverter#canCast(org.mindswap.owl.OWLObject, boolean) */
		public boolean canCast(final OWLObject object, final boolean strictConversion)
		{
			if (object instanceof OWLClass) return true;

			if (object instanceof OWLEntityImpl<?>)
			{
				final OWLEntityImpl<?> entity = (OWLEntityImpl<?>) object;
				final Resource res = entity.getImplementation();
				return !strictConversion || res.canAs(OntClass.class);
			}

			return false;
		}

		/* @see org.mindswap.owl.OWLObjectConverter#cast(org.mindswap.owl.OWLObject, boolean) */
		public OWLClass cast(final OWLObject object, final boolean strictConversion)
		{
			if (object instanceof OWLClass) return (OWLClass) object; // shortcut --> cast is superfluous

			if (canCast(object, strictConversion))
			{
				final OWLEntityImpl<?> entity = (OWLEntityImpl<?>) object;
				final Resource res = entity.getImplementation();
				final OntClass c = res.as(OntClass.class);
				return new OWLClassImpl(entity.getOntology(), c);
			}

			throw CastingException.create(object , OWLClass.class);
		}

		/* @see java.lang.Object#toString() */
		@Override
		public String toString()
		{
			return "Converter " + OWLEntity.class.getSimpleName() + " -> " + OWLClass.class.getSimpleName();
		}
	}

	static final class IndividualConverter implements OWLObjectConverter<OWLIndividual>
	{
		/* @see org.mindswap.owl.OWLObjectConverter#canCast(org.mindswap.owl.OWLObject, boolean) */
		public boolean canCast(final OWLObject object, final boolean strictConversion)
		{
			if (object instanceof OWLEntityImpl<?>) return true;
			return false;
		}

		/* @see org.mindswap.owl.OWLObjectConverter#cast(org.mindswap.owl.OWLObject, boolean) */
		public OWLIndividual cast(final OWLObject object, final boolean strictConversion)
		{
			if (object instanceof OWLIndividual) return (OWLIndividual) object; // shortcut --> cast is superfluous

			if (canCast(object, strictConversion))
			{
				final OWLEntityImpl<?> entity = (OWLEntityImpl<?>) object;
				final Resource res = entity.getImplementation();
				return new OWLIndividualImpl(entity.getOntology(), res);
			}

			throw CastingException.create(object, OWLIndividual.class);
		}

		/* @see java.lang.Object#toString() */
		@Override
		public String toString()
		{
			return "Converter " + OWLEntity.class.getSimpleName() + " -> " + OWLIndividual.class.getSimpleName();
		}
	}

	static final class DataTypeConverter implements OWLObjectConverter<OWLDataType>
	{
		/* @see org.mindswap.owl.OWLObjectConverter#canCast(org.mindswap.owl.OWLObject, boolean) */
		public boolean canCast(final OWLObject object, final boolean strictConversion)
		{
			if (object instanceof OWLDataType) return true;

			if (object instanceof OWLEntity)
			{
				final OWLEntity entity = (OWLEntity) object;
				return (entity.getKB().getDataType(entity.getURI()) != null);
			}

			return false;
		}

		/* @see org.mindswap.owl.OWLObjectConverter#cast(org.mindswap.owl.OWLObject, boolean) */
		public OWLDataType cast(final OWLObject object, final boolean strictConversion)
		{
			if (object instanceof OWLDataType) return (OWLDataType) object; // shortcut --> cast is superfluous

			if (canCast(object, strictConversion))
			{
				final OWLEntity entity = (OWLEntity) object;
				final URI uri = entity.getURI();
				return entity.getKB().getDataType(uri);
			}

			throw CastingException.create(object, OWLDataType.class);
		}

		/* @see java.lang.Object#toString() */
		@Override
		public String toString()
		{
			return "Converter " + OWLEntity.class.getSimpleName() + " -> " + OWLDataType.class.getSimpleName();
		}
	}
}

