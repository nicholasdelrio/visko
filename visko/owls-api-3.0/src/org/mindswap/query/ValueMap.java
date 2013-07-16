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
 * Created on Dec 27, 2003
 */
package org.mindswap.query;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.mindswap.common.Variable;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLValue;

/**
 * This class provides a way to assign {@link OWLValue values} to
 * {@link Variable variables}. When a process needs to be executed the values
 * for the input parameters are specified with this interface. The values of
 * output parameters of the execution are also provided by means of this class.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class ValueMap<V extends Variable, W extends OWLValue> implements Iterable<Entry<V, W>>
{
	/**
	 * The empty value map (immutable).
	 *
	 * @see #emptyValueMap()
	 * @deprecated Use {@link #emptyValueMap()} instead to prevent compiler
	 * 	warnings for generic use.
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public static final ValueMap EMPTY_VALUE_MAP = new ValueMap(Collections.EMPTY_MAP);

	private final Map<V, W> map;

	/**
	 * Creates an initially empty value map.
	 */
	public ValueMap()
	{
		this(new HashMap<V, W>());
	}

	/**
	 * @param mappings Initial mappings.
	 * @throws AssertionError If <code>mappings</code> is <code>null</code>.
	 */
	public ValueMap(final Map<V,W> mappings)
	{
		assert (mappings != null) : "Invalid: Map is null.";
		this.map = mappings;
	}

	/**
	 * Set the value of the given variable. If the map previously contained a
	 * mapping for this variable, the old value is replaced by the specified
	 * value.
	 *
	 * @param var Variable with which the specified value is to be associated.
	 * @param value Value to be associated with the specified key.
	 * @return Previous value associated with specified variable, or <code>null</code>
	 * 	if there was no mapping for variable.
	 * @throws NullPointerException In case the variable and/or its associated
	 * 	value is <code>null</code>.
	 */
	public W setValue(final V var, final W value)
	{
		if (var == null) throw new NullPointerException("ValueMap cannot set a value for null variable");
		if (value == null)
			throw new NullPointerException("Value of variable '" + var + "' cannot be set to null");

		return map.put(var, value);
	}

	/**
	 * Get the value of the given variable
	 *
	 * @param var Variable whose associated value is to be returned.
	 * @return The value to which this map binds the specified variable, or
	 * 	<code>null</code> if the map contains no mapping for this variable.
	 */
	public W getValue(final Variable var)
	{
		return map.get(var);
	}

	/**
	 * Get the value bound to the given variable cast to an {@link OWLDataValue}.
	 *
	 * @param var Variable whose associated value is to be returned.
	 * @return The value to which this map binds the specified variable, or
	 * 	<code>null</code> if the map contains no mapping for this variable.
	 * @throws ClassCastException if the variable is bound to an {@link OWLIndividual}.
	 */
	public OWLDataValue getDataValue(final Variable var)
	{
		return (OWLDataValue) map.get(var);
	}

	/**
	 * Get the value bound to the variable with the given variable name cast to
	 * a {@link OWLDataValue}.
	 *
	 * @param var Variable name whose associated value is to be returned.
	 * @return The value to which this map binds the specified variable, or
	 * 	<code>null</code> if the map contains no mapping for this variable.
	 * @throws ClassCastException if the variable is bound to an {@link OWLIndividual}.
	 */
	public OWLDataValue getDataValue(final String var)
	{
		return (OWLDataValue) getValue(var);
	}

	/**
	 * Get the value bound to the given variable as string. More precisely, this
	 * method simply invokes {@link Object#toString()} on the value.
	 *
	 * @param var Variable whose associated value is to be returned.
	 * @return The value as string to which this map binds the specified
	 * 	variable, or <code>null</code> if the map contains no mapping for
	 * 	this variable.
	 */
	public String getStringValue(final Variable var)
	{
		OWLValue tmp = map.get(var);
		return tmp == null? null : tmp.toString();
	}

	/**
	 * Get the value bound to the variable with the given name as string. More
	 * precisely, this method simply invokes {@link Object#toString()} on the
	 * value.
	 *
	 * @param var Variable name whose associated value is to be returned.
	 * @return The value as string to which this map binds the specified
	 * 	variable, or <code>null</code> if the map contains no mapping for
	 * 	this variable.
	 */
	public String getStringValue(final String var)
	{
		OWLValue tmp = getValue(var);
		return tmp == null? null : tmp.toString();
	}

	/**
	 * Get the value of the given variable cast to a {@link OWLIndividual}.
	 *
	 * @param var Variable whose associated value is to be returned.
	 * @return The value to which this map binds the specified variable, or
	 * 	<code>null</code> if the map contains no mapping for this variable.
	 * @throws ClassCastException if the variable is bound to an {@link OWLDataValue}.
	 */
	public OWLIndividual getIndividualValue(final Variable var)
	{
		return (OWLIndividual) map.get(var);
	}

	/**
	 * Get the value bound to the variable with the given variable name cast to
	 * a {@link OWLIndividual}.
	 *
	 * @param var Variable name whose associated value is to be returned.
	 * @return The value to which this map binds the specified variable, or
	 * 	<code>null</code> if the map contains no mapping for this variable.
	 * @throws ClassCastException if the variable is bound to an {@link OWLDataValue}.
	 */
	public OWLIndividual getIndividualValue(final String var)
	{
		return (OWLIndividual) getValue(var);
	}

	/**
	 * Get the value of the variable with the given variable name.
	 *
	 * @param varName Variable name whose associated value is to be returned.
	 * @return The value to which this map binds the specified variable, or
	 * 	<code>null</code> if the map contains no mapping for this variable.
	 */
	public W getValue(final String varName)
	{
		for (Entry<V, W> e : map.entrySet())
		{
			final V var = e.getKey();
			final String varname = var.getName();
			if (varname != null && varname.equals(varName)) return e.getValue();
		}
		return null;
	}

	/**
	 * Return <code>true</code> if the given variable is bound to a value in
	 * this map.
	 *
	 * @param var Variable whose presence in this map is to be tested.
	 * @return <code>true</code> if this map contains a mapping for the specified
	 * 	variable.
	 */
	public boolean hasValue(final Variable var)
	{
		return map.containsKey(var);
	}

	/**
	 * Remove the value that has been assigned to the given variable.
	 *
	 * @param var Variable whose mapping is to be removed from the map.
	 * @return Previous value bound to the specified variable, or
	 * 	<code>null</code> if there was no mapping before.
	 */
	public OWLValue removeValue(final Variable var)
	{
		return map.remove(var);
	}

	/**
	 * Clear all the variable-value mappings.
	 */
	public void clear()
	{
		map.clear();
	}

	/**
	 * Store all the value bindings defined in the given ValueMap. If this map
	 * previously contained a mapping for some variable, the old value will be
	 * replaced by the specified value.
	 *
	 * @param valueMap The variable-value mappings to be stored in this map.
	 */
	public void putMap(final ValueMap<? extends V, ? extends W> valueMap)
	{
		map.putAll(valueMap.map);
	}

	/**
	 * @return A set view of the variables contained in this map.
	 */
	public Set<V> getVariables()
	{
		return map.keySet();
	}

	/**
	 * @return A collection view of the values contained in this map.
	 */
	public Collection<W> getValues()
	{
		return map.values();
	}

	/**
	 * @return <code>true</code> if this map contains no variable-value mappings.
	 */
	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	/**
	 * @return The number of variable-value mappings in this map.
	 */
	public int size()
	{
		return map.size();
	}

	/* @see java.lang.Object#toString() */
	@Override public String toString()
	{
		return map.toString();
	}

	/**
	 * This method should not be used in production environments as it may
	 * make sensitive data public!
	 *
	 * @return Detailed string containing each variable and its associated
	 * 	value; lexical form in case of {@link OWLDataValue}, or RDF/XML
	 * 	serialization in case of {@link OWLIndividual}.
	 */
	public String debugString()
	{
		StringBuilder sb = new StringBuilder();

		for (Iterator<Entry<V, W>> i = map.entrySet().iterator(); i.hasNext(); )
		{
			Entry<V, W> entry = i.next();
			V var = entry.getKey();
			W val = entry.getValue();

			final String value = val.isDataValue()? val.toString() : ((OWLIndividual) val).toRDF(false, true);

			sb.append("(").append(var.getName()).append(" = ").append(value).append(")");

			if (i.hasNext()) sb.append(" & ");
		}

		return sb.toString();
	}

	/* @see java.lang.Iterable#iterator() */
	public Iterator<Entry<V, W>> iterator()
	{
		return map.entrySet().iterator();
	}

	/**
	 * The empty value map (immutable).
	 *
	 * <p>
	 * This example illustrates the type-safe way to obtain an empty value map:
	 * <pre>
	 *     ValueMap&lt;Input, OWLIndividual&gt; vm = ValueMap.emptyValueMap();
	 * </pre>
	 * Implementation note:  Implementations of this method need not
	 * create a separate <tt>ValueMap</tt> object for each call. Using this
	 * method is likely to have comparable cost to using the like-named
	 * field.  (Unlike this method, the field does not provide type safety.)
	 *
	 * @see #EMPTY_VALUE_MAP
	 */
	@SuppressWarnings("unchecked")
	public static final <V extends Variable, W extends OWLValue> ValueMap<V,W> emptyValueMap()
   {
   	return EMPTY_VALUE_MAP;
   }

	/**
	 * Returns an unmodifiable view of the specified value map, i.e., with a
	 * "read-only" access. Attempts to modify the returned value map, whether
	 * direct or via its collection views, result in an
	 * {@link UnsupportedOperationException}.
	 *
	 * @param vm The value map for which an unmodifiable view is to be returned.
	 * @return An unmodifiable view of the specified value map.
	 */
	public static final <V extends Variable, W extends OWLValue> ValueMap<V, W>
		unmodifyableValueMap(final ValueMap<V,W> vm)
	{
		return new ValueMap<V, W>(Collections.unmodifiableMap(vm.map));
	}
}
