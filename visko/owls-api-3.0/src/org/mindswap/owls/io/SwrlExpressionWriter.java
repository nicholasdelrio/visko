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
 * Created on May 7, 2005
 */
package org.mindswap.owls.io;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.list.OWLList;
import org.mindswap.owl.vocabulary.SWRLB;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.AtomVisitor;
import org.mindswap.swrl.BuiltinAtom;
import org.mindswap.swrl.ClassAtom;
import org.mindswap.swrl.DataPropertyAtom;
import org.mindswap.swrl.DifferentIndividualsAtom;
import org.mindswap.swrl.IndividualPropertyAtom;
import org.mindswap.swrl.SameIndividualAtom;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
class SwrlExpressionWriter extends BaseExpressionWriter<OWLList<Atom>> implements AtomVisitor
{
	static final Map<OWLIndividual, String[]> SYMBOLS;
	static
	{
		SYMBOLS = new HashMap<OWLIndividual, String[]>();
		SYMBOLS.put(SWRLB.add,						new String[] {"=", "+"});
		SYMBOLS.put(SWRLB.subtract,				new String[] {"=", "-"});
		SYMBOLS.put(SWRLB.divide,					new String[] {"=", "/"});
		SYMBOLS.put(SWRLB.multiply,				new String[] {"=", "*"});
		SYMBOLS.put(SWRLB.mod,						new String[] {"=", "%"});
		SYMBOLS.put(SWRLB.pow,						new String[] {"=", "^"});
		SYMBOLS.put(SWRLB.lessThan,				new String[] {"<"});
		SYMBOLS.put(SWRLB.lessThanOrEqual,		new String[] {"<="});
		SYMBOLS.put(SWRLB.greaterThan,			new String[] {">"});
		SYMBOLS.put(SWRLB.greaterThanOrEqual,	new String[] {">="});
		SYMBOLS.put(SWRLB.equal,					new String[] {"="});
		SYMBOLS.put(SWRLB.notEqual,				new String[] {"~", "="});
	}

	/* @see org.mindswap.owls.io.BaseExpressionWriter#write(org.mindswap.owl.list.OWLList) */
	@Override
	protected void write(final OWLList<Atom> atoms)
	{
		if (atoms == null || atoms.isEmpty())
		{
			out.print("<Empty-Expression>");
			return;
		}
		boolean first = true;
		for (final Iterator<Atom> it = atoms.iterator(); it.hasNext();)
		{
			if (!first || firstLineIndent) out.print(indent);
			else first = false;

			it.next().accept(this);

			if (it.hasNext()) out.println(" &");
		}
	}

	/* @see org.mindswap.swrl.AtomVisitor#visit(org.mindswap.swrl.BuiltinAtom) */
	public void visit(final BuiltinAtom atom)
	{
		final OWLIndividual builtin = atom.getBuiltin();
		final int count = atom.getArgumentCount();
		final String[] symbols = SYMBOLS.get(builtin);
		if (symbols != null)
		{
			int extra = 0;
			if (symbols.length == count)
			{
				out.print(symbols[0]);
				extra = 1;
			}

			out.print("(");
			for (int i = 0; i < count; i++)
			{
				out.print(atom.getArgument(i));
				if (i < count - 1) out.print(" " + symbols[i + extra] + " ");
			}
			out.print(")");
		}
		else
		{
			print(builtin);
			out.print("(");

			for (int i = 0; i < atom.getArgumentCount(); i++)
			{
				if (i > 0) out.print(", ");
				print(atom.getArgument(i));
			}
			out.print(")");
		}
	}

	/* @see org.mindswap.swrl.AtomVisitor#visit(org.mindswap.swrl.ClassAtom) */
	public void visit(final ClassAtom atom)
	{
		print(atom.getClassPredicate());
		out.print("(");
		print(atom.getArgument1());
		out.print(")");
	}

	/* @see org.mindswap.swrl.AtomVisitor#visit(org.mindswap.swrl.DataPropertyAtom) */
	public void visit(final DataPropertyAtom atom)
	{
		print(atom.getPropertyPredicate());
		out.print("(");
		print(atom.getArgument1());
		out.print(", ");
		print(atom.getArgument2());
		out.print(")");
	}

	/* @see org.mindswap.swrl.AtomVisitor#visit(org.mindswap.swrl.DifferentIndividualsAtom) */
	public void visit(final DifferentIndividualsAtom atom)
	{
		out.print("~(");
		print(atom.getArgument1());
		out.print(" = ");
		print(atom.getArgument2());
		out.print(")");
	}

	/* @see org.mindswap.swrl.AtomVisitor#visit(org.mindswap.swrl.IndividualPropertyAtom) */
	public void visit(final IndividualPropertyAtom atom)
	{
		print(atom.getPropertyPredicate());
		out.print("(");
		print(atom.getArgument1());
		out.print(", ");
		print(atom.getArgument2());
		out.print(")");
	}

	/* @see org.mindswap.swrl.AtomVisitor#visit(org.mindswap.swrl.SameIndividualAtom) */
	public void visit(final SameIndividualAtom atom)
	{
		out.print("(");
		print(atom.getArgument1());
		out.print(" = ");
		print(atom.getArgument2());
		out.print(")");
	}

}
