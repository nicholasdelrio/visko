/*
 * Created on 04.08.2008
 *
 * (c) 2008 Thorsten Möller - University of Basel Switzerland
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
package org.mindswap.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class ServiceFinder
{
	private static final Logger logger = LoggerFactory.getLogger(ServiceFinder.class);

	private static final ServiceFinder instance = new ServiceFinder();

	/** Maps interface class to first implementation found to speed up recurrent instantiation. */
	private final Map<Class<?>, Class<?>> implCache;

	/* Implementation note: Methods of this class can be simplified as soon
	 * as we drop the requirement of compatibility with Java 5 and move on
	 * to Java 6 (or later). Starting from Java 6, the JDK contains the
	 * class java.?.Service which should be used then.
	 * On the other hand, if we would move to a IOC platform such as OSGi,
	 * or Spring, then we could also drop most of the code by letting the
	 * container discover and inject implementations. */

	private ServiceFinder()
	{
		super();
		implCache = new HashMap<Class<?>, Class<?>>();
	}

	/**
	 * @return The singleton instance.
	 */
	public static final ServiceFinder instance()
	{
		return instance;
	}

	/**
	 * Given a (factory) interface, find and try to instantiate a implementation.
	 * This method uses the following ordered lookup procedure to determine the
	 * implementation class to load and instantiate:
	 *
	 * <ul>
	 *   <li>
	 *   Use a system property equally named as the fully qualified name of the
	 *   of the given interface.
	 *   </li>
	 *   <li>
	 *   Use the Services API (as detailed in the JAR specification), if
	 *   available, to determine the class name. The Services API will look
	 *   for a class name in files equally named as the fully qualified name
	 *   of the interface. The files are expected in <tt>META-INF/services/</tt>
	 *   in jars available to the runtime.
	 *   </li>
	 *   <li>Fallback to use the default implementation.</li>
	 * </ul>
	 *
	 * In contrast to {@link #loadImplementation(Class, String, Object...)} this
	 * method is fast for recurrent invocation with a interface class used already.
	 * The expensive search for implementations is done only for very first
	 * invocation with a particular interface class. Afterwards, the
	 * implementation that was found is reused, i.e., the method will always
	 * return instances of the implementation that was found at very first
	 * invocation. (If no implementation is found, the expensive search is done
	 * again and again).
	 *
	 * @param <I> The actual interface.
	 * @param interfaze A class denoting the interface to search for a
	 * 	implementation.
	 * @param deflt Fallback implementation that will be used if discovery
	 * 	did not find an implementation. Fully qualified class name expected.
	 * @param args Optional list of arguments. If <code>null</code> the default
	 * 	no-args constructor will be used to instantiate an implementation.
	 * 	Otherwise, a constructor is searched that is accessible and matches
	 * 	the number of arguments and their type. If none exists this will be
	 * 	silently ignored.
	 * @return A new instance that implements the given interface, or
	 * 	<code>null</code> if no implementation was found or instantiation
	 * 	using the given arguments failed.
	 */
	public final <I> I createInstance(final Class<I> interfaze, final String deflt, final Object... args)
	{
		// this cast is not critical as long as implCache is filled with assignable classes
		Class<? extends I> implClass = (Class<? extends I>) implCache.get(interfaze);

		if (implClass == null)
		{
			final List<Class<? extends I>> implementations = findImplementations(interfaze, deflt, true);
			if (implementations.isEmpty()) return null;
			implClass = implementations.get(0);
			implCache.put(interfaze, implClass);
		}

		return instantiate(implClass, args);
	}

	/**
    * Given a (factory) interface, find and try to instantiate all implementations.
    * The jars available to the runtime are searched for service provider files
    * (expected in <tt>META-INF/services/</tt>) as detailed in the JAR
    * specification.
    * <p>
    * In contrast to {@link #loadImplementation(Class, String, Object...)} a
    * possibly existing system property has no effect, i.e., will not be taken
    * into account.
    *
    * @param <I> The actual interface.
	 * @param interfaze A class denoting the interface to search for implementations.
    * @param args Optional list of arguments. If <code>null</code> the default
	 * 	no-args constructor will be used to instantiate an implementation.
	 * 	Otherwise, a constructor is searched that is accessible and matches
	 * 	the number of arguments and their type. If none exists this will be
	 * 	silently ignored.
    * @return All implementations of the interface, or an empty list if none
    * 	were found.
    */
	public final <I> List<I> loadImplementations(final Class<I> interfaze, final Object... args)
	{
		return findImpls(interfaze, args);
	}

	/**
    * Given a (factory) interface, find and try to instantiate a implementation.
	 * This method uses the following ordered lookup procedure to determine the
	 * implementation class to load and instantiate:
	 *
	 * <ul>
	 *   <li>
	 *   Use a system property equally named as the fully qualified name of the
	 *   of the given interface.
	 *   </li>
	 *   <li>
	 *   Use the Services API (as detailed in the JAR specification), if
	 *   available, to determine the class name. The Services API will look
	 *   for a class name in files equally named as the fully qualified name
	 *   of the interface. The files are expected in <tt>META-INF/services/</tt>
	 *   in jars available to the runtime.
	 *   </li>
	 *   <li>Fallback to use the default implementation.</li>
	 * </ul>
    *
    * @param <I> The actual interface.
	 * @param interfaze A class denoting the interface to search for a
	 * 	implementation.
    * @param deflt Fallback implementation that will be used if discovery
	 * 	did not find an implementation. Fully qualified class name expected.
	 * @param args Optional list of arguments. If <code>null</code> the default
	 * 	no-args constructor will be used to instantiate an implementation.
	 * 	Otherwise, a constructor is searched that is accessible and matches
	 * 	the number of arguments and their type. If none exists this will be
	 * 	silently ignored.
    * @return A new instance of the first implementation of the interface which
    * 	was found, or <code>null</code> if none was found.
    */
	public final <I> I loadImplementation(final Class<I> interfaze, final String deflt, final Object... args)
	{
		return findImpl(interfaze, deflt, args);
   }

	private final <I> I instantiate(final Class<? extends I> impl, final Object... args)
	{
		try
		{
			// use default no-args constructor if no arguments are given
			if (args == null || args.length == 0) return impl.newInstance();

			// search for constructor according to given arguments
			final Constructor<?>[] cs = impl.getConstructors();
			OUTER: for (final Constructor<?> c : cs)
			{
				final Class<?>[] paramTypes = c.getParameterTypes();
				if (paramTypes.length != args.length) continue; // number of arguments do not match

				for (int i = 0; i < paramTypes.length; i++)
				{
					if (!paramTypes[i].isAssignableFrom(args[i].getClass())) continue OUTER;
				}

				// if we come to this point we have found matching constructor
				return (I) c.newInstance(args); // cast is not critical because 'cs' are from 'impl'
			}
		}
		catch (final Exception e) // InstantiationException, IllegalAccessException, IllegalArgumentException,
		{                   // SecurityException, InvocationTargetException, NoSuchMethodException, NPE
			logger.debug("Failed to instantiate {}. Details: {}", impl, e.toString());
		}

		return null;
	}

	private final <I> I findImpl(final Class<I> interfaze, final String deflt, final Object... args)
	{
		final List<Class<? extends I>> implementations = findImplementations(interfaze, deflt, true);
		if (implementations.isEmpty()) return null;

		final Class<? extends I> implClass = implementations.get(0);
		final I implInstance = instantiate(implClass, args);

		if (implementations.size() > 1)
		{
			logger.info(
				"More than one implementation of {} found. Using the first one found {}", interfaze, implClass);
		}
		else
		{
			logger.debug("Using {} implementation of {}", implClass, interfaze);
		}

		return implInstance;
   }

	private final <I> List<I> findImpls(final Class<I> interfaze, final Object... args)
	{
		final List<Class<? extends I>> implClasses = findImplementations(interfaze, null, false);
		final List<I> implInstances = new ArrayList<I>(implClasses.size());

		for (final Class<? extends I> implClass : implClasses)
		{
			final I implInstance = instantiate(implClass, args);
			if (implInstance != null) implInstances.add(implInstance);
		}

		return implInstances;
	}

	private final <I> List<Class<? extends I>> findImplementations(final Class<I> interfaze,
		final String deflt, final boolean considerSystemProperty)
	{
		final ClassLoader loader = ServiceFinder.class.getClassLoader();
		final Enumeration<URL> providerLists;
		final List<Class<? extends I>> providers = new ArrayList<Class<? extends I>>();

		// Use the system property first
		if (considerSystemProperty)
		{
			final Class<? extends I> provider = checkSystemProperty(interfaze);
			if (provider != null) providers.add(provider);
		}

		// Second, try JAR service provider mechanism if system property was not set or failed
		if (providers.isEmpty())
		{
			try
			{
				providerLists = loader.getResources("META-INF/services/" + interfaze.getName());

				while (providerLists.hasMoreElements())
				{
					final URL providerList = providerLists.nextElement();
					providers.addAll(findInFile(providerList, interfaze));
				}
			}
			catch (final IOException ex)
			{
				logger.error("Failed to locate service provider meta file(s) for {}.", interfaze, ex);
			}
		}

		// Finally, fallback to default implementation
		if (providers.isEmpty() && deflt != null)
		{
			try
			{
				final Class<? extends I> defImpl = Class.forName(deflt).asSubclass(interfaze);
				providers.add(defImpl);
			}
			catch (Exception e) // CNFE, CCE
			{
				logger.debug("Faile to load default implementation for {}. Details: {}", interfaze, e.toString());
			}
		}

		if (providers.size() == 0) logger.debug(
			"No implementations of {} found; neither via so named system property, classpath, and default.", interfaze);
		return providers;
	}

	/**
    * Find all implementation classes given a URL to a file containing class
    * names which must be found on the current class path. The class names are
    * listed on separate lines.  Comments marked with a '#', even if placed
    * after a class name in a line, are ignored.
    *
    * @param implFile The URL to a file containing a list of implementation
    * 	class name(s).
    * @param interfaze The interface expected to be implemented by the classes
    * 	listed in the file.
    * @return List of classes implementing the given interface, or a empty list
    * 	if none were found.
    */
	private final <I> List<Class<? extends I>> findInFile(final URL implFile, final Class<I> interfaze)
	{
		BufferedReader reader = null;
		final List<Class<? extends I>> implementations = new ArrayList<Class<? extends I>>();
		try
		{
			reader = new BufferedReader(new InputStreamReader(implFile.openStream()));

			String implClassName;
			Class<? extends I> implClass;

			while ((implClassName = reader.readLine()) != null)
			{
				final int comment = implClassName.indexOf('#');
				if (comment != -1) implClassName = implClassName.substring(0, comment);

				implClassName = implClassName.trim();
				if (implClassName.length() > 0)
				{
					try
					{
						implClass = Class.forName(implClassName).asSubclass(interfaze);
						implementations.add(implClass);
					}
					catch (final Exception e)
					{
						logger.debug("Failed to load " + implClassName, e);
					}
				}
			}
		}
		catch (final IOException ex)
		{
			logger.error("Failed to read service provider file {}", implFile, ex);
		}
		finally
		{
			if (null != reader) try
			{
				reader.close();
			}
			catch(final IOException ignored) { /* we can not recover anyway */	}
		}

		return implementations;
   }

	private final <I> Class<? extends I> checkSystemProperty(final Class<I> interfaze)
	{
		final String prop = System.getProperty(interfaze.getName());
		if (prop == null) return null;

		logger.debug("Found system property {}, value={}", interfaze.getName(), prop);
		try
		{
			return Class.forName(prop).asSubclass(interfaze);
		}
		catch (final Exception e)
		{
			logger.debug("Class {} could not be loaded. Details {}", prop, e.toString());
			return null;
		}
	}

//	protected static final <T> T createFactory(final Class<T> interfaze, final String... implementations)
//	{
//		T result = null;
//		for(int i = 0; (result == null) && (i < implementations.length); i++)
//		{
//			try
//			{
//				final Class<? extends T> impl = Class.forName(implementations[i]).asSubclass(interfaze);
//				result = impl.newInstance();
//			}
//			catch(final Exception e)
//			{
//				logger.debug("Cannot create factory for interface {}. Details: {}", interfaze, e.toString());
//			}
//		}
//		return result;
//	}

}
