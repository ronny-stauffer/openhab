/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.core.library.types;

import java.util.EnumSet;
import java.util.Set;

import javax.swing.text.html.MinimalHTMLWriter;

import org.openhab.core.library.types.ExtendedJalousieType.Flags;
import org.openhab.core.types.PrimitiveType;
import org.openhab.core.types.State;

/**
 * The undefinable type primarily wraps a primitive type
 * but can also be used to represent an undefined value.
 * 
 * @author Ronny Stauffer
 * 
 */
public class UndefinableType<T extends PrimitiveType> implements WildcardType, PrimitiveType, State, Comparable<UndefinableType<T>> {
	
	public enum Qualifier {
		NONE,
		MAXIMUM,
		MINIMUM
	}
	
	public interface ValueCreator<T> {
		T create(String value);
	}

	private static final long serialVersionUID = 4226845847123464690L;

	public static final String UNDEFINED_LITERAL = "-";
	private static final String MAXIMUM_TOKEN = "<";
	private static final String MINIMUM_TOKEN = ">";
	
	public static final String WILDCARD_LITERAL = "*";	
	
	public static final UndefinableType<?> UNDEFINED = new UndefinableType();
	public static <T extends PrimitiveType> UndefinableType<T> UNDEFINED() {
		//return new UndefinableType<T>();
		return (UndefinableType<T>)UNDEFINED;
	}

	protected enum Flags {
		NONE,
		IS_WILDCARD
	}
	
	public static final UndefinableType<?> WILDCARD = new UndefinableType(Flags.IS_WILDCARD);

	protected T value;
	protected Qualifier qualifier = Qualifier.NONE;
	
	protected boolean isWildcard;

	private UndefinableType(Flags... _flags) {
//		this.value = null;
		
		Set<Flags> flags = EnumSet.<Flags>of(Flags.NONE, _flags);
		if (flags.contains(Flags.IS_WILDCARD)) {
			this.isWildcard = true;
		}
	}

	protected UndefinableType(T value, boolean isWildcard) {
		this.value = value;
		this.isWildcard = isWildcard;
	}
	
	protected UndefinableType(T value, Qualifier qualifier) {
		this.value = value;
		this.qualifier = qualifier;
	}

	public static <T extends PrimitiveType> UndefinableType<T> valueOf(T value) {
		return new UndefinableType<T>(value, false);
	}
	
	public static <T extends PrimitiveType> UndefinableType<T> valueOf(String value, ValueCreator<T> valueCreator) {
		if (value == null || value.isEmpty()) {
			throw new IllegalArgumentException("value must not be null or empty!");
		}
		
		// Handle special case value 'Undefined'
		if (UNDEFINED_LITERAL.equals(value)) {
			return UNDEFINED();
		}
		
		// Handle wildcard
		if (WILDCARD_LITERAL.equals(value)) {
			return (UndefinableType<T>)WILDCARD;
		}
		
		// Handle optional qualifier
		Qualifier qualifier = Qualifier.NONE;
		String subValue = value;
		if (value.length() > 1) {
			if (MAXIMUM_TOKEN.equals(value.substring(0, 1))) {
				qualifier = Qualifier.MAXIMUM;
				subValue = value.substring(1);
			} else if (MINIMUM_TOKEN.equals(value.substring(0, 1))) {
				qualifier = Qualifier.MINIMUM;
				subValue = value.substring(1);
			}
		}

		return new UndefinableType<T>(valueCreator.create(subValue), qualifier);
	}

	public boolean isSet() {
		return value != null;
	}
	
	public T getValueSet() {
		if (value == null) {
			throw new IllegalStateException("Value is undefined!");
		}
		
		return value;
	}
	
	public boolean isQualifierSet() {
		return qualifier != Qualifier.NONE;
	}
	
	public Qualifier getQualifier() {
		return qualifier;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (this == other) {
			return true;
		}
		if (!(other instanceof UndefinableType)) {
			return false;
		}
		UndefinableType<?> otherUndefinableType = (UndefinableType<?>)other;
		if (/* isWildcard
				|| otherUndefinableType.isWildcard
				|| */ value == null && otherUndefinableType.value == null
				//|| otherUndefinableType.value != null && otherUndefinableType.value.equals(value)) {
				|| value != null && value.equals(otherUndefinableType.value)) {
			return true;
		}
		
		return false;
	}
	
	public boolean matches(Object other) {
		if (other == null) {
			return false;
		}
		if (this == other) {
			return true;
		}
		if (!(other instanceof UndefinableType)) {
			return false;
		}
		UndefinableType<?> otherUndefinableType = (UndefinableType<?>)other;
		if (isWildcard
				|| otherUndefinableType.isWildcard
				|| this.equals(otherUndefinableType)) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		if (value != null) {
			int hashCode = value.hashCode();
		
			return hashCode;
		} 
		
		return 0;
	}
	
	public String toString() {
		if (isWildcard) {
			return WILDCARD_LITERAL;
		}
		
		if (value != null) {
			String prefix = "";
			switch (qualifier) {
			case MAXIMUM:
				prefix = MAXIMUM_TOKEN;
				
				break;
			case MINIMUM:
				prefix = MINIMUM_TOKEN;
				
				break;
			}
			
			return String.format("%s%s", prefix, value);
		}
		
		return UNDEFINED_LITERAL;
	}
	
	public String getLabel() {
		if (value != null) {
			String unit = "";
			// Compensate a lack of the percent type
			if (PercentType.class.equals(value.getClass())) {
				unit = "%";
			}
			
			switch (qualifier) {
			case MAXIMUM:
				return String.format("%s%s (Maximum)", value, unit);
			case MINIMUM:
				return String.format("%s%s (Minimum)", value, unit);
			default:
				return String.format("%s%s", value, unit);
			}
		}
		
		return "<Undefined>";
	}

	@Override
	public String format(String pattern) {
		return toString();
	}
	
	public int compareTo(UndefinableType<T> other) {
		if (value != null && other.value != null) {
			if (value instanceof Comparable) {
				return ((Comparable<T>)value).compareTo(other.value);
			}
		}
		
		return -1;
	}
}