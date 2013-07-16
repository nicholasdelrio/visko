/*
 * Created 10.08.2009
 *
 * (c) 2009 Thorsten Möller - University of Basel Switzerland
 *
 * The MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package org.mindswap.owl;

import impl.owl.CombinedOWLConverter;
import impl.owl.ListConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mindswap.common.ServiceFinder;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.owl.list.OWLList;
import org.mindswap.owls.OWLSProvider;
import org.mindswap.owls.grounding.JavaGroundingProvider;
import org.mindswap.owls.grounding.UPnPGroundingProvider;
import org.mindswap.owls.grounding.WSDLGroundingProvider;
import org.mindswap.swrl.SWRLProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Global singleton registry for all {@link OWLObjectConverter}. There are two
 * ways how to register converters. First, directly using the methods provided
 * by this class. Second, by implementing {@link OWLObjectConverterProvider}
 * and adding such implementations as service providers; for details on how to
 * do so see {@link ServiceFinder}.
 * <p>
 * This class is thread safe, using {@link ConcurrentHashMap} as the backing
 * data structure. However, OWL object converter registered here may not be
 * thread safe themselves! Consequently, modifications of them (if possible)
 * may require appropriate synchronizations means.
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class OWLObjectConverterRegistry
{
	private static final Logger logger = LoggerFactory.getLogger(OWLObjectConverterRegistry.class);

	private static final OWLObjectConverterRegistry instance = new OWLObjectConverterRegistry();

	// read access vastly outnumbers write access for the two maps
	private final ConverterMap converters;
	private final ListConverterMap listConverters;

	private OWLObjectConverterRegistry()
	{
		converters = new ConverterMap();
		listConverters = new ListConverterMap();

		// First, load and instantiate required converter providers that always need to exist
		OWLProvider owlProvider = ServiceFinder.instance().loadImplementation(
			OWLProvider.class, OWLProvider.DEFAULT_OWL_PROVIDER);
		OWLSProvider owlsProvider = ServiceFinder.instance().loadImplementation(
			OWLSProvider.class, OWLSProvider.DEFAULT_OWLS_PROVIDER);
		JavaGroundingProvider javaGdgProvider = ServiceFinder.instance().loadImplementation(
			JavaGroundingProvider.class, JavaGroundingProvider.DEFAULT_JAVA_GROUNDING_PROVIDER);
		WSDLGroundingProvider wsdlGdgProvider = ServiceFinder.instance().loadImplementation(
			WSDLGroundingProvider.class, WSDLGroundingProvider.DEFAULT_WSDL_GROUNDING_PROVIDER);
		UPnPGroundingProvider upnpGdgProvider = ServiceFinder.instance().loadImplementation(
			UPnPGroundingProvider.class, UPnPGroundingProvider.DEFAULT_UPNP_GROUNDING_PROVIDER);
		SWRLProvider swrlProvider = ServiceFinder.instance().loadImplementation(
			SWRLProvider.class, SWRLProvider.DEFAULT_SWRL_PROVIDER);

		if (owlProvider != null) owlProvider.registerConverters(this);
		else logNoImplFound(OWLProvider.class);

		if (owlsProvider != null) owlsProvider.registerConverters(this);
		else logNoImplFound(OWLSProvider.class);

		if (wsdlGdgProvider != null) wsdlGdgProvider.registerConverters(this);
		else logNoImplFound(WSDLGroundingProvider.class);

		if (javaGdgProvider != null) javaGdgProvider.registerConverters(this);
		else logNoImplFound(JavaGroundingProvider.class);

		if (upnpGdgProvider != null) upnpGdgProvider.registerConverters(this);
		else logNoImplFound(UPnPGroundingProvider.class);

		if (swrlProvider != null) swrlProvider.registerConverters(this);
		else logNoImplFound(SWRLProvider.class);

		// Second, let additional providers (which we do not know in advance) contribute their converters
		List<OWLObjectConverterProvider> providers =
			ServiceFinder.instance().loadImplementations(OWLObjectConverterProvider.class);
		for (OWLObjectConverterProvider provider : providers)
		{
			provider.registerConverters(this);
		}
	}

	/**
	 * @return The global singleton OWL object converter instance.
	 */
	public static final OWLObjectConverterRegistry instance()
	{
		return instance;
	}

	/**
	 * Special type of registering some OWL object converter. The method checks
	 * if a converter for the target type was already registered. If so, it will
	 * check if the associated converter is a {@link CombinedOWLConverter}. If
	 * this is the case then the given converter will be added to the combined
	 * converter. Otherwise, the existing converter will be replaced by a new
	 * combined converter containing the existing converter and the given one.
	 *
	 * @param target The target Java class.
	 * @param converter The converter to add.
	 */
	public <T extends OWLObject> void extendByConverter(final Class<T> target,
		final OWLObjectConverter<? extends T> converter)
	{
		// you may ask why is this method implemented in this (unintuitive) way
		// the answer is simple: it ensures snapshot isolation properties

		final List<OWLObjectConverter<? extends T>> cs = new ArrayList<OWLObjectConverter<? extends T>>(3);
		cs.add(converter);
		final OWLObjectConverter<T> c = converters.putIfAbsent(target, new CombinedOWLConverter<T>(target, cs));

		if (c == null) return; // target not yet registered, i.e., new combined converter was put
		else if (c instanceof CombinedOWLConverter<?>) // combined converter already registered
		{
			final CombinedOWLConverter<T> cc = (CombinedOWLConverter<T>) c;
			cc.addConverter(converter);
		}
		else // c != null && !(c instanceof CombinedConverter)
		{    // --> overwrite 'c' by new combined converter consisting of 'c' and 'converter'
			cs.add(c);
			converters.put(target, new CombinedOWLConverter<T>(target, cs));
		}
	}

	/**
	 * Get the converter that is able to check and convert some {@link OWLObject}
	 * into the given Java class.
	 *
	 * @param target The target Java class.
	 * @return The converter that was registered to convert to the target
	 * 	Java class, or <code>null</code> if none is registered.
	 */
	public <T extends OWLObject> OWLObjectConverter<T> getConverter(final Class<T> target)
	{
		return converters.get(target);
	}

	/**
	 * Get the converter that is able to check and convert some {@link OWLIndividual}
	 * into a {@link OWLList} (containing elements according to the properties
	 * specified by the given list vocabulary.
	 *
	 * @param target The target list vocabulary.
	 * @return The converter that was registered to convert to the target
	 * 	list vocabulary, or <code>null</code> if none is registered.
	 */
	public <T extends OWLValue> ListConverter<T> getListConverter(final ListVocabulary<T> target)
	{
		return listConverters.get(target);
	}

	/**
	 * Register some OWL object converter that is able to determine if some
	 * {@link OWLObject} can be viewed as the target type, and that implements
	 * the conversion procedure.
	 * <p>
	 * Note that {@link ListConverter} need to be registered using
	 * {@link #registerListConverter(ListVocabulary, ListConverter)}.
	 *
	 * @param target The target Java class.
	 * @param converter The converter that can be used to convert some
	 * 	{@link OWLObject} into the target class.
	 * @return Previous converter associated to the target Java class, or
	 * 	<code>null</code> if there was no converter registered.
	 */
	public <T extends OWLObject> OWLObjectConverter<T> registerConverter(
		final Class<T> target, final OWLObjectConverter<T> converter)
	{
		final OWLObjectConverter<T> old = converters.put(target, converter);
		if (old != null) logger.info("OWL object converter for {} already registered. Existing converter {}" +
			" was overridden by {}", new Object[]{target.getName(), old, converter});
		return old;
	}

	/**
	 * Register some OWL list converter that is able to check and convert to
	 * {@link OWLList} according to the properties defined by the given list
	 * vocabulary.
	 *
	 * @param vocabulary The vocabulary used by corresponding lists.
	 * @param converter The converter that is able to determine if a
	 * 	{@link OWLIndividual} has the properties that are required in order
	 * 	to view it as a OWL list, and that implements the conversion procedure.
	 * @return Previous converter associated to the vocabulary, or
	 * 	<code>null</code> if there was no converter registered.
	 */
	public <T extends OWLValue> ListConverter<T> registerListConverter(
		final ListVocabulary<T> vocabulary, final ListConverter<T> converter)
	{
		final ListConverter<T> old = listConverters.put(vocabulary, converter);
		if (old != null) logger.info("OWL list converter for {} already registered. Existing converter {}" +
			" was overridden by {}", new Object[]{vocabulary, old, converter});
		return old;
	}

	private void logNoImplFound(final Class<?> interfaze)
	{
		logger.error("No implementation found for {}", interfaze);
	}

	private static final class ConverterMap
	{
		final ConcurrentHashMap<Class<? extends OWLObject>, OWLObjectConverter<? extends OWLObject>> map;

		ConverterMap()
		{
			// we expect quite a lot converters
			map = new ConcurrentHashMap<Class<? extends OWLObject>, OWLObjectConverter<? extends OWLObject>>(60);
		}

		// casts are not critical because of the way the put and putIfAbsent method signatures are declared

		@SuppressWarnings("unchecked")
		<C extends OWLObjectConverter<T>, T extends OWLObject> C put(final Class<T> target, final C converter)
		{
			return (C) map.put(target, converter);
		}

		@SuppressWarnings("unchecked")
		<C extends OWLObjectConverter<T>, T extends OWLObject> C putIfAbsent(final Class<T> target, final C converter)
		{
			return (C) map.putIfAbsent(target, converter);
		}

		@SuppressWarnings("unchecked")
		<C extends OWLObjectConverter<T>, T extends OWLObject> C get(final Class<T> target)
		{
			return (C) map.get(target);
		}
	}

	private static final class ListConverterMap
	{
		final Map<ListVocabulary<? extends OWLValue>, ListConverter<? extends OWLValue>> map;

		ListConverterMap()
		{
			// only a few list converters expected
			map = new ConcurrentHashMap<ListVocabulary<? extends OWLValue>, ListConverter<? extends OWLValue>>(10);
		}

		// casts are not critical because of the way the put method signature is declared

		@SuppressWarnings("unchecked")
		<T extends OWLValue> ListConverter<T> put(final ListVocabulary<T> target, final ListConverter<T> converter)
		{
			return (ListConverter<T>) map.put(target, converter);
		}

		@SuppressWarnings("unchecked")
		<T extends OWLValue> ListConverter<T> get(final ListVocabulary<T> target)
		{
			return (ListConverter<T>) map.get(target);
		}
	}

}
